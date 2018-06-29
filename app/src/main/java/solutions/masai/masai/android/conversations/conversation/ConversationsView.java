package solutions.masai.masai.android.conversations.conversation;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.masai.masai.android.App;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.conversations.base.BaseConversationController;
import solutions.masai.masai.android.conversations.base.BaseConversationView;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.Auth0Helper;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.TimeHelper;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.rocketchat.Room;

class ConversationsView extends BaseConversationView implements ConversationRoomListAdapter.ConversationRoomCallback {

	private ConversationsViewListener listener;
	private Button fabGoToAddHostController;
	private Toolbar toolbar;
	private DrawerLayout drawerLayout;
	private ImageView ivMenu;
	private TextView tvTitle;
	private TextView tvNavigationUsername;
	private TextView tvNavigationEmail;
	private Handler handler;
	private TextView tvReconnected;
	private TextView tvConnectionError;
	private ProgressDialog progressDialog;

	private ConversationRoomListAdapter adapter;

	public ConversationsView(BaseConversationController activity, ConversationsViewListener listener) {
		super(activity);
		this.listener = listener;
		handler = new Handler(Looper.getMainLooper());
		initNav();
		initViews();
		adapter = new ConversationRoomListAdapter(activity.getApplicationContext(), this);
		rvRooms.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
		rvRooms.setAdapter(adapter);
	}

	private void initNav() {
		drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R
				.string.navigation_drawer_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		ivMenu = (ImageView) rootView.findViewById(R.id.main_ala_action_bar_menu);
		tvTitle = (TextView) rootView.findViewById(R.id.main_ala_action_bar_title);
		NavigationView navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
		View hView = navigationView.getHeaderView(0);
		tvNavigationUsername = (TextView) hView.findViewById(R.id.nav_user_name);
		tvNavigationEmail = (TextView) hView.findViewById(R.id.nav_user_email);
		tvReconnected = (TextView) rootView.findViewById(R.id.main_connected_message);
		tvConnectionError = (TextView) rootView.findViewById(R.id.main_connection_error);

		tvTitle.setText(activity.getText(R.string.app_name));
		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawerLayout.openDrawer(GravityCompat.START);
			}
		});
		navigationView.setNavigationItemSelectedListener((ConversationsController) activity);
	}

	void setNavigationHeader() {
		handler.post(() -> {
			String travelUserFirstname = ((TravelfolderUserRepo.getInstance().getTravelfolderUser().getPersonalData() != null) ?
					TravelfolderUserRepo.getInstance().getTravelfolderUser().getPersonalData().getFirstname() : null);
			String travelUserLastname = ((TravelfolderUserRepo.getInstance().getTravelfolderUser().getPersonalData() != null) ?
					TravelfolderUserRepo.getInstance().getTravelfolderUser().getPersonalData().getLastname() : null);
			String travelUserEmail = ((TravelfolderUserRepo.getInstance().getTravelfolderUser().getContactInfo() != null) ?
					TravelfolderUserRepo.getInstance().getTravelfolderUser().getContactInfo().getPrimaryEmail() : null);

			String name = "";
			if (travelUserFirstname != null && travelUserFirstname.length() > 0 &&
					travelUserLastname != null && travelUserLastname.length() > 0) {
				name = travelUserFirstname + " " + travelUserLastname;
			} else {
				User user = ConnectionManager.getInstance().getUser();
				if (user.getAuth0Id().contains(Auth0Helper.SOCIAL_LOGIN_GOOGLE_CONNECTION) ||
						user.getAuth0Id().contains(Auth0Helper.SOCIAL_LOGIN_FACEBOOK_CONNECTION) ||
						user.getAuth0Id().contains(Auth0Helper.SOCIAL_LOGIN_TWITTER_CONNECTION) ||
						user.getAuth0Id().contains(Auth0Helper.SOCIAL_LOGIN_LINKEDIN_CONNECTION)) {
					name = user.getFullName();
				} else {
					name = user.getUsername();
				}
			}

			String email = "";
			if (travelUserEmail != null && travelUserEmail.length() > 0) {
				email = travelUserEmail;
			} else {
				email = ConnectionManager.getInstance().getUser().getEmail();
			}

			tvNavigationUsername.setText(String.format(activity.getString(R.string.hey_user), name));
			tvNavigationEmail.setText(email);
		});
	}

	private void initViews() {
		progressDialog = new ProgressDialog(rootView.getContext(), ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
		progressDialog.setCanceledOnTouchOutside(false);
		fabGoToAddHostController = (Button) rootView.findViewById(R.id.app_bar_main_open_new_chat);
		fabGoToAddHostController.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onGoToAddHostController();
			}
		});
		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		activity.setSupportActionBar(toolbar);
	}

	public void showData(Room room) {
		tvEmptyScreen.setVisibility(View.GONE);
		adapter.addRoom(room);
	}

	public void showEmptyScreen() {
		tvEmptyScreen.setVisibility(View.VISIBLE);
	}

	public void clearRooms() {
		adapter.clearData();
	}

	public void showInfoReconnecting() {
		tvReconnected.setVisibility(View.VISIBLE);
		handler.postDelayed(() -> tvReconnected.setVisibility(View.GONE), TimeHelper.SECONDS_THREE);
	}

	public void showProgress() {
		handler.post(() -> progressDialog.show());
	}

	public void hideProgress() {
		handler.post(() -> {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		});
	}

	public void showConnectionError() {
		tvConnectionError.setText(App.getContext().getString(R.string.connection_error));
		tvConnectionError.setVisibility(View.VISIBLE);
		tvConnectionError.bringToFront();
		handler.postDelayed(() -> tvConnectionError.setVisibility(View.GONE), TimeHelper.SECONDS_THREE);
	}

	public void connectionEstablished() {
		fabGoToAddHostController.setVisibility(View.VISIBLE);
	}

	public void hideAddHostButton() {
		fabGoToAddHostController.setVisibility(View.GONE);
	}

	@Override
	public void onRoomClicked(Room room) {
		listener.onRoomSelected(room);
	}

	interface ConversationsViewListener {

		void onGoToAddHostController();

		void onRoomSelected(Room room);
	}
}

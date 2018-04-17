package solutions.masai.masai.android.conversations.conversation;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.embiq.communicationmanager.CommunicationManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import solutions.masai.masai.android.App;
import solutions.masai.masai.android.BuildConfig;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.add_host.AddHostController;
import solutions.masai.masai.android.conversations.base.BaseConversationController;
import solutions.masai.masai.android.conversations.history.ConversationHistoryController;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.Auth0Helper;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.communication.HostConnection;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.ConnectivityChangeReceiver;
import solutions.masai.masai.android.core.helper.PrefsHelper;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.model.Host;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.rocketchat.Room;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;
import solutions.masai.masai.android.login.LoginController;
import solutions.masai.masai.android.my_tickets.MyTicketsController;
import solutions.masai.masai.android.profile.ProfileController;

public class ConversationsController extends BaseConversationController
		implements NavigationView.OnNavigationItemSelectedListener, ConversationsView.ConversationsViewListener,
		ConnectionManager.IConnectListener, ConnectivityChangeReceiver.INetworkState {

	private ConversationsView view;
	private ConnectivityChangeReceiver connectivityChangeReceiver;
	private ConnectionManager.IConnectListener connectListener;
	private boolean isAddingHostsTaskRunning = false;
	private int addingHostCounter;
	private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			configureUser();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectListener = this;
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		initCore();
		C.L("ConvController: Firebase Token refreshed = " + FirebaseInstanceId.getInstance().getToken());
		view = new ConversationsView(this, this);
		CommunicationManager.setContext(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		connectivityChangeReceiver = ConnectivityChangeReceiver.registerNetworkStateReceiver(this, this);
		LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(C.REFRESH_CONVERSATIONS_BY_NOTIFICATION));
		configureUser();
	}

	@Override
	protected void onPause() {
		handler.removeCallbacksAndMessages(null);
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (connectivityChangeReceiver != null) {
			unregisterReceiver(connectivityChangeReceiver);
			connectivityChangeReceiver = null;
		}
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nav_profile:
			launchProfile();
			break;
		case R.id.nav_tickets:
			launchTickets();
			break;
		case R.id.nav_conversation_history:
			launchConversationHistory();
			break;
		case R.id.nav_help:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL, new String[]{C.HELP_UND_FEEDBACK_EMAIL});
			i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.help_and_feedback));
			startActivity(Intent.createChooser(i, "Send mail..."));
			break;
		case R.id.nav_logout:
			ConnectionManager.getInstance().removeAll();
			ConnectionManager.getInstance().setUser(null);
			ConnectionManager.clear();
			Auth0Helper.clear();
			TravelfolderUserRepo.getInstance().clearTravelfolderUser();
			PrefsHelper.getInstance().removePref(C.PREFERENCES_KEY_USER);
			RealmHelper.getInstance().removeAllHosts();
			RealmHelper.getInstance().dropRocketChatUsers();
			RealmHelper.getInstance().dropMessages();
			new Thread(() -> {
				try {
					FirebaseInstanceId.getInstance().deleteInstanceId();
					FirebaseInstanceId.getInstance().getToken();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
			launchLogin();
			break;
		case R.id.nav_version_number:
			String appVersion = String.format(this.getResources().getString(R.string.app_version_info), BuildConfig.VERSION_NAME);
			SpannableString ss=  new SpannableString(appVersion);
			StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
			ss.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ss.setSpan(new RelativeSizeSpan(1.5f), 0,4, 0);
			AlertDialog.Builder versionInfoDialogBuilder = new AlertDialog.Builder(this);
			TextView versionInfoTextView = new TextView(this);
			versionInfoTextView.setText(ss);
			versionInfoTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			versionInfoTextView.setPadding(0, (int) this.getResources().getDimension(R.dimen.margin_m), 0 ,0);
			AlertDialog versionInfoDialog = versionInfoDialogBuilder.setView(versionInfoTextView).setPositiveButton(R.string.ok,
					(dialogInterface, di) -> dialogInterface.dismiss()).create();
			versionInfoDialog.show();
			Button positiveButton = versionInfoDialog.getButton(AlertDialog.BUTTON_POSITIVE);
			LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
			positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
			positiveButton.setLayoutParams(positiveButtonLL);
			break;
		default:
			break;
		}
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onGoToAddHostController() {
		Intent addHost = new Intent(getApplicationContext(), AddHostController.class);
		startActivity(addHost);
	}

	@Override
	public void onDisconnected(int pos) {
		C.L(C.LOG_HOST + pos + " disconnected");
		checkIfHostsLoadingFinished();
	}

	@Override
	public void onConnectivityError(int pos) {
		C.L(C.LOG_HOST + pos + " connectivity error");
		checkIfHostsLoadingFinished();
	}

	@Override
	public void onHostNotReachable(int pos) {
		C.L(C.LOG_HOST + ConnectionManager.getInstance().getHostConnectionForPos(pos).getHost().getHostRocketChatApiUrl() + " not " +
				"reachable!");
		/*ConnectionManager.getInstance().removeConnection(pos);
		addingHostCounter--;
		checkIfHostsLoadingFinished();*/
	}

	@Override
	public void onHostLoadingFinished(int pos) {
		C.L(C.LOG_HOST + pos + " loading finsihed");
		loadMissingMessagesFromServer(ConnectionManager.getInstance().getHostConnectionForPos(pos));
		checkIfHostsLoadingFinished();
	}

	@Override
	public void onConnectedAndLoggedIn(final int pos) {
		C.L(C.LOG_HOST + pos + " connected");
	}

	@Override
	public void onBadPassword(int pos) {
		C.L(C.LOG_HOST + pos + " bad password");
	}

	@Override
	public void onUserNotFound(int pos) {
		C.L("host " + pos + " user not found");
	}

	@Override
	public void onNoUsername(int pos) {
		C.L("host " + pos + " no username");
	}

	@Override
	public void onNewMessage(int pos, Room room) {
		C.L(C.LOG_HOST + pos + " new message");
		view.showData(room);
	}

	@Override
	public void onRoomCreated(Room room) {
		handler.post(() -> {
			HostConnection hostcon = ConnectionManager.getInstance().getHostConnectionForPos(room.getHostConPosition());
			hostcon.loadMessagesFromWhileIWasAway(room.getId(), new HostConnection.IGetMessagesCallback() {
				@Override
				public void onMessagesReady() {
					view.showData(room);
				}

				@Override
				public void onConnectivityError() {
					//not used here
				}
			});
		});
	}

	@Override
	public void onRoomSelected(Room room) {
		startChat(room.getHostConPosition(), room, true);
	}

	@Override
	public void onStateConnected() {
		configureUser();
	}

	@Override
	public void onStateDisconnected() {
		showOfflineChats();
	}

	private void initCore() {
		PrefsHelper.init(this);
		RealmHelper.getInstance().init(this.getApplicationContext());
		BackendManager.setContext(this.getApplicationContext());
	}

	private void configureUser() {
		if (ConnectionManager.getInstance().getUser() == null) {
			launchLogin();
		} else {
			initTravelFolderUser();
			initConnections();
		}
	}

	private void initTravelFolderUser() {
		BackendManager.getInstance().getBackendService().getTravelfolderUser(C.AUTHORIZATION_BEARER_PREFIX +
				ConnectionManager.getInstance().getUser().getIdToken())
				.enqueue(new Callback<TravelfolderUser>() {
					@Override
					public void onResponse(Call<TravelfolderUser> call, Response<TravelfolderUser> response) {
						TravelfolderUser travelUser = null;
						if(response.errorBody() == null) {
							travelUser = response.body();
							TravelfolderUserRepo.getInstance().setTravelfolderUser(travelUser);
						}

						String username = "";
						if(travelUser != null && travelUser.getPersonalData() != null) {
							username = travelUser.getPersonalData().getFirstname() + " " + travelUser.getPersonalData().getLastname();
						} else {
							username = ConnectionManager.getInstance().getUser().getEmail();
						}

						if(travelUser.getPersonalData() != null) {
							ConnectionManager.getInstance().getUser().setUsername(username);
						}

						view.setNavigationHeader();
					}
					@Override
					public void onFailure(Call<TravelfolderUser> call, Throwable t) {
					}
				});
	}

	private void initConnections() {
		C.L("Init host connections... -> task actually running: " + isAddingHostsTaskRunning);
		if (!isAddingHostsTaskRunning) {
			view.showProgress();
			isAddingHostsTaskRunning = true;
			view.clearRooms();
			ConnectionManager.getInstance().initHostListFromServer(new ConnectionManager.IHostsAddedListener() {
				@Override
				public void onHostListUpdated() {
					ConnectionManager.getInstance().removeAll();
					addConnections();
					view.connectionEstablished();
				}

				@Override
				public void onError() {
					showOfflineChats();
				}
			});
		}
	}

	private void addConnections() {
		for (Host host : ConnectionManager.getInstance().getHostList()) {
			boolean isHostConnectionActive = ConnectionManager.getInstance().isHostConnectionActive(host);
			if (!isHostConnectionActive) {
				ConnectionManager.getInstance().addConnection(App.getContext(), ConnectionManager.getInstance().getHostList().indexOf
						(host), true, connectListener);
			}
		}
	}

	private void showOfflineChats() {
		C.L("Adding offline chats...");
		ConnectionManager.getInstance().loadHostConnectionsFromDB();
		if (!ConnectionManager.getInstance().getHostList().isEmpty()) {
			List<Host> hostList = ConnectionManager.getInstance().getHostList();
			for (Host localHost : hostList) {
				for (Room openedRoom : localHost.getOpenedRooms()) {
					view.showData(openedRoom);
				}
				checkIfHostsLoadingFinished();
			}
		} else {
			checkIfHostsLoadingFinished();
		}
		view.hideAddHostButton();
		view.showConnectionError();
	}

	private void checkIfHostsLoadingFinished() {
		if(isAddingHostsTaskRunning) {
			addingHostCounter++;
			List<HostConnection> hostConList = ConnectionManager.getInstance().getHostConnectionList();
			if (addingHostCounter == hostConList.size() || hostConList.isEmpty()) {
				isAddingHostsTaskRunning = false;
				addingHostCounter = 0;
				view.hideProgress();
				if (!ConnectionManager.getInstance().areOpenRoomsAvailable()) {
					view.showEmptyScreen();
				}
			}
		}
	}

	private void loadMissingMessagesFromServer(HostConnection hostCon) {
		for(Room closedRoom : hostCon.getHost().getClosedRooms()) {
			Message firstMessage = RealmHelper.getInstance().getFirstMessage(closedRoom.getId());
			Message lastMessage = RealmHelper.getInstance().getLatestMessage(closedRoom.getId());
			if(firstMessage == null || (lastMessage != null && !lastMessage.getMessage().contains(getString(R.string.chat_closed)))) {
				hostCon.loadMessagesFromWhileIWasAway(closedRoom.getId(), new HostConnection.IGetMessagesCallback() {
					@Override
					public void onMessagesReady() {
						//not used here
					}

					@Override
					public void onConnectivityError() {
						//not used here
					}
				});
			}
		}
	}

	private void launchLogin() {
		Intent login = new Intent(getApplicationContext(), LoginController.class);
		startActivity(login);
	}

	private void launchProfile() {
		Intent profile = new Intent(getApplicationContext(), ProfileController.class);
		startActivity(profile);
	}

	private void launchTickets() {
		Intent profile = new Intent(getApplicationContext(), MyTicketsController.class);
		startActivity(profile);
	}

	private void launchConversationHistory() {
		Intent history = new Intent(getApplicationContext(), ConversationHistoryController.class);
		startActivity(history);
	}
}
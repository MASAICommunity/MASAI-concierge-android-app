package solutions.masai.masai.android.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import solutions.masai.masai.android.BuildConfig;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.AutoFitText;
import solutions.masai.masai.android.profile.adapter.ViewPagerAdapter;
import solutions.masai.masai.android.profile.controller.access_privileges.AccessPrivilegesFragment;
import solutions.masai.masai.android.profile.controller.journey_preferences.JourneyPreferencesController;
import solutions.masai.masai.android.profile.controller.my_data.MyDataController;

public class ProfileView {

	private static final int[] TAB_ICONS = {
			R.drawable.menu_my_data,
			R.drawable.menu_preferences,
			R.drawable.menu_permissions
	};

	private View rootView;
	private ProfileViewListener listener;
	private Context context;
	private ProgressDialog progressDialog;
	private Handler handler;

	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	ProfileView(View rootView, Context context, ProfileViewListener listener) {
		this.rootView = rootView;
		this.context = context;
		this.listener = listener;
		handler = new Handler(Looper.getMainLooper());
		initViews();
	}

	private void initViews() {
		progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
		progressDialog.setCanceledOnTouchOutside(false);

		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		this.listener.initToolbar(toolbar);
		toolbar.setNavigationOnClickListener(v -> listener.onBackArrowPressed());
		//initCurrentVersionDialog();

		initTabView();
	}

	private void initTabView() {
		viewPager = (ViewPager) rootView.findViewById(R.id.activity_profile_view_viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) rootView.findViewById(R.id.activity_profile_view_tabs);
		tabLayout.setupWithViewPager(viewPager);

		setupTabs();
	}

	private void setupTabs() {
		RelativeLayout myDataTab = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
		AutoFitText myDataTabText = (AutoFitText) myDataTab.findViewById(R.id.custom_tab_text);
		myDataTabText.setText(R.string.my_information);
		ImageView myDataTabIcon = (ImageView) myDataTab.findViewById(R.id.custom_tab_icon);
		myDataTabIcon.setImageDrawable(ContextCompat.getDrawable(context, TAB_ICONS[0]));
		tabLayout.getTabAt(0).setCustomView(myDataTab);

		RelativeLayout myPreferencesTab = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
		AutoFitText myPreferencesTabText = (AutoFitText) myPreferencesTab.findViewById(R.id.custom_tab_text);
		myPreferencesTabText.setText(R.string.travel_preferences);
		ImageView myPreferencesTabIcon = (ImageView) myPreferencesTab.findViewById(R.id.custom_tab_icon);
		myPreferencesTabIcon.setImageDrawable(ContextCompat.getDrawable(context, TAB_ICONS[1]));
		tabLayout.getTabAt(1).setCustomView(myPreferencesTab);

		RelativeLayout myPrivilegesTab = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
		AutoFitText myPrivilegesTabText = (AutoFitText) myPrivilegesTab.findViewById(R.id.custom_tab_text);
		myPrivilegesTabText.setText(R.string.access_management);
		ImageView myPrivilegesTabIcon = (ImageView) myPrivilegesTab.findViewById(R.id.custom_tab_icon);
		myPrivilegesTabIcon.setImageDrawable(ContextCompat.getDrawable(context, TAB_ICONS[2]));
		tabLayout.getTabAt(2).setCustomView(myPrivilegesTab);
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(((FragmentActivity) context).getSupportFragmentManager());
		adapter.addFragment(new MyDataController(), context.getString(R.string.my_information));
		adapter.addFragment(new JourneyPreferencesController(), context.getString(R.string.travel_preferences));
		adapter.addFragment(new AccessPrivilegesFragment(), context.getString(R.string.access_management));
		viewPager.setAdapter(adapter);
	}

	/*private void initCurrentVersionDialog() {
		toolbar.findViewById(R.id.toolbar_info_logo).setOnClickListener(v -> {
			String appVersion = String.format(context.getResources().getString(R.string.app_version_info), BuildConfig.VERSION_NAME);
			SpannableString ss=  new SpannableString(appVersion);
			StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
			ss.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ss.setSpan(new RelativeSizeSpan(1.5f), 0,4, 0);
			AlertDialog.Builder versionInfoDialogBuilder = new AlertDialog.Builder(context);
			TextView versionInfoTextView = new TextView(context);
			versionInfoTextView.setText(ss);
			versionInfoTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			versionInfoTextView.setPadding(0, (int) context.getResources().getDimension(R.dimen.margin_m), 0 ,0);
			AlertDialog versionInfoDialog = versionInfoDialogBuilder.setView(versionInfoTextView).setPositiveButton(R.string.ok,
					(dialogInterface, i) -> dialogInterface.dismiss()).create();
			versionInfoDialog.show();
			Button positiveButton = versionInfoDialog.getButton(AlertDialog.BUTTON_POSITIVE);
			LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
			positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
			positiveButton.setLayoutParams(positiveButtonLL);
		});
	}*/

	public void showProgress() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				progressDialog.show();
			}
		});
	}

	public void hideProgress() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		});
	}

	interface ProfileViewListener {

		void onBackArrowPressed();

		void onUpdate(String name, String password);

		void initToolbar(Toolbar toolbar);
	}
}
package solutions.masai.masai.android.profile.controller.access_privileges;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.GsonBuilder;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.journey.Item;
import solutions.masai.masai.android.core.model.journey.UserAccessManagement;
import solutions.masai.masai.android.databinding.ActivityAccessPrivilegeSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.access_privileges.AccessPrivilegesSettingsView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tom on 28.08.17.
 */

public class AccessPrivilegeSettingsController extends BaseController implements AccessPrivilegesSettingsView.AccessPrivilegesSettingsViewListener {

	private AccessPrivilegesSettingsView accessPrivilegesSettingsView;
	private ActivityAccessPrivilegeSettingsBinding binding;
	private UserAccessManagement userAccessManagement;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_access_privilege_settings);
		accessPrivilegesSettingsView = new AccessPrivilegesSettingsView(binding.getRoot(), this, this, binding);

		initAccessUsers();
	}
	private void initAccessUsers() {
		final User user = ConnectionManager.getInstance().getUser();
		BackendManager.getInstance().getBackendService().getGrantedUsers(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
				.enqueue(new Callback<ResponseBody>() {
					@Override
					public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
						if (response.errorBody() == null) {
							GsonBuilder gsonBuilder = new GsonBuilder();
							UserAccessManagement uac = null;
							try {
								uac = gsonBuilder.create().fromJson(response.body().string(), UserAccessManagement
										.class);

							} catch (IOException e) {
								e.printStackTrace();
							}
							if(uac != null) {
								userAccessManagement = uac;
							}
							else {
								userAccessManagement = new UserAccessManagement();
							}

							accessPrivilegesSettingsView.setContent(uac);
						}
					}

					@Override
					public void onFailure(Call<ResponseBody> call, Throwable t) {
						C.L(t.toString());
					}
				});

	}


	@Override
	public void revokePrivilegesButtonPressed() {

		final User user = ConnectionManager.getInstance().getUser();

		for (Item item: userAccessManagement.getItems()) {
			BackendManager.getInstance().getBackendService().deleteGrantedUser(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken(),item.getGrantedUserId().getS()).enqueue(
					new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
							if (response.isSuccessful()) {

							}
						}

						@Override
						public void onFailure(Call<ResponseBody> call, Throwable t) {

						}
					}
			);
		}
		userAccessManagement = null;
		accessPrivilegesSettingsView.setContent(userAccessManagement);
	}

	@Override
	public void updateView() {

	}
}

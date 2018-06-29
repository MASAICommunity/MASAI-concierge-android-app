package solutions.masai.masai.android.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;
import solutions.masai.masai.android.profile.controller.access_privileges.AccessPrivilegesFragment;
import solutions.masai.masai.android.profile.controller.journey_preferences.JourneyPreferencesController;
import solutions.masai.masai.android.profile.controller.my_data.MyDataController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileController extends AppCompatActivity implements ProfileView.ProfileViewListener, MyDataController.OnFragmentInteractionListener, JourneyPreferencesController.OnFragmentInteractionListener, AccessPrivilegesFragment.OnFragmentInteractionListener {

	private ProfileView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_view);

		view = new ProfileView(findViewById(android.R.id.content), this, this);
	}

	private void showError() {
		Toast.makeText(this.getApplicationContext(), R.string.travelfolder_not_safed, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackArrowPressed() {
		if (TravelfolderUserRepo.getInstance().getTravelfolderUser() != null) {
			final User user = ConnectionManager.getInstance().getUser();
			TravelfolderUser tfuser = TravelfolderUserRepo.getInstance().getTravelfolderUser();

			BackendManager.getInstance().getBackendService().updateTravelfolderUser(tfuser, C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
					.enqueue(new Callback<okhttp3.ResponseBody>() {
						@Override
						public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
							if(response.errorBody() != null) {
								showError();
							}
						}

						@Override
						public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
							showError();
						}
					});
		}

		onBackPressed();
	}

	@Override
	public void onUpdate(String name, String password) {
		view.showProgress();
	}

	@Override
	public void initToolbar(Toolbar toolbar) {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		//not used yet
	}
}
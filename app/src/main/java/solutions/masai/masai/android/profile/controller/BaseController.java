package solutions.masai.masai.android.profile.controller;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cWahl on 25.08.2017.
 */

public class BaseController extends AppCompatActivity {

	private static View lastFocusedView;

	public BaseController() {
		lastFocusedView = null;
	}

	public static void setLastFocusedView(View view) {
		lastFocusedView = view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if(lastFocusedView != null) {
			//hide keyboard if currently open
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(lastFocusedView.getWindowToken(), 0);

			lastFocusedView.clearFocus();
		}

		if (TravelfolderUserRepo.getInstance().getTravelfolderUser() != null) {
			final User user = ConnectionManager.getInstance().getUser();
			TravelfolderUser tfuser = TravelfolderUserRepo.getInstance().getTravelfolderUser();

			BackendManager.getInstance().getBackendService().updateTravelfolderUser(tfuser, C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
					.enqueue(new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
							if(response.errorBody() != null) {
							}
						}

						@Override
						public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
						}
					});
		}
	}

	public void onBackArrowPressed() {
		if (TravelfolderUserRepo.getInstance().getTravelfolderUser() != null) {
			final User user = ConnectionManager.getInstance().getUser();
			TravelfolderUser tfuser = TravelfolderUserRepo.getInstance().getTravelfolderUser();

			BackendManager.getInstance().getBackendService().updateTravelfolderUser(tfuser, C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
					.enqueue(new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
							if(response.errorBody() != null) {
							}
						}

						@Override
						public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
						}
					});
		}


		onBackPressed();
	}


	public void saveProfile() {

	}
}

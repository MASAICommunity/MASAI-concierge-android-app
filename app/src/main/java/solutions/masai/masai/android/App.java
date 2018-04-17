package solutions.masai.masai.android;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.Foreground;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.ChoicesConverter;
import solutions.masai.masai.android.core.helper.exception.NoConnectivityException;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoicesList;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;

/**
 * Created by Semko on 2017-01-12.
 */

public class App extends Application implements Foreground.Listener {

	private static Context context;
	private Toast errorMsg;
	private static boolean isAppInBackground;
	private static boolean clearHostConnections = true;

	public static boolean isAppInBackground() {
		return isAppInBackground;
	}

	public static Context getContext() {
		return context;
	}

	public static void setClearHostConnections(boolean clearHostConnections) {
		App.clearHostConnections = clearHostConnections;
	}

	public void onCreate() {
		super.onCreate();
		context = this;

		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		C.COUNTRY_CODE = tm.getSimCountryIso().toUpperCase();

		Foreground.init(this).addListener(this);
	}

	@Override
	public void onBecameForeground() {
		isAppInBackground = false;
		initTravelFolderUser();
		initChoices();
	}

	@Override
	public void onBecameBackground() {
		isAppInBackground = true;
		dismissError();
		saveTravelFolderUser();
		if(clearHostConnections) {
			C.L("Clearing Host Connections...");
			ConnectionManager.getInstance().removeAll();
		}
	}

	private void saveTravelFolderUser() {
		final User user = ConnectionManager.getInstance().getUser();
		TravelfolderUser tfuser = TravelfolderUserRepo.getInstance().getTravelfolderUser();
		BackendManager.getInstance().getBackendService().updateTravelfolderUser(tfuser, C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
				.enqueue(new Callback<okhttp3.ResponseBody>() {
					@Override
					public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {

						if (response.errorBody() != null) {
							showError();
						}
					}

					@Override
					public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
						showError();
					}
				});
	}

	private void initTravelFolderUser() {
		final User user = ConnectionManager.getInstance().getUser();
		if (user != null && user.getIdToken() != null) {
			BackendManager.getInstance().getBackendService().getTravelfolderUser(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
					.enqueue(new Callback<TravelfolderUser>() {
						@Override
						public void onResponse(Call<TravelfolderUser> call, Response<TravelfolderUser> response) {
							if (response.errorBody() == null) {
								TravelfolderUser travelUser = response.body();
								TravelfolderUserRepo.getInstance().setTravelfolderUser(travelUser);
							} else {
								showError();
							}
						}

						@Override
						public void onFailure(Call<TravelfolderUser> call, Throwable t) {
							if (t instanceof NoConnectivityException) {
								Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
							} else {
								showError();
							}
						}
					});
		}
	}

	private void initChoices() {
		BackendManager.getInstance().getBackendService().getChoices()
				.enqueue(new Callback<okhttp3.ResponseBody>() {
					@Override
					public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
						if (response.errorBody() == null) {
							GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(ChoicesList.class, new ChoicesConverter());
							ChoicesList choicesList = null;
							try {
								choicesList = gsonBuilder.create().fromJson(response.body().string(), ChoicesList.class);
							} catch (IOException e) {
								e.printStackTrace();
								showError();
							}
							TravelfolderUserRepo.getInstance().setChoiceList(choicesList);
						} else {
							showError();
						}
					}

					@Override
					public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
						if (t instanceof NoConnectivityException) {
							Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
						} else {
							showError();
						}
					}
				});
	}

	private void showError() {
		if (!isAppInBackground()) {
			errorMsg = Toast.makeText(this.getApplicationContext(), R.string.travelfolder_not_loaded, Toast.LENGTH_SHORT);
			errorMsg.show();
		}
	}

	private void dismissError() {
		if (errorMsg != null) {
			errorMsg.cancel();
		}
	}
}

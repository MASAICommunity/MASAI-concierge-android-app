package solutions.masai.masai.android.register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.Auth0Helper;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.rocketchat.Room;
import solutions.masai.masai.android.core.model.travelfolder_user.ContactInfo;
import solutions.masai.masai.android.core.model.travelfolder_user.PersonalData;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;

public class RegisterController extends AppCompatActivity implements RegisterView.LoginViewListener, ConnectionManager.IConnectListener {

	private RegisterView view;
	private Handler handler = new Handler(Looper.getMainLooper());
	private boolean successfullyRegistered = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		view = new RegisterView(findViewById(android.R.id.content), this, this);
	}

	@Override
	public void onContinueRegister(final String email, final String firstname, final String lastname, final String password) {
		view.showProgress();
		Auth0Helper.getInstance().singUp(email, password, new Auth0Helper.ISingUp() {
			@Override
			public void onUsernameFormatIncorrect() {
				view.showUsernameFormatError();
				view.hideProgress();
			}

			@Override
			public void onBadUser() {
				view.showEmailExistError();
				view.hideProgress();
			}

			@Override
			public void onBadUsername() {
				view.showUsernameTakenError();
				view.hideProgress();
			}

			@Override
			public void onSingedUp() {
				handler.post(() -> {
					view.hideProgress();
					startConversationsController();
					successfullyRegistered = true;
				});

				TravelfolderUser tfuser = TravelfolderUserRepo.getInstance().getTravelfolderUser();
				if (tfuser != null) {
					if(tfuser.getPersonalData() == null) {
						tfuser.setPersonalData(new PersonalData());
					}
					tfuser.getPersonalData().setFirstname(firstname);
					tfuser.getPersonalData().setLastname(lastname);

					if(tfuser.getContactInfo() == null) {
						tfuser.setContactInfo(new ContactInfo());
					}
					tfuser.getContactInfo().setPrimaryEmail(email);

					final User user = ConnectionManager.getInstance().getUser();
					user.setUsername(firstname + " " + lastname);

					BackendManager.getInstance().getBackendService().updateTravelfolderUser(tfuser, C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
							.enqueue(new Callback<ResponseBody>() {
								@Override
								public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
									if(response.errorBody() != null) {
										view.showTravelfolderUserSavingError();
									}
								}

								@Override
								public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
									view.showTravelfolderUserSavingError();
								}
							});
				}
			}

			@Override
			public void onSingUpError() {
				view.showRegisterError();
				view.hideProgress();
			}

			@Override
			public void onBadConnection() {
				view.showNoInternetError();
				view.hideProgress();
			}
		});
	}

	@Override
	protected void onPause() {
		if (!successfullyRegistered) {
			ConnectionManager.getInstance().setUser(null);
			while (!ConnectionManager.getInstance().getHostConnectionList().isEmpty()) {
				ConnectionManager.getInstance().removeAll();
			}
		}
		super.onPause();
	}

	@Override
	public void onBackClicked() {
		onBackPressed();
	}

	@Override
	public void onDisconnected(int pos) {
		view.showConnectivityError();
		view.hideProgress();
	}

	@Override
	public void onConnectedAndLoggedIn(int pos) {
		startConversationsController();
		successfullyRegistered = true;
	}

	@Override
	public void onBadPassword(int pos) {
		//not used here
	}

	@Override
	public void onUserNotFound(int pos) {
		//not used here
	}

	@Override
	public void onNoUsername(int pos) {
		//not used here
	}

	@Override
	public void onNewMessage(int pos, Room room) {
		//not used here
	}

	@Override
	public void onConnectivityError(int pos) {
		view.showConnectivityError();
		view.hideProgress();
	}

	@Override
	public void onHostNotReachable(int pos) {
		//not used here
	}

	@Override
	public void onHostLoadingFinished(int pos) {
		//not used here
	}

	@Override
	public void onRoomCreated(Room room) {
		//not used here
	}

	private void startConversationsController() {
		handler.post(() -> finish());
	}
}

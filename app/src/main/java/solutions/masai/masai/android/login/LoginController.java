package solutions.masai.masai.android.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.auth0.android.provider.WebAuthProvider;

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
import solutions.masai.masai.android.core.helper.PrefsHelper;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.travelfolder_user.ContactInfo;
import solutions.masai.masai.android.core.model.travelfolder_user.PersonalData;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;
import solutions.masai.masai.android.password_reset.PasswordResetController;
import solutions.masai.masai.android.register.RegisterController;

public class LoginController extends AppCompatActivity implements LoginView.LoginViewListener {
    private LoginView view;
    private boolean successfullyLoggedIn = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        view = new LoginView(findViewById(android.R.id.content), this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrefsHelper.init(this);
        if (ConnectionManager.getInstance().getHostConnectionList().size() > 0) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        if (!successfullyLoggedIn) {
            ConnectionManager.getInstance().setUser(null);
            while (ConnectionManager.getInstance().getHostConnectionList().size() > 0) {
                ConnectionManager.getInstance().removeAll();
            }
        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (WebAuthProvider.resume(intent)) {
            return;
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onRegisterClicked() {
        startActivity(new Intent(this, RegisterController.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void onPasswordResetClicked() {
        startActivity(new Intent(this, PasswordResetController.class));
    }

    @Override
    public void onContinueLogin(String email, String username, String password) {
        view.showProgress();
        Auth0Helper.getInstance().logIn(email, password, username, loginCallback);
    }

    private Auth0Helper.ILogIn loginCallback = new Auth0Helper.ILogIn() {
        @Override
        public void onLoggedIn() {
            handler.post(() -> {
                successfullyLoggedIn = true;
                view.hideProgress();
                finish();
            });

            TravelfolderUser tfuser = TravelfolderUserRepo.getInstance().getTravelfolderUser();
            User user = ConnectionManager.getInstance().getUser();
            if (tfuser != null && user.getAuth0Id().contains(Auth0Helper.SOCIAL_LOGIN_GOOGLE_CONNECTION) ||
                    user.getAuth0Id().contains(Auth0Helper.SOCIAL_LOGIN_FACEBOOK_CONNECTION)) {
                if(tfuser.getPersonalData() == null) {
                    tfuser.setPersonalData(new PersonalData());
                }

                if(user.getFullName() != null) {
                    String[] name = user.getFullName().split(" ");
                    if (name.length > 1) {
                        tfuser.getPersonalData().setFirstname(name[0]);
                        tfuser.getPersonalData().setLastname(name[1]);
                    }
                }

                String email = user.getEmail();
                if(tfuser.getContactInfo() == null) {
                    tfuser.setContactInfo(new ContactInfo());
                }
                tfuser.getContactInfo().setPrimaryEmail(email);

                BackendManager.getInstance().getBackendService().updateTravelfolderUser(tfuser, C.AUTHORIZATION_BEARER_PREFIX +
                        user.getIdToken()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.errorBody() != null) {
                                    //view.showTravelfolderUserSavingError();
                                }
                            }

                            @Override
                            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                                //view.showTravelfolderUserSavingError();
                            }
                        });
            }
        }

        @Override
        public void onLoginError() {
            view.hideProgress();
            view.showOkDialog(R.string.connection_restore);
        }

        @Override
        public void onBadCredentials() {
            view.hideProgress();
            view.showOkDialog(R.string.bad_user_or_pass);
        }
    };

    @Override
    public void onLogInWithFacebook() {
        Auth0Helper.getInstance().logInWithFacebook(this, loginCallback);
    }

    @Override
    public void onLogInWithGoogle() {
        Auth0Helper.getInstance().logInWithGoogle(this, loginCallback);
    }

    @Override
    public void onLogInWithTwitter() {
        Auth0Helper.getInstance().logInWithTwitter(this, loginCallback);
    }

    @Override
    public void onLogInWithLinkedIn() {
        Auth0Helper.getInstance().logInWithLinkedIn(this, loginCallback);
    }
}
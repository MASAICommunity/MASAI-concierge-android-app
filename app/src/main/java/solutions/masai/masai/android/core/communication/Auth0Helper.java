package solutions.masai.masai.android.core.communication;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.request.SignUpRequest;
import com.auth0.android.callback.AuthenticationCallback;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.request.AuthenticationRequest;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.google.gson.GsonBuilder;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.ChoicesConverter;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoicesList;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;
import solutions.masai.masai.android.core.model.journey.UserAccessManagement;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Semko on 2017-04-05.
 */

public class Auth0Helper {
    private static final String CLIENT_ID = "tEq0Sh3dluFDnDnxt6yV0k43uWxKOKz1";
    private static final String DOMAIN = "masai-prod.eu.auth0.com";
    private static final String CONNECTION_NAME = "Username-Password-Authentication";
    public static final String SOCIAL_LOGIN_GOOGLE_CONNECTION = "google-oauth2";
    public static final String SOCIAL_LOGIN_FACEBOOK_CONNECTION = "facebook";
    public static final String SOCIAL_LOGIN_TWITTER_CONNECTION = "twitter";
    public static final String SOCIAL_LOGIN_LINKEDIN_CONNECTION = "linkedin";
    private static final String SOCIA_LOGIN_SCHEME = "masai";
    private AuthenticationAPIClient authentication;
    private static Auth0Helper instance;
    private Auth0 auth0;

    private Auth0Helper() {
        init();
    }

    public static Auth0Helper getInstance() {
        if (instance == null) {
            instance = new Auth0Helper();
        }
        return instance;
    }

    private void init() {
        auth0 = new Auth0(CLIENT_ID, DOMAIN);
        auth0.setOIDCConformant(true);
        authentication = new AuthenticationAPIClient(auth0);
    }

    public static void clear() {
        instance = null;
    }

    public void singUp(final String email, final String password, final ISingUp listener) {
        SignUpRequest signUpRequest;
        signUpRequest = authentication.signUp(email, password, CONNECTION_NAME);
        signUpRequest.start(new AuthenticationCallback<Credentials>() {
            @Override
            public void onSuccess(Credentials payload) {
                getAuth0UserInfo(payload, new ILogIn() {
                    @Override
                    public void onLoggedIn() {
                        listener.onSingedUp();
                    }

                    @Override
                    public void onLoginError() {
                        listener.onSingUpError();
                    }

                    @Override
                    public void onBadCredentials() {
                        listener.onSingUpError();
                    }
                });
            }

            @Override
            public void onFailure(AuthenticationException error) {
                if (error.getCode() != null && error.getCode().contains("user_exists")) {
                    listener.onBadUser();
                }
                else if(error.getCode() != null && error.getCode().contains("username_exists")) {
                    listener.onBadUsername();
                }
                else if(error.getCode() != null && error.getCode().contains("Request failed")) {
                    listener.onBadConnection();
                }
                else if(error.getCode() != null && error.getCode().contains("missing_property")) {
                    listener.onUsernameFormatIncorrect();
                }
                else {
                    listener.onSingUpError();
                }
            }
        });
    }

    private String getUsernameFromEmail(String email) {
        String emailSplit[] = email.split("[@]");
        if (emailSplit.length > 0) {
            return emailSplit[0];
        }
        return email;
    }

    public void logIn(final String email, final String password, final String username, final ILogIn listener) {
        AuthenticationRequest loginRequest;

        if (username == null || username.isEmpty()) {
            loginRequest = authentication.login(email, password, CONNECTION_NAME);
        } else {
            loginRequest = authentication.login(username, password, CONNECTION_NAME);
        }

        loginRequest.setRealm(CONNECTION_NAME);
        loginRequest.setScope("openid profile offline_access");
        loginRequest.start(new AuthenticationCallback<Credentials>() {
            @Override
            public void onSuccess(Credentials payload) {
                getAuth0UserInfo(payload, listener);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                ConnectionManager.getInstance().setUser(null);
                if (error.getCode() != null && error.getCode().contains("invalid_grant")) {
                    listener.onBadCredentials();
                } else {
                    listener.onLoginError();
                }
            }
        });
    }

    public void logInWithFacebook(Activity activity, final ILogIn listener) {
        handleSocialLogin(WebAuthProvider.init(auth0).withConnection(SOCIAL_LOGIN_FACEBOOK_CONNECTION)
                .withConnectionScope("public_profile,email,user_friends")
                .withScheme(SOCIA_LOGIN_SCHEME)
                .withScope("openid"), listener, activity);
    }

    public void logInWithGoogle(Activity activity, final ILogIn listener) {
        handleSocialLogin(WebAuthProvider.init(auth0).withConnection(SOCIAL_LOGIN_GOOGLE_CONNECTION).withScheme(SOCIA_LOGIN_SCHEME)
                .withScope("openid"), listener, activity);
    }

    public void logInWithTwitter(Activity activity, final ILogIn listener) {
        handleSocialLogin(WebAuthProvider.init(auth0).withConnection(SOCIAL_LOGIN_TWITTER_CONNECTION).withScheme(SOCIA_LOGIN_SCHEME)
                .withScope("openid"), listener, activity);
    }

    public void logInWithLinkedIn(Activity activity, final ILogIn listener) {
        handleSocialLogin(WebAuthProvider.init(auth0).withConnection(SOCIAL_LOGIN_LINKEDIN_CONNECTION).withScheme(SOCIA_LOGIN_SCHEME)
                .withScope("openid"), listener, activity);
    }

    private void getAuth0UserInfo(final Credentials credentials, final ILogIn listener) {
        authentication.userInfo(credentials.getAccessToken()).start(new AuthenticationCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile payload) {
                if (payload != null && payload.getId() != null) {
                    String email = "";
                    //ToDo check if twitter and linkedin return the email property correct
                    if(payload.getId().contains(SOCIAL_LOGIN_GOOGLE_CONNECTION) ||
                            payload.getId().contains(SOCIAL_LOGIN_FACEBOOK_CONNECTION) ||
                            payload.getId().contains(SOCIAL_LOGIN_TWITTER_CONNECTION) ||
                            payload.getId().contains(SOCIAL_LOGIN_LINKEDIN_CONNECTION)) {
                        email = payload.getEmail();
                    }
                    else {
                        /*NOTE: auth0 default login returns the email as name property */
                        email = payload.getName();
                    }

                    initTravelFolderUser(credentials, email, payload, listener);
                } else {
                    listener.onLoginError();
                }
            }

            @Override
            public void onFailure(AuthenticationException error) {
                listener.onLoginError();
            }
        });
    }

    private void handleSocialLogin(WebAuthProvider.Builder builder, final ILogIn listener, Activity activity) {
        builder.start(activity, new AuthCallback() {
            @Override
            public void onFailure(@NonNull Dialog dialog) {
                listener.onLoginError();
            }

            @Override
            public void onFailure(AuthenticationException exception) {
                listener.onLoginError();
            }

            @Override
            public void onSuccess(final @NonNull Credentials credentials) {
                getAuth0UserInfo(credentials, listener);
            }
        });
    }

    private void handleLogin(String idToken, String accessToken, final String email, final String username, final String password, final String fullname, final String auth0Id) {
        User newUser = new User();
        newUser.setAuth0Id(auth0Id);
        newUser.setEmail(email);
        if (password != null && !password.isEmpty()) {
            newUser.setPassword(password);
        }
        if (username == null || username.isEmpty()) {
            newUser.setUsername(getUsernameFromEmail(email));
        } else {
            newUser.setUsername(username);
        }
        if (fullname != null && !fullname.isEmpty()) {
            newUser.setFullName(fullname);
        }
        newUser.setIdToken(idToken);
        newUser.setAccessToken(accessToken);
        ConnectionManager.getInstance().setUser(newUser);
    }

    private void initTravelFolderUser(Credentials credentials, String email, UserProfile payload, final ILogIn listener) {
        BackendManager.getInstance().getBackendService().getTravelfolderUser(C.AUTHORIZATION_BEARER_PREFIX + credentials.getIdToken())
                .enqueue(new Callback<TravelfolderUser>() {
                    @Override
                    public void onResponse(Call<TravelfolderUser> call, Response<TravelfolderUser> response) {
                        TravelfolderUser travelUser = null;
                        if(response.errorBody() == null) {
                            travelUser = response.body();
                            TravelfolderUserRepo.getInstance().setTravelfolderUser(travelUser);
                        }

                        String username = "";
                        if(travelUser != null && travelUser.getPersonalData() != null &&
                                travelUser.getPersonalData().getFirstname() != null && travelUser.getPersonalData().getLastname() != null) {
                            username = travelUser.getPersonalData().getFirstname() + " " + travelUser.getPersonalData().getLastname();
                        } else if(payload.getGivenName() != null && payload.getFamilyName() != null &&
                                !payload.getGivenName().isEmpty() && !payload.getFamilyName().isEmpty()) {
                            username = payload.getGivenName() + " " + payload.getFamilyName();
                        } else {
                            username = email;
                        }

                        handleLogin(credentials.getIdToken(), credentials.getAccessToken(), email, username, null,
                                payload.getName(), payload.getId());

                        initAccessUsers();
                        initChoices();

                        listener.onLoggedIn();
                    }
                    @Override
                    public void onFailure(Call<TravelfolderUser> call, Throwable t) {
                    }
                });
    }

    private void initChoices() {
        BackendManager.getInstance().getBackendService().getChoices()
                .enqueue(new Callback<okhttp3.ResponseBody>() {
                    @Override
                    public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                        if(response.errorBody() == null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(ChoicesList.class, new ChoicesConverter());
                            ChoicesList choicesList = null;
                            try {
                                choicesList = gsonBuilder.create().fromJson(response.body().string(), ChoicesList.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            TravelfolderUserRepo.getInstance().setChoiceList(choicesList);
                        }
                    }

                    @Override
                    public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                    }
                });
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
                            TravelfolderUserRepo.getInstance().setUserAccessManagement(uac);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

    }

    public interface ILogIn {
        void onLoggedIn();

        void onLoginError();

        void onBadCredentials();

    }

    public interface ISingUp {

        void onUsernameFormatIncorrect();

        void onBadUser();

        void onBadUsername();

        void onSingedUp();

        void onSingUpError();

        void onBadConnection();
    }
}

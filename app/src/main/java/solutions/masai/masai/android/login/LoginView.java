package solutions.masai.masai.android.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DialogHelper;

public class LoginView {
    private View rootView;
    private LoginViewListener listener;
    private Context context;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvResetPassword;
    private TextView tvRegister;
    private Button btnContinue;
    private ImageView ivLogInWithFacebook;
    private ImageView ivLogInWithGoogle;
    private ImageView ivLogInWithTwitter;
    private ImageView ivLogInWithLinkedIn;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler(Looper.getMainLooper());

    LoginView(View rootView, Context context, LoginViewListener listener) {
        this.rootView = rootView;
        this.context = context;
        this.listener = listener;
        initViews();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
        progressDialog.setCanceledOnTouchOutside(false);
        etUsername = (EditText) rootView.findViewById(R.id.et_user_name);
        etPassword = (EditText) rootView.findViewById(R.id.et_user_pass);
        tvResetPassword = (TextView) rootView.findViewById(R.id.tv_reset_pass);
        tvRegister = (TextView) rootView.findViewById(R.id.tv_register);
        ivLogInWithGoogle = (ImageView) rootView.findViewById(R.id.iv_log_in_with_google);
        ivLogInWithFacebook = (ImageView) rootView.findViewById(R.id.iv_log_in_with_facebook);
        ivLogInWithTwitter = (ImageView) rootView.findViewById(R.id.iv_log_in_with_twitter);
        ivLogInWithLinkedIn = (ImageView) rootView.findViewById(R.id.iv_log_in_with_ln);
        ivLogInWithTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLogInWithTwitter();
            }
        });
        ivLogInWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLogInWithFacebook();
            }
        });
        ivLogInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLogInWithGoogle();
            }
        });
        ivLogInWithLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLogInWithLinkedIn();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRegisterClicked();
            }
        });
        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPasswordResetClicked();
            }
        });
        btnContinue = (Button) rootView.findViewById(R.id.btn_user_login);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameOrEmail = etUsername.getText().toString();
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError(context.getString(R.string.enter_correct_name));
                } else {
                    if (etPassword.getText().toString().isEmpty()) {
                        etPassword.setError(context.getString(R.string.enter_password));
                    } else {
                        if (Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()) {
                            listener.onContinueLogin(usernameOrEmail, "", etPassword.getText().toString());
                        } else {
                            listener.onContinueLogin("", usernameOrEmail, etPassword.getText().toString());
                        }
                    }
                }
            }
        });
    }

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

    public void showOkDialog(final int message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.dialogOk(context, message);
            }
        });
    }

    interface LoginViewListener {

        void onContinueLogin(String email, String username, String password);

        void onRegisterClicked();

        void onPasswordResetClicked();

        void onLogInWithFacebook();

        void onLogInWithGoogle();

        void onLogInWithTwitter();

        void onLogInWithLinkedIn();
    }
}

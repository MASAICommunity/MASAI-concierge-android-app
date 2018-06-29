package solutions.masai.masai.android.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DialogHelper;

public class RegisterView {
    private View rootView;
    private LoginViewListener listener;
    private Context context;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword2;
    private Button btnContinue;
    private ProgressDialog progressDialog;
    private Handler handler;

    RegisterView(View rootView, Context context, LoginViewListener listener) {
        this.rootView = rootView;
        this.context = context;
        this.listener = listener;
        handler = new Handler(Looper.getMainLooper());
        initViews();
    }

    private void showOkDialog(final int message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.dialogOk(context, message);
            }
        });
    }

    private void initViews() {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
        progressDialog.setCanceledOnTouchOutside(false);
        etFirstName = (EditText) rootView.findViewById(R.id.et_register_firstname);
        etLastName = (EditText) rootView.findViewById(R.id.et_register_lastname);
        etEmail = (EditText) rootView.findViewById(R.id.et_register_email);
        etPassword = (EditText) rootView.findViewById(R.id.et_register_pass);
        etPassword2 = (EditText) rootView.findViewById(R.id.et_register_pass2);
        btnContinue = (Button) rootView.findViewById(R.id.btn_submit_username);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String firstname = etFirstName.getText().toString();
                String lastname = etLastName.getText().toString();
                String password = etPassword.getText().toString();
                String password2 = etPassword2.getText().toString();
                if (firstname.isEmpty()) {
                    etFirstName.setError(context.getString(R.string.fill_out_firstname));
                    return;
                } else {
                    etFirstName.setError(null);
                }
                if (lastname.isEmpty()) {
                    etLastName.setError(context.getString(R.string.fill_out_lastname));
                    return;
                } else {
                    etLastName.setError(null);
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError(context.getString(R.string.enter_correct_email));
                    return;
                } else {
                    etEmail.setError(null);
                }
                if (password.isEmpty()) {
                    etPassword.setError(context.getString(R.string.enter_password));
                    return;
                } else {
                    etPassword.setError(null);
                }
                if (password2.isEmpty()) {
                    etPassword2.setError(context.getString(R.string.enter_password));
                    return;
                } else {
                    etPassword2.setError(null);
                }
                if (!password.equals(password2)) {
                    etPassword.setError(context.getString(R.string.passwords_dont_match));
                    etPassword2.setError(context.getString(R.string.passwords_dont_match));
                    return;
                } else {
                    etPassword2.setError(null);
                    etPassword.setError(null);
                }
                listener.onContinueRegister(email, firstname, lastname, password);
            }
        });
    }

    void showEmailExistError() {
        showOkDialog(R.string.email_exist_error);
    }

    void showConnectivityError() {
        showOkDialog(R.string.connection_restore);
    }

    void showProgress() {
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

    void showServerError() {
        showOkDialog(R.string.server_error);
    }

    void showUsernameTakenError() {
        showOkDialog(R.string.username_taken);
    }

    void showRegisterError() {
        showOkDialog(R.string.register_error);
    }

    void showTravelfolderUserSavingError() {
        showOkDialog(R.string.travelfolder_not_safed);
    }

    void showNoInternetError() {
        showOkDialog(R.string.no_internet_error);
    }

    void showUsernameFormatError() {
        showOkDialog(R.string.username_register_format_error);
    }

    interface LoginViewListener {

        void onContinueRegister(String email, String firstname, String lastname, String password);

        void onBackClicked();
    }
}

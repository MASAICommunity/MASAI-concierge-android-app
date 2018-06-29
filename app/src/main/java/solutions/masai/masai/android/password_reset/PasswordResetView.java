package solutions.masai.masai.android.password_reset;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DialogHelper;

public class PasswordResetView {
    private View rootView;
    private PasswordResetViewListener listener;
    private Context context;
    private EditText etEmail;
    private Button btnContinue;
    private ProgressDialog progressDialog;
    private Handler handler;

    PasswordResetView(View rootView, Context context, PasswordResetViewListener listener) {
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
        etEmail = (EditText) rootView.findViewById(R.id.et_user_email);
        btnContinue = (Button) rootView.findViewById(R.id.btn_user_reset_pass);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameOrEmail = etEmail.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()) {
                    Toast.makeText(context, R.string.enter_correct_email, Toast.LENGTH_LONG).show();
                } else {
                    listener.onEmailProvided(usernameOrEmail);
                }
            }
        });
    }

    private void showOkDialog(final int message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.dialogOk(context, message);
            }
        });
    }

    void showEmailSentInfo() {
        showOkDialog(R.string.reset_pass_email_sent);
    }

    void showConnectionError() {
        showOkDialog(R.string.connection_restore);
    }

    void showEmailErrorInfo() {
        showOkDialog(R.string.reset_pass_email_fail);
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

    interface PasswordResetViewListener {
        void onEmailProvided(String email);
    }
}

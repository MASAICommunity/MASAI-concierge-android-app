package solutions.masai.masai.android.password_reset;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.communication.Auth0Helper;

public class PasswordResetController extends AppCompatActivity implements PasswordResetView.PasswordResetViewListener {
    PasswordResetView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        view = new PasswordResetView(findViewById(android.R.id.content), this, this);
    }

    @Override
    public void onEmailProvided(String email) {
        view.showProgress();
        Auth0Helper.getInstance().resetPasswort(email, new Auth0Helper.IResetCallback() {
            @Override
            public void success() {
                view.hideProgress();
                view.showEmailSentInfo();
            }

            @Override
            public void error() {

                view.hideProgress();
                view.showEmailErrorInfo();
            }
        });
    }
}
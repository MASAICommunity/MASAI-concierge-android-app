package solutions.masai.masai.android.profile.controller.my_data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import solutions.masai.masai.android.R;

public class PaymentController extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydata_payment);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}

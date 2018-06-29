package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Passport;
import solutions.masai.masai.android.databinding.ActivityPassportSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.journey_preferences.PassportSettingsView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tom on 01.09.17.
 */

public class PassportSettingsController extends BaseController implements PassportSettingsView.PassportSettingsViewListener {

	private PassportSettingsView passportSettingsView;
	private ActivityPassportSettingsBinding binding;
	private Passport passportData;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_passport_settings);

		passportData = TravelfolderUserRepo.getInstance().getTravelfolderUser().getPassport();
		if (passportData == null) {
			passportData = new Passport();
		}

		passportSettingsView = new PassportSettingsView(binding.getRoot(), this, this, binding, passportData);

		passportSettingsView.setContent(passportData);
	}

	@Override
	public void updatePassportData(View view) {

		switch (view.getId()) {
		case R.id.activity_passport_number:
			passportData.setNumber(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_passport_country_of_issuance:
			passportData.setCountry(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_passport_city_of_issuance:
			passportData.setCity(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_passport_date_issued:
			try {
				Date convertedDate = DateFormat.getDateInstance().parse(((EditTextRichDrawable) view).getText().toString());
				passportData.setDateIssued(convertedDate);
			}
			catch(Exception e) {
			}
			break;
		case R.id.activity_passport_expiry_date:
			try {
				Date convertedDate = DateFormat.getDateInstance().parse(((EditTextRichDrawable) view).getText().toString());
				passportData.setExpiry(convertedDate);
			}
			catch(Exception e) {
			}
			break;
		default:
			break;
		}

		TravelfolderUserRepo.getInstance().getTravelfolderUser().setPassport(passportData);
		passportSettingsView.setContent(passportData);
	}

	@Override
	public void updateIssueDate(String date) {

		try {
			passportData.setDateIssued(dateFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void expiresDate(String date) {

		try {
			passportData.setExpiry(dateFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

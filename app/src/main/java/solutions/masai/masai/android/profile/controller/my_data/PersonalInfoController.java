package solutions.masai.masai.android.profile.controller.my_data;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.PersonalData;
import solutions.masai.masai.android.databinding.ActivityMydataPersonalInfoBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.my_data.PersonalInfoView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

import java.text.DateFormat;
import java.util.Date;

public class PersonalInfoController extends BaseController implements PersonalInfoView.PersonalInfoViewListener {

	private PersonalInfoView personalInfoView;
	private ActivityMydataPersonalInfoBinding binding;
	private PersonalData personalData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_mydata_personal_info);
		personalInfoView = new PersonalInfoView(binding.getRoot(), this, this, binding);

		personalData = TravelfolderUserRepo.getInstance().getTravelfolderUser().getPersonalData();
		if (personalData == null) {
			personalData = new PersonalData();
		}
		personalInfoView.setContent(personalData);
	}

	@Override
	public void updatePersonalData(View view) {
		switch (view.getId()) {
		case R.id.activity_mydata_personal_info_birthdate:
			try {
				Date convertedDate = DateFormat.getDateInstance().parse(((EditTextRichDrawable) view).getText().toString());
				personalData.setBirthdate(convertedDate);
			}
			catch(Exception e) {

			}
			break;
		case R.id.activity_mydata_personal_info_title:
			personalData.setTitle(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_personal_info_firstname:
			personalData.setFirstname(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_personal_info_middlename:
			personalData.setMiddlename(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_personal_info_lastname:
			personalData.setLastname(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_personal_info_nationality:
			personalData.setNationality(((EditTextRichDrawable) view).getText().toString());
			break;
		default:
			break;
		}

		TravelfolderUserRepo.getInstance().getTravelfolderUser().setPersonalData(personalData);
		personalInfoView.setContent(personalData);
	}
}

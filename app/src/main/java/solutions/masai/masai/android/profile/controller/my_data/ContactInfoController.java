package solutions.masai.masai.android.profile.controller.my_data;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.travelfolder_user.ContactInfo;
import solutions.masai.masai.android.databinding.ActivityMydataContactInfoBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.my_data.ContactInfoView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

public class ContactInfoController extends BaseController implements ContactInfoView.ContactInfoViewListener {

	private ContactInfoView contactInfoView;
	private ActivityMydataContactInfoBinding binding;
	private ContactInfo contactInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_mydata_contact_info);
		contactInfoView = new ContactInfoView(binding.getRoot(), this, this, binding);

		contactInfo = TravelfolderUserRepo.getInstance().getTravelfolderUser().getContactInfo();
		if (contactInfo == null) {
			contactInfo = new ContactInfo();
		}
		contactInfoView.setContent(contactInfo);
	}

	@Override
	public void updateContactInfo(View view) {
		switch (view.getId()) {
		case R.id.activity_mydata_personal_info_email:
			String email2 = ((EditTextRichDrawable) view).getText().toString();

			if (email2=="" || email2=="" || Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
				contactInfo.setPersonalEmail(email2);
			} else {
				contactInfoView.showError(view, getString(R.string.enter_correct_email));
			}
			break;
		case R.id.activity_mydata_contact_info_email:
			String email = ((EditTextRichDrawable) view).getText().toString();

			if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
				contactInfo.setPrimaryEmail(email);
			} else {
				contactInfoView.showError(view, getString(R.string.enter_correct_email));
			}
			break;
		case R.id.activity_mydata_contact_info_phone_number:
			String phonenumber = ((EditTextRichDrawable) view).getText().toString();

			if(phonenumber.length() == 0) {
				contactInfo.setPrimaryPhone(null);
			}

			boolean isError = false;
			try {
				PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
				Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phonenumber, C.COUNTRY_CODE);
				if (phoneUtil.isValidNumber(numberProto)) {
					contactInfo.setPrimaryPhone(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
				} else {
					isError = true;
				}
			} catch (NumberParseException e) {
				C.L("Phone NumberParseException was thrown: " + e.toString());
				isError = true;
			}

			if(isError) {
				contactInfoView.showError(view, getString(R.string.enter_correct_phone));
				binding.activityMydataContactInfoEmail.setError(null);
			}
			break;
		default:
			break;
		}

		TravelfolderUserRepo.getInstance().getTravelfolderUser().setContactInfo(contactInfo);
		contactInfoView.setContent(contactInfo);
	}
}

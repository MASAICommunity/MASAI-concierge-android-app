package solutions.masai.masai.android.profile.view.my_data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.travelfolder_user.ContactInfo;
import solutions.masai.masai.android.databinding.ActivityMydataContactInfoBinding;
import solutions.masai.masai.android.profile.view.BaseView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

/**
 * Created by cWahl on 25.08.2017.
 */

public class ContactInfoView extends BaseView implements View.OnKeyListener {

	private ContactInfoView.ContactInfoViewListener listener;
	private ActivityMydataContactInfoBinding binding;

	public ContactInfoView(View rootView, Context context, ContactInfoView.ContactInfoViewListener listener,
	                       ActivityMydataContactInfoBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.contact_info));

		this.binding = binding;
		this.listener = listener;

		setListener();
	}

	public void setListener() {
		binding.activityMydataContactInfoEmail.setOnFocusChangeListener(this);
		binding.activityMydataPersonalInfoEmail.setOnFocusChangeListener(this);
		binding.activityMydataContactInfoPhoneNumber.setOnFocusChangeListener(this);

		binding.activityMydataContactInfoEmail.setOnKeyListener(this);
		binding.activityMydataPersonalInfoEmail.setOnKeyListener(this);
		binding.activityMydataContactInfoPhoneNumber.setOnKeyListener(this);

		binding.activityMydataContactInfoPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher(C.COUNTRY_CODE));
	}

	public void setContent(ContactInfo contactInfo) {
		if (contactInfo.getPersonalEmail() != null && !contactInfo.getPersonalEmail().isEmpty()) {
			binding.activityMydataPersonalInfoEmail.setText(contactInfo.getPersonalEmail());
		}
		if (contactInfo.getPrimaryEmail() != null && !contactInfo.getPrimaryEmail().isEmpty()) {
			binding.activityMydataContactInfoEmail.setText(contactInfo.getPrimaryEmail());
		} else {
			binding.activityMydataContactInfoEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.border_red));
		}
		if (contactInfo.getPrimaryPhone() != null) {
			binding.activityMydataContactInfoPhoneNumber.setText(contactInfo.getPrimaryPhone());
		}

	}

	public void showError(View view, String message) {
		EditTextRichDrawable field = (EditTextRichDrawable) view;
		field.setError(message);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			listener.updateContactInfo(v);
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			listener.updateContactInfo(view);
		}
	}

	public interface ContactInfoViewListener {

		void updateContactInfo(View view);

		void onBackArrowPressed();
	}
}

package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Esta;

import solutions.masai.masai.android.databinding.ActivityEstaSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.journey_preferences.EstaSettingsView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by tom on 01.09.17.
 */

public class EstaSettingsController extends BaseController implements EstaSettingsView.EstaSettingsViewListener {

	EstaSettingsView estaSettingsView;
	ActivityEstaSettingsBinding binding;
	Esta estaData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_esta_settings);
		estaSettingsView = new EstaSettingsView(binding.getRoot(), this, this, binding);

		estaData = TravelfolderUserRepo.getInstance().getTravelfolderUser().getEsta();
		if (estaData == null) {
			estaData = new Esta();
		}

		estaSettingsView.setContent(estaData);
	}

	@Override
	public void updateEstaInfo(View view) {
		switch (view.getId()) {
		case R.id.activity_esta_settings_text:
			EditTextRichDrawable applicationNumberTextView = (EditTextRichDrawable) view.findViewById(R.id.item_form_text_edit_text);
			estaData.setApplicationNumber(applicationNumberTextView.getText().toString());
			break;
		case R.id.activity_esta_settings_date:
			EditTextRichDrawable validUntilTextView = (EditTextRichDrawable) view.findViewById(R.id.item_form_date_edit_text);
			Date convertedDate = new Date();
			try {
				convertedDate = DateFormat.getDateInstance().parse(validUntilTextView.getText().toString());
				estaData.setValidUntil(convertedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}

		TravelfolderUserRepo.getInstance().getTravelfolderUser().setEsta(estaData);
		estaSettingsView.setContent(estaData);
	}
}

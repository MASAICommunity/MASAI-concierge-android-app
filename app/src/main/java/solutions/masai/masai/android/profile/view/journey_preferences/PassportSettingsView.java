package solutions.masai.masai.android.profile.view.journey_preferences;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.Passport;
import solutions.masai.masai.android.databinding.ActivityPassportSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by tom on 01.09.17.
 */

public class PassportSettingsView extends BaseView implements View.OnKeyListener {

	PassportSettingsViewListener listener;
	ActivityPassportSettingsBinding binding;
	Passport passport;
	Choices countryChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PASSPORT_COUNTRY_OF_ISSUANCE);
	ArrayAdapter<Choice> adapter;

	DatePickerDialog issuanceDate;
	DatePickerDialog expiryDate;

	Spinner countrySpinner;

	Calendar myCalendar = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener issueDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
		                      int dayOfMonth) {

			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateIssueDateView();
		}
	};

	DatePickerDialog.OnDateSetListener expireDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
		                      int dayOfMonth) {

			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateExpireDateView();
		}
	};

	public PassportSettingsView(View rootView, Context context, PassportSettingsView.PassportSettingsViewListener listener,
	                            ActivityPassportSettingsBinding binding, Passport passport) {
		super(rootView, context, context.getResources().getString(R.string.passport));
		this.listener = listener;
		this.binding = binding;
		this.passport = passport;

		setupUI();
		setListeners();
	}

	private void setupUI() {

		issuanceDate = new DatePickerDialog(context, issueDateSetListener, myCalendar
				.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
				myCalendar.get(Calendar.DAY_OF_MONTH));

		expiryDate = new DatePickerDialog(context, expireDateSetListener, myCalendar
				.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
				myCalendar.get(Calendar.DAY_OF_MONTH));

		countrySpinner = binding.activityPassportCountryOfIssuanceSpinner;

		List<Choice> choices = countryChoices.getChoices();
		Locale currentLocale = Locale.getDefault();
		Collections.sort(choices, new Comparator<Choice>() {
			@Override
			public int compare(Choice o1, Choice o2) {
				int cmp = 0;
				if (currentLocale.getISO3Language().equalsIgnoreCase("deu")) {
					cmp = o1.getTextDE().compareTo(o2.getTextDE());
				} else {
					cmp = o1.getTextEN().compareTo(o2.getTextEN());
				}
				return cmp;
			}
		});

		adapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item, choices);
		countrySpinner.setAdapter(adapter);
		countrySpinner.setBackground(null);
		countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				String countryCode = countryChoices.getChoices().get(position).getValue();
				binding.activityPassportCountryOfIssuance.setText(countryCode);
				listener.updatePassportData(binding.activityPassportCountryOfIssuance);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void setListeners() {
		binding.activityPassportNumber.setOnFocusChangeListener(this);
		binding.activityPassportCountryOfIssuance.setOnFocusChangeListener(this);
		binding.activityPassportCityOfIssuance.setOnFocusChangeListener(this);
		binding.activityPassportDateIssued.setOnFocusChangeListener(this);
		binding.activityPassportExpiryDate.setOnFocusChangeListener(this);

		binding.activityPassportNumber.setOnKeyListener(this);
		binding.activityPassportCountryOfIssuance.setOnKeyListener(this);
		binding.activityPassportCityOfIssuance.setOnKeyListener(this);
		binding.activityPassportDateIssued.setOnKeyListener(this);
		binding.activityPassportExpiryDate.setOnKeyListener(this);
	}

	public void setContent(Passport passport) {

		if (passport.getNumber() != null) {
			binding.activityPassportNumber.setText(passport.getNumber());
		}
		if (passport.getCountry() != null) {
			binding.activityPassportCountryOfIssuance.setText(passport.getCountry());
		}
		if (passport.getCity() != null) {
			binding.activityPassportCityOfIssuance.setText(passport.getCity());
		}
		if (passport.getDateIssued() != null) {
			binding.activityPassportDateIssued.setText(DateFormat.getDateInstance().format(passport.getDateIssued()));
		}
		if (passport.getExpiry() != null) {
			binding.activityPassportExpiryDate.setText(DateFormat.getDateInstance().format(passport.getExpiry()));
		}

		for (Choice choice : countryChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(passport.getCountry())) {
				countrySpinner.setSelection(adapter.getPosition(choice));
			}
		}
	}

	private void updateIssueDateView() {
		binding.activityPassportDateIssued.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
		listener.updatePassportData(binding.activityPassportDateIssued);
	}

	private void updateExpireDateView() {
		binding.activityPassportExpiryDate.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
		listener.updatePassportData(binding.activityPassportExpiryDate);
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			listener.updatePassportData(view);
		} else {
			boolean isDatepicker = false;
			if (view == binding.activityPassportDateIssued) {
				issuanceDate.show();
				isDatepicker=true;
			} else if (view == binding.activityPassportExpiryDate) {
				expiryDate.show();
				isDatepicker = true;
			}

			if(isDatepicker) {
				view.setFocusable(false);
				view.setFocusableInTouchMode(true);
			}
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			listener.updatePassportData(v);
		}
		return false;
	}

	public interface PassportSettingsViewListener {

		void updatePassportData(View view);

		void updateIssueDate(String date);

		void expiresDate(String date);

		void onBackArrowPressed();
	}
}

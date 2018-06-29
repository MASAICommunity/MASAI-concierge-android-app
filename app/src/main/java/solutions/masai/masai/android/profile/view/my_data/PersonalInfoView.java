package solutions.masai.masai.android.profile.view.my_data;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.PersonalData;
import solutions.masai.masai.android.databinding.ActivityMydataPersonalInfoBinding;
import solutions.masai.masai.android.profile.view.BaseView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cWahl on 22.08.2017.
 */

public class PersonalInfoView extends BaseView implements View.OnFocusChangeListener, View.OnKeyListener {

	private PersonalInfoView.PersonalInfoViewListener listener;
	private ActivityMydataPersonalInfoBinding binding;
	private Choices countryChoices;
	private Spinner nationalitySpinner;
	private ArrayAdapter adapter;

	Calendar myCalendar = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
		myCalendar.set(Calendar.YEAR, year);
		myCalendar.set(Calendar.MONTH, monthOfYear);
		myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		updateDateView();
	};

	public PersonalInfoView(View rootView, Context context, PersonalInfoView.PersonalInfoViewListener listener,
	                        ActivityMydataPersonalInfoBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.personal_info));
		this.listener = listener;
		this.binding = binding;
		this.countryChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PASSPORT_COUNTRY_OF_ISSUANCE);
		setNationalitySpinner();
		setListeners();
	}

	private void setNationalitySpinner() {
		nationalitySpinner = binding.activityMydataPersonalInfoCountryOfIssuanceSpinner;

		List<Choice> choices = countryChoices.getChoices();
		Collections.sort(choices, new Comparator<Choice>() {
			@Override
			public int compare(Choice o1, Choice o2) {
				return o1.getTextDE().compareTo(o2.getTextDE());
			}
		});
		adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, choices);
		nationalitySpinner.setAdapter(adapter);
		nationalitySpinner.setBackground(null);
		nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String countryCode = countryChoices.getChoices().get(position).getValue();
				binding.activityMydataPersonalInfoNationality.setText(countryCode);
				listener.updatePersonalData(binding.activityMydataPersonalInfoNationality);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				//not needed
			}
		});
	}

	public void setListeners() {
		binding.activityMydataPersonalInfoBirthdate.setOnFocusChangeListener(this);
		binding.activityMydataPersonalInfoTitle.setOnFocusChangeListener(this);
		binding.activityMydataPersonalInfoFirstname.setOnFocusChangeListener(this);
		binding.activityMydataPersonalInfoMiddlename.setOnFocusChangeListener(this);
		binding.activityMydataPersonalInfoLastname.setOnFocusChangeListener(this);
		binding.activityMydataPersonalInfoNationality.setOnFocusChangeListener(this);

		binding.activityMydataPersonalInfoBirthdate.setOnKeyListener(this);
		binding.activityMydataPersonalInfoTitle.setOnKeyListener(this);
		binding.activityMydataPersonalInfoFirstname.setOnKeyListener(this);
		binding.activityMydataPersonalInfoMiddlename.setOnKeyListener(this);
		binding.activityMydataPersonalInfoLastname.setOnKeyListener(this);
		binding.activityMydataPersonalInfoNationality.setOnKeyListener(this);
	}

	public void setContent(PersonalData personalData) {
		if (personalData.getBirthdate() != null) {
			String dateString = DateFormat.getDateInstance().format(personalData.getBirthdate());
			binding.activityMydataPersonalInfoBirthdate.setText(dateString);
		}
		if (personalData.getFirstname() != null) {
			binding.activityMydataPersonalInfoFirstname.setText(personalData.getFirstname());
		}
		if (personalData.getLastname() != null) {
			binding.activityMydataPersonalInfoLastname.setText(personalData.getLastname());
		}
		if (personalData.getMiddlename() != null) {
			binding.activityMydataPersonalInfoMiddlename.setText(personalData.getMiddlename());
		}
		if (personalData.getNationality() != null) {
			binding.activityMydataPersonalInfoNationality.setText(personalData.getNationality());

			for (Choice choice : countryChoices.getChoices()) {
				if (choice.getValue().equalsIgnoreCase(personalData.getNationality())) {
					nationalitySpinner.setSelection(adapter.getPosition(choice));
				}
			}
		}
		if (personalData.getTitle() != null) {
			binding.activityMydataPersonalInfoTitle.setText(personalData.getTitle());
		}
	}

	private void updateDateView() {
		binding.activityMydataPersonalInfoBirthdate.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
		listener.updatePersonalData(binding.activityMydataPersonalInfoBirthdate);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			listener.updatePersonalData(v);
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			listener.updatePersonalData(view);
		}

		if (isFocused && view == binding.activityMydataPersonalInfoBirthdate) {
			new DatePickerDialog(context, dateSetListener, myCalendar
					.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH)).show();

			view.setFocusable(false);
			view.setFocusableInTouchMode(true);
		}
	}

	public interface PersonalInfoViewListener {

		void updatePersonalData(View view);

		void onBackArrowPressed();
	}
}
package solutions.masai.masai.android.profile.view.journey_preferences;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatImageButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Car;
import solutions.masai.masai.android.core.model.travelfolder_user.CarLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.databinding.ActivityCarSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.regex.Pattern;

/**
 * Created by tom on 05.09.17.
 */

public class CarSettingsView extends BaseView {

	private CarSettingsViewListener listener;
	private ActivityCarSettingsBinding binding;
	private Car car;
	LinearLayout layout;

	EditTextRichDrawable bookingClassTextView;
	Spinner bookingClassSpinner;
	Choices bookingClassChoices;
	ArrayAdapter<Choice> bookingClassAdapter;

	EditTextRichDrawable carTypeTextView;
	Spinner carTypeSpinner;
	Choices carTypeChoices;
	ArrayAdapter<Choice> carTypeAdapter;

	TextView loyalityProgramOverTextView;
	TextView loyalityProgramTextView;
	AppCompatImageButton loyalityProgramOverViewButton;

	EditTextRichDrawable additionalInfoTextView;
	TextView loyalityEndTextView;

	public CarSettingsView(View rootView, Context context, CarSettingsViewListener listener, ActivityCarSettingsBinding binding, Car car) {
		super(rootView, context, context.getResources().getString(R.string.car));

		this.listener = listener;
		this.binding = binding;
		this.car = car;

		setupUI();
	}

	private void setupUI() {
		layout = binding.activityCarSettingsLinearLayout;
		LayoutInflater inflater = LayoutInflater.from(context);

		// Header
		View headerView = inflater.inflate(R.layout.item_form_header, layout, false);
		TextViewRichDrawable headerText = (TextViewRichDrawable) headerView.findViewById(R.id.item_form_header_title_text_view);
		Drawable img = ContextCompat.getDrawable(context, R.drawable.ic_action_icon_rental_car);
		img.setBounds( 0, 0, (int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size),
				(int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size ));
		headerText.setCompoundDrawables(img, null, null, null);
		headerText.setText(context.getString(R.string.car));
		layout.addView(headerView);

		// Booking class
		bookingClassChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.CAR_BOOKING_CLASS);
		View bookingClassView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		bookingClassTextView = (EditTextRichDrawable) bookingClassView.findViewById(R.id.item_form_chooser_edit_text);
		bookingClassSpinner = (Spinner) bookingClassView.findViewById(R.id.item_form_chooser_spinner);
		bookingClassAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item,
				bookingClassChoices.getChoices() );
		bookingClassSpinner.setAdapter(bookingClassAdapter);
		TextView bookingClassTitle = (TextView) bookingClassView.findViewById(R.id.item_form_chooser_text_view);
		bookingClassTitle.setText(bookingClassChoices.toString());
		layout.addView(bookingClassView);
		bookingClassSpinner.setBackground(null);
		bookingClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = bookingClassChoices.getChoices().get(position).getValue();
				bookingClassTextView.setText(value);
				car.setBookingClass(value);
				listener.updateCarData(car);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Car type
		carTypeChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.CAR_TYPE);
		View carTypeView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		carTypeTextView = (EditTextRichDrawable) carTypeView.findViewById(R.id.item_form_chooser_edit_text);
		carTypeSpinner = (Spinner) carTypeView.findViewById(R.id.item_form_chooser_spinner);
		carTypeAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item,
				carTypeChoices.getChoices() );
		carTypeSpinner.setAdapter(carTypeAdapter);
		TextView carTypeTitle = (TextView) carTypeView.findViewById(R.id.item_form_chooser_text_view);
		carTypeTitle.setText(carTypeChoices.toString());
		layout.addView(carTypeView);
		carTypeSpinner.setBackground(null);
		carTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = carTypeChoices.getChoices().get(position).getValue();
				carTypeTextView.setText(value);
				car.setType(value);
				listener.updateCarData(car);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// car features
		Choices preferencesChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.CAR_PREFERENCES);
		View preferencesHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(preferencesHeader);
		TextView preferencesTitle = (TextView) preferencesHeader.findViewById(R.id.item_form_section_header_text_view);
		preferencesTitle.setText(preferencesChoices.toString());
		for (Choice preference: preferencesChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.CAR_PREFERENCES.toString() + "|" + preference.getValue());
			checkTextView.setText(preference.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						car.getPreferences().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						car.getPreferences().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateCarData(car);
				}
			});
			layout.addView(checkBoxView);
		}

		// Extras
		Choices extrasChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.CAR_EXTRAS);
		View extrasHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(extrasHeader);
		TextView extrasTitle = (TextView) extrasHeader.findViewById(R.id.item_form_section_header_text_view);
		extrasTitle.setText(extrasChoices.toString());
		for (Choice extra: extrasChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.CAR_EXTRAS.toString() + "|" + extra.getValue());
			checkTextView.setText(extra.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						car.getExtras().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						car.getExtras().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateCarData(car);
				}
			});
			layout.addView(checkBoxView);
		}


		// Car Rental companiesd
		Choices rentalChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.CAR_RENTAL_COMPANIES);
		View rentalHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(rentalHeader);
		TextView rentalTitle = (TextView) rentalHeader.findViewById(R.id.item_form_section_header_text_view);
		rentalTitle.setText(rentalChoices.toString());
		for (Choice rental: rentalChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.CAR_RENTAL_COMPANIES.toString() + "|" + rental.getValue());
			checkTextView.setText(rental.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();
					
					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						car.getRentalCompanies().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						car.getRentalCompanies().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateCarData(car);
				}
			});
			layout.addView(checkBoxView);
		}

		// Loyality program
		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.CAR_LOYALTY_PROGRAM);
		View loyalityProgramHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(loyalityProgramHeader);
		TextView loyalityProgramTitle = (TextView) loyalityProgramHeader.findViewById(R.id.item_form_section_header_text_view);
		loyalityProgramTitle.setText(context.getString(R.string.loyalty_programs));
		View loyalityProgramOverView = inflater.inflate(R.layout.item_form_text_view, layout, false);
		loyalityProgramOverTextView = (TextView) loyalityProgramOverView.findViewById(R.id.item_form_text_view_text_view);
		layout.addView(loyalityProgramOverView);
		loyalityProgramOverViewButton = (AppCompatImageButton) loyalityProgramOverView.findViewById(R.id
				.item_form_text_view_button);
		loyalityProgramOverViewButton.setVisibility(View.INVISIBLE);
		loyalityProgramOverViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loyalityProgramOverTextView.setText("");
				loyalityProgramOverViewButton.setVisibility(View.INVISIBLE);
				car.getLoyaltyPrograms().clear();
				listener.updateCarData(car);
			}
		});
		View loyalityProgramView = inflater.inflate(R.layout.item_form_spinner_text, layout, false);
		Spinner loyalityProgramSpinner = (Spinner) loyalityProgramView.findViewById(R.id.item_form_spinner);
		ArrayAdapter<Choice> loyalityProgramAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item,
				loyalityProgramChoices.getChoices());
		loyalityProgramSpinner.setAdapter(loyalityProgramAdapter);
		loyalityProgramSpinner.setBackground(null);
		loyalityProgramTextView = (EditTextRichDrawable) loyalityProgramView.findViewById(R.id.item_form_text_field);
		loyalityProgramTextView.setHint(context.getString(R.string.form_list_number_field_placeholder));
		AppCompatButton loyalityProgramButton = (AppCompatButton) loyalityProgramView.findViewById(R.id.item_form_button);
		layout.addView(loyalityProgramView);
		final int[] loyalityProgramSpinnerSelection = {-1};
		loyalityProgramSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				loyalityProgramSpinnerSelection[0] = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		loyalityProgramButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (loyalityProgramTextView.getText().length() != 0) {
					// User facing
					String spinnerText = loyalityProgramChoices.getChoices().get(loyalityProgramSpinnerSelection[0]).getValue();
					String textViewText = loyalityProgramTextView.getText().toString();
					CarLoyaltyProgram program = new CarLoyaltyProgram();
					program.setId(spinnerText);
					program.setNumber(textViewText);

					Boolean didChangeProgram = false;
					for (CarLoyaltyProgram loyalityProgram: car.getLoyaltyPrograms()) {
						if (loyalityProgram.getId().equalsIgnoreCase(program.getId())) {
							loyalityProgram.setNumber(program.getNumber());
							didChangeProgram = true;
						}
					}
					if (!didChangeProgram) {
						car.getLoyaltyPrograms().add(program);
					}

					loyalityProgramTextView.setText("");
					loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
					listener.updateCarData(car);
				}
			}
		});


		View layEndView = inflater.inflate(R.layout.item_form_date, layout, false);
		TextView layEndHeaderText = (TextView) layEndView.findViewById(R.id.item_form_date_text_view);
		layEndHeaderText.setText(context.getString(R.string.loyalty_end));
		loyalityEndTextView = (EditTextRichDrawable) layEndView.findViewById(R.id.item_form_date_edit_text);
		loyalityEndTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					car.setLoyalityEnd(loyalityEndTextView.getText().toString());
					listener.updateCarData(car);
				}
			}
		});
		loyalityEndTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					car.setLoyalityEnd(loyalityEndTextView.getText().toString());
					listener.updateCarData(car);
				}
				return false;
			}
		});
		layout.addView(layEndView);

		// Additional Info
		View additionalInfoView = inflater.inflate(R.layout.item_form_text, layout, false);
		TextView additionalInfoHeaderText = (TextView) additionalInfoView.findViewById(R.id
				.item_form_text_text_view);
		additionalInfoHeaderText.setText(context.getString(R.string.anything_else));
		additionalInfoTextView = (EditTextRichDrawable) additionalInfoView.findViewById(R.id
				.item_form_text_edit_text);
		additionalInfoTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					car.setAnythingElse(additionalInfoTextView.getText().toString());
				}
			}
		});
		additionalInfoTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					car.setAnythingElse(additionalInfoTextView.getText().toString());
				}
				return false;
			}
		});
		layout.addView(additionalInfoView);

	}

	public void setContent(Car car) {

		this.car = car;

		for (Choice choice: bookingClassChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(car.getBookingClass())) {
				bookingClassSpinner.setSelection(bookingClassAdapter.getPosition(choice));
			}
		}

		for (Choice choice: carTypeChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(car.getType())) {
				carTypeSpinner.setSelection(carTypeAdapter.getPosition(choice));
			}
		}

		for (String option: car.getPreferences()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.CAR_PREFERENCES.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: car.getExtras()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.CAR_EXTRAS.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: car.getRentalCompanies()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.CAR_RENTAL_COMPANIES.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		String content = "";
		for (CarLoyaltyProgram loyalityProgram: car.getLoyaltyPrograms()) {
			String loyalityProgramString = getStringFromChoiceValue(loyalityProgram);
			content = content + "\n" + loyalityProgramString;
			loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
		}
		loyalityProgramOverTextView.setText(content);

		additionalInfoTextView.setText(car.getAnythingElse());
		loyalityEndTextView.setText(car.getLoyalityEnd());
	}

	private String getStringFromChoiceValue(CarLoyaltyProgram loyalityProgram) {

		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.CAR_LOYALTY_PROGRAM);

		for (Choice choice: loyalityProgramChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(loyalityProgram.getId())) {
				return choice.toString() + " " + loyalityProgram.getNumber();
			}
		}
		return "";
	}

	public interface CarSettingsViewListener {

		void updateCarData(Car car);

		void onBackArrowPressed();
	}
}

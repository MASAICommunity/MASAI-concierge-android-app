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
import solutions.masai.masai.android.core.model.travelfolder_user.AirlineLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.Flight;
import solutions.masai.masai.android.databinding.ActivityFlightSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.regex.Pattern;

/**
 * Created by tom on 04.09.17.
 */

public class FlightSettingsView extends BaseView {

	FlightSettingsViewListener listener;
	ActivityFlightSettingsBinding binding;
	Flight flight;
	LinearLayout layout;

	EditTextRichDrawable bookingClassShortTextView;
	Spinner bookingClassShortSpinner;
	EditTextRichDrawable bookingClassMiddleTextView;
	Spinner bookingClassMiddleSpinner;
	EditTextRichDrawable bookingClassLongTextView;
	Spinner bookingClassLongSpinner;
	EditTextRichDrawable preferredSeatTextView;
	Spinner preferredSeatSpinner;
	EditTextRichDrawable preferredSeatRowTextView;
	Spinner preferredSeatRowSpinner;
	EditTextRichDrawable preferredMealTextView;
	Spinner preferredMealSpinner;

	Choices bookingClassShortChoices;
	ArrayAdapter<Choice> bookingClassShortAdapter;

	Choices bookingClassMiddleChoices;
	ArrayAdapter<Choice> bookingClassMiddleAdapter;

	Choices bookingClassLongChoices;
	ArrayAdapter<Choice> bookingClassLongAdapter;

	Choices preferredSeatChoices;
	ArrayAdapter<Choice> preferredSeatAdapter;

	Choices preferredSeatRowChoices;
	ArrayAdapter<Choice> preferredSeatRowAdapter;

	Choices preferredMealChoices;
	ArrayAdapter<Choice> preferredMealAdapter;

	TextView loyalityProgramOverTextView;
	TextView loyalityProgramTextView;
	AppCompatImageButton loyalityProgramOverViewButton;

	EditTextRichDrawable additionalInfoTextView;
	TextView loyalityEndTextView;


	public FlightSettingsView(View rootView, Context context, FlightSettingsViewListener listener, ActivityFlightSettingsBinding binding,
	 Flight flight) {
		super(rootView, context, context.getResources().getString(R.string.flights));

		this.listener = listener;
		this.binding = binding;
		this.context = context;
		this.flight = flight;

		setupUI();
	}

	private void setupUI() {
		layout = binding.activityFlightSettingsLinearLayout;
		LayoutInflater inflater = LayoutInflater.from(context);

		// Header
		View headerView = inflater.inflate(R.layout.item_form_header, layout, false);
		TextViewRichDrawable headerText = (TextViewRichDrawable) headerView.findViewById(R.id.item_form_header_title_text_view);
		Drawable img = ContextCompat.getDrawable(context, R.drawable.ic_action_icon_flights);
		img.setBounds( 0, 0, (int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size),
				(int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size ));
		headerText.setCompoundDrawables(img, null, null, null);
		headerText.setText(context.getString(R.string.flights));
		layout.addView(headerView);

		// Booking short Haul
		bookingClassShortChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PLANE_BOOKING_CLASS_SHORT_HAUL);
		View bookingClassShortView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		bookingClassShortTextView = (EditTextRichDrawable) bookingClassShortView.findViewById(R.id.item_form_chooser_edit_text);
		bookingClassShortSpinner = (Spinner) bookingClassShortView.findViewById(R.id.item_form_chooser_spinner);
		bookingClassShortAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item,
				bookingClassShortChoices.getChoices() );
		bookingClassShortSpinner.setAdapter(bookingClassShortAdapter);
		bookingClassShortSpinner.setBackground(null);
		TextView bookingClassShortTitle = (TextView) bookingClassShortView.findViewById(R.id.item_form_chooser_text_view);
		bookingClassShortTitle.setText(bookingClassShortChoices.toString());
		layout.addView(bookingClassShortView);
		bookingClassShortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = bookingClassShortChoices.getChoices().get(position).getValue();
				bookingClassShortTextView.setText(value);
				flight.setBookingClassShortHaul(value);
				listener.updateFlightData(flight);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Booking medium Haul
		bookingClassMiddleChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PLANE_BOOKING_CLASS_MEDIUM_HAUL);
		View bookingClassMiddleView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		bookingClassMiddleTextView = (EditTextRichDrawable) bookingClassMiddleView.findViewById(R.id.item_form_chooser_edit_text);
		bookingClassMiddleSpinner = (Spinner) bookingClassMiddleView.findViewById(R.id.item_form_chooser_spinner);
		bookingClassMiddleAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item,
				bookingClassMiddleChoices.getChoices() );
		bookingClassMiddleSpinner.setAdapter(bookingClassMiddleAdapter);
		bookingClassMiddleSpinner.setBackground(null);
		TextView bookingClassMiddleTitle = (TextView) bookingClassMiddleView.findViewById(R.id.item_form_chooser_text_view);
		bookingClassMiddleTitle.setText(bookingClassMiddleChoices.toString());
		layout.addView(bookingClassMiddleView);
		bookingClassMiddleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = bookingClassMiddleChoices.getChoices().get(position).getValue();
				bookingClassMiddleTextView.setText(value);
				flight.setBookingClassMediumHaul(value);
				listener.updateFlightData(flight);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Booking long Haul
		bookingClassLongChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PLANE_BOOKING_CLASS_LONG_HAUL);
		View bookingClassLongView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		bookingClassLongTextView = (EditTextRichDrawable) bookingClassLongView.findViewById(R.id.item_form_chooser_edit_text);
		bookingClassLongSpinner = (Spinner) bookingClassLongView.findViewById(R.id.item_form_chooser_spinner);
		bookingClassLongAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item,
				bookingClassLongChoices.getChoices() );
		bookingClassLongSpinner.setAdapter(bookingClassLongAdapter);
		bookingClassLongSpinner.setBackground(null);
		TextView bookingClassLongTitle = (TextView) bookingClassLongView.findViewById(R.id.item_form_chooser_text_view);
		bookingClassLongTitle.setText(bookingClassLongChoices.toString());
		layout.addView(bookingClassLongView);
		bookingClassLongSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = bookingClassLongChoices.getChoices().get(position).getValue();
				bookingClassLongTextView.setText(value);
				flight.setBookingClassLongHaul(value);
				listener.updateFlightData(flight);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Booking preferred Seat
		preferredSeatChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.FLYING_SEAT);
		View preferredSeatView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		preferredSeatTextView = (EditTextRichDrawable) preferredSeatView.findViewById(R.id.item_form_chooser_edit_text);
		preferredSeatSpinner = (Spinner) preferredSeatView.findViewById(R.id.item_form_chooser_spinner);
		preferredSeatAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item, preferredSeatChoices.getChoices() );
		preferredSeatSpinner.setAdapter(preferredSeatAdapter);
		preferredSeatSpinner.setBackground(null);
		TextView preferredSeatTitle = (TextView) preferredSeatView.findViewById(R.id.item_form_chooser_text_view);
		preferredSeatTitle.setText(preferredSeatChoices.toString());
		layout.addView(preferredSeatView);
		preferredSeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = preferredSeatChoices.getChoices().get(position).getValue();
				preferredSeatTextView.setText(value);
				flight.setSeat(value);
				listener.updateFlightData(flight);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Booking preferred Row for Seat
		preferredSeatRowChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.FLYING_SEAT_ROW);
		View preferredSeatRowView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		preferredSeatRowTextView = (EditTextRichDrawable) preferredSeatRowView.findViewById(R.id.item_form_chooser_edit_text);
		preferredSeatRowSpinner = (Spinner) preferredSeatRowView.findViewById(R.id.item_form_chooser_spinner);
		preferredSeatRowAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item, preferredSeatRowChoices.getChoices() );
		preferredSeatRowSpinner.setAdapter(preferredSeatRowAdapter);
		preferredSeatRowSpinner.setBackground(null);
		TextView preferredSeatRowTitle = (TextView) preferredSeatRowView.findViewById(R.id.item_form_chooser_text_view);
		preferredSeatRowTitle.setText(preferredSeatRowChoices.toString());
		layout.addView(preferredSeatRowView);
		preferredSeatRowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = preferredSeatRowChoices.getChoices().get(position).getValue();
				preferredSeatRowTextView.setText(value);
				flight.setSeatRow(value);
				listener.updateFlightData(flight);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Booking preferred meal
		preferredMealChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.FLYING_MEAL);
		View preferredMealView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		preferredMealTextView = (EditTextRichDrawable) preferredMealView.findViewById(R.id.item_form_chooser_edit_text);
		preferredMealSpinner = (Spinner) preferredMealView.findViewById(R.id.item_form_chooser_spinner);
		preferredMealAdapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item, preferredMealChoices.getChoices() );
		preferredMealSpinner.setAdapter(preferredMealAdapter);
		preferredMealSpinner.setBackground(null);
		TextView preferredMealTitle = (TextView) preferredMealView.findViewById(R.id.item_form_chooser_text_view);
		preferredMealTitle.setText(preferredMealChoices.toString());
		layout.addView(preferredMealView);
		preferredMealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String value = preferredMealChoices.getChoices().get(position).getValue();
				preferredMealTextView.setText(value);
				flight.setMeal(value);
				listener.updateFlightData(flight);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Plane options
		Choices planeOptionsChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PLANE_OPTIONS);
		View optionsHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(optionsHeader);
		TextView optionsTitle = (TextView) optionsHeader.findViewById(R.id.item_form_section_header_text_view);
		optionsTitle.setText(planeOptionsChoices.toString());
		for (Choice planeOption: planeOptionsChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.PLANE_OPTIONS.toString() + "|" + planeOption.getValue());
			checkTextView.setText(planeOption.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						flight.getOptions().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						flight.getOptions().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateFlightData(flight);
				}
			});
			layout.addView(checkBoxView);
		}

		// likeable Airlines
		Choices planeAirlinesChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PLANE_AIRLINES);
		View planeAirlinesHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(planeAirlinesHeader);
		TextView planeAirlinesTitle = (TextView) planeAirlinesHeader.findViewById(R.id.item_form_section_header_text_view);
		planeAirlinesTitle.setText(planeAirlinesChoices.toString());
		for (Choice planeAirline: planeAirlinesChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.PLANE_AIRLINES.toString() + "|" + planeAirline.getValue());
			checkTextView.setText(planeAirline.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						flight.getAirlines().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						flight.getAirlines().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateFlightData(flight);
				}
			});
			layout.addView(checkBoxView);
		}

		// blacklisted and maybe not so likable Airlines
		Choices planeAirlinesBlacklistChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.PLANE_AIRLINES_BLACKLIST);
		View planeAirlinesBlacklistHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(planeAirlinesBlacklistHeader);
		TextView planeAirlinesBlacklistTitle = (TextView) planeAirlinesBlacklistHeader.findViewById(R.id.item_form_section_header_text_view);
		planeAirlinesBlacklistTitle.setText(planeAirlinesBlacklistChoices.toString());
		for (Choice planeAirline: planeAirlinesBlacklistChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.PLANE_AIRLINES_BLACKLIST.toString() + "|" + planeAirline.getValue());
			checkTextView.setText(planeAirline.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						flight.getAirlinesBlacklist().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						flight.getAirlinesBlacklist().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateFlightData(flight);
				}
			});
			layout.addView(checkBoxView);
		}

		// Loyality program
		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.AIRLINE_LOYALTY_PROGRAM);
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
				flight.getAirlineLoyaltyPrograms().clear();
				listener.updateFlightData(flight);
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
					AirlineLoyaltyProgram program = new AirlineLoyaltyProgram();
					program.setId(spinnerText);
					program.setNumber(textViewText);

					Boolean didChangeProgram = false;
					for (AirlineLoyaltyProgram loyalityProgram: flight.getAirlineLoyaltyPrograms()) {
						if (loyalityProgram.getId().equalsIgnoreCase(program.getId())) {
							loyalityProgram.setNumber(program.getNumber());
							didChangeProgram = true;
						}
					}
					if (!didChangeProgram) {
						flight.getAirlineLoyaltyPrograms().add(program);
					}

					loyalityProgramTextView.setText("");
					loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
					listener.updateFlightData(flight);
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
					flight.setLoyalityEnd(loyalityEndTextView.getText().toString());
					listener.updateFlightData(flight);
				}
			}
		});
		loyalityEndTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					flight.setLoyalityEnd(loyalityEndTextView.getText().toString());
					listener.updateFlightData(flight);
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
					flight.setAnythingElse(additionalInfoTextView.getText().toString());
				}
			}
		});
		additionalInfoTextView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					flight.setAnythingElse(additionalInfoTextView.getText().toString());
				}
				return false;
			}
		});
		layout.addView(additionalInfoView);

	}

	public void setContent(Flight flight) {

		this.flight = flight;

		for (Choice choice: bookingClassShortChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(flight.getBookingClassShortHaul())) {
				bookingClassShortSpinner.setSelection(bookingClassShortAdapter.getPosition(choice));
			}
		}

		for (Choice choice: bookingClassMiddleChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(flight.getBookingClassMediumHaul())) {
				bookingClassMiddleSpinner.setSelection(bookingClassMiddleAdapter.getPosition(choice));
			}
		}

		for (Choice choice: bookingClassLongChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(flight.getBookingClassLongHaul())) {
				bookingClassLongSpinner.setSelection(bookingClassLongAdapter.getPosition(choice));
			}
		}

		for (Choice choice: preferredSeatChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(flight.getSeat())) {
				preferredSeatSpinner.setSelection(preferredSeatAdapter.getPosition(choice));
			}
		}

		for (Choice choice: preferredSeatRowChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(flight.getSeatRow())) {
				preferredSeatRowSpinner.setSelection(preferredSeatRowAdapter.getPosition(choice));
			}
		}

		for (Choice choice: preferredMealChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(flight.getMeal())) {
				preferredMealSpinner.setSelection(preferredMealAdapter.getPosition(choice));
			}
		}

		for (String option: flight.getOptions()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.PLANE_OPTIONS.toString() + "|"
					+option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String airline: flight.getAirlines()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.PLANE_AIRLINES.toString() + "|"
					+airline);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String airlineBlacklist: flight.getAirlinesBlacklist()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.PLANE_AIRLINES_BLACKLIST.toString() + "|"
					+airlineBlacklist);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		String content = "";
		for (AirlineLoyaltyProgram loyalityProgram: flight.getAirlineLoyaltyPrograms()) {
			String loyalityProgramString = getStringFromChoiceValue(loyalityProgram);
			content = content + "\n" + loyalityProgramString;
			loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
		}
		loyalityProgramOverTextView.setText(content);

		additionalInfoTextView.setText(flight.getAnythingElse());
		loyalityEndTextView.setText(flight.getLoyalityEnd());

	}

	private String getStringFromChoiceValue(AirlineLoyaltyProgram loyalityProgram) {

		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.AIRLINE_LOYALTY_PROGRAM);

		for (Choice choice: loyalityProgramChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(loyalityProgram.getId())) {
				return choice.toString() + " " + loyalityProgram.getNumber();
			}
		}
		return "";
	}

	public interface FlightSettingsViewListener {

		void updateFlightData(Flight flight);

		void onBackArrowPressed();
	}

}

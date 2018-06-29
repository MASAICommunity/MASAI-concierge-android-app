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
import solutions.masai.masai.android.core.Utils;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.Train;
import solutions.masai.masai.android.core.model.travelfolder_user.TrainLoyaltyProgram;
import solutions.masai.masai.android.databinding.ActivityRailSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.List;

/**
 * Created by tom on 05.09.17.
 */
public class RailSettingsView extends BaseView implements AdapterView.OnItemSelectedListener, View.OnClickListener {

	//region members
	private RailSettingsViewListener listener;
	private ActivityRailSettingsBinding binding;
	private LinearLayout layout;

	private TextView loyalityProgramOverTextView;
	private AppCompatImageButton loyalityProgramOverViewButton;

	private EditTextRichDrawable additionalInfoTextView;

	private Choices travelClassChoices;
	private EditTextRichDrawable travelClassTextView;
	private Spinner travelClassSpinner;
	private ArrayAdapter<Choice> travelClassAdapter;

	private Choices compartmentTypeChoices;
	private EditTextRichDrawable compartmentTypeTextView;
	private Spinner compartmentTypeSpinner;
	private ArrayAdapter<Choice> compartmentTypeAdapter;

	private Choices seatLocationChoices;
	private EditTextRichDrawable seatLocationTextView;
	private Spinner seatLocationSpinner;
	private ArrayAdapter<Choice> seatLocationAdapter;

	private Choices trainZoneChoices;
	private EditTextRichDrawable trainZoneTextView;
	private Spinner trainZoneSpinner;
	private ArrayAdapter<Choice> trainZoneAdapter;

	private Choices trainMobilityChoices;
	private EditTextRichDrawable trainMobilityTextView;
	private Spinner trainMobilitySpinner;
	private ArrayAdapter<Choice> trainMobilityAdapter;
	TextView loyalityEndTextView;
	//endregion

	public interface RailSettingsViewListener {

		void modifyMultipleChoiceList(String value, ChoiceType choiceType, boolean add);

		void modifyChoiceField(String value, ChoiceType choiceType);

		void modifyAdditionalInfo(String value);
		void modifyLoyalityDate(String value);

		List<TrainLoyaltyProgram> getLoyaltyPrograms();

		void addLoyaltyProgram(TrainLoyaltyProgram program);

		void clearLoyaltyPrograms();

		void onBackArrowPressed();
	}

	public RailSettingsView(View rootView, Context context, RailSettingsViewListener listener, ActivityRailSettingsBinding binding) {
		super(rootView, context, context.getString(R.string.train));
		this.listener = listener;
		this.binding = binding;
		setupUI();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String currentValue = null;
		ChoiceType currentChoiceType = ChoiceType.getByValue(parent.getTag().toString());
		switch (currentChoiceType) {
		case TRAIN_TRAVEL_CLASS:
			currentValue = travelClassChoices.getChoices().get(position).getValue();
			travelClassTextView.setText(currentValue);
			break;
		case TRAIN_COMPARTMENT_TYPE:
			currentValue = compartmentTypeChoices.getChoices().get(position).getValue();
			compartmentTypeTextView.setText(currentValue);
			break;
		case TRAIN_SEAT_LOCATION:
			currentValue = seatLocationChoices.getChoices().get(position).getValue();
			seatLocationTextView.setText(currentValue);
			break;
		case TRAIN_ZONE:
			currentValue = trainZoneChoices.getChoices().get(position).getValue();
			trainZoneTextView.setText(currentValue);
			break;
		case TRAIN_MOBILITY_SERVICE:
			currentValue = trainMobilityChoices.getChoices().get(position).getValue();
			trainMobilityTextView.setText(currentValue);
			break;
		default:
			break;
		}

		listener.modifyChoiceField(currentValue, currentChoiceType);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//not needed in project
	}

	@Override
	public void onClick(View v) {
		clearCurrentFocus();
		AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) v;

		if (checkTextView.isChecked()) {
			checkTextView.setCheckMarkDrawable(null);
			checkTextView.setChecked(false);
		} else {
			checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			checkTextView.setChecked(true);
		}

		String choiceText = Utils.getChoiceTextFromCheckTextViewTag(checkTextView.getTag().toString());
		listener.modifyMultipleChoiceList(choiceText, Utils.getChoiceTypeFromCheckTextViewTag(checkTextView.getTag().toString()),
				checkTextView.isChecked());
	}

	public void setContent(Train train) {
		for (Choice choice : travelClassChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(train.getTravelClass())) {
				travelClassSpinner.setSelection(travelClassAdapter.getPosition(choice));
			}
		}
		for (Choice choice : compartmentTypeChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(train.getCompartmentType())) {
				compartmentTypeSpinner.setSelection(compartmentTypeAdapter.getPosition(choice));
			}
		}
		for (Choice choice : seatLocationChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(train.getSeatLocation())) {
				seatLocationSpinner.setSelection(seatLocationAdapter.getPosition(choice));
			}
		}
		for (Choice choice : trainZoneChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(train.getZone())) {
				trainZoneSpinner.setSelection(trainZoneAdapter.getPosition(choice));
			}
		}
		for (Choice choice : trainMobilityChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(train.getMobilityService())) {
				trainMobilitySpinner.setSelection(trainMobilityAdapter.getPosition(choice));
			}
		}
		for (String option : train.getPreferred()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.TRAIN_PREFERRED.toString() +
					"|" + option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}
		for (String option : train.getSpecificBooking()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.TRAIN_SPECIFIC_BOOKING
					.toString() + "|" + option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}
		for (String option : train.getTicket()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.TRAIN_TICKET.toString()
					+ "|" + option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		StringBuilder strBuilder = new StringBuilder();
		for (TrainLoyaltyProgram loyalityProgram : train.getLoyaltyPrograms()) {
			strBuilder.append("\n" + getStringFromChoiceValue(loyalityProgram));
			loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
		}
		loyalityProgramOverTextView.setText(strBuilder.toString());

		additionalInfoTextView.setText(train.getAnythingElse());
		loyalityEndTextView.setText(train.getLoyalityEnd());
	}

	private void setupUI() {
		layout = binding.activityRailSettingsLinearLayout;
		LayoutInflater inflater = LayoutInflater.from(context);

		// region Header
		View headerView = inflater.inflate(R.layout.item_form_header, layout, false);
		TextViewRichDrawable headerText = (TextViewRichDrawable) headerView.findViewById(R.id.item_form_header_title_text_view);
		Drawable img = ContextCompat.getDrawable(context, R.drawable.ic_action_icon_train);
		img.setBounds(0, 0, (int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size),
				(int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size));
		headerText.setCompoundDrawables(img, null, null, null);
		headerText.setText(context.getString(R.string.train));
		layout.addView(headerView);
		//endregion

		//region Travel class choices
		travelClassChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_TRAVEL_CLASS);
		View travelClassView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		travelClassTextView = (EditTextRichDrawable) travelClassView.findViewById(R.id.item_form_chooser_edit_text);
		travelClassSpinner = (Spinner) travelClassView.findViewById(R.id.item_form_chooser_spinner);
		travelClassSpinner.setTag(ChoiceType.TRAIN_TRAVEL_CLASS);
		travelClassAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, travelClassChoices.getChoices());
		travelClassSpinner.setAdapter(travelClassAdapter);
		travelClassSpinner.setBackground(null);
		TextView travelClassTitle = (TextView) travelClassView.findViewById(R.id.item_form_chooser_text_view);
		travelClassTitle.setText(travelClassChoices.toString());
		layout.addView(travelClassView);
		travelClassSpinner.setOnItemSelectedListener(this);
		//endregion

		//region compartment type choices
		compartmentTypeChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_COMPARTMENT_TYPE);
		View compartmentTypeView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		compartmentTypeTextView = (EditTextRichDrawable) compartmentTypeView.findViewById(R.id.item_form_chooser_edit_text);
		compartmentTypeSpinner = (Spinner) compartmentTypeView.findViewById(R.id.item_form_chooser_spinner);
		compartmentTypeSpinner.setTag(ChoiceType.TRAIN_COMPARTMENT_TYPE);
		compartmentTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
				compartmentTypeChoices.getChoices());
		compartmentTypeSpinner.setAdapter(compartmentTypeAdapter);
		compartmentTypeSpinner.setBackground(null);
		TextView compartmentTypeTitle = (TextView) compartmentTypeView.findViewById(R.id.item_form_chooser_text_view);
		compartmentTypeTitle.setText(compartmentTypeChoices.toString());
		layout.addView(compartmentTypeView);
		compartmentTypeSpinner.setOnItemSelectedListener(this);
		//endregion

		//region Seat location choices
		seatLocationChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_SEAT_LOCATION);
		View seatLocationView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		seatLocationTextView = (EditTextRichDrawable) seatLocationView.findViewById(R.id.item_form_chooser_edit_text);
		seatLocationSpinner = (Spinner) seatLocationView.findViewById(R.id.item_form_chooser_spinner);
		seatLocationSpinner.setTag(ChoiceType.TRAIN_SEAT_LOCATION);
		seatLocationAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
				seatLocationChoices.getChoices());
		seatLocationSpinner.setAdapter(seatLocationAdapter);
		seatLocationSpinner.setBackground(null);
		TextView seatLocationTitle = (TextView) seatLocationView.findViewById(R.id.item_form_chooser_text_view);
		seatLocationTitle.setText(seatLocationChoices.toString());
		layout.addView(seatLocationView);
		seatLocationSpinner.setOnItemSelectedListener(this);
		//endregion

		//region train zone
		trainZoneChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_ZONE);
		View trainZoneView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		trainZoneTextView = (EditTextRichDrawable) trainZoneView.findViewById(R.id.item_form_chooser_edit_text);
		trainZoneSpinner = (Spinner) trainZoneView.findViewById(R.id.item_form_chooser_spinner);
		trainZoneAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
				trainZoneChoices.getChoices());
		trainZoneSpinner.setAdapter(trainZoneAdapter);
		trainZoneSpinner.setTag(ChoiceType.TRAIN_ZONE);
		trainZoneSpinner.setBackground(null);
		TextView trainZoneTitle = (TextView) trainZoneView.findViewById(R.id.item_form_chooser_text_view);
		trainZoneTitle.setText(trainZoneChoices.toString());
		layout.addView(trainZoneView);
		trainZoneSpinner.setOnItemSelectedListener(this);
		//endregion

		//region train preferred
		Choices trainPreferredChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_PREFERRED);
		View trainPreferredHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(trainPreferredHeader);
		TextView trainPreferredTitle = (TextView) trainPreferredHeader.findViewById(R.id.item_form_section_header_text_view);
		trainPreferredTitle.setText(trainPreferredChoices.toString());
		for (Choice option : trainPreferredChoices.getChoices()) {
			layout.addView(getCheckBoxView(option, ChoiceType.TRAIN_PREFERRED));
		}
		//endregion

		//region train booking
		Choices trainSpecificChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_SPECIFIC_BOOKING);
		View trainSpecificHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(trainSpecificHeader);
		TextView trainSpecificTitle = (TextView) trainSpecificHeader.findViewById(R.id.item_form_section_header_text_view);
		trainSpecificTitle.setText(trainSpecificChoices.toString());
		for (Choice option : trainSpecificChoices.getChoices()) {
			layout.addView(getCheckBoxView(option, ChoiceType.TRAIN_SPECIFIC_BOOKING));
		}
		//endregion

		//region train_mobility_service
		trainMobilityChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_MOBILITY_SERVICE);
		View trainMobilityView = inflater.inflate(R.layout.item_form_chooser, layout, false);
		trainMobilityTextView = (EditTextRichDrawable) trainMobilityView.findViewById(R.id.item_form_chooser_edit_text);
		trainMobilitySpinner = (Spinner) trainMobilityView.findViewById(R.id.item_form_chooser_spinner);
		trainMobilitySpinner.setTag(ChoiceType.TRAIN_MOBILITY_SERVICE);
		trainMobilityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, trainMobilityChoices.getChoices());
		trainMobilitySpinner.setAdapter(trainMobilityAdapter);
		TextView trainMobilityTitle = (TextView) trainMobilityView.findViewById(R.id.item_form_chooser_text_view);
		trainMobilityTitle.setText(trainMobilityChoices.toString());
		layout.addView(trainMobilityView);
		trainMobilitySpinner.setOnItemSelectedListener(this);
		//endregion

		//region train_ticket
		Choices trainTicketChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_TICKET);
		View trainTicketHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(trainTicketHeader);
		TextView trainTicketTitle = (TextView) trainTicketHeader.findViewById(R.id.item_form_section_header_text_view);
		trainTicketTitle.setText(trainTicketChoices.toString());
		for (Choice option : trainTicketChoices.getChoices()) {
			layout.addView(getCheckBoxView(option, ChoiceType.TRAIN_TICKET));
		}
		//endregion

		//region Loyality program
		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.TRAIN_LOYALTY_PROGRAM);
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
		loyalityProgramOverViewButton.setOnClickListener(v -> {
			loyalityProgramOverTextView.setText("");
			loyalityProgramOverViewButton.setVisibility(View.INVISIBLE);
			listener.clearLoyaltyPrograms();
		});
		View loyalityProgramView = inflater.inflate(R.layout.item_form_spinner_text, layout, false);
		Spinner loyalityProgramSpinner = (Spinner) loyalityProgramView.findViewById(R.id.item_form_spinner);
		ArrayAdapter<Choice> loyalityProgramAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
				loyalityProgramChoices.getChoices());
		loyalityProgramSpinner.setAdapter(loyalityProgramAdapter);
		loyalityProgramSpinner.setBackground(null);
		EditTextRichDrawable loyalityProgramTextView = (EditTextRichDrawable) loyalityProgramView.findViewById(R.id.item_form_text_field);
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
				//not used in project
			}
		});
		loyalityProgramButton.setOnClickListener(v -> {

			if (loyalityProgramTextView.getText().length() != 0) {
				// User facing
				String spinnerText = loyalityProgramChoices.getChoices().get(loyalityProgramSpinnerSelection[0]).getValue();
				String textViewText = loyalityProgramTextView.getText().toString();
				TrainLoyaltyProgram program = new TrainLoyaltyProgram();
				program.setId(spinnerText);
				program.setNumber(textViewText);

				Boolean didChangeProgram = false;
				for (TrainLoyaltyProgram loyalityProgram : listener.getLoyaltyPrograms()) {
					if (loyalityProgram.getId().equalsIgnoreCase(program.getId())) {
						loyalityProgram.setNumber(program.getNumber());
						didChangeProgram = true;
					}
				}
				if (!didChangeProgram) {
					listener.addLoyaltyProgram(program);
				}

				loyalityProgramTextView.setText("");
				loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
			}
		});
		//endregion

		View layEndView = inflater.inflate(R.layout.item_form_date, layout, false);
		TextView layEndHeaderText = (TextView) layEndView.findViewById(R.id.item_form_date_text_view);
		layEndHeaderText.setText(context.getString(R.string.loyalty_end));
		loyalityEndTextView = (EditTextRichDrawable) layEndView.findViewById(R.id.item_form_date_edit_text);
		loyalityEndTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					listener.modifyLoyalityDate(loyalityEndTextView.getText().toString());
				}
			}
		});
		loyalityEndTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {

					listener.modifyLoyalityDate(loyalityEndTextView.getText().toString());
				}
				return false;
			}
		});
		layout.addView(layEndView);

		//region Additional Info
		View additionalInfoView = inflater.inflate(R.layout.item_form_text, layout, false);
		TextView additionalInfoHeaderText = (TextView) additionalInfoView.findViewById(R.id
				.item_form_text_text_view);
		additionalInfoHeaderText.setText(context.getString(R.string.anything_else));
		additionalInfoTextView = (EditTextRichDrawable) additionalInfoView.findViewById(R.id
				.item_form_text_edit_text);
		additionalInfoTextView.setOnFocusChangeListener((v, hasFocus) -> {
			if (!hasFocus) {
				listener.modifyAdditionalInfo(additionalInfoTextView.getText().toString());
			}
		});
		additionalInfoTextView.setOnKeyListener((v, keyCode, event) -> {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				listener.modifyAdditionalInfo(additionalInfoTextView.getText().toString());
			}
			return false;
		});
		layout.addView(additionalInfoView);
		//endregion
	}

	private View getCheckBoxView(Choice option, ChoiceType choiceType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
		AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
		checkTextView.setTag(choiceType.toString() + "|" + option.getValue());
		checkTextView.setText(option.toString());

		checkTextView.setOnClickListener(this);
		return checkBoxView;
	}

	private String getStringFromChoiceValue(TrainLoyaltyProgram loyalityProgram) {
		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.TRAIN_LOYALTY_PROGRAM);

		for (Choice choice : loyalityProgramChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(loyalityProgram.getId())) {
				return choice.toString() + " " + loyalityProgram.getNumber();
			}
		}
		return "";
	}
}
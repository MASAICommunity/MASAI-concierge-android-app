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
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.Hotel;
import solutions.masai.masai.android.core.model.travelfolder_user.HotelLoyaltyProgram;
import solutions.masai.masai.android.databinding.ActivityHotelSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.regex.Pattern;

/**
 * Created by tom on 05.09.17.
 */

public class HotelSettingsView extends BaseView {

	HotelSettingsViewListener listener;
	ActivityHotelSettingsBinding binding;
	Hotel hotel;
	LinearLayout layout;

	TextView loyalityProgramOverTextView;
	TextView loyalityProgramTextView;
	TextView loyalityEndTextView;
	AppCompatImageButton loyalityProgramOverViewButton;

	EditTextRichDrawable additionalInfoTextView;

	public HotelSettingsView(View rootView, Context context, HotelSettingsViewListener listener, ActivityHotelSettingsBinding binding,
	                         Hotel hotel) {
		super(rootView, context, context.getResources().getString(R.string.hotel));

		this.listener = listener;
		this.binding = binding;
		this.hotel = hotel;

		setupUI();
	}

	private void setupUI() {
		layout = binding.activityHotelSettingsLinearLayout;
		LayoutInflater inflater = LayoutInflater.from(context);

		// Header
		View headerView = inflater.inflate(R.layout.item_form_header, layout, false);
		TextViewRichDrawable headerText = (TextViewRichDrawable) headerView.findViewById(R.id.item_form_header_title_text_view);
		Drawable img = ContextCompat.getDrawable(context, R.drawable.ic_action_icon_hotel);
		img.setBounds( 0, 0, (int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size),
				(int) context.getResources().getDimension(R.dimen.textview_profile_drawable_size ));
		headerText.setCompoundDrawables(img, null, null, null);
		headerText.setText(context.getString(R.string.hotel));
		layout.addView(headerView);

		// Category
		Choices hotelCategoryChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_CATEGORIES);
		View hotelCategoryHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(hotelCategoryHeader);
		TextView hotelCategoryTitle = (TextView) hotelCategoryHeader.findViewById(R.id.item_form_section_header_text_view);
		hotelCategoryTitle.setText(hotelCategoryChoices.toString());
		for (Choice hotelCategory: hotelCategoryChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_CATEGORIES.toString() + "|" + hotelCategory.getValue());
			checkTextView.setText(hotelCategory.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getCategories().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getCategories().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// Type
		Choices hotelTypeChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_TYPES);
		View hotelTypeHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(hotelTypeHeader);
		TextView hotelTypeTitle = (TextView) hotelTypeHeader.findViewById(R.id.item_form_section_header_text_view);
		hotelTypeTitle.setText(hotelTypeChoices.toString());
		for (Choice hotelType: hotelTypeChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_TYPES.toString() + "|" + hotelType.getValue());
			checkTextView.setText(hotelType.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getTypes().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getTypes().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// Location
		Choices hotelLocationChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_LOCATION);
		View hotelLocationHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(hotelLocationHeader);
		TextView hotelLocationTitle = (TextView) hotelLocationHeader.findViewById(R.id.item_form_section_header_text_view);
		hotelLocationTitle.setText(hotelLocationChoices.toString());
		for (Choice hotelLocation: hotelLocationChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_LOCATION.toString() + "|" + hotelLocation.getValue());
			checkTextView.setText(hotelLocation.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getLocations().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getLocations().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// Bed
		Choices bedChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_BED_TYPES);
		View bedHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(bedHeader);
		TextView bedTitle = (TextView) bedHeader.findViewById(R.id.item_form_section_header_text_view);
		bedTitle.setText(bedChoices.toString());
		for (Choice bedType: bedChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_BED_TYPES.toString() + "|" + bedType.getValue());
			checkTextView.setText(bedType.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getBedTypes().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getBedTypes().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// Roomclass
		Choices roomStandardChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_ROOM_STANDARDS);
		View roomStandardHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(roomStandardHeader);
		TextView roomStandardTitle = (TextView) roomStandardHeader.findViewById(R.id.item_form_section_header_text_view);
		roomStandardTitle.setText(roomStandardChoices.toString());
		for (Choice roomStandard: roomStandardChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_ROOM_STANDARDS.toString() + "|" + roomStandard.getValue());
			checkTextView.setText(roomStandard.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getRoomStandards().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getRoomStandards().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// Location of Room
		Choices roomLocationChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_ROOM_LOCATION);
		View roomLocationHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(roomLocationHeader);
		TextView roomLocationTitle = (TextView) roomLocationHeader.findViewById(R.id.item_form_section_header_text_view);
		roomLocationTitle.setText(roomLocationChoices.toString());
		for (Choice roomLocation: roomLocationChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_ROOM_LOCATION.toString() + "|" + roomLocation.getValue());
			checkTextView.setText(roomLocation.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getRoomLocation().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getRoomLocation().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// interioer
		Choices hotelAmenitiesChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_AMENITIES);
		View hotelAmenitiesHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(hotelAmenitiesHeader);
		TextView hotelAmenitiesTitle = (TextView) hotelAmenitiesHeader.findViewById(R.id.item_form_section_header_text_view);
		hotelAmenitiesTitle.setText(hotelAmenitiesChoices.toString());
		for (Choice hotelAmenity: hotelAmenitiesChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_AMENITIES.toString() + "|" + hotelAmenity.getValue());
			checkTextView.setText(hotelAmenity.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getAmenities().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getAmenities().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// likable hotels
		Choices chainChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_CHAINS);
		View chainHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(chainHeader);
		TextView chainTitle = (TextView) chainHeader.findViewById(R.id.item_form_section_header_text_view);
		chainTitle.setText(chainChoices.toString());
		for (Choice hotelChain: chainChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_CHAINS.toString() + "|" + hotelChain.getValue());
			checkTextView.setText(hotelChain.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getChains().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getChains().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// unlikable hotels
		Choices chainBlacklistChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.HOTEL_CHAINS_BLACKLIST);
		View chainBlacklistHeader = inflater.inflate(R.layout.item_form_section_header, layout, false);
		layout.addView(chainBlacklistHeader);
		TextView chainBlacklistTitle = (TextView) chainBlacklistHeader.findViewById(R.id.item_form_section_header_text_view);
		chainBlacklistTitle.setText(chainBlacklistChoices.toString());
		for (Choice chainBlacklist: chainBlacklistChoices.getChoices()) {
			View checkBoxView = inflater.inflate(R.layout.item_form_checkbox, layout, false);
			AppCompatCheckedTextView checkTextView = (AppCompatCheckedTextView) checkBoxView.findViewById(R.id.item_form_checked_text_field);
			checkTextView.setTag(ChoiceType.HOTEL_CHAINS_BLACKLIST.toString() + "|" + chainBlacklist.getValue());
			checkTextView.setText(chainBlacklist.toString());
			checkTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearCurrentFocus();

					if (checkTextView.isChecked()) {
						checkTextView.setCheckMarkDrawable(null);
						checkTextView.setChecked(false);
						hotel.getChainsBlacklist().remove((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					} else {
						checkTextView.setCheckMarkDrawable(R.drawable.icon_edit_done);
						checkTextView.setChecked(true);
						hotel.getChainsBlacklist().add((String)checkTextView.getTag().toString().split(Pattern.quote("|"))[1]);
					}
					listener.updateHotelData(hotel);
				}
			});
			layout.addView(checkBoxView);
		}

		// Loyality program
		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.HOTEL_LOYALTY_PROGRAM);
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
				hotel.getLoyaltyPrograms().clear();
				listener.updateHotelData(hotel);
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
					HotelLoyaltyProgram program = new HotelLoyaltyProgram();
					program.setId(spinnerText);
					program.setNumber(textViewText);

					Boolean didChangeProgram = false;
					for (HotelLoyaltyProgram loyalityProgram: hotel.getLoyaltyPrograms()) {
						if (loyalityProgram.getId().equalsIgnoreCase(program.getId())) {
							loyalityProgram.setNumber(program.getNumber());
							didChangeProgram = true;
						}
					}
					if (!didChangeProgram) {
						hotel.getLoyaltyPrograms().add(program);
					}

					loyalityProgramTextView.setText("");
					loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
					listener.updateHotelData(hotel);
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
					hotel.setLoyalityEnd(loyalityEndTextView.getText().toString());
					listener.updateHotelData(hotel);
				}
			}
		});
		loyalityEndTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					hotel.setLoyalityEnd(loyalityEndTextView.getText().toString());
					listener.updateHotelData(hotel);
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
					hotel.setAnythingElse(additionalInfoTextView.getText().toString());
				}
			}
		});
		additionalInfoTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					hotel.setAnythingElse(additionalInfoTextView.getText().toString());
				}
				return false;
			}
		});
		layout.addView(additionalInfoView);

	}

	public void setContent(Hotel hotel) {

		this.hotel = hotel;

		for (String option: hotel.getCategories()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_CATEGORIES.toString() + "|" + option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getTypes()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_TYPES.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getLocations()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_LOCATION.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getBedTypes()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_BED_TYPES.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getRoomStandards()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_ROOM_STANDARDS.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getRoomLocation()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_ROOM_LOCATION.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getAmenities()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_AMENITIES.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getChains()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_CHAINS.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		for (String option: hotel.getChainsBlacklist()) {
			AppCompatCheckedTextView textView = (AppCompatCheckedTextView) layout.findViewWithTag(ChoiceType.HOTEL_CHAINS_BLACKLIST.toString() + "|" +
					option);
			textView.setCheckMarkDrawable(R.drawable.icon_edit_done);
			textView.setChecked(true);
		}

		String content = "";
		for (HotelLoyaltyProgram loyalityProgram: hotel.getLoyaltyPrograms()) {
			String loyalityProgramString = getStringFromChoiceValue(loyalityProgram);
			content = content + "\n" + loyalityProgramString;
			loyalityProgramOverViewButton.setVisibility(View.VISIBLE);
		}
		loyalityProgramOverTextView.setText(content);

		additionalInfoTextView.setText(hotel.getAnythingElse());
		loyalityEndTextView.setText(hotel.getLoyalityEnd());
	}

	private String getStringFromChoiceValue(HotelLoyaltyProgram loyalityProgram) {

		Choices loyalityProgramChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType
				.HOTEL_LOYALTY_PROGRAM);

		for (Choice choice: loyalityProgramChoices.getChoices()) {
			if (choice.getValue().equalsIgnoreCase(loyalityProgram.getId())) {
				return choice.toString() + " " + loyalityProgram.getNumber();
			}
		}
		return "";
	}

	public interface HotelSettingsViewListener {

		void updateHotelData(Hotel hotel);

		void onBackArrowPressed();
	}
}

package solutions.masai.masai.android.profile.adapter.journey_preferences;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.components.form.TextFormItem;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;

import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tom on 01.09.17.
 */

public class PassportSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM_TEXT = 1;
	private static final int TYPE_ITEM_DATE = 2;
	private static final int TYPE_ITEM_SPINNER = 3;

	private static final int DATE_POSITION_PASSPORTISSUANCE = 4;
	private static final int DATE_POSITION_PASSPORTEXPIRY = 5;

	private static final int SPINNER_POSITION_COUNTRY = 2;



	ArrayList<TextFormItem> items;
	LayoutInflater inflater;
	RecyclerView.ViewHolder holder = null;
	Context context;

	public PassportSettingsAdapter(Context context, ArrayList<TextFormItem> items) {
		this.items = items;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = null;

		if (viewType == TYPE_HEADER) {
			v = inflater.inflate(R.layout.item_form_header, parent, false);
			holder = new HeaderViewHolder(v);
		} else if (viewType == TYPE_ITEM_TEXT) {
			v = inflater.inflate(R.layout.item_form_text, parent, false);
			holder = new FormTextViewHolder(v);
		} else if (viewType == TYPE_ITEM_DATE) {
			v = inflater.inflate(R.layout.item_form_date, parent, false);
			holder = new FormDateViewHolder(v);
		} else if (viewType == TYPE_ITEM_SPINNER) {
			v = inflater.inflate(R.layout.item_form_chooser, parent, false);
			holder = new FormChooserViewHolder(v);
		}

		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		TextFormItem item = items.get(position);

		if (holder instanceof HeaderViewHolder) {

			HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
			viewHolder.titleTextView.setText(item.getTitle());

		} else if (holder instanceof FormTextViewHolder) {

			FormTextViewHolder viewHolder = (FormTextViewHolder) holder;
			viewHolder.titleTextView.setText(item.getTitle());
			viewHolder.contentTextView.setText(item.getText());

		} else if (holder instanceof FormDateViewHolder) {

			FormDateViewHolder viewHolder = (FormDateViewHolder) holder;
			viewHolder.titleTextView.setText(item.getTitle());

		} else if (holder instanceof FormChooserViewHolder) {

			FormChooserViewHolder viewHolder = (FormChooserViewHolder) holder;
			viewHolder.titleTextView.setText(item.getTitle());
			viewHolder.contentTextView.setText(item.getText());
		}

	}

	@Override
	public int getItemCount() {
//		return items.size() + 1;
		return items.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position)) {
			return TYPE_HEADER;
		} else if (isPositionDate(position)) {
			return TYPE_ITEM_DATE;
		} else if (isPositionSpinner(position)) {
			return TYPE_ITEM_SPINNER;
		}

		return TYPE_ITEM_TEXT;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	private boolean isPositionDate(int position) {
		if (position == DATE_POSITION_PASSPORTISSUANCE || position == DATE_POSITION_PASSPORTEXPIRY) {
			return true;
		}

		return false;
	}

	private boolean isPositionSpinner(int position) {
		if (position == SPINNER_POSITION_COUNTRY) {
			return true;
		}

		return false;
	}

	private TextFormItem getItem(int position) {
//		return items.get(position -1);
		return items.get(position);
	}


	public class FormTextViewHolder extends RecyclerView.ViewHolder {

		TextView titleTextView;
		EditTextRichDrawable contentTextView;

		public FormTextViewHolder(View itemView) {
			super(itemView);

			titleTextView = (TextView) itemView.findViewById(R.id.item_form_text_text_view);
			contentTextView = (EditTextRichDrawable) itemView.findViewById(R.id.item_form_text_edit_text);
		}
	}

	public class FormDateViewHolder extends RecyclerView.ViewHolder {

		TextView titleTextView;
		EditTextRichDrawable dateTextView;

		Calendar myCalendar = Calendar.getInstance();
		DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
			                      int dayOfMonth) {

				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateDateView();
			}

		};

		public FormDateViewHolder(View itemView) {
			super(itemView);

			titleTextView = (TextView) itemView.findViewById(R.id.item_form_date_text_view);
			dateTextView = (EditTextRichDrawable) itemView.findViewById(R.id.item_form_date_edit_text);
			dateTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new DatePickerDialog(context, dateSetListener, myCalendar
							.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
							myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				}
			});
		}

		private void updateDateView() {
			SimpleDateFormat sdf = new SimpleDateFormat();

			dateTextView.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
		}
	}

	public class FormChooserViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {

		TextView titleTextView;
		EditTextRichDrawable contentTextView;
		Spinner spinner;
		Choices countryChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PASSPORT_COUNTRY_OF_ISSUANCE);

		public FormChooserViewHolder(View itemView) {
			super(itemView);

			titleTextView = (TextView) itemView.findViewById(R.id.item_form_chooser_text_view);
			contentTextView = (EditTextRichDrawable) itemView.findViewById(R.id.item_form_chooser_edit_text);
			spinner = (Spinner) itemView.findViewById(R.id.item_form_chooser_spinner);

			ArrayAdapter<Choice> adapter = new ArrayAdapter<Choice>(context, android.R.layout.simple_spinner_item, countryChoices.getChoices() );
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(this);
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			String countryCode = countryChoices.getChoices().get(position).getValue();
			contentTextView.setText(countryCode);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public class HeaderViewHolder extends RecyclerView.ViewHolder {

		TextViewRichDrawable titleTextView;

		public HeaderViewHolder(View itemView) {
			super(itemView);

			titleTextView = (TextViewRichDrawable) itemView.findViewById(R.id.item_form_header_title_text_view);
		}
	}

}

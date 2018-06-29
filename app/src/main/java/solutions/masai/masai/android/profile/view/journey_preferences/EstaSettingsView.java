package solutions.masai.masai.android.profile.view.journey_preferences;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.model.travelfolder_user.Esta;
import solutions.masai.masai.android.databinding.ActivityEstaSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by tom on 01.09.17.
 */

public class EstaSettingsView extends BaseView implements View.OnKeyListener {

	ActivityEstaSettingsBinding binding;
	EstaSettingsViewListener listener;

	EditTextRichDrawable dateTextView;
	EditTextRichDrawable numberTextView;

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

	public EstaSettingsView(View rootView, Context context, EstaSettingsViewListener listener,
	                        ActivityEstaSettingsBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.esta_header));
		this.listener = listener;
		this.binding = binding;
		this.context = context;

		setupUI();
		setListeners();
	}

	private void setupUI() {

		View headerView = binding.activityEstaSettingsHeader;
		TextView headerTextView = (TextView) headerView.findViewById(R.id.item_form_header_title_text_view);
		headerTextView.setText(context.getString(R.string.esta_header));

		View numberView = binding.activityEstaSettingsText;
		TextView numberHeaderTextView = (TextView) numberView.findViewById(R.id.item_form_text_text_view);
		numberHeaderTextView.setText(context.getString(R.string.application_number));

		numberTextView = (EditTextRichDrawable) numberView.findViewById(R.id.item_form_text_edit_text);

		View dateView = binding.activityEstaSettingsDate;
		TextView dateHeaderTextView = (TextView) dateView.findViewById(R.id.item_form_date_text_view);
		dateHeaderTextView.setText(context.getString(R.string.expiry_date));

		dateTextView = (EditTextRichDrawable) dateView.findViewById(R.id.item_form_date_edit_text);
	}

	public void setListeners() {
		numberTextView.setOnFocusChangeListener(this);
		numberTextView.setOnKeyListener(this);
		dateTextView.setOnFocusChangeListener(this);
		dateTextView.setOnKeyListener(this);
	}

	private void updateDateView() {
		dateTextView.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
	}

	public void setContent(Esta esta) {

		if (esta.getApplicationNumber() != null) {
			numberTextView.setText(esta.getApplicationNumber());
		}

		if (esta.getValidUntil() != null) {
			dateTextView.setText(DateFormat.getDateInstance().format(esta.getValidUntil()));
		}
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			View v = (View) view.getParent();
			listener.updateEstaInfo(v);
		} else if (isFocused && view == dateTextView) {
			DatePickerDialog d = new DatePickerDialog(context, dateSetListener, myCalendar
					.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH));
			d.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					View v = (View) dateTextView.getParent();
					listener.updateEstaInfo(v);
				}
			});
			d.show();

			view.setFocusable(false);
			view.setFocusableInTouchMode(true);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			View v1 = (View) v.getParent();
			listener.updateEstaInfo(v1);
		}
		return false;
	}

	public interface EstaSettingsViewListener {

		void updateEstaInfo(View view);

		void onBackArrowPressed();
	}
}
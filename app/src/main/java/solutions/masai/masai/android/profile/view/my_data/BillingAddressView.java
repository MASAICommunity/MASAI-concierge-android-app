package solutions.masai.masai.android.profile.view.my_data;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.BillingAddress;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.databinding.ActivityMydataBillingAddressBinding;
import solutions.masai.masai.android.profile.view.BaseView;

import java.util.List;

/**
 * Created by tom on 04.09.17.
 */

public class BillingAddressView extends BaseView implements View.OnKeyListener {

	private BillingAddressViewListener listener;
	private ActivityMydataBillingAddressBinding binding;
	private Choices countryChoices;
	private Spinner countrySpinner;
	private ArrayAdapter adapter;

	public BillingAddressView(View rootView, Context context, BillingAddressView.BillingAddressViewListener listener,
	                          ActivityMydataBillingAddressBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.billing_address));

		this.listener = listener;
		this.binding = binding;
		this.countryChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PASSPORT_COUNTRY_OF_ISSUANCE);
		setCountrySpinner();
		setListeners();
	}

	private void setCountrySpinner() {
		countrySpinner = binding.activityMydataBillingAddressCountrySpinner;

		List<Choice> choices = countryChoices.getChoices();
		adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, choices);
		countrySpinner.setAdapter(adapter);
		countrySpinner.setBackground(null);
		countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String countryCode = countryChoices.getChoices().get(position).getValue();
				binding.activityMydataBillingAddressCountry.setText(countryCode);
				listener.updateBillingAddressInfo(binding.activityMydataBillingAddressCountry);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				//not needed
			}
		});
	}

	private void setListeners() {
		binding.activityMydataBillingAddressCompanyName.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressLine1.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressLine2.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressCity.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressState.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressZip.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressCountry.setOnFocusChangeListener(this);
		binding.activityMydataBillingAddressVat.setOnFocusChangeListener(this);

		binding.activityMydataBillingAddressCompanyName.setOnKeyListener(this);
		binding.activityMydataBillingAddressLine1.setOnKeyListener(this);
		binding.activityMydataBillingAddressLine2.setOnKeyListener(this);
		binding.activityMydataBillingAddressCity.setOnKeyListener(this);
		binding.activityMydataBillingAddressState.setOnKeyListener(this);
		binding.activityMydataBillingAddressZip.setOnKeyListener(this);
		binding.activityMydataBillingAddressCountry.setOnKeyListener(this);
		binding.activityMydataBillingAddressVat.setOnKeyListener(this);
	}

	public void setContent(BillingAddress billingAddress) {
		if (billingAddress.getCompanyName() != null) {
			binding.activityMydataBillingAddressCompanyName.setText(billingAddress.getCompanyName());
		}
		if (billingAddress.getAddressLine1() != null) {
			binding.activityMydataBillingAddressLine1.setText(billingAddress.getAddressLine1());
		}
		if (billingAddress.getAddressLine2() != null) {
			binding.activityMydataBillingAddressLine2.setText(billingAddress.getAddressLine2());
		}
		if (billingAddress.getCity() != null) {
			binding.activityMydataBillingAddressCity.setText(billingAddress.getCity());
		}
		if (billingAddress.getState() != null) {
			binding.activityMydataBillingAddressState.setText(billingAddress.getState());
		}
		if (billingAddress.getZip() != null) {
			binding.activityMydataBillingAddressZip.setText(billingAddress.getZip());
		}
		if (billingAddress.getCountry() != null) {
			binding.activityMydataBillingAddressCountry.setText(billingAddress.getCountry());

			for (Choice choice : countryChoices.getChoices()) {
				if (choice.getValue().equalsIgnoreCase(billingAddress.getCountry())) {
					countrySpinner.setSelection(adapter.getPosition(choice));
				}
			}
		}
		if (billingAddress.getVat() != null) {
			binding.activityMydataBillingAddressVat.setText(billingAddress.getVat());
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			listener.updateBillingAddressInfo(v);
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			listener.updateBillingAddressInfo(view);
		}
	}

	public interface BillingAddressViewListener {

		void updateBillingAddressInfo(View view);

		void onBackArrowPressed();
	}
}

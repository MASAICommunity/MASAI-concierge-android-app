package solutions.masai.masai.android.profile.view.my_data;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.PrivateAddress;
import solutions.masai.masai.android.databinding.ActivityMydataPrivateAddressBinding;
import solutions.masai.masai.android.profile.view.BaseView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cWahl on 25.08.2017.
 */

public class PrivateAddressView extends BaseView implements View.OnFocusChangeListener, View.OnKeyListener {

	private PrivateAddressView.PrivateAddressViewListener listener;
	private ActivityMydataPrivateAddressBinding binding;
	private Choices countryChoices;
	private Spinner countrySpinner;
	private ArrayAdapter adapter;

	public PrivateAddressView(View rootView, Context context, PrivateAddressView.PrivateAddressViewListener listener,
	                          ActivityMydataPrivateAddressBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.private_address));
		this.listener = listener;
		this.binding = binding;
		this.countryChoices = TravelfolderUserRepo.getInstance().getChoiceList().getChoices(ChoiceType.PASSPORT_COUNTRY_OF_ISSUANCE);
		setCountrySpinner();
		setListeners();
	}

	private void setCountrySpinner() {
		countrySpinner = binding.activityMydataPrivateAddressCountrySpinner;

		List<Choice> choices = countryChoices.getChoices();
		Collections.sort(choices, new Comparator<Choice>() {
			@Override
			public int compare(Choice o1, Choice o2) {
				return o1.getTextDE().compareTo(o2.getTextDE());
			}
		});
		adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, choices);
		countrySpinner.setAdapter(adapter);
		countrySpinner.setBackground(null);
		countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String countryCode = countryChoices.getChoices().get(position).getValue();
				binding.activityMydataPrivateAddressCountry.setText(countryCode);
				listener.updatePrivateAddress(binding.activityMydataPrivateAddressCountry);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				//not needed
			}
		});
	}

	public void setListeners() {
		binding.activityMydataPrivateAddressLine1.setOnFocusChangeListener(this);
		binding.activityMydataPrivateAddressLine2.setOnFocusChangeListener(this);
		binding.activityMydataPrivateAddressCity.setOnFocusChangeListener(this);
		binding.activityMydataPrivateAddressState.setOnFocusChangeListener(this);
		binding.activityMydataPrivateAddressZip.setOnFocusChangeListener(this);
		binding.activityMydataPrivateAddressCountry.setOnFocusChangeListener(this);

		binding.activityMydataPrivateAddressLine1.setOnKeyListener(this);
		binding.activityMydataPrivateAddressLine2.setOnKeyListener(this);
		binding.activityMydataPrivateAddressCity.setOnKeyListener(this);
		binding.activityMydataPrivateAddressState.setOnKeyListener(this);
		binding.activityMydataPrivateAddressZip.setOnKeyListener(this);
		binding.activityMydataPrivateAddressCountry.setOnKeyListener(this);

		binding.activityMydataPrivateAddressIsBillingAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				listener.updatePrivateAddress(buttonView);
			}
		});
	}

	public void setContent(PrivateAddress privateAddress) {
		if (privateAddress.getAddressLine1() != null) {
			binding.activityMydataPrivateAddressLine1.setText(privateAddress.getAddressLine1());
		}
		if (privateAddress.getAddressLine2() != null) {
			binding.activityMydataPrivateAddressLine2.setText(privateAddress.getAddressLine2());
		}
		if (privateAddress.getCity() != null) {
			binding.activityMydataPrivateAddressCity.setText(privateAddress.getCity());
		}
		if (privateAddress.getState() != null) {
			binding.activityMydataPrivateAddressState.setText(privateAddress.getState());
		}
		if (privateAddress.getZip() != null) {
			binding.activityMydataPrivateAddressZip.setText(privateAddress.getZip());
		}
		if (privateAddress.getCountry() != null) {
			binding.activityMydataPrivateAddressCountry.setText(privateAddress.getCountry());

			for (Choice choice : countryChoices.getChoices()) {
				if (choice.getValue().equalsIgnoreCase(privateAddress.getCountry())) {
					countrySpinner.setSelection(adapter.getPosition(choice));
				}
			}
		}
		if(privateAddress.isBillingAddress()) {
			binding.activityMydataPrivateAddressIsBillingAddress.setChecked(true);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			listener.updatePrivateAddress(v);
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			listener.updatePrivateAddress(view);
		}
	}

	public interface PrivateAddressViewListener {

		void updatePrivateAddress(View view);

		void onBackArrowPressed();
	}
}
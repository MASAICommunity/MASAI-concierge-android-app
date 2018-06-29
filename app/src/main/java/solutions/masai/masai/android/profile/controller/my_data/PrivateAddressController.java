package solutions.masai.masai.android.profile.controller.my_data;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.BillingAddress;
import solutions.masai.masai.android.core.model.travelfolder_user.PrivateAddress;
import solutions.masai.masai.android.databinding.ActivityMydataPrivateAddressBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.my_data.PrivateAddressView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

public class PrivateAddressController extends BaseController implements PrivateAddressView.PrivateAddressViewListener {

	private PrivateAddressView privateAddressView;
	private ActivityMydataPrivateAddressBinding binding;
	private PrivateAddress privateAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_mydata_private_address);

		privateAddressView = new PrivateAddressView(binding.getRoot(), this, this, binding);

		privateAddress = TravelfolderUserRepo.getInstance().getTravelfolderUser().getPrivateAddress();
		if (privateAddress == null) {
			privateAddress = new PrivateAddress();
		}
		privateAddressView.setContent(privateAddress);
	}

	@Override
	public void updatePrivateAddress(View view) {
		switch (view.getId()) {
		case R.id.activity_mydata_private_address_line1:
			privateAddress.setAddressLine1(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_private_address_line2:
			privateAddress.setAddressLine2(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_private_address_city:
			privateAddress.setCity(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_private_address_state:
			privateAddress.setState(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_private_address_zip:
			privateAddress.setZip(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_private_address_country:
			privateAddress.setCountry(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_private_address_is_billing_address:
			boolean isBillingAddress = ((CheckBox) view).isChecked();
			privateAddress.setBillingAddress(isBillingAddress);
			if(isBillingAddress) {
				copyPrivateAddressToBillingAddress();
			}
			break;
		default:
			break;
		}

		TravelfolderUserRepo.getInstance().getTravelfolderUser().setPrivateAddress(privateAddress);
		privateAddressView.setContent(privateAddress);
	}

	private void copyPrivateAddressToBillingAddress() {
		BillingAddress billingAddress = new BillingAddress();
		if(privateAddress.getAddressLine1() != null && !privateAddress.getAddressLine1().isEmpty()) {
			billingAddress.setAddressLine1(privateAddress.getAddressLine1());
		}
		if(privateAddress.getAddressLine2() != null && !privateAddress.getAddressLine2().isEmpty()) {
			billingAddress.setAddressLine2(privateAddress.getAddressLine2());
		}
		if(privateAddress.getCity() != null && !privateAddress.getCity().isEmpty()) {
			billingAddress.setCity(privateAddress.getCity());
		}
		if(privateAddress.getCountry() != null && !privateAddress.getCountry().isEmpty()) {
			billingAddress.setCountry(privateAddress.getCountry());
		}
		if(privateAddress.getState() != null && !privateAddress.getState().isEmpty()) {
			billingAddress.setState(privateAddress.getState());
		}
		if(privateAddress.getZip() != null && !privateAddress.getZip().isEmpty()) {
			billingAddress.setZip(privateAddress.getZip());
		}
		TravelfolderUserRepo.getInstance().getTravelfolderUser().setBillingAddress(billingAddress);
	}

	@Override
	public void onBackArrowPressed() {
		onBackPressed();
	}
}

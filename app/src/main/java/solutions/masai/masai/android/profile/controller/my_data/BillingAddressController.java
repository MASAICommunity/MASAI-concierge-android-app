package solutions.masai.masai.android.profile.controller.my_data;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.BillingAddress;

import solutions.masai.masai.android.databinding.ActivityMydataBillingAddressBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.my_data.BillingAddressView;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

/**
 * Created by cWahl on 25.08.2017.
 */

public class BillingAddressController extends BaseController implements BillingAddressView.BillingAddressViewListener{

	private BillingAddressView billingAddressView;
	private ActivityMydataBillingAddressBinding binding;
	private BillingAddress billingAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_mydata_billing_address);
		billingAddressView = new BillingAddressView(binding.getRoot(), this, this, binding);

		billingAddress = TravelfolderUserRepo.getInstance().getTravelfolderUser().getBillingAddress();
		if (billingAddress == null) {
			billingAddress = new BillingAddress();
		}
		billingAddressView.setContent(billingAddress);
	}

	@Override
	public void updateBillingAddressInfo(View view) {
		switch (view.getId()) {
		case R.id.activity_mydata_billing_address_company_name:
			billingAddress.setCompanyName(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_line1:
			billingAddress.setAddressLine1(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_line2:
			billingAddress.setAddressLine2(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_city:
			billingAddress.setCity(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_state:
			billingAddress.setState(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_zip:
			billingAddress.setZip(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_country:
			billingAddress.setCountry(((EditTextRichDrawable) view).getText().toString());
			break;
		case R.id.activity_mydata_billing_address_vat:
			billingAddress.setVat(((EditTextRichDrawable) view).getText().toString());
			break;
		}

		TravelfolderUserRepo.getInstance().getTravelfolderUser().setBillingAddress(billingAddress);
		billingAddressView.setContent(billingAddress);
	}
}

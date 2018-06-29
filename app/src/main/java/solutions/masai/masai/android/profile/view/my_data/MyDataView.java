package solutions.masai.masai.android.profile.view.my_data;

import android.content.Context;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.databinding.FragmentMyDataBinding;
import solutions.masai.masai.android.profile.view.BaseView;

/**
 * Created by cWahl on 21.08.2017.
 */

public class MyDataView extends BaseView implements View.OnClickListener {

	private MyDataView.MyDataViewListener listener;
	private FragmentMyDataBinding binding;

	public MyDataView(View rootView, Context context, MyDataView.MyDataViewListener listener, FragmentMyDataBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.profile));
		this.listener = listener;
		this.binding = binding;
		setClickListener();
	}

	private void setClickListener() {
		binding.fragmentMyDataOpenPersonalData.setOnClickListener(this);
		binding.fragmentMyDataOpenContactInfo.setOnClickListener(this);
		binding.fragmentMyDataOpenPrivateAddress.setOnClickListener(this);
		binding.fragmentMyDataOpenBillingAddress.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == binding.fragmentMyDataOpenPersonalData.getId()) {
			listener.openPersonalData();
		} else if (view.getId() == binding.fragmentMyDataOpenContactInfo.getId()) {
			listener.openContactInfo();
		} else if (view.getId() == binding.fragmentMyDataOpenBillingAddress.getId()) {
			listener.openBillingAddress();
		} else if (view.getId() == binding.fragmentMyDataOpenPrivateAddress.getId()) {
			listener.openPrivateAddress();
		}
	}

	public interface MyDataViewListener {

		void openPersonalData();

		void openContactInfo();

		void openPrivateAddress();

		void openBillingAddress();

		void onBackArrowPressed();
	}
}

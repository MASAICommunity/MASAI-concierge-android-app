package solutions.masai.masai.android.profile.view.access_privileges;

import android.content.Context;
import android.view.View;

import solutions.masai.masai.android.R;

import solutions.masai.masai.android.core.model.journey.UserAccessManagement;
import solutions.masai.masai.android.databinding.ActivityAccessPrivilegeSettingsBinding;
import solutions.masai.masai.android.profile.view.BaseView;

/**
 * Created by tom on 28.08.17.
 */

public class AccessPrivilegesSettingsView extends BaseView implements View.OnFocusChangeListener {

	private AccessPrivilegesSettingsView.AccessPrivilegesSettingsViewListener listener;
	private ActivityAccessPrivilegeSettingsBinding binding;

	public AccessPrivilegesSettingsView(View rootView, Context context, AccessPrivilegesSettingsView.AccessPrivilegesSettingsViewListener
	 listener, ActivityAccessPrivilegeSettingsBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.profile));
		this.listener = listener;
		this.binding = binding;
		setOnFocusChangedListener();

		binding.activityAccessPrivilegeSettingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.revokePrivilegesButtonPressed();
			}
		});
	}

	public void setOnFocusChangedListener() {
		binding.activityAccessPrivilegeSettingsButton.setOnFocusChangeListener(this);
		binding.activityAccessPrivilegeSettingsImageview.setOnFocusChangeListener(this);
		binding.activityAccessPrivilegeSettingsTextview.setOnFocusChangeListener(this);
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		super.onFocusChange(view, isFocused);

		if (!isFocused) {
			listener.updateView();
		}
	}

	public void setContent(UserAccessManagement userAccessManagement) {

		if (userAccessManagement == null || userAccessManagement.getCount() == 0) {
			binding.activityAccessPrivilegeSettingsImageview.setImageResource(R.drawable.icon_lock_closed);
			binding.activityAccessPrivilegeSettingsButton.setVisibility(View.GONE);
			binding.activityAccessPrivilegeSettingsTextview.setText(R.string.currently_no_access);
		} else {
			binding.activityAccessPrivilegeSettingsImageview.setImageResource(R.drawable.icon_lock_open);
			binding.activityAccessPrivilegeSettingsButton.setVisibility(View.VISIBLE);
			if (userAccessManagement.getCount() == 1) {
				binding.activityAccessPrivilegeSettingsTextview.setText(String.format(context.getString(R.string.currently_access_singular),
						userAccessManagement.getCount()));
			} else {
				binding.activityAccessPrivilegeSettingsTextview.setText(String.format(context.getString(R.string.currently_access_plural),
						userAccessManagement.getCount()));
			}
		}
	}

	public interface AccessPrivilegesSettingsViewListener {

		void revokePrivilegesButtonPressed();

		void updateView();

		void onBackArrowPressed();
	}
}

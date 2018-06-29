package solutions.masai.masai.android.profile.view.journey_preferences;

import android.content.Context;
import android.view.View;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.databinding.ActivityJourneyPreferencesBinding;
import solutions.masai.masai.android.profile.view.BaseView;

/**
 * Created by tom on 12.09.17.
 */

public class JourneyPreferencesView extends BaseView implements View.OnClickListener {

	JourneyPreferencesViewListener listener;
	ActivityJourneyPreferencesBinding binding;

	public JourneyPreferencesView(View rootView, Context context, JourneyPreferencesViewListener listener,
	                              ActivityJourneyPreferencesBinding binding) {
		super(rootView, context, context.getResources().getString(R.string.profile));
		this.listener = listener;
		this.binding = binding;
		setClickListener();
	}

	private void setClickListener() {
		binding.activityJourneyPreferencesPassport.setOnClickListener(this);
		binding.activityJourneyPreferencesEsta.setOnClickListener(this);
		binding.activityJourneyPreferencesHotel.setOnClickListener(this);
		binding.activityJourneyPreferencesFlight.setOnClickListener(this);
		binding.activityJourneyPreferencesCar.setOnClickListener(this);
		binding.activityJourneyPreferencesTrain.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == binding.activityJourneyPreferencesPassport.getId()) {
			listener.openPassportSettings();
		} else if (v.getId() == binding.activityJourneyPreferencesEsta.getId()) {
			listener.openEstaSettings();
		} else if (v.getId() == binding.activityJourneyPreferencesHotel.getId()) {
			listener.openHotelSettings();
		} else if (v.getId() == binding.activityJourneyPreferencesFlight.getId()) {
			listener.openFlightSettings();
		} else if (v.getId() == binding.activityJourneyPreferencesCar.getId()) {
			listener.openCarSettings();
		} else if (v.getId() == binding.activityJourneyPreferencesTrain.getId()) {
			listener.openTrainSettings();
		}

	}

	public interface JourneyPreferencesViewListener {

		void openPassportSettings();

		void openEstaSettings();

		void openHotelSettings();

		void openFlightSettings();

		void openCarSettings();

		void openTrainSettings();
	}
}

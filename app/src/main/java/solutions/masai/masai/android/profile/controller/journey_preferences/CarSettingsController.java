package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Car;
import solutions.masai.masai.android.databinding.ActivityCarSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.journey_preferences.CarSettingsView;

/**
 * Created by tom on 05.09.17.
 */

public class CarSettingsController extends BaseController implements CarSettingsView.CarSettingsViewListener {

	private CarSettingsView carSettingsView;
	private ActivityCarSettingsBinding binding;
	private Car car;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_car_settings);

		car = TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().getCar();

		carSettingsView = new CarSettingsView(binding.getRoot(), this, this, binding, car);
		carSettingsView.setContent(car);
	}

	@Override
	public void updateCarData(Car car) {

		TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().setCar(car);
		carSettingsView.setContent(car);
	}
}

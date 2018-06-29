package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Flight;
import solutions.masai.masai.android.databinding.ActivityFlightSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.journey_preferences.FlightSettingsView;

/**
 * Created by tom on 04.09.17.
 */

public class FlightSettingsController extends BaseController implements FlightSettingsView.FlightSettingsViewListener {

	private FlightSettingsView flightSettingsView;
	private ActivityFlightSettingsBinding binding;
	private Flight flight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_flight_settings);

		flight = TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().getFlights();

		flightSettingsView = new FlightSettingsView(binding.getRoot(), this, this, binding, flight);
		flightSettingsView.setContent(flight);
	}

	@Override
	public void updateFlightData(Flight flight) {

		TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().setFlights(flight);
		flightSettingsView.setContent(flight);
	}
}

package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.Hotel;
import solutions.masai.masai.android.databinding.ActivityHotelSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.journey_preferences.HotelSettingsView;

/**
 * Created by tom on 05.09.17.
 */

public class HotelSettingsController extends BaseController implements HotelSettingsView.HotelSettingsViewListener {

	private HotelSettingsView hotelSettingsView;
	private ActivityHotelSettingsBinding binding;
	private Hotel hotel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_hotel_settings);

		hotel = TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().getHotel();

		hotelSettingsView = new HotelSettingsView(binding.getRoot(), this, this, binding, hotel);
		hotelSettingsView.setContent(hotel);
	}

	@Override
	public void updateHotelData(Hotel hotel) {

		TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().setHotel(hotel);
		hotelSettingsView.setContent(hotel);
	}
}

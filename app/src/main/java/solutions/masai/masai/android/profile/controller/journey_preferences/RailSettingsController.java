package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;
import solutions.masai.masai.android.core.model.travelfolder_user.Train;
import solutions.masai.masai.android.core.model.travelfolder_user.TrainLoyaltyProgram;
import solutions.masai.masai.android.databinding.ActivityRailSettingsBinding;
import solutions.masai.masai.android.profile.controller.BaseController;
import solutions.masai.masai.android.profile.view.journey_preferences.RailSettingsView;

import java.util.List;

/**
 * Created by wolfg on 04.09.17.
 */

public class RailSettingsController extends BaseController implements RailSettingsView.RailSettingsViewListener {

	private RailSettingsView railSettingsView;
	private Train train;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityRailSettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_rail_settings);

		train = TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().getTrain();

		railSettingsView = new RailSettingsView(binding.getRoot(), this, this, binding);
		railSettingsView.setContent(train);
	}

	@Override
	public void modifyMultipleChoiceList(String value, ChoiceType choiceType, boolean add) {
		switch (choiceType) {
		case TRAIN_PREFERRED:
			if (add) {
				train.getPreferred().add(value);
			} else {
				train.getPreferred().remove(value);
			}
			break;
		case TRAIN_SPECIFIC_BOOKING:
			if (add) {
				train.getSpecificBooking().add(value);
			} else {
				train.getSpecificBooking().remove(value);
			}
			break;
		case TRAIN_TICKET:
			if (add) {
				train.getTicket().add(value);
			} else {
				train.getTicket().remove(value);
			}
			break;
		default:
			break;
		}

		updateRailData();
	}

	@Override
	public void modifyChoiceField(String value, ChoiceType choiceType) {
		switch (choiceType) {
		case TRAIN_TRAVEL_CLASS:
			train.setTravelClass(value);
			break;
		case TRAIN_COMPARTMENT_TYPE:
			train.setCompartmentType(value);
			break;
		case TRAIN_SEAT_LOCATION:
			train.setSeatLocation(value);
			break;
		case TRAIN_ZONE:
			train.setZone(value);
			break;
		case TRAIN_MOBILITY_SERVICE:
			train.setMobilityService(value);
			break;
		default:
			break;
		}

		updateRailData();
	}

	@Override
	public void modifyAdditionalInfo(String value) {
		train.setAnythingElse(value);
		updateRailData();
	}

	@Override
	public void modifyLoyalityDate(String value) {
		train.setLoyalityEnd(value);
		updateRailData();
	}

	@Override
	public List<TrainLoyaltyProgram> getLoyaltyPrograms() {
		return train.getLoyaltyPrograms();
	}

	@Override
	public void addLoyaltyProgram(TrainLoyaltyProgram program) {
		train.getLoyaltyPrograms().add(program);
		updateRailData();
	}

	@Override
	public void clearLoyaltyPrograms() {
		train.getLoyaltyPrograms().clear();
		updateRailData();
	}

	private void updateRailData() {
		TravelfolderUserRepo.getInstance().getTravelfolderUser().getJourneyPreferences().setTrain(train);
		railSettingsView.setContent(train);
	}
}
package solutions.masai.masai.android.core;

import solutions.masai.masai.android.core.model.travelfolder_user.ChoicesList;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;
import solutions.masai.masai.android.core.model.journey.UserAccessManagement;

/**
 * Created by cWahl on 24.08.2017.
 */

public class TravelfolderUserRepo {

	private static TravelfolderUserRepo instance;
	private TravelfolderUser travelfolderUser;
	private ChoicesList choiceList;
	private UserAccessManagement userAccessManagement;

	private TravelfolderUserRepo() {
	}

	public static TravelfolderUserRepo getInstance() {
		if (instance == null) {
			instance = new TravelfolderUserRepo();
		}
		return instance;
	}

	//region properties
	public TravelfolderUser getTravelfolderUser() {
		if (travelfolderUser == null) {
			travelfolderUser = new TravelfolderUser();
		}
		return travelfolderUser;
	}

	public void setTravelfolderUser(TravelfolderUser travelfolderUser) {
		this.travelfolderUser = travelfolderUser;
	}

	public void clearTravelfolderUser() {
		this.travelfolderUser = null;
	}

	public ChoicesList getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(ChoicesList choiceList) {
		this.choiceList = choiceList;
	}

	public static void setInstance(TravelfolderUserRepo instance) {
		TravelfolderUserRepo.instance = instance;
	}

	public UserAccessManagement getUserAccessManagement() {
		return userAccessManagement;
	}

	public void setUserAccessManagement(UserAccessManagement userAccessManagement) {
		this.userAccessManagement = userAccessManagement;
	}

	//endregion
}

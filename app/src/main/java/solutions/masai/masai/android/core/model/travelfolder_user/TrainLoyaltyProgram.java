package solutions.masai.masai.android.core.model.travelfolder_user;

/**
 * Created by cWahl on 23.08.2017.
 */

public class TrainLoyaltyProgram {

	private String id;
	private String number;

	//region properties
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	//endregion

	@Override
	public String toString() {

		return id + " " + number;
	}
}

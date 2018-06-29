package solutions.masai.masai.android.core.model.travelfolder_user;

/**
 * Created by cWahl on 22.08.2017.
 */

public class ContactInfo {

	private String primaryEmail;
	private String primaryPhone;
	private String personalEmail;

	//region properties
	public String getPrimaryEmail() {
		return primaryEmail;
	}
	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getPrimaryPhone() {
		return primaryPhone;
	}
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}
	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}
	//endregion
}

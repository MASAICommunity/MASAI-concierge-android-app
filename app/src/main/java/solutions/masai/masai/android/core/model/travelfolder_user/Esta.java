package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.Date;

/**
 * Created by cWahl on 22.08.2017.
 */

public class Esta {

	private Date validUntil;
	private String applicationNumber;

	//region properties
	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	//endregion
}

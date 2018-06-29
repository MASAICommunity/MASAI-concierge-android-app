package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.Date;

/**
 * Created by cWahl on 22.08.2017.
 */

public class Passport {

	private String number;
	private String country;
	private String city;
	private Date dateIssued;
	private Date expiry;

	//region properties
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	//endregion
}

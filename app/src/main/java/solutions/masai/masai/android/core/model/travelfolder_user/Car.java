package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cWahl on 23.08.2017.
 */

public class Car {

	private String bookingClass;
	private String type;
	private List<String> preferences;
	private List<String> extras;
	private List<String> rentalCompanies;
	private List<CarLoyaltyProgram> loyaltyPrograms;
	private String anythingElse;
	private String loyalityEnd;

	//region properties
	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getPreferences() {
		if (preferences == null) {
			preferences = new ArrayList<>();
		}
		return preferences;
	}

	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}

	public List<String> getExtras() {
		if (extras == null) {
			extras = new ArrayList<>();
		}
		return extras;
	}

	public void setExtras(List<String> extras) {
		this.extras = extras;
	}

	public List<String> getRentalCompanies() {
		if (rentalCompanies == null) {
			rentalCompanies = new ArrayList<>();
		}
		return rentalCompanies;
	}

	public void setRentalCompanies(List<String> rentalCompanies) {
		this.rentalCompanies = rentalCompanies;
	}

	public List<CarLoyaltyProgram> getLoyaltyPrograms() {
		if (loyaltyPrograms == null) {
			loyaltyPrograms = new ArrayList<>();
		}
		return loyaltyPrograms;
	}

	public void setLoyaltyPrograms(List<CarLoyaltyProgram> loyaltyPrograms) {
		this.loyaltyPrograms = loyaltyPrograms;
	}

	public String getAnythingElse() {
		return anythingElse;
	}

	public void setAnythingElse(String anythingElse) {
		this.anythingElse = anythingElse;
	}
	//endregion

	public String getLoyalityEnd() {
		return loyalityEnd;
	}

	public void setLoyalityEnd(String loyalityEnd) {
		this.loyalityEnd = loyalityEnd;
	}
	// loyalityEnd
}

package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cWahl on 23.08.2017.
 */

public class Flight {

	private String bookingClassShortHaul;
	private String bookingClassMediumHaul;
	private String bookingClassLongHaul;
	private String seat;
	private String seatRow;
	private String meal;
	private List<String> options;
	private List<String> airlines;
	private List<String> airlinesBlacklist;
	private List<AirlineLoyaltyProgram> airlineLoyaltyPrograms;
	private String anythingElse;
	private String loyalityEnd;


	//region properties
	public String getBookingClassShortHaul() {
		return bookingClassShortHaul;
	}

	public void setBookingClassShortHaul(String bookingClassShortHaul) {
		this.bookingClassShortHaul = bookingClassShortHaul;
	}

	public String getBookingClassMediumHaul() {
		return bookingClassMediumHaul;
	}

	public void setBookingClassMediumHaul(String bookingClassMediumHaul) {
		this.bookingClassMediumHaul = bookingClassMediumHaul;
	}

	public String getBookingClassLongHaul() {
		return bookingClassLongHaul;
	}

	public void setBookingClassLongHaul(String bookingClassLongHaul) {
		this.bookingClassLongHaul = bookingClassLongHaul;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public String getMeal() {
		return meal;
	}

	public void setMeal(String meal) {
		this.meal = meal;
	}

	public List<String> getOptions() {
		if (options == null) {
			options = new ArrayList<>();
		}
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public List<String> getAirlines() {
		if (airlines == null) {
			airlines = new ArrayList<>();
		}
		return airlines;
	}

	public void setAirlines(List<String> airlines) {
		this.airlines = airlines;
	}

	public List<String> getAirlinesBlacklist() {
		if (airlinesBlacklist == null) {
			airlinesBlacklist = new ArrayList<>();
		}
		return airlinesBlacklist;
	}

	public void setAirlinesBlacklist(List<String> airlinesBlacklist) {
		this.airlinesBlacklist = airlinesBlacklist;
	}

	public List<AirlineLoyaltyProgram> getAirlineLoyaltyPrograms() {
		if (airlineLoyaltyPrograms == null) {
			airlineLoyaltyPrograms = new ArrayList<>();
		}
		return airlineLoyaltyPrograms;
	}

	public void setAirlineLoyaltyPrograms(List<AirlineLoyaltyProgram> airlineLoyaltyPrograms) {
		this.airlineLoyaltyPrograms = airlineLoyaltyPrograms;
	}

	public String getAnythingElse() {
		return anythingElse;
	}

	public void setAnythingElse(String anythingElse) {
		this.anythingElse = anythingElse;
	}
	public String getLoyalityEnd() {
		return loyalityEnd;
	}

	public void setLoyalityEnd(String loyalityEnd) {
		this.loyalityEnd = loyalityEnd;
	}
	//endregion
}

package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cWahl on 23.08.2017.
 */

public class Train {

	private String anythingElse;
	private String travelClass;
	private String compartmentType;
	private String seatLocation;
	private String zone;
	private String mobilityService;

	private List<String> preferred;
	private List<String> specificBooking;
	private List<String> ticket;
	private String loyalityEnd;

	private List<TrainLoyaltyProgram> loyaltyPrograms;

	//region properties
	public String getAnythingElse() {
		return anythingElse;
	}

	public void setAnythingElse(String anythingElse) {
		this.anythingElse = anythingElse;
	}

	public String getCompartmentType() {
		return compartmentType;
	}

	public void setCompartmentType(String compartmentType) {
		this.compartmentType = compartmentType;
	}

	public String getSeatLocation() {
		return seatLocation;
	}

	public void setSeatLocation(String seatLocation) {
		this.seatLocation = seatLocation;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getTravelClass() {
		return travelClass;
	}

	public void setTravelClass(String travelClass) {
		this.travelClass = travelClass;
	}

	public List<TrainLoyaltyProgram> getLoyaltyPrograms() {
		if (loyaltyPrograms == null) {
			loyaltyPrograms = new ArrayList<>();
		}
		return loyaltyPrograms;
	}

	public void setLoyaltyPrograms(List<TrainLoyaltyProgram> loyaltyPrograms) {
		this.loyaltyPrograms = loyaltyPrograms;
	}

	public List<String> getPreferred() {
		if(preferred == null) {
			preferred = new ArrayList<>();
		}
		return preferred;
	}

	public void setPreferred(List<String> preferred) {
		this.preferred = preferred;
	}

	public List<String> getSpecificBooking() {
		if(specificBooking == null) {
			specificBooking = new ArrayList<>();
		}
		return specificBooking;
	}

	public void setSpecificBooking(List<String> specificBooking) {
		this.specificBooking = specificBooking;
	}

	public String getMobilityService() {
		return mobilityService;
	}

	public void setMobilityService(String mobilityService) {
		this.mobilityService = mobilityService;
	}

	public List<String> getTicket() {
		if(ticket == null) {
			ticket = new ArrayList<>();
		}
		return ticket;
	}

	public void setTicket(List<String> ticket) {
		this.ticket = ticket;
	}


	public String getLoyalityEnd() {
		return loyalityEnd;
	}

	public void setLoyalityEnd(String loyalityEnd) {
		this.loyalityEnd = loyalityEnd;
	}
//endregion
}

package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cWahl on 23.08.2017.
 */

public class Hotel {

	private List<String> types;
	private List<String> categories;
	private List<String> locations;
	private List<String> bedTypes;
	private List<String> roomStandards;
	private List<String> roomLocations;
	private List<String> amenities;
	private List<String> chains;
	private List<String> chainsBlacklist;
	private List<HotelLoyaltyProgram> loyaltyPrograms;
	private String anythingElse;
	private String loyalityEnd;

	//region properties
	public List<String> getTypes() {
		if (types == null) {
			types = new ArrayList<>();
		}
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public List<String> getCategories() {
		if (categories == null) {
			categories = new ArrayList<>();
		}
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getLocations() {
		if (locations == null) {
			locations = new ArrayList<>();
		}
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public List<String> getBedTypes() {
		if (bedTypes == null) {
			bedTypes = new ArrayList<>();
		}
		return bedTypes;
	}

	public void setBedTypes(List<String> bedTypes) {
		this.bedTypes = bedTypes;
	}

	public List<String> getRoomStandards() {
		if (roomStandards == null) {
			roomStandards = new ArrayList<>();
		}
		return roomStandards;
	}

	public void setRoomStandards(List<String> roomStandards) {
		this.roomStandards = roomStandards;
	}

	public List<String> getRoomLocation() {
		if (roomLocations == null) {
			roomLocations = new ArrayList<>();
		}
		return roomLocations;
	}

	public void setRoomLocation(List<String> roomLocations) {
		this.roomLocations = roomLocations;
	}

	public List<String> getAmenities() {
		if (amenities == null) {
			amenities = new ArrayList<>();
		}
		return amenities;
	}

	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
	}

	public List<String> getChains() {
		if (chains == null) {
			chains = new ArrayList<>();
		}
		return chains;
	}

	public void setChains(List<String> chains) {
		this.chains = chains;
	}

	public List<String> getChainsBlacklist() {
		if (chainsBlacklist == null) {
			chainsBlacklist = new ArrayList<>();
		}
		return chainsBlacklist;
	}

	public void setChainsBlacklist(List<String> chainsBlacklist) {
		this.chainsBlacklist = chainsBlacklist;
	}

	public List<HotelLoyaltyProgram> getLoyaltyPrograms() {
		if (loyaltyPrograms == null) {
			loyaltyPrograms = new ArrayList<>();
		}
		return loyaltyPrograms;
	}

	public void setLoyaltyPrograms(List<HotelLoyaltyProgram> loyaltyPrograms) {
		this.loyaltyPrograms = loyaltyPrograms;
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

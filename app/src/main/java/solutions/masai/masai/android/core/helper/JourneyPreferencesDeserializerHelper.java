package solutions.masai.masai.android.core.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import solutions.masai.masai.android.core.model.travelfolder_user.AirlineLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.Car;
import solutions.masai.masai.android.core.model.travelfolder_user.CarLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.Flight;
import solutions.masai.masai.android.core.model.travelfolder_user.Hotel;
import solutions.masai.masai.android.core.model.travelfolder_user.HotelLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.Train;
import solutions.masai.masai.android.core.model.travelfolder_user.TrainLoyaltyProgram;

import java.util.ArrayList;
import java.util.List;

import static solutions.masai.masai.android.core.helper.DeserializerHelper.getListObject;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getMapObject;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getStringField;

/**
 * Created by cWahl on 23.08.2017.
 */

public class JourneyPreferencesDeserializerHelper {

	private JourneyPreferencesDeserializerHelper() {
	}

	public static Hotel deserializeHotel(JsonObject jsonPreferences) {
		Hotel hotel = new Hotel();
		JsonObject jsonHotel = getMapObject(jsonPreferences, "hotel");

		if (jsonHotel != null) {
			JsonArray jsonRoomStandards = getListObject(jsonHotel, "room_standards");
			if (jsonRoomStandards != null) {
				List<String> roomStandards = new ArrayList<>();
				for (JsonElement jsonRoomStandard : jsonRoomStandards) {
					roomStandards.add(getStringField(jsonRoomStandard.getAsJsonObject()));
				}
				hotel.setRoomStandards(roomStandards);
			}

			JsonArray jsonAmenities = getListObject(jsonHotel, "amenities");
			if (jsonAmenities != null) {
				List<String> amenities = new ArrayList<>();
				for (JsonElement jsonAmenity : jsonAmenities) {
					amenities.add(getStringField(jsonAmenity.getAsJsonObject()));
				}
				hotel.setAmenities(amenities);
			}

			JsonArray jsonTypes = getListObject(jsonHotel, "types");
			if (jsonTypes != null) {
				List<String> types = new ArrayList<>();
				for (JsonElement jsonType : jsonTypes) {
					types.add(getStringField(jsonType.getAsJsonObject()));
				}
				hotel.setTypes(types);
			}

			JsonArray jsonChains = getListObject(jsonHotel, "chains");
			if (jsonChains != null) {
				List<String> chains = new ArrayList<>();
				for (JsonElement jsonChain : jsonChains) {
					chains.add(getStringField(jsonChain.getAsJsonObject()));
				}
				hotel.setChains(chains);
			}

			JsonArray jsonHotelLoyaltyProgram = getListObject(jsonHotel, "loyalty_program");
			if (jsonHotelLoyaltyProgram != null) {
				List<HotelLoyaltyProgram> hotelLoyaltyPrograms = new ArrayList<>();
				for (JsonElement jsonHoteLoayaltyProgram : jsonHotelLoyaltyProgram) {
					HotelLoyaltyProgram hotelLoyaltyProgram = new HotelLoyaltyProgram();
					hotelLoyaltyProgram.setNumber(getStringField(getMapObject(jsonHoteLoayaltyProgram.getAsJsonObject()), "number"));
					hotelLoyaltyProgram.setId(getStringField(getMapObject(jsonHoteLoayaltyProgram.getAsJsonObject()), "id"));
					hotelLoyaltyPrograms.add(hotelLoyaltyProgram);
				}
				hotel.setLoyaltyPrograms(hotelLoyaltyPrograms);
			}

			JsonArray jsonChainsBlacklist = getListObject(jsonHotel, "chains_blacklist");
			if (jsonChainsBlacklist != null) {
				List<String> chainsBlacklist = new ArrayList<>();
				for (JsonElement jsonBlackListed : jsonChainsBlacklist) {
					chainsBlacklist.add(getStringField(jsonBlackListed.getAsJsonObject()));
				}
				hotel.setChainsBlacklist(chainsBlacklist);
			}

			JsonArray jsonHoteLocation = getListObject(jsonHotel, "location");
			if (jsonHoteLocation != null) {
				List<String> hotelLocations = new ArrayList<>();
				for (JsonElement jsonHoteLoc : jsonHoteLocation) {
					hotelLocations.add(getStringField(jsonHoteLoc.getAsJsonObject()));
				}
				hotel.setLocations(hotelLocations);
			}

			JsonArray jsonRoomLocation = getListObject(jsonHotel, "room_location");
			if (jsonRoomLocation != null) {
				List<String> roomLocations = new ArrayList<>();
				for (JsonElement jsonRoomLoc : jsonRoomLocation) {
					roomLocations.add(getStringField(jsonRoomLoc.getAsJsonObject()));
				}
				hotel.setRoomLocation(roomLocations);
			}

			JsonArray jsonBedTypes = getListObject(jsonHotel, "bed_types");
			if (jsonBedTypes != null) {
				List<String> bedTypes = new ArrayList<>();
				for (JsonElement jsonBedType : jsonBedTypes) {
					bedTypes.add(getStringField(jsonBedType.getAsJsonObject()));
				}
				hotel.setBedTypes(bedTypes);
			}

			JsonArray jsonHotelCategories = getListObject(jsonHotel, "categories");
			if (jsonHotelCategories != null) {
				List<String> hotelCategories = new ArrayList<>();
				for (JsonElement jsoHotelCategorye : jsonHotelCategories) {
					hotelCategories.add(getStringField(jsoHotelCategorye.getAsJsonObject()));
				}
				hotel.setCategories(hotelCategories);
			}

			hotel.setAnythingElse(getStringField(jsonHotel, "anything_else"));
		}

		return hotel;
	}

	public static Flight deserializeFlight(JsonObject jsonPreferences) {
		Flight flight = new Flight();
		JsonObject jsonFlight = getMapObject(jsonPreferences, "flights");
		if (jsonFlight != null) {
			flight.setMeal(getStringField(jsonFlight, "meal"));
			flight.setSeat(getStringField(jsonFlight, "seat"));
			flight.setBookingClassShortHaul(getStringField(jsonFlight, "booking_class_short_haul"));
			flight.setSeatRow(getStringField(jsonFlight, "seat_row"));
			flight.setBookingClassLongHaul(getStringField(jsonFlight, "booking_class_long_haul"));
			flight.setBookingClassMediumHaul(getStringField(jsonFlight, "booking_class_medium_haul"));

			JsonArray jsonAirlinesBlacklist = getListObject(jsonFlight, "airlines_blacklist");
			if (jsonAirlinesBlacklist != null) {
				List<String> airlinesBlacklist = new ArrayList<>();
				for (JsonElement jsonAirlineBlacklisted : jsonAirlinesBlacklist) {
					airlinesBlacklist.add(getStringField(jsonAirlineBlacklisted.getAsJsonObject()));
				}
				flight.setAirlinesBlacklist(airlinesBlacklist);
			}

			JsonArray jsonAirlineLoyaltyPrograms = getListObject(jsonFlight, "airline_loyalty_program");
			if (jsonAirlineLoyaltyPrograms != null) {
				List<AirlineLoyaltyProgram> airLineLoyaltyPrograms = new ArrayList<>();
				for (JsonElement jsonAirlineLoyaltyProgram : jsonAirlineLoyaltyPrograms) {
					AirlineLoyaltyProgram airlineLoyaltyProgram = new AirlineLoyaltyProgram();
					airlineLoyaltyProgram.setNumber(getStringField(getMapObject(jsonAirlineLoyaltyProgram.getAsJsonObject()), "number"));
					airlineLoyaltyProgram.setId(getStringField(getMapObject(jsonAirlineLoyaltyProgram.getAsJsonObject()), "id"));
					airLineLoyaltyPrograms.add(airlineLoyaltyProgram);
				}
				flight.setAirlineLoyaltyPrograms(airLineLoyaltyPrograms);
			}

			JsonArray jsonFlightOptions = getListObject(jsonFlight, "options");
			if (jsonFlightOptions != null) {
				List<String> flightOptions = new ArrayList<>();
				for (JsonElement jsonFlightOption : jsonFlightOptions) {
					flightOptions.add(getStringField(jsonFlightOption.getAsJsonObject()));
				}
				flight.setOptions(flightOptions);
			}

			JsonArray jsonAirlines = getListObject(jsonFlight, "airlines");
			if (jsonAirlines != null) {
				List<String> airlines = new ArrayList<>();
				for (JsonElement jsonAirline : jsonAirlines) {
					airlines.add(getStringField(jsonAirline.getAsJsonObject()));
				}
				flight.setAirlines(airlines);
			}

			flight.setAnythingElse(getStringField(jsonFlight, "anything_else"));
		}
		return flight;
	}

	public static Car deserializeCar(JsonObject jsonPreferences) {
		Car car = new Car();
		JsonObject jsonCar = getMapObject(jsonPreferences, "car");

		if (jsonCar != null) {
			car.setBookingClass(getStringField(jsonCar, "booking_class"));
			car.setType(getStringField(jsonCar, "type"));

			JsonArray jsonCarLoyaltyPrograms = getListObject(jsonCar, "loyalty_programs");
			if (jsonCarLoyaltyPrograms != null) {
				List<CarLoyaltyProgram> carLoyaltyPrograms = new ArrayList<>();
				for (JsonElement jsonCarLoyaltyProgram : jsonCarLoyaltyPrograms) {
					CarLoyaltyProgram carLoyaltyProgram = new CarLoyaltyProgram();
					carLoyaltyProgram.setNumber(getStringField(getMapObject(jsonCarLoyaltyProgram.getAsJsonObject()), "number"));
					carLoyaltyProgram.setId(getStringField(getMapObject(jsonCarLoyaltyProgram.getAsJsonObject()), "id"));
					carLoyaltyPrograms.add(carLoyaltyProgram);
				}
				car.setLoyaltyPrograms(carLoyaltyPrograms);
			}

			JsonArray jsonCarPerferences = getListObject(jsonCar, "preferences");
			if (jsonCarPerferences != null) {
				List<String> carPreferences = new ArrayList<>();
				for (JsonElement jsonCarPreference : jsonCarPerferences) {
					carPreferences.add(getStringField(jsonCarPreference.getAsJsonObject()));
				}
				car.setPreferences(carPreferences);
			}

			JsonArray jsonRentalCompanies = getListObject(jsonCar, "rental_companies");
			if (jsonRentalCompanies != null) {
				List<String> rentalCompanies = new ArrayList<>();
				for (JsonElement jsonRentalCompany : jsonRentalCompanies) {
					rentalCompanies.add(getStringField(jsonRentalCompany.getAsJsonObject()));
				}
				car.setRentalCompanies(rentalCompanies);
			}

			JsonArray jsonExtras = getListObject(jsonCar, "extras");
			if (jsonExtras != null) {
				List<String> extras = new ArrayList<>();
				for (JsonElement jsonExtra : jsonExtras) {
					extras.add(getStringField(jsonExtra.getAsJsonObject()));
				}
				car.setExtras(extras);
			}

			car.setAnythingElse(getStringField(jsonCar, "anything_else"));
		}

		return car;
	}

	public static Train deserilizeTrain(JsonObject jsonPreferences) {
		Train train = new Train();
		JsonObject jsonTrain = getMapObject(jsonPreferences, "train");
		if (jsonTrain != null) {
			train.setTravelClass(getStringField(jsonTrain, "travel_class"));
			train.setCompartmentType(getStringField(jsonTrain, "compartment_type"));
			train.setSeatLocation(getStringField(jsonTrain, "seat_location"));
			train.setZone(getStringField(jsonTrain, "zone"));
			train.setMobilityService(getStringField(jsonTrain, "mobility_service"));

			JsonArray jsonPrefs = getListObject(jsonTrain, "preferred_trains");
			if (jsonPrefs != null) {
				List<String> prefs = new ArrayList<>();
				for (JsonElement jsonPref : jsonPrefs) {
					prefs.add(getStringField(jsonPref.getAsJsonObject()));
				}
				train.setPreferred(prefs);
			}

			JsonArray jsonBookings = getListObject(jsonTrain, "train_specific_booking");
			if (jsonBookings != null) {
				List<String> itms = new ArrayList<>();
				for (JsonElement jsonElm : jsonBookings) {
					itms.add(getStringField(jsonElm.getAsJsonObject()));
				}
				train.setSpecificBooking(itms);
			}

			JsonArray jsonTicket = getListObject(jsonTrain, "ticket");
			if (jsonTicket != null) {
				List<String> itms = new ArrayList<>();
				for (JsonElement jsonElm : jsonTicket) {
					itms.add(getStringField(jsonElm.getAsJsonObject()));
				}
				train.setTicket(itms);
			}

			JsonArray jsonTrainLoyaltyPrograms = getListObject(jsonTrain, "loyalty_programs");
			if (jsonTrainLoyaltyPrograms != null) {
				List<TrainLoyaltyProgram> trainLoyaltyPrograms = new ArrayList<>();
				for (JsonElement jsonTrainLoyaltyProgram : jsonTrainLoyaltyPrograms) {
					TrainLoyaltyProgram trainLoyaltyProgram = new TrainLoyaltyProgram();
					trainLoyaltyProgram.setNumber(getStringField(getMapObject(jsonTrainLoyaltyProgram.getAsJsonObject()), "number"));
					trainLoyaltyProgram.setId(getStringField(getMapObject(jsonTrainLoyaltyProgram.getAsJsonObject()), "id"));
					trainLoyaltyPrograms.add(trainLoyaltyProgram);
				}
				train.setLoyaltyPrograms(trainLoyaltyPrograms);

				train.setAnythingElse(getStringField(jsonTrain, "anything_else"));

			}
		}

		return train;
	}
}
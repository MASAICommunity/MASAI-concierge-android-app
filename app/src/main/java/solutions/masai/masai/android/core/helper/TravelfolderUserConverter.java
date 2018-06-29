package solutions.masai.masai.android.core.helper;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import solutions.masai.masai.android.core.model.travelfolder_user.AirlineLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.BillingAddress;
import solutions.masai.masai.android.core.model.travelfolder_user.Car;
import solutions.masai.masai.android.core.model.travelfolder_user.CarLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.ContactInfo;
import solutions.masai.masai.android.core.model.travelfolder_user.Esta;
import solutions.masai.masai.android.core.model.travelfolder_user.Flight;
import solutions.masai.masai.android.core.model.travelfolder_user.Hotel;
import solutions.masai.masai.android.core.model.travelfolder_user.HotelLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.JourneyPreferences;
import solutions.masai.masai.android.core.model.travelfolder_user.Passport;
import solutions.masai.masai.android.core.model.travelfolder_user.PersonalData;
import solutions.masai.masai.android.core.model.travelfolder_user.PrivateAddress;
import solutions.masai.masai.android.core.model.travelfolder_user.PrivatePayment;
import solutions.masai.masai.android.core.model.travelfolder_user.Train;
import solutions.masai.masai.android.core.model.travelfolder_user.TrainLoyaltyProgram;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static solutions.masai.masai.android.core.helper.DeserializerHelper.getBooleanField;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getMapObject;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getNumberField;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getStringField;

/**
 * Created by cWahl on 22.08.2017.
 */

public class TravelfolderUserConverter implements JsonSerializer<TravelfolderUser>, JsonDeserializer<TravelfolderUser> {
	
	private DateFormat dateFormat;

	public DateFormat getDateFormat() {
		if(this.dateFormat == null) {
			 this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		}
		return this.dateFormat;
	}

	private HashMap<String, String> addString(String string){
		HashMap<String, String> ret = new HashMap<>();
		ret.put("S", string);
		return ret;
	}

	private HashMap<String, Boolean> addBoolean(Boolean bool) {
		HashMap<String, Boolean> ret = new HashMap<>();
		ret.put("BOOL", bool);
		return ret;
	}

	private HashMap<String, Object> addMap(Object object) {
		HashMap<String, Object> ret = new HashMap<>();
		ret.put("M", object);
		return ret;
	}
	private HashMap<String, List> addList(List list) {
		HashMap<String, List> ret = new HashMap<>();
		ret.put("L", list);
		return ret;
	}

	private void serializeList(HashMap<String, Object> hash, List<String> list, String serializeName) {
		List<HashMap<String, String>> itms = new LinkedList<>();
		for(String itm : list) {
			itms.add(addString(itm));
		}
		if(!itms.isEmpty()) {
			hash.put(serializeName, addList(itms));
		}
	}
	private void serializeString(HashMap<String, Object> hash, String str, String serializeName) {
		if(str != null && !str.isEmpty()) {
			hash.put(serializeName, addString(str));
		}
	}

	private void serializeBoolean(HashMap<String, Object> hash, Boolean bool, String serializeName) {
		hash.put(serializeName, addBoolean(bool));
	}

	private void serializeDate(HashMap<String, Object> hash, Date date, String serializeName) {
		if(date != null) {
			hash.put(serializeName, addString(getDateFormat().format(date)));
		}
	}

	private HashMap<String, Object> serializeEsta(Esta obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {
			serializeString(ret, obj.getApplicationNumber(), "application_number");
			serializeDate(ret, obj.getValidUntil(), "valid_until");
		}
		return ret;
	}


	private HashMap<String, Object> serializeBilling(BillingAddress obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {
			serializeString(ret, obj.getZip(), "zip");
			serializeString(ret, obj.getCountry(), "country");
			serializeString(ret, obj.getCity(), "city");
			serializeString(ret, obj.getCompanyName(), "company_name");
			serializeString(ret, obj.getAddressLine1(), "address_line_1");
			serializeString(ret, obj.getAddressLine2(), "address_line_2");
			serializeString(ret, obj.getState(), "state");
			serializeString(ret, obj.getVat(), "vat");
		}

		return ret;
	}
	private HashMap<String, Object> serializeContact(ContactInfo obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {
			serializeString(ret, obj.getPrimaryEmail(), "primary_email");
			serializeString(ret, obj.getPrimaryPhone(), "primary_phone");
		}

		return ret;
	}
	private HashMap<String, Object> serializePassport(Passport obj) {
		HashMap<String, Object> ret = new HashMap<>();

		if(obj != null) {
			serializeString(ret, obj.getCountry(), "country");
			serializeString(ret, obj.getNumber(), "number");
			serializeDate(ret, obj.getExpiry(), "expiry");
			serializeString(ret, obj.getCity(), "city");
			serializeDate(ret, obj.getDateIssued(), "date_issued");
		}
		return ret;
	}



	private HashMap<String, Object> serializeHotel(Hotel obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {

//			room_standards < l
			serializeList(ret, obj.getRoomStandards(), "room_standards");
//			amenities < l
			serializeList(ret, obj.getAmenities(), "amenities");
//			types < l
			serializeList(ret, obj.getTypes(), "types");
//			chains < l
			serializeList(ret, obj.getChains(), "chains");
//			chains_blacklist < l
			serializeList(ret, obj.getChainsBlacklist(), "chains_blacklist");
//			location < l
			serializeList(ret, obj.getLocations(), "location");
//			room_location < l
			serializeList(ret, obj.getRoomLocation(), "room_location");
//			bed_types < l
			serializeList(ret, obj.getBedTypes(), "bed_types");
//			categories < l
			serializeList(ret, obj.getCategories(), "categories");
//			anything_else < s
			serializeString(ret, obj.getAnythingElse(), "anything_else");
			serializeString(ret, obj.getLoyalityEnd(), "loyalty_date");

//			loyalty_program < list
			List<HashMap<String, Object>> loyaltyPrograms = new LinkedList<>();
			for(HotelLoyaltyProgram itm : obj.getLoyaltyPrograms()) {
				HashMap<String, Object> lp = new HashMap<>();
				lp.put("number", addString(itm.getNumber()));
				lp.put("id", addString(itm.getId()));
				loyaltyPrograms.add(addMap(lp));
			}
			if(!loyaltyPrograms.isEmpty()) {
				ret.put("loyalty_program", addList(loyaltyPrograms));
			}
		}
		return ret;
	}


	private HashMap<String, Object> serializeCar(Car obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {

//			loyalty_programs < list
			List<HashMap<String, Object>> loyaltyPrograms = new LinkedList<>();
			for(CarLoyaltyProgram itm : obj.getLoyaltyPrograms()) {
				HashMap<String, Object> lp = new HashMap<>();
				lp.put("number", addString(itm.getNumber()));
				lp.put("id", addString(itm.getId()));
				loyaltyPrograms.add(addMap(lp));
			}
			if(!loyaltyPrograms.isEmpty()) {
				ret.put("loyalty_programs", addList(loyaltyPrograms));
			}
//			preferences < list
			serializeList(ret, obj.getPreferences(), "preferences");
//			rental_companies < l
			serializeList(ret, obj.getRentalCompanies(), "rental_companies");
//			extras < l
			serializeList(ret, obj.getExtras(), "extras");
//			anything_else < s
			serializeString(ret, obj.getAnythingElse(), "anything_else");
//			type < s
			serializeString(ret, obj.getType(), "type");
//			booking_class < s
			serializeString(ret, obj.getBookingClass(), "booking_class");
			serializeString(ret, obj.getLoyalityEnd(), "loyalty_date");

		}
		return ret;
	}
	private HashMap<String, Object> serializeTrain(Train obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {
//			loyalty_programs < list
			List<HashMap<String, Object>> loyaltyPrograms = new LinkedList<>();
			for(TrainLoyaltyProgram itm : obj.getLoyaltyPrograms()) {
				HashMap<String, Object> lp = new HashMap<>();
				lp.put("number", addString(itm.getNumber()));
				lp.put("id", addString(itm.getId()));
				loyaltyPrograms.add(addMap(lp));
			}
			if(!loyaltyPrograms.isEmpty()) {
				ret.put("loyalty_programs", addList(loyaltyPrograms));
			}

			serializeString(ret, obj.getTravelClass(), "travel_class");
			serializeString(ret, obj.getMobilityService(), "mobility_service");
			serializeString(ret, obj.getCompartmentType(), "compartment_type");
			serializeString(ret, obj.getSeatLocation(), "seat_location");
			serializeString(ret, obj.getZone(), "zone");
			serializeList(ret, obj.getPreferred(), "preferred_trains");
			serializeList(ret, obj.getSpecificBooking(), "train_specific_booking");
			serializeList(ret, obj.getTicket(), "ticket");

//			anything_else < s
			serializeString(ret, obj.getAnythingElse(), "anything_else");
			serializeString(ret, obj.getLoyalityEnd(), "loyalty_date");
		}
		return ret;
	}
	private HashMap<String, Object> serializeFlight(Flight obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {
//			meal < s
			serializeString(ret, obj.getMeal(), "meal");
//			seat < s
			serializeString(ret, obj.getSeat(), "seat");
//			booking_class_short_haul < s
			serializeString(ret, obj.getBookingClassShortHaul(), "booking_class_short_haul");
//			seat_row < s
			serializeString(ret, obj.getSeatRow(), "seat_row");
//			booking_class_long_haul < s
			serializeString(ret, obj.getBookingClassLongHaul(), "booking_class_long_haul");
//			booking_class_medium_haul < s
			serializeString(ret, obj.getBookingClassMediumHaul(), "booking_class_medium_haul");
//			anything_else < s
			serializeString(ret, obj.getAnythingElse(), "anything_else");
//			airlines_blacklist < l
			serializeList(ret, obj.getAirlinesBlacklist(), "airlines_blacklist");
//			options < list
			serializeList(ret, obj.getOptions(), "options");
//			airlines < l
			serializeList(ret, obj.getAirlines(), "airlines");
// 			airline_loyalty_program < l
			List<HashMap<String, Object>> loyaltyPrograms = new LinkedList<>();
			for(AirlineLoyaltyProgram itm : obj.getAirlineLoyaltyPrograms()) {
				HashMap<String, Object> lp = new HashMap<>();
				lp.put("number", addString(itm.getNumber()));
				lp.put("id", addString(itm.getId()));
				loyaltyPrograms.add(addMap(lp));
			}
			if(!loyaltyPrograms.isEmpty()) {
				ret.put("airline_loyalty_program", addList(loyaltyPrograms));
			}
			serializeString(ret, obj.getLoyalityEnd(), "loyalty_date");
		}
		return ret;
	}

	private HashMap<String, Object> serializeJourneyPreferences(JourneyPreferences obj) {
		HashMap<String, Object> ret = new HashMap<>();
		if(obj != null) {
			HashMap<String, Object> car = serializeCar(obj.getCar());
			if(!car.isEmpty()) {
				ret.put("car", addMap(car));
			}
			HashMap<String, Object> hotel = serializeHotel(obj.getHotel());
			if(!hotel.isEmpty()) {
				ret.put("hotel", addMap(hotel));
			}
			HashMap<String, Object> flight = serializeFlight(obj.getFlights());
			if(!flight.isEmpty()) {
				ret.put("flights", addMap(flight));
			}
			HashMap<String, Object> train = serializeTrain(obj.getTrain());
			if(!train.isEmpty()) {
				ret.put("train", addMap(train));
			}
		}
		return ret;
	}
	private HashMap<String, Object> serializePersonal(PersonalData obj) {
		HashMap<String, Object> ret = new HashMap<>();

		if(obj != null) {
			serializeString(ret, obj.getNationality(), "nationality");
			serializeDate(ret, obj.getBirthdate(), "birth_date");
			serializeString(ret, obj.getLastname(), "last_name");
			serializeString(ret, obj.getMiddlename(), "middle_name");
			serializeString(ret, obj.getFirstname(), "first_name");
			serializeString(ret, obj.getTitle(), "title");
		}
		return ret;
	}
	private HashMap<String, Object> serializePrivateAddress(PrivateAddress obj) {
		HashMap<String, Object> ret = new HashMap<>();

		if(obj != null) {
			serializeString(ret, obj.getZip(), "zip");
			serializeString(ret, obj.getAddressLine1(), "address_line_1");
			serializeString(ret, obj.getAddressLine2(), "address_line_2");
			serializeString(ret, obj.getState(), "state");
			serializeString(ret, obj.getCity(), "city");
			serializeString(ret, obj.getCountry(), "country");
			serializeBoolean(ret, obj.isBillingAddress(), "accountsame");
		}
		return ret;
	}


	@Override
	public JsonElement serialize(TravelfolderUser src, Type typeOfSrc, JsonSerializationContext context) {

		HashMap<String, Object> resultHash = new HashMap<>();
		//esta
		HashMap<String, Object> esta = serializeEsta(src.getEsta());
		if(!esta.isEmpty()) {
			resultHash.put("esta", addMap(esta));
		}

		//billing address
		HashMap<String, Object> billing = serializeBilling(src.getBillingAddress());
		if(!billing.isEmpty()) {
			resultHash.put("address_billing", addMap(billing));
		}

		//contact
		HashMap<String, Object> contact = serializeContact(src.getContactInfo());

		if(!contact.isEmpty()) {
			resultHash.put("contact", addMap(contact));
		}

		//passport
		HashMap<String, Object> passport = serializePassport(src.getPassport());
		if(!passport.isEmpty()) {
			resultHash.put("passport", addMap(passport));
		}

		//private address
		HashMap<String, Object> privateAddress = serializePrivateAddress(src.getPrivateAddress());
		if(!privateAddress.isEmpty()) {
			resultHash.put("address_private", addMap(privateAddress));
		}

		//personal
		HashMap<String, Object> personal = serializePersonal(src.getPersonalData());
		if(!personal.isEmpty()) {
			if (src.getContactInfo()!=null && src.getContactInfo().getPersonalEmail()!=null) {
				personal.put("email", src.getContactInfo().getPersonalEmail());
			}
		}
		if(!personal.isEmpty()) {
			resultHash.put("personal", addMap(personal));
		}

		//journey
		HashMap<String, Object> journey = serializeJourneyPreferences(src.getJourneyPreferences());
		if(!journey.isEmpty()) {
			resultHash.put("preference", addMap(journey));
		}

		return new Gson().toJsonTree(resultHash);
	}

	@Override
	public TravelfolderUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		DateFormat dateFormat = getDateFormat();

		final JsonObject jsonObject = json.getAsJsonObject().get("Items").getAsJsonArray().get(0).getAsJsonObject();

		TravelfolderUser user = new TravelfolderUser();
		user.setUserId(getStringField(jsonObject, "UserId"));
		user.setIdentitySource(getStringField(jsonObject, "IdentitySource"));

		String created = getNumberField(jsonObject, "created");
		if (created != null) {
			user.setCreated(new Date(Long.parseLong(created)));
		}
		String modified = getNumberField(jsonObject, "modified");
		if (modified != null) {
			user.setModified(new Date(Long.parseLong(modified)));
		}

		//region esta
		JsonObject jsonEsta = getMapObject(jsonObject, "esta");
		if (jsonEsta != null) {
			Esta esta = new Esta();
			String validUntil = getStringField(jsonEsta, "valid_until");
			String applicationNumber = getStringField(jsonEsta, "application_number");
			if (validUntil != null) {
				try {
					esta.setValidUntil(dateFormat.parse(validUntil));
				} catch (ParseException e) {
					C.L(e.getMessage());
				}
			}
			esta.setApplicationNumber(applicationNumber);
			user.setEsta(esta);
		}
		//endregion

		//region billing address
		JsonObject jsonBillingAddress = getMapObject(jsonObject, "address_billing");
		BillingAddress billingAddress = new BillingAddress();
		if (jsonBillingAddress != null) {
			billingAddress.setZip(getStringField(jsonBillingAddress, "zip"));
			billingAddress.setCountry(getStringField(jsonBillingAddress, "country"));
			billingAddress.setCity(getStringField(jsonBillingAddress, "city"));
			billingAddress.setCompanyName(getStringField(jsonBillingAddress, "company_name"));
			billingAddress.setAddressLine1(getStringField(jsonBillingAddress, "address_line_1"));
			billingAddress.setAddressLine2(getStringField(jsonBillingAddress, "address_line_2"));
			billingAddress.setState(getStringField(jsonBillingAddress, "state"));
			billingAddress.setVat(getStringField(jsonBillingAddress, "vat"));
			user.setBillingAddress(billingAddress);
		}
		//endregion

		//region private payment
		JsonObject jsonPrivatePayment = getMapObject(jsonObject, "private_payment");
		if (jsonPrivatePayment != null) {
			PrivatePayment privatePayment = new PrivatePayment();
			privatePayment.setCreditCard(getStringField(jsonPrivatePayment, "credit_card"));
			privatePayment.setDefaultPaymentMethod(getStringField(jsonPrivatePayment, "default_payment_method"));
			user.setPrivatePayment(privatePayment);
		}
		//endregion

		//region contact
		JsonObject jsonContactInfo = getMapObject(jsonObject, "contact");
		if (jsonContactInfo != null) {
			ContactInfo contactInfo = new ContactInfo();
			contactInfo.setPrimaryEmail(getStringField(jsonContactInfo, "primary_email"));
			contactInfo.setPrimaryPhone(getStringField(jsonContactInfo, "primary_phone"));

			JsonObject jsonContactInfo2 = getMapObject(jsonObject, "personal");
			if (jsonContactInfo2 != null) {

				contactInfo.setPersonalEmail(getStringField(jsonContactInfo, "email"));
			}
			user.setContactInfo(contactInfo);
		}
		//endregion

		//region passport
		JsonObject jsonPassport = getMapObject(jsonObject, "passport");
		if (jsonPassport != null) {
			Passport passport = new Passport();
			passport.setCountry(getStringField(jsonPassport, "country"));
			passport.setNumber(getStringField(jsonPassport, "number"));
			passport.setCity(getStringField(jsonPassport, "city"));
			try {
				String expiry = getStringField(jsonPassport, "expiry");
				String dateIssued = getStringField(jsonPassport, "date_issued");
				if (expiry != null) {
					passport.setExpiry(dateFormat.parse(expiry));
				}
				if (dateIssued != null) {
					passport.setDateIssued(dateFormat.parse(dateIssued));
				}
			} catch (ParseException e) {
				C.L(e.getMessage());
			}
			user.setPassport(passport);
		}
		//endregion

		//region journey preferences
		JsonObject jsonPreferences = getMapObject(jsonObject, "preference");
		if (jsonPreferences != null) {
			Hotel hotel = JourneyPreferencesDeserializerHelper.deserializeHotel(jsonPreferences);
			Flight flight = JourneyPreferencesDeserializerHelper.deserializeFlight(jsonPreferences);
			Car car = JourneyPreferencesDeserializerHelper.deserializeCar(jsonPreferences);
			Train train = JourneyPreferencesDeserializerHelper.deserilizeTrain(jsonPreferences);
			JourneyPreferences journeyPreferences = new JourneyPreferences();
			journeyPreferences.setHotel(hotel);
			journeyPreferences.setFlights(flight);
			journeyPreferences.setCar(car);
			journeyPreferences.setTrain(train);
			user.setJourneyPreferences(journeyPreferences);
		}
		//endregion

		//region private address
		JsonObject jsonPrivateAddress = getMapObject(jsonObject, "address_private");
		if (jsonPrivateAddress != null) {
			PrivateAddress privateAddress = new PrivateAddress();
			privateAddress.setZip(getStringField(jsonPrivateAddress, "zip"));
			privateAddress.setAddressLine1(getStringField(jsonPrivateAddress, "address_line_1"));
			privateAddress.setCountry(getStringField(jsonPrivateAddress, "country"));
			privateAddress.setAddressLine2(getStringField(jsonPrivateAddress, "address_line_2"));
			privateAddress.setState(getStringField(jsonPrivateAddress, "state"));
			privateAddress.setCity(getStringField(jsonPrivateAddress, "city"));
			privateAddress.setBillingAddress(getBooleanField(jsonPrivateAddress, "accountsame"));
			user.setPrivateAddress(privateAddress);
		}
		//endregion

		//region personal data
		JsonObject jsonPersonalData = getMapObject(jsonObject, "personal");
		if (jsonPersonalData != null) {
			PersonalData personalData = new PersonalData();
			personalData.setTitle(getStringField(jsonPersonalData, "title"));
			personalData.setFirstname(getStringField(jsonPersonalData, "first_name"));
			personalData.setMiddlename(getStringField(jsonPersonalData, "middle_name"));
			personalData.setLastname(getStringField(jsonPersonalData, "last_name"));
			personalData.setNationality(getStringField(jsonPersonalData, "nationality"));
			String birthDate = getStringField(jsonPersonalData, "birth_date");
			if (birthDate != null) {
				try {
					personalData.setBirthdate(dateFormat.parse(birthDate));
				}
				catch (Exception e) {
					C.L(e.getMessage());
				}

			}
			user.setPersonalData(personalData);
		}
		//endregion

		return user;
	}
}

package solutions.masai.masai.android.core.model.travelfolder_user;

/**
 * Created by wolfg on 04/09/2017.
 */

public enum ChoiceType {

    PLANE_BOOKING_CLASS_SHORT_HAUL("plane_booking_class_short_haul"),
    PLANE_BOOKING_CLASS_MEDIUM_HAUL("plane_booking_class_medium_haul"),
    PLANE_BOOKING_CLASS_LONG_HAUL("plane_booking_class_long_haul"),
    PLANE_AIRLINES_BLACKLIST("plane_airlines_blacklist"),
    PLANE_AIRLINES("plane_airlines"),
    PLANE_OPTIONS("plane_options"),
    FLYING_SEAT_ROW("flying_seat_row"),
    FLYING_SEAT("flying_seat"),
    FLYING_MEAL("flying_meal"),
    AIRLINE_LOYALTY_PROGRAM("airline_loyalty_program"),
    HOTEL_LOYALTY_PROGRAM("hotel_loyalty_program"),
    HOTEL_ROOM_STANDARDS("hotel_room_standards"),
    HOTEL_BED_TYPES("hotel_bed_types"),
    HOTEL_AMENITIES("hotel_amenities"),
    HOTEL_CHAINS_BLACKLIST("hotel_chains_blacklist"),
    HOTEL_CATEGORIES("hotel_categories"),
    HOTEL_TYPES("hotel_types"),
    HOTEL_CHAINS("hotel_chains"),
    HOTEL_ROOM_LOCATION("hotel_room_location"),
    HOTEL_LOCATION("hotel_location"),
    CAR_BOOKING_CLASS("car_booking_class"),
    CAR_PREFERENCES("car_preferences"),
    CAR_EXTRAS("car_extras"),
    CAR_RENTAL_COMPANIES("car_rental_companies"),
    CAR_LOYALTY_PROGRAM("car_loyalty_program"),
    CAR_TYPE("car_type"),
    TRAIN_PREFERRED("train_preferred"),
    TRAIN_SPECIFIC_BOOKING("train_specific_booking"),
    TRAIN_COMPARTMENT_TYPE("train_compartment_type"),
    TRAIN_SEAT_LOCATION("train_seat_location"),
    TRAIN_TRAVEL_CLASS("train_travel_class"),
    TRAIN_MOBILITY_SERVICE("train_mobility_service"),
    TRAIN_TICKET("train_ticket"),
    TRAIN_ZONE("train_zone"),
    TRAIN_LOYALTY_PROGRAM("train_loyalty_program"),
    PASSPORT_COUNTRY_OF_ISSUANCE("passport_country_of_issuance");

    private final String str;

    ChoiceType(final String s) {
        this.str = s;
    }

    @Override
    public String toString(){
        return this.str;
    }

    public static ChoiceType getByValue(String value) {
        for(ChoiceType choiceType : values()) {
            if(choiceType.toString().equals(value)) {
                return choiceType;
            }
        }
        return null;
    }
}

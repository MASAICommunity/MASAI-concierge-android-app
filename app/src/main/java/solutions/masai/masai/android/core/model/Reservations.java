package solutions.masai.masai.android.core.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Semko on 2017-01-10.
 */

public class Reservations {
    private String status;
    private TravelItem items[];

    public TravelItem[] getItems() {
        return items;
    }

    public class TravelItem {
        private TravelSegment segment[];
        @SerializedName("hotel-reservation")
        private HotelReservation hotelReservation;
        private FlightReservation flight;
        @SerializedName("public-transportation")
        private PublicTransportation publicTransportation;

        public HotelReservation getHotelReservation() {
            return hotelReservation;
        }

        public FlightReservation getFlight() {
            return flight;
        }

        public PublicTransportation getPublicTransportation() {
            return publicTransportation;
        }

        public TravelSegment[] getSegments() {
            return segment;
        }
    }

    public class FlightReservation {
        private long duration;
        @SerializedName("distance-in-miles")
        private long distanceInMiles;
        private String notes;
        @SerializedName("class-type")
        private String classType;
        private Traveler traveler;
        @SerializedName("booking-details")
        private BookingDetails bookingDetails;
        private ArrivalDeparture arrival;
        private ArrivalDeparture departure;
        @SerializedName("total-price")
        private Price totalPrice;
        @SerializedName("provider-details")
        private ProviderDetails providerDetails;
        private FlightDetails details;

        public FlightDetails getDetails() {
            return details;
        }

        public ProviderDetails getProviderDetails() {
            return providerDetails;
        }

        public Price getTotalPrice() {
            return totalPrice;
        }

        public ArrivalDeparture getDeparture() {
            return departure;
        }

        public ArrivalDeparture getArrival() {
            return arrival;
        }

        public BookingDetails getBookingDetails() {
            return bookingDetails;
        }

        public Traveler getTraveler() {
            return traveler;
        }

        public String getClassType() {
            return classType;
        }

        public String getNotes() {
            return notes;
        }

        public long getDistanceInMiles() {
            return distanceInMiles;
        }

        public long getDuration() {
            return duration;
        }
    }

    public class HotelReservation {
        @SerializedName("booking-details")
        private BookingDetails bookingDetails;
        @SerializedName("provider-details")
        private ProviderDetails providerDetails;
        @SerializedName("hotel-name")
        private String hotelName;
        private Address address;
        @SerializedName("check-in")
        private String checkIn;
        @SerializedName("check-out")
        private String checkOut;
        private String phone;
        private Guest guest;

        public BookingDetails getBookingDetails() {
            return bookingDetails;
        }

        public ProviderDetails getProviderDetails() {
            return providerDetails;
        }

        public String getHotelName() {
            return hotelName;
        }

        public Address getAddress() {
            return address;
        }

        public String getCheckIn() {
            return checkIn;
        }

        public String getCheckOut() {
            return checkOut;
        }

        public String getPhone() {
            return phone;
        }

        public Guest getGuest() {
            return guest;
        }
    }

    public class PublicTransportation {
        @SerializedName("booking-details")
        private BookingDetails bookingDetails;
        @SerializedName("total-price")
        private Price totalPrice;
        @SerializedName("provider-details")
        private String providerDetails;
        @SerializedName("train-name")
        private String trainName;
        @SerializedName("train-number")
        private String trainNumber;
        private String seat;
        private String cabin;
        @SerializedName("class-type")
        private String classType;
        private long duration;
        private Traveler traveler;
        private ArrivalDeparture arrival;
        private ArrivalDeparture departure;

        public BookingDetails getBookingDetails() {
            return bookingDetails;
        }

        public Price getTotalPrice() {
            return totalPrice;
        }

        public String getProviderDetails() {
            return providerDetails;
        }

        public String getTrainNumber() {
            return trainNumber;
        }

        public String getSeat() {
            return seat;
        }

        public String getCabin() {
            return cabin;
        }

        public String getClassType() {
            return classType;
        }

        public long getDuration() {
            return duration;
        }

        public Traveler getTraveler() {
            return traveler;
        }

        public ArrivalDeparture getArrival() {
            return arrival;
        }

        public ArrivalDeparture getDeparture() {
            return departure;
        }

        public String getTrainName() {
            return trainName;
        }
    }

    public class Traveler {
        @SerializedName("e-ticket")
        private String eTicket;
        @SerializedName("loyalty-program")
        private LoyaltyProgram loyaltyProgram;
        @SerializedName("class-type")
        private String classType;
        @SerializedName("first-name")
        private String firstName;
        @SerializedName("last-name")
        private String lastName;
        private String seat;

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getClassType() {
            return classType;
        }

        public LoyaltyProgram getLoyaltyProgram() {
            return loyaltyProgram;
        }

        public String geteTicket() {
            return eTicket;
        }

        public String getSeat() {
            return seat;
        }
    }

    public class FlightDetails {
        private long number;
        @SerializedName("airline-code")
        private String airlineCode;

        public long getNumber() {
            return number;
        }

        public String getAirlineCode() {
            return airlineCode;
        }
    }

    public class ArrivalDeparture {
        @SerializedName("utc-date-time")
        private String utcDateTime;
        @SerializedName("local-date-time")
        private String localDateTime;
        private float latitude;
        @SerializedName("airport-code")
        private String airportCode;
        private float longitude;
        private String name;
        private String terminal;
        private String gate;
        @SerializedName("train-number")
        private String trainNumber;
        private String seat;
        private String cabin;
        private String platform;
        @SerializedName("station-name")
        private String stationName;
        @SerializedName("station-code")
        private String stationCode;
        private Address address;

        public String getGate() {
            return gate;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getName() {
            return name;
        }

        public float getLongitude() {
            return longitude;
        }

        public float getLatitude() {
            return latitude;
        }

        public String getLocalDateTime() {
            return localDateTime;
        }

        public String getUtcDateTime() {
            return utcDateTime;
        }

        public String getAirportCode() {
            return airportCode;
        }

        public String getTrainNumber() {
            return trainNumber;
        }

        public String getSeat() {
            return seat;
        }

        public String getCabin() {
            return cabin;
        }

        public String getStationName() {
            return stationName;
        }
    }

    public class LoyaltyProgram {
        private String name;
        private String number;

        public String getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }
    }

    public class Guest {
        @SerializedName("first-name")
        private String firstName;
        @SerializedName("last-name")
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    public class Price {
        @SerializedName("totalCost")
        private long totalCost;
        @SerializedName("currency-code")
        private String currencyCode;

        public long getTotalCost() {
            return totalCost;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }
    }

    public class Address {
        private float latitude;
        private float longitude;
        private String street;
        private String city;
        @SerializedName("country-name")
        private String countryName;

        public String getCountryName() {
            return countryName;
        }

        public String getCity() {
            return city;
        }

        public String getStreet() {
            return street;
        }

        public float getLongitude() {
            return longitude;
        }

        public float getLatitude() {
            return latitude;
        }
    }

    public class ProviderDetails {
        private String name;
        private String phone;
        @SerializedName("confirmation-number")
        private String confirmationNumber;

        public String getConfirmationNumber() {
            return confirmationNumber;
        }

        public String getPhone() {
            return phone;
        }

        public String getName() {
            return name;
        }
    }

    public class BookingDetails {
        private String name;
        @SerializedName("confirmation-number")
        private String confirmationNumber;
        private String url;

        public String getConfirmationNumber() {
            return confirmationNumber;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}

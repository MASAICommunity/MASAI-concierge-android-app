package solutions.masai.masai.android.core.model;

import com.google.gson.annotations.SerializedName;

public class TravelSegment {
    @SerializedName("hotel-reservation")
    private Reservations.HotelReservation hotelReservation;
    private Reservations.FlightReservation flight;

    public TravelSegment() {
    }

    @SerializedName("public-transportation")
    private Reservations.PublicTransportation publicTransportation;

    public Reservations.HotelReservation getHotelReservation() {
        return hotelReservation;
    }

    public Reservations.FlightReservation getFlight() {
        return flight;
    }

    public Reservations.PublicTransportation getPublicTransportation() {
        return publicTransportation;
    }

    public void setFlight(Reservations.FlightReservation flight) {
        this.flight = flight;
    }

    public void setHotelReservation(Reservations.HotelReservation hotelReservation) {
        this.hotelReservation = hotelReservation;
    }

    public void setPublicTransportation(Reservations.PublicTransportation publicTransportation) {
        this.publicTransportation = publicTransportation;
    }
}
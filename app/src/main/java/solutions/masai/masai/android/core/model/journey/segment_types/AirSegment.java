
package solutions.masai.masai.android.core.model.journey.segment_types;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import solutions.masai.masai.android.core.model.journey.Segment;

public class AirSegment extends Segment {

    @SerializedName("airline")
    @Expose
    private String airline;
    @SerializedName("arrival_datetime")
    @Expose
    private String arrivalDatetime;
    @SerializedName("arrival_time_zone_id")
    @Expose
    private String arrivalTimeZoneId;
    @SerializedName("class_of_service")
    @Expose
    private String classOfService;
    @SerializedName("confirmation_no")
    @Expose
    private String confirmationNo;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("departure_datetime")
    @Expose
    private String departureDatetime;
    @SerializedName("departure_time_zone_id")
    @Expose
    private String departureTimeZoneId;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("destination_name")
    @Expose
    private String destinationName;
    @SerializedName("destination_admin_code")
    @Expose
    private String destinationAdminCode;
    @SerializedName("destination_city_name")
    @Expose
    private String destinationCityName;
    @SerializedName("destination_country")
    @Expose
    private String destinationCountry;
    @SerializedName("destination_lat")
    @Expose
    private String destinationLat;
    @SerializedName("destination_lon")
    @Expose
    private String destinationLon;
    @SerializedName("fare_basis_code")
    @Expose
    private String fareBasisCode;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("flight_number")
    @Expose
    private String flightNumber;
    @SerializedName("iata_code")
    @Expose
    private String iataCode;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("normalized_airline")
    @Expose
    private String normalizedAirline;
    @SerializedName("number_of_pax")
    @Expose
    private String numberOfPax;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("origin_name")
    @Expose
    private String originName;
    @SerializedName("origin_admin_code")
    @Expose
    private String originAdminCode;
    @SerializedName("origin_city_name")
    @Expose
    private String originCityName;
    @SerializedName("origin_country")
    @Expose
    private String originCountry;
    @SerializedName("origin_lat")
    @Expose
    private String originLat;
    @SerializedName("origin_lon")
    @Expose
    private String originLon;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("seat_assignment")
    @Expose
    private String seatAssignment;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ticket_number")
    @Expose
    private String ticketNumber;
    @SerializedName("travelers")
    @Expose
    private List<Traveler> travelers = null;
    @SerializedName("tickets")
    @Expose
    private List<String> tickets = null;
    @SerializedName("seats")
    @Expose
    private List<String> seats = null;
    @SerializedName("price_details")
    @Expose
    private List<PriceDetail> priceDetails = null;

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getArrivalDatetime() {
        return arrivalDatetime;
    }

    public void setArrivalDatetime(String arrivalDatetime) {
        this.arrivalDatetime = arrivalDatetime;
    }

    public String getArrivalTimeZoneId() {
        return arrivalTimeZoneId;
    }

    public void setArrivalTimeZoneId(String arrivalTimeZoneId) {
        this.arrivalTimeZoneId = arrivalTimeZoneId;
    }

    public String getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(String classOfService) {
        this.classOfService = classOfService;
    }

    public String getConfirmationNo() {
        return confirmationNo;
    }

    public void setConfirmationNo(String confirmationNo) {
        this.confirmationNo = confirmationNo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(String departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public String getDepartureTimeZoneId() {
        return departureTimeZoneId;
    }

    public void setDepartureTimeZoneId(String departureTimeZoneId) {
        this.departureTimeZoneId = departureTimeZoneId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationAdminCode() {
        return destinationAdminCode;
    }

    public void setDestinationAdminCode(String destinationAdminCode) {
        this.destinationAdminCode = destinationAdminCode;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLon() {
        return destinationLon;
    }

    public void setDestinationLon(String destinationLon) {
        this.destinationLon = destinationLon;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNormalizedAirline() {
        return normalizedAirline;
    }

    public void setNormalizedAirline(String normalizedAirline) {
        this.normalizedAirline = normalizedAirline;
    }

    public String getNumberOfPax() {
        return numberOfPax;
    }

    public void setNumberOfPax(String numberOfPax) {
        this.numberOfPax = numberOfPax;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginAdminCode() {
        return originAdminCode;
    }

    public void setOriginAdminCode(String originAdminCode) {
        this.originAdminCode = originAdminCode;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginLat() {
        return originLat;
    }

    public void setOriginLat(String originLat) {
        this.originLat = originLat;
    }

    public String getOriginLon() {
        return originLon;
    }

    public void setOriginLon(String originLon) {
        this.originLon = originLon;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeatAssignment() {
        return seatAssignment;
    }

    public void setSeatAssignment(String seatAssignment) {
        this.seatAssignment = seatAssignment;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<Traveler> travelers) {
        this.travelers = travelers;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public List<PriceDetail> getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(List<PriceDetail> priceDetails) {
        this.priceDetails = priceDetails;
    }

}

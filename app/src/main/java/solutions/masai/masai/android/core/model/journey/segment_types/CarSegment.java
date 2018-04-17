package solutions.masai.masai.android.core.model.journey.segment_types;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import solutions.masai.masai.android.core.model.journey.Segment;

import java.util.List;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 01/09/2017
 */

public class CarSegment extends Segment {

    @SerializedName("car_company")
    @Expose
    private String carCompany;
    @SerializedName("car_description")
    @Expose
    private Object carDescription;
    @SerializedName("car_type")
    @Expose
    private String carType;
    @SerializedName("confirmation_no")
    @Expose
    private String confirmationNo;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("dropoff_address1")
    @Expose
    private String dropoffAddress1;
    @SerializedName("dropoff_address2")
    @Expose
    private Object dropoffAddress2;
    @SerializedName("dropoff_admin_code")
    @Expose
    private String dropoffAdminCode;
    @SerializedName("dropoff_city_name")
    @Expose
    private String dropoffCityName;
    @SerializedName("dropoff_country")
    @Expose
    private String dropoffCountry;
    @SerializedName("dropoff_datetime")
    @Expose
    private String dropoffDatetime;
    @SerializedName("dropoff_lat")
    @Expose
    private String dropoffLat;
    @SerializedName("dropoff_lon")
    @Expose
    private String dropoffLon;
    @SerializedName("dropoff_postal_code")
    @Expose
    private String dropoffPostalCode;
    @SerializedName("dropoff_time_zone_id")
    @Expose
    private String dropoffTimeZoneId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("hours_of_operation")
    @Expose
    private String hoursOfOperation;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("normalized_car_company")
    @Expose
    private String normalizedCarCompany;
    @SerializedName("pickup_address1")
    @Expose
    private String pickupAddress1;
    @SerializedName("pickup_address2")
    @Expose
    private Object pickupAddress2;
    @SerializedName("pickup_admin_code")
    @Expose
    private String pickupAdminCode;
    @SerializedName("pickup_city_name")
    @Expose
    private String pickupCityName;
    @SerializedName("pickup_country")
    @Expose
    private String pickupCountry;
    @SerializedName("pickup_datetime")
    @Expose
    private String pickupDatetime;
    @SerializedName("pickup_lat")
    @Expose
    private String pickupLat;
    @SerializedName("pickup_lon")
    @Expose
    private String pickupLon;
    @SerializedName("pickup_postal_code")
    @Expose
    private String pickupPostalCode;
    @SerializedName("pickup_time_zone_id")
    @Expose
    private String pickupTimeZoneId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("travelers")
    @Expose
    private List<Traveler> travelers = null;
    @SerializedName("price_details")
    @Expose
    private List<PriceDetail> priceDetails = null;

    public String getCarCompany() {
        return carCompany;
    }

    public void setCarCompany(String carCompany) {
        this.carCompany = carCompany;
    }

    public Object getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(Object carDescription) {
        this.carDescription = carDescription;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public String getDropoffAddress1() {
        return dropoffAddress1;
    }

    public void setDropoffAddress1(String dropoffAddress1) {
        this.dropoffAddress1 = dropoffAddress1;
    }

    public Object getDropoffAddress2() {
        return dropoffAddress2;
    }

    public void setDropoffAddress2(Object dropoffAddress2) {
        this.dropoffAddress2 = dropoffAddress2;
    }

    public String getDropoffAdminCode() {
        return dropoffAdminCode;
    }

    public void setDropoffAdminCode(String dropoffAdminCode) {
        this.dropoffAdminCode = dropoffAdminCode;
    }

    public String getDropoffCityName() {
        return dropoffCityName;
    }

    public void setDropoffCityName(String dropoffCityName) {
        this.dropoffCityName = dropoffCityName;
    }

    public String getDropoffCountry() {
        return dropoffCountry;
    }

    public void setDropoffCountry(String dropoffCountry) {
        this.dropoffCountry = dropoffCountry;
    }

    public String getDropoffDatetime() {
        return dropoffDatetime;
    }

    public void setDropoffDatetime(String dropoffDatetime) {
        this.dropoffDatetime = dropoffDatetime;
    }

    public String getDropoffLat() {
        return dropoffLat;
    }

    public void setDropoffLat(String dropoffLat) {
        this.dropoffLat = dropoffLat;
    }

    public String getDropoffLon() {
        return dropoffLon;
    }

    public void setDropoffLon(String dropoffLon) {
        this.dropoffLon = dropoffLon;
    }

    public String getDropoffPostalCode() {
        return dropoffPostalCode;
    }

    public void setDropoffPostalCode(String dropoffPostalCode) {
        this.dropoffPostalCode = dropoffPostalCode;
    }

    public String getDropoffTimeZoneId() {
        return dropoffTimeZoneId;
    }

    public void setDropoffTimeZoneId(String dropoffTimeZoneId) {
        this.dropoffTimeZoneId = dropoffTimeZoneId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(String hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNormalizedCarCompany() {
        return normalizedCarCompany;
    }

    public void setNormalizedCarCompany(String normalizedCarCompany) {
        this.normalizedCarCompany = normalizedCarCompany;
    }

    public String getPickupAddress1() {
        return pickupAddress1;
    }

    public void setPickupAddress1(String pickupAddress1) {
        this.pickupAddress1 = pickupAddress1;
    }

    public Object getPickupAddress2() {
        return pickupAddress2;
    }

    public void setPickupAddress2(Object pickupAddress2) {
        this.pickupAddress2 = pickupAddress2;
    }

    public String getPickupAdminCode() {
        return pickupAdminCode;
    }

    public void setPickupAdminCode(String pickupAdminCode) {
        this.pickupAdminCode = pickupAdminCode;
    }

    public String getPickupCityName() {
        return pickupCityName;
    }

    public void setPickupCityName(String pickupCityName) {
        this.pickupCityName = pickupCityName;
    }

    public String getPickupCountry() {
        return pickupCountry;
    }

    public void setPickupCountry(String pickupCountry) {
        this.pickupCountry = pickupCountry;
    }

    public String getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(String pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLon() {
        return pickupLon;
    }

    public void setPickupLon(String pickupLon) {
        this.pickupLon = pickupLon;
    }

    public String getPickupPostalCode() {
        return pickupPostalCode;
    }

    public void setPickupPostalCode(String pickupPostalCode) {
        this.pickupPostalCode = pickupPostalCode;
    }

    public String getPickupTimeZoneId() {
        return pickupTimeZoneId;
    }

    public void setPickupTimeZoneId(String pickupTimeZoneId) {
        this.pickupTimeZoneId = pickupTimeZoneId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<Traveler> travelers) {
        this.travelers = travelers;
    }

    public List<PriceDetail> getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(List<PriceDetail> priceDetails) {
        this.priceDetails = priceDetails;
    }


}

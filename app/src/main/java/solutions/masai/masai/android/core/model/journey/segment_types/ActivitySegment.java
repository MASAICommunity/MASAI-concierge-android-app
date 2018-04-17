package solutions.masai.masai.android.core.model.journey.segment_types;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import solutions.masai.masai.android.core.model.journey.Segment;

public class ActivitySegment extends Segment {

    @SerializedName("activity_type")
    @Expose
    private String activityType;
    @SerializedName("activity_name")
    @Expose
    private String activityName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("start_name")
    @Expose
    private String startName;
    @SerializedName("start_admin_code")
    @Expose
    private String startAdminCode;
    @SerializedName("start_address1")
    @Expose
    private String startAddress1;
    @SerializedName("start_address2")
    @Expose
    private Object startAddress2;
    @SerializedName("start_city_name")
    @Expose
    private String startCityName;
    @SerializedName("start_country")
    @Expose
    private String startCountry;
    @SerializedName("start_lat")
    @Expose
    private String startLat;
    @SerializedName("start_lon")
    @Expose
    private String startLon;
    @SerializedName("start_postal_code")
    @Expose
    private String startPostalCode;
    @SerializedName("end_name")
    @Expose
    private String endName;
    @SerializedName("end_admin_code")
    @Expose
    private String endAdminCode;
    @SerializedName("end_address1")
    @Expose
    private String endAddress1;
    @SerializedName("end_address2")
    @Expose
    private Object endAddress2;
    @SerializedName("end_city_name")
    @Expose
    private String endCityName;
    @SerializedName("end_country")
    @Expose
    private String endCountry;
    @SerializedName("end_lat")
    @Expose
    private String endLat;
    @SerializedName("end_lon")
    @Expose
    private String endLon;
    @SerializedName("end_postal_code")
    @Expose
    private String endPostalCode;
    @SerializedName("start_datetime")
    @Expose
    private String startDatetime;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;
    @SerializedName("confirmation_no")
    @Expose
    private String confirmationNo;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time_zone_id")
    @Expose
    private String timeZoneId;
    @SerializedName("travelers")
    @Expose
    private List<Traveler> travelers = null;
    @SerializedName("price_details")
    @Expose
    private List<PriceDetail> priceDetails = null;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getStartAdminCode() {
        return startAdminCode;
    }

    public void setStartAdminCode(String startAdminCode) {
        this.startAdminCode = startAdminCode;
    }

    public String getStartAddress1() {
        return startAddress1;
    }

    public void setStartAddress1(String startAddress1) {
        this.startAddress1 = startAddress1;
    }

    public Object getStartAddress2() {
        return startAddress2;
    }

    public void setStartAddress2(Object startAddress2) {
        this.startAddress2 = startAddress2;
    }

    public String getStartCityName() {
        return startCityName;
    }

    public void setStartCityName(String startCityName) {
        this.startCityName = startCityName;
    }

    public String getStartCountry() {
        return startCountry;
    }

    public void setStartCountry(String startCountry) {
        this.startCountry = startCountry;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLon() {
        return startLon;
    }

    public void setStartLon(String startLon) {
        this.startLon = startLon;
    }

    public String getStartPostalCode() {
        return startPostalCode;
    }

    public void setStartPostalCode(String startPostalCode) {
        this.startPostalCode = startPostalCode;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getEndAdminCode() {
        return endAdminCode;
    }

    public void setEndAdminCode(String endAdminCode) {
        this.endAdminCode = endAdminCode;
    }

    public String getEndAddress1() {
        return endAddress1;
    }

    public void setEndAddress1(String endAddress1) {
        this.endAddress1 = endAddress1;
    }

    public Object getEndAddress2() {
        return endAddress2;
    }

    public void setEndAddress2(Object endAddress2) {
        this.endAddress2 = endAddress2;
    }

    public String getEndCityName() {
        return endCityName;
    }

    public void setEndCityName(String endCityName) {
        this.endCityName = endCityName;
    }

    public String getEndCountry() {
        return endCountry;
    }

    public void setEndCountry(String endCountry) {
        this.endCountry = endCountry;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLon() {
        return endLon;
    }

    public void setEndLon(String endLon) {
        this.endLon = endLon;
    }

    public String getEndPostalCode() {
        return endPostalCode;
    }

    public void setEndPostalCode(String endPostalCode) {
        this.endPostalCode = endPostalCode;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
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

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
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

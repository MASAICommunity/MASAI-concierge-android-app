package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-03-06.
 */

public class LocationAddress {
    private String locationAddress;
    private float lat;
    private float lng;

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
}

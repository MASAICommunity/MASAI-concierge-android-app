package solutions.masai.masai.android.core.model;

import io.realm.RealmObject;

/**
 * Created by Semko on 2017-03-01.
 */

public class Location extends RealmObject {
    private String type = "Point";
    private float lat;
    private float lng;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
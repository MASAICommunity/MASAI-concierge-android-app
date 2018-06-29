package solutions.masai.masai.android.core.helper;

import solutions.masai.masai.android.core.model.LocationAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semko on 2017-03-06.
 */

public class LocationHelper {
    private static List<LocationAddress> locationList = new ArrayList<>();

    public static String getLocation(float lat, float lng) {
        for (LocationAddress location : locationList) {
            if (location != null && location.getLng() == lng && location.getLat() == lat) {
                if (location.getLocationAddress() != null && !location.getLocationAddress().isEmpty()) {
                    return location.getLocationAddress();
                }
            }
        }
        return null;
    }

    public static void addLocation(float lat, float lng, String address) {
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.setLat(lat);
        locationAddress.setLng(lng);
        locationAddress.setLocationAddress(address);
        locationList.add(locationAddress);
    }
}

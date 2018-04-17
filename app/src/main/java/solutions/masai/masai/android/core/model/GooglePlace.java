package solutions.masai.masai.android.core.model;


/**
 * Created by Semko on 2016-12-21.
 */

public class GooglePlace {
    private String formatted_address;
    private String name;
    private float rating;
    private Photo photos[];
    private String[] types;
    private Geometry geometry;

    public float getLatitude() {
        return geometry.getLat();
    }

    public float getLongitude() {
        return geometry.getLng();
    }

    public boolean hasLocation() {
        return geometry != null;
    }

    public String getPhotoReference() {
        if (photos != null && photos.length >= 0) {
            return photos[0].getPhotoReference();
        }
        return null;
    }

    public float getRating() {
        return rating;
    }

    public String[] getTypes() {
        return types;
    }

    public String getName() {
        return name;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    private class Photo {
        private String photo_reference;

        public String getPhotoReference() {
            return photo_reference;
        }
    }

    private class Geometry {
        private Location location;

        public float getLat() {
            return location.getLat();
        }

        public float getLng() {
            return location.getLng();
        }
    }

    private class Location {
        private float lat;
        private float lng;

        public float getLat() {
            return lat;
        }

        public float getLng() {
            return lng;
        }
    }
}

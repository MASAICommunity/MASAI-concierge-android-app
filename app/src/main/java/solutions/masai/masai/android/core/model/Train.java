package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2016-12-21.
 */

public class Train {
    private String replySuggestion;
    private LocationAndTime departure;
    private LocationAndTime arrival;
    private int travelDuration;
    private int travelChanges;
    private float stdPrice;
    private float lowPrice;
    private String bookingLink;
    private String creator;
    private String topic;

    public String getReplySuggestion() {
        return replySuggestion;
    }

    public void setReplySuggestion(String replySuggestion) {
        this.replySuggestion = replySuggestion;
    }

    public String getDepartureTime() {
        return departure.getTime();
    }

    public String getDepartureLocation() {
        return departure.getLocation();
    }

    public String getArrivalTime() {
        return arrival.getTime();
    }

    public String getArrivalLocation() {
        return arrival.getLocation();
    }

    public void setDeparture(LocationAndTime departure) {
        this.departure = departure;
    }

    public LocationAndTime getArrival() {
        return arrival;
    }

    public void setArrival(LocationAndTime arrival) {
        this.arrival = arrival;
    }

    public int getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(int travelDuration) {
        this.travelDuration = travelDuration;
    }

    public int getTravelChanges() {
        return travelChanges;
    }

    public void setTravelChanges(int travelChanges) {
        this.travelChanges = travelChanges;
    }

    public float getStdPrice() {
        return stdPrice;
    }

    public void setStdPrice(float stdPrice) {
        this.stdPrice = stdPrice;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getBookingLink() {
        return bookingLink;
    }

    public void setBookingLink(String bookingLink) {
        this.bookingLink = bookingLink;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private class LocationAndTime {
        private String location;
        private String time;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }


}

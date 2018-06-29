package solutions.masai.masai.android.core.model.journey;

/**
 * Created by wolfg on 31/08/2017.
 */

public class Journey {

    private String journeyId;

    private String userId;

    private Segments segments = null;

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Segments getSegments() {
        return segments;
    }

    public void setSegments(Segments segments) {
        this.segments = segments;
    }

    public void addSegment(Segment s){
        if (this.segments != null)
            this.segments.add(s);
        else {
            this.segments = new Segments();
            this.segments.add(s);
        }

    }
}

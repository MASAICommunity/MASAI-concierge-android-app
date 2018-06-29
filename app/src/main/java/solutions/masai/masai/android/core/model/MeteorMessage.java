package solutions.masai.masai.android.core.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Semko on 2017-02-21.
 */

public class MeteorMessage {
    @JsonProperty("_id")
    private String _id;
    private String rid;
    private String msg;
    private Location location;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Location getLocation() {
        return location;
    }

    public void setPoint(float latitude, float longitude) {
        location = new Location();
        location.setPoint(latitude, longitude);
    }

    private class Location {
        private String type = "Point";
        private float coordinates[] = new float[2];

        public void setPoint(float latitude, float longitude) {
            coordinates[1] = latitude;
            coordinates[0] = longitude;
        }

        public float[] getCoordinates() {
            return coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

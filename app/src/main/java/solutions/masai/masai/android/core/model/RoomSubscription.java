package solutions.masai.masai.android.core.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomSubscription {
    public static final String TYPE_CHANNEL = "c";
    public static final String TYPE_PRIVATE = "p";
    public static final String TYPE_DIRECT_MESSAGE = "d";

    private String _id;
    private String rid;
    private String name;
    private String t;
    private boolean open;
    private boolean alert;
    private int unread;

    public static JSONObject customizeJson(JSONObject roomSubscriptionJson) throws JSONException {
        return roomSubscriptionJson;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getRoomId() {
        return rid;
    }

    public void setRoomId(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return t;
    }

    public void setType(String t) {
        this.t = t;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}

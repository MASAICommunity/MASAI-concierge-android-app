package solutions.masai.masai.android.core.model;

import com.google.gson.Gson;

import io.realm.annotations.Ignore;
import solutions.masai.masai.android.core.communication.ConnectionManager;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Message extends RealmObject {
    @PrimaryKey
    private String _id;
    private String t;
    private String rid;
    private int syncstate = SyncState.NOT_SYNCED;
    private long ts;
    private String msg;
    private User u;
    private String urls;
    private boolean groupable;
    private String attachments;
    private int connectionPositionOnList;
    private Location location;
    private boolean permissionConfirmed = false;
    private String webPageMetaArray;
    private boolean isSearched = false;

    public Message() {
    }

    public boolean hasAttachments() {
        return getAttachments() != null && getAttachments().length > 0;
    }

    public boolean isMine() {
        if (ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList) != null
                && ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost() != null
                && ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser().getUserId()
                != null
                && !ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser().getUserId()
                .isEmpty()) {
            String liveChatUserId = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser()
                    .getUserId();
            if (u != null && u.getId() != null && u.getId().equals(liveChatUserId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isContentEmpty() {
        return msg != null && msg.isEmpty();
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getType() {
        return t;
    }

    public void setType(String t) {
        this.t = t;
    }

    public String getRoomId() {
        return rid;
    }

    public void setRoomId(String rid) {
        this.rid = rid;
    }

    public int getSyncstate() {
        return syncstate;
    }

    public void setSyncstate(int syncstate) {
        this.syncstate = syncstate;
    }

    public long getTimeStamp() {
        return ts;
    }

    public void setTimeStamp(long ts) {
        this.ts = ts;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return u;
    }

    public void setUser(User u) {
        this.u = u;
    }

    public boolean isGroupable() {
        return groupable;
    }

    public void setGroupable(boolean groupable) {
        this.groupable = groupable;
    }

    public boolean hasSameSenderAs(Message message) {
        return u.getUsername() != null && message.getUser().getUsername() != null && u.getUsername().equals(message.getUser().getUsername())
                || u.getEmail() != null && message.getUser().getEmail() != null && u.getEmail().equals(message.getUser().getEmail());
    }

    public int getConnectionPositionOnList() {
        return connectionPositionOnList;
    }

    public void setConnectionPositionOnList(int connectionPositionOnList) {
        this.connectionPositionOnList = connectionPositionOnList;
    }

    public MessageAttachment[] getAttachments() {
        MessageAttachment messageAttachments[] = null;
        if (attachments != null && !attachments.isEmpty()) {
            messageAttachments = new Gson().fromJson(attachments, MessageAttachment[].class);
        }
        return messageAttachments;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isPermissionConfirmed() {
        return permissionConfirmed;
    }

    public void setPermissionConfirmed(boolean permissionConfirmed) {
        this.permissionConfirmed = permissionConfirmed;
    }

    public String getWebPageMetaArray() {
        return webPageMetaArray;
    }

    public void setWebPageMetaArray(String webPageMetaArray) {
        this.webPageMetaArray = webPageMetaArray;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public boolean isSearched() {
        return isSearched;
    }

    public void setSearched(boolean searched) {
        isSearched = searched;
    }
}

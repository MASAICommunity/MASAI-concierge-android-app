package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-04-25.
 */

public class TravelFolderPermissionResponse {
    public static final String PERMISSION_GRANTED = "permission granted";
    public static final String PERMISSION_DENIED = "permission denied";
    private String status;
    private String granted_for;
    private String rid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrantedFor() {
        return granted_for;
    }

    public void setGrantedFor(String userId) {
        this.granted_for = userId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}

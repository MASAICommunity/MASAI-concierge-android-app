package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-03-02.
 */

public class LiveChatMessage {
    private String _id;
    private String rid;
    private String msg;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String get_id() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}

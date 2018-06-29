package solutions.masai.masai.android.core.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Semko on 2016-12-16.
 */

public class RoomId extends RealmObject {
    @PrimaryKey
    private String rid;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}

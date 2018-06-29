package solutions.masai.masai.android.core.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Semko on 2016-12-02.
 */

public class Token extends RealmObject {
    private String token;
    private String id;
    @Ignore
    private Date tokenExpires;

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpires() {
        return tokenExpires.get$date();
    }

    private class Date {
        private long $date;

        public long get$date() {
            return $date;
        }
    }

}

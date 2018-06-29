package solutions.masai.masai.android.core.model;

import java.util.List;

/**
 * Created by Semko on 2016-12-20.
 */

public class UserInfo {
    private String _id;
    private String name;
    private String username;
    private List<Email> emails;

    public String getEmail() {
        return emails.get(0).getAddress();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private class Email {
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

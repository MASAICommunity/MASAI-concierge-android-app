package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2016-12-07.
 */

public enum MessageType {
    USER_JOINED("uj");
    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

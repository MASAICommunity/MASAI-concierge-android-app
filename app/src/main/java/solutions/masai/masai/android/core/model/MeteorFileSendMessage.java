package solutions.masai.masai.android.core.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Semko on 2017-02-22.
 */

public class MeteorFileSendMessage {
    private String type;
    private long size;
    private String name;
    @JsonProperty("_id")
    private String _id;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

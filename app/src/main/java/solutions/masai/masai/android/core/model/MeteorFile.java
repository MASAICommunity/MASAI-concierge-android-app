package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-02-22.
 */

public class MeteorFile {
    private String name;
    private long size;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}

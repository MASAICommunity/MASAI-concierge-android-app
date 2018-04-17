
package solutions.masai.masai.android.core.model.journey;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccessManagement {

    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Items")
    @Expose
    private List<Item> items = null;
    @SerializedName("ScannedCount")
    @Expose
    private Integer scannedCount;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getScannedCount() {
        return scannedCount;
    }

    public void setScannedCount(Integer scannedCount) {
        this.scannedCount = scannedCount;
    }

}

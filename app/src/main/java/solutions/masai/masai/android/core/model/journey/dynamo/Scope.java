
package solutions.masai.masai.android.core.model.journey.dynamo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scope {

    @SerializedName("L")
    @Expose
    private List<L> l = null;

    public List<L> getL() {
        return l;
    }

    public void setL(List<L> l) {
        this.l = l;
    }

}


package solutions.masai.masai.android.core.model.journey.dynamo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class L {

    @SerializedName("S")
    @Expose
    private String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

}

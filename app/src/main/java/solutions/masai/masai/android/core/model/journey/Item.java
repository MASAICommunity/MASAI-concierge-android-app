
package solutions.masai.masai.android.core.model.journey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import solutions.masai.masai.android.core.model.journey.dynamo.GrantedUserId;
import solutions.masai.masai.android.core.model.journey.dynamo.Scope;
import solutions.masai.masai.android.core.model.journey.dynamo.UserId;

public class Item {

    @SerializedName("UserId")
    @Expose
    private UserId userId;
    @SerializedName("Scope")
    @Expose
    private Scope scope;
    @SerializedName("GrantedUserId")
    @Expose
    private GrantedUserId grantedUserId;

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public GrantedUserId getGrantedUserId() {
        return grantedUserId;
    }

    public void setGrantedUserId(GrantedUserId grantedUserId) {
        this.grantedUserId = grantedUserId;
    }

}

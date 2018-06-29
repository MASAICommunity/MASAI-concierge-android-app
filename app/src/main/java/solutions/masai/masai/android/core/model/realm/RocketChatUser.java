package solutions.masai.masai.android.core.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by cWahl on 09.11.2017.
 */

public class RocketChatUser extends RealmObject {
	@PrimaryKey
	private String rocketchatUrl;

	private String userId;
	private String pushId;
	private String loginToken;
	private String loginTokenExpires;

	//region properties
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRocketchatUrl() {
		return rocketchatUrl;
	}

	public void setRocketchatUrl(String rocketchatUrl) {
		this.rocketchatUrl = rocketchatUrl;
	}

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getLoginTokenExpires() {
		return loginTokenExpires;
	}

	public void setLoginTokenExpires(String loginTokenExpires) {
		this.loginTokenExpires = loginTokenExpires;
	}

	//endregion
}

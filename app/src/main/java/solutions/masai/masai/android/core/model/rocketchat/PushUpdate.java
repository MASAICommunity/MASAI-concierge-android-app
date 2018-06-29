package solutions.masai.masai.android.core.model.rocketchat;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cWahl on 13.11.2017.
 */

public class PushUpdate {
	private String id;
	private GCMToken token;
	private String appName;
	private String userId;
	private Map<String, String> metadata;

	public PushUpdate() {
		this.token = new GCMToken();
		this.token.setGcm(FirebaseInstanceId.getInstance().getToken());
		this.metadata = new HashMap<String, String>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GCMToken getToken() {
		return token;
	}

	public void setToken(GCMToken token) {
		this.token = token;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}
}

class GCMToken {
	private String gcm;

	public String getGcm() {
		return gcm;
	}

	public void setGcm(String gcm) {
		this.gcm = gcm;
	}
}

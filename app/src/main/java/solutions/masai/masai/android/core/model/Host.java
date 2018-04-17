package solutions.masai.masai.android.core.model;

import com.google.gson.annotations.SerializedName;

import solutions.masai.masai.android.core.model.realm.RocketChatUser;
import solutions.masai.masai.android.core.model.rocketchat.Room;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Semko on 2016-12-01.
 */

public class Host extends RealmObject {

	@SerializedName("socket")
	private String hostUrl;

	@PrimaryKey
	@SerializedName("url")
	private String hostRocketChatApiUrl;

	@SerializedName("name")
	private String humanName;

	@SerializedName("liveChatToken")
	private String liveChatToken;

	private RocketChatUser rcUser;
	private RealmList<Room> openedRooms = new RealmList<>();
	private RealmList<Room> closedRooms = new RealmList<>();

	//region properties
	public String getHumanName() {
		return humanName;
	}

	public void setHumanName(String humanName) {
		this.humanName = humanName;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getHostRocketChatApiUrl() {
		return hostRocketChatApiUrl;
	}

	public void setHostRocketChatApiUrl(String hostRocketChatApiUrl) {
		this.hostRocketChatApiUrl = hostRocketChatApiUrl;
	}

	public String getLiveChatToken() {
		return liveChatToken;
	}

	public void setLiveChatToken(String liveChatToken) {
		this.liveChatToken = liveChatToken;
	}

	public RealmList<Room> getOpenedRooms() {
		return openedRooms;
	}

	public void setOpenedRooms(RealmList<Room> openedRooms) {
		this.openedRooms = openedRooms;
	}

	public RealmList<Room> getClosedRooms() {
		return closedRooms;
	}

	public void setClosedRooms(RealmList<Room> closedRooms) {
		this.closedRooms = closedRooms;
	}

	public Room getRoomById(String roomId) {
		for (Room room : this.openedRooms) {
			if (room.getId().equals(roomId)) {
				return room;
			}
		}
		for (Room room : this.closedRooms) {
			if (room.getId().equals(roomId)) {
				return room;
			}
		}
		return null;
	}

	public RocketChatUser getRcUser() {
		return rcUser;
	}

	public void setRcUser(RocketChatUser rcUser) {
		this.rcUser = rcUser;
	}
	//endregion

	public boolean equals(Object hostObject) {
		if (hostObject == null) {
			return false;
		}
		if (hostObject == this) {
			return true;
		}

		Host host = (Host) hostObject;
		return this.hostRocketChatApiUrl.equals(host.getHostRocketChatApiUrl());
	}
}
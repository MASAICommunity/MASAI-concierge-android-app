package solutions.masai.masai.android.core.model.rocketchat;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by cWahl on 23.10.2017.
 */

public class Room extends RealmObject {

	@SerializedName("_id")
	private String id;

	@SerializedName("closedAt")
	private ClosedAt closedAt;

	@SerializedName("msgs")
	private int messagesCounter;

	private String hostHumanName;
	private int hostConPosition;
	private boolean closed;

	//region properties
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMessagesCounter() {
		return messagesCounter;
	}

	public void setMessagesCounter(int messagesCounter) {
		this.messagesCounter = messagesCounter;
	}

	public ClosedAt getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(ClosedAt closedAt) {
		this.closedAt = closedAt;
	}

	public String getHostHumanName() {
		return hostHumanName;
	}

	public void setHostHumanName(String hostHumanName) {
		this.hostHumanName = hostHumanName;
	}

	public int getHostConPosition() {
		return hostConPosition;
	}

	public void setHostConPosition(int hostConPosition) {
		this.hostConPosition = hostConPosition;
	}

	public boolean isClosed() {
		return this.closedAt != null;
	}
	//endregion

	public boolean equals(Object roomObject) {
		if (roomObject == null) {
			return false;
		}
		if (roomObject == this) {
			return true; //if both pointing towards same object on heap
		}

		Room room = (Room) roomObject;
		return this.id.equals(room.id);
	}
}
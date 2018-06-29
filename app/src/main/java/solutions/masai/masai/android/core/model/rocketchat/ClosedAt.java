package solutions.masai.masai.android.core.model.rocketchat;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ClosedAt extends RealmObject {

	public ClosedAt(){}

	@SerializedName("$date")
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}

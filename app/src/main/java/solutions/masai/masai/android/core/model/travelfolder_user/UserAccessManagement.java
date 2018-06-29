package solutions.masai.masai.android.core.model.travelfolder_user;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tom on 28.08.17.
 */

public class UserAccessManagement {

	@SerializedName("Count")
	private int count;

	@SerializedName("Items")
	private ArrayList<String> items;

	@SerializedName("ScannedCount")
	private int scannedCount;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}

	public int getScannedCount() {
		return scannedCount;
	}

	public void setScannedCount(int scannedCount) {
		this.scannedCount = scannedCount;
	}
}

package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.Locale;

/**
 * Created by cWahl on 25.08.2017.
 */

public class Choice {

	private String value;
	private String textDE;
	private String textEN;

	//region properties
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTextDE() {
		return textDE;
	}

	public void setTextDE(String text) {
		this.textDE = text;
	}

	public String getTextEN() {
		return textEN;
	}

	public void setTextEN(String text) {
		this.textEN = text;
	}
	//endregion

	public String toString() {
		Locale currentLocale = Locale.getDefault();
		if (currentLocale.getISO3Language().equalsIgnoreCase("deu")) {
			return getTextDE();
		} else {
			return getTextEN();
		}
	}
}

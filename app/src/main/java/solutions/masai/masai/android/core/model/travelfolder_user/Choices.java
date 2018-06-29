package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.List;
import java.util.Locale;

/**
 * Created by cWahl on 25.08.2017.
 */

public class Choices {

	private String dataKey;
	private String textDE;
	private String textEN;
	private List<Choice> choices;

	//region properties
	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
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

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
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

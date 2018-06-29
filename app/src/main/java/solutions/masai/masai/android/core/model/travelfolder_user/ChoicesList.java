package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.Map;

/**
 * Created by cWahl on 25.08.2017.
 */

public class ChoicesList {
	private Map<String, Choices> choicesList;

	public void setChoicesList(Map<String, Choices> choicesList) {
		this.choicesList = choicesList;
	}

	public Choices getChoices(ChoiceType type) {
		return choicesList.get(type.toString());
	}
}

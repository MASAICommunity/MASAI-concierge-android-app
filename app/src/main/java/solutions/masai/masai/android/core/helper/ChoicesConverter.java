package solutions.masai.masai.android.core.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import solutions.masai.masai.android.core.model.travelfolder_user.Choice;
import solutions.masai.masai.android.core.model.travelfolder_user.Choices;
import solutions.masai.masai.android.core.model.travelfolder_user.ChoicesList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static solutions.masai.masai.android.core.helper.DeserializerHelper.getListObject;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getMapObject;
import static solutions.masai.masai.android.core.helper.DeserializerHelper.getStringField;

/**
 * Created by cWahl on 25.08.2017.
 */

public class ChoicesConverter implements JsonDeserializer<ChoicesList> {

	@Override
	public ChoicesList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonArray jsonItems = json.getAsJsonObject().get("Items").getAsJsonArray();

		Map<String, Choices> choicesTempList = new HashMap<>();
		for (JsonElement jsonItem : jsonItems) {
			Choices choices = new Choices();
			choices.setDataKey(getStringField(jsonItem.getAsJsonObject(), "DataKey"));
			choices.setTextEN(getStringField(jsonItem.getAsJsonObject(), "Text_EN"));
			choices.setTextDE(getStringField(jsonItem.getAsJsonObject(), "Text_DE"));

			JsonArray jsonChoices = getListObject(jsonItem.getAsJsonObject(), "Choices");
			List<Choice> choiceTempList = new ArrayList<>();
			for (JsonElement jsonChoice : jsonChoices) {
				Choice choice = new Choice();
				choice.setValue(getStringField(getMapObject(jsonChoice.getAsJsonObject()), "Value"));
				choice.setTextEN(getStringField(getMapObject(jsonChoice.getAsJsonObject()), "Text_EN"));
				choice.setTextDE(getStringField(getMapObject(jsonChoice.getAsJsonObject()), "Text_DE"));
				choiceTempList.add(choice);
			}
			choices.setChoices(choiceTempList);
			choicesTempList.put(choices.getDataKey(), choices);
		}

		ChoicesList choiceList = new ChoicesList();
		choiceList.setChoicesList(choicesTempList);

		return choiceList;
	}
}

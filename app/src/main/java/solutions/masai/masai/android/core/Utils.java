package solutions.masai.masai.android.core;

import solutions.masai.masai.android.core.model.travelfolder_user.ChoiceType;

import java.util.regex.Pattern;

/**
 * Created by cWahl on 18.10.2017.
 */

public class Utils {

	private Utils(){}

	public static ChoiceType getChoiceTypeFromCheckTextViewTag(String tag) {
		return ChoiceType.getByValue(tag.split(Pattern.quote("|"))[0]);
	}

	public static String getChoiceTextFromCheckTextViewTag(String tag) {
		return tag.split(Pattern.quote("|"))[1];
	}
}

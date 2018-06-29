package solutions.masai.masai.android.core.helper;

import android.support.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by cWahl on 25.08.2017.
 */

public class DeserializerHelper {

	public static final String MAP_INDICATOR = "M";
	public static final String STRING_INDICATOR = "S";
	public static final String LIST_INDICATOR = "L";
	public static final String NUMBER_INDICATOR = "N";
	public static final String BOOLEAN_INDICATOR = "BOOL";

	@Nullable
	public static Boolean getBooleanField(JsonObject jsonObject, String fieldName) {
		if (jsonObject.has(fieldName)) {
			return jsonObject.get(fieldName).getAsJsonObject().get(BOOLEAN_INDICATOR).getAsBoolean();
		} else {
			return null;
		}
	}

	@Nullable
	public static String getStringField(JsonObject jsonObject, String fieldName) {
		if (jsonObject.has(fieldName)) {
			return getStringField(jsonObject.get(fieldName).getAsJsonObject());
		} else {
			return null;
		}
	}

	@Nullable
	public static String getStringField(JsonObject jsonObject) {
		if (jsonObject.has(STRING_INDICATOR)) {
			return jsonObject.get(STRING_INDICATOR).getAsString();
		} else {
			return null;
		}
	}

	@Nullable
	public static String getNumberField(JsonObject jsonObject, String fieldName) {
		if (jsonObject.has(fieldName)) {
			return getNumberField(jsonObject.get(fieldName).getAsJsonObject());
		} else {
			return null;
		}
	}

	@Nullable
	public static String getNumberField(JsonObject jsonObject) {
		if (jsonObject.has(NUMBER_INDICATOR)) {
			return jsonObject.get(NUMBER_INDICATOR).getAsString();
		} else {
			return null;
		}
	}

	@Nullable
	public static JsonObject getMapObject(JsonObject jsonObject, String fieldName) {
		if (jsonObject.has(fieldName)) {
			return getMapObject(jsonObject.get(fieldName).getAsJsonObject());
		} else {
			return null;
		}
	}

	@Nullable
	public static JsonObject getMapObject(JsonObject jsonObject) {
		if (jsonObject.has(MAP_INDICATOR)) {
			return jsonObject.get(MAP_INDICATOR).getAsJsonObject();
		} else {
			return null;
		}
	}

	@Nullable
	public static JsonArray getListObject(JsonObject jsonObject, String fieldName) {
		if (jsonObject.has(fieldName)) {
			return getListObject(jsonObject.get(fieldName).getAsJsonObject());
		} else {
			return null;
		}
	}

	@Nullable
	public static JsonArray getListObject(JsonObject jsonObject) {
		if (jsonObject.has(LIST_INDICATOR)) {
			return jsonObject.get(LIST_INDICATOR).getAsJsonArray();
		} else {
			return null;
		}
	}
}

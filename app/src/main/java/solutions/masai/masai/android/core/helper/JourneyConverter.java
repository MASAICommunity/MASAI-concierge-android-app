package solutions.masai.masai.android.core.helper;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import solutions.masai.masai.android.core.model.journey.Journey;
import solutions.masai.masai.android.core.model.journey.Segment;
import solutions.masai.masai.android.core.model.journey.segment_types.ActivitySegment;
import solutions.masai.masai.android.core.model.journey.segment_types.AirSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.CarSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.HotelSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.RailSegment;

import java.lang.reflect.Type;

/**
 * Created by wolfg on 31/08/2017.
 */

public class JourneyConverter implements JsonDeserializer<Journey> {

    @Override
    public Journey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Journey journey = new Journey();

        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.get("JourneyId").getAsString();
        String userId = jsonObject.get("UserId").getAsString();

        journey.setJourneyId(id);
        journey.setUserId(userId);

        JsonArray segments = jsonObject.getAsJsonArray("segments");

        for (JsonElement jsonElement : segments) {
            String type = ((JsonObject) jsonElement).get("type").getAsString();

            Segment typeModel = null;

            if("Air".equals(type)) {
                typeModel =
                gsonBuilder.create().fromJson(jsonElement.toString(), AirSegment
                        .class);
            } else if("Car".equals(type)) {
                typeModel =
                gsonBuilder.create().fromJson(jsonElement.toString(), CarSegment
                        .class);
            }else if("Hotel".equals(type)) {
                typeModel =
                gsonBuilder.create().fromJson(jsonElement.toString(), HotelSegment
                        .class);
            }else if("Rail".equals(type)) {
                typeModel =
                gsonBuilder.create().fromJson(jsonElement.toString(), RailSegment
                        .class);
            }else if("Activity".equals(type)) {
                typeModel =
                gsonBuilder.create().fromJson(jsonElement.toString(), ActivitySegment
                        .class);
            }

            journey.addSegment(typeModel);
        }

        return journey;
    }
}

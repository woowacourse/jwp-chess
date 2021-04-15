package chess.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonConverter {
    private static final com.google.gson.Gson gson = new com.google.gson.Gson();

    public static JsonObject toJsonObject(String jsonString) {
        return gson.fromJson(jsonString, JsonObject.class);
    }

    public static JsonObject toJsonObject(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, JsonObject.class);
    }
}

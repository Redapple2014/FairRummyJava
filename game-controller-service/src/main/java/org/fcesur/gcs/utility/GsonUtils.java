package org.fcesur.gcs.utility;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GsonUtils {

    public static Gson gson = getGson();

    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    public static <T> String toJson(T jsonElement) {
        return gson.toJson(jsonElement);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static JsonObject getJsonObject(String jsonInput) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(jsonInput).getAsJsonObject();
        return obj;
    }

}

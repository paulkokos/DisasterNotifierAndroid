package gr.teicm.ieee.madc.disasternotifierandroid.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

class MapConverterService {
    public static String toString(Map<String, String> map) throws JSONException {
        JSONObject result = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result.toString();
    }
}

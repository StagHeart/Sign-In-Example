package com.wesgue.signinexample.Custom.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wesley Gue on 10/12/2016.
 *  Reads JSON Strings from the Server
 */

public class JSONReader {


    // Returns Session ID
    public static String sessionJSONReader(String string) throws JSONException {
        JSONObject reader = new JSONObject(string);
        return reader.optString("session");
    }


    // Returns User ID
    public static String userJSONReader(String string) throws JSONException {
        JSONObject reader = new JSONObject(string);
        return reader.optString("user_id");
    }
}

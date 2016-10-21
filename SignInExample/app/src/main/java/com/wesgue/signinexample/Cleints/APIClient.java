package com.wesgue.signinexample.Cleints;

/**
 * Created by Wesley Gue on 10/21/2016.
 */

/**
 * Created by Wesley Gue on 10/11/2016.
 *
 * A Singleton that Helps with Server Side Task and Information
 */
public class APIClient {

    // Member Variables
    private static APIClient mInstance = null;
    private static boolean isDebugging = false; // If set to true Crashlytics will not run
    private static String mBaseURL = "https://your base URL"; // Your Base URL
    private static String mUserId; // Users ID from Server
    private static String mSessionId; // User's Session ID from Server


    private APIClient() {
    }

    public static APIClient getInstance() {
        if (mInstance == null) {
            mInstance = new APIClient();
        }
        return mInstance;
    }


    // Getters and Setters

    public boolean isDebugging() {
        return isDebugging;
    }

    public void setDebugging(boolean debugging) {
        isDebugging = debugging;
    }

    public String getBaseURL() {
        return mBaseURL;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmSessionId() {
        return mSessionId;
    }

    public void setmSessionId(String mSessionId) {
        this.mSessionId = mSessionId;
    }

}

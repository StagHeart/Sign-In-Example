package com.wesgue.signinexample.AsyncTasks;

import android.os.AsyncTask;

import com.wesgue.signinexample.Activities.SplashActivity;
import com.wesgue.signinexample.Cleints.APIClient;
import com.wesgue.signinexample.Databases.DatabaseHelper;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wesley Gue on 10/14/2016.
 *
 *  Check with the Server if Session is Valid
 */

public class CheckSessionAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member Variables
    private SplashActivity mSplashActivity; // activity to run needed functions
    int mResponseCode; // response code from server

    /// Constructor
    public CheckSessionAsyncTask(SplashActivity splashActivity) {
        mSplashActivity = splashActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpURLConnection httpURLConnection = null;
        try {
            // session url for server.
            URL url = new URL(APIClient.getInstance().getBaseURL() + "/api/sessions");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");

            // get Session Server ID
            DatabaseHelper db = new DatabaseHelper(mSplashActivity);
            String session_id = db.getSession(1).getSession_server_id();

            // header of the key
            httpURLConnection.setRequestProperty("x-session-id", session_id);

            // Response code from server
            mResponseCode = httpURLConnection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();

            // Some kind of network issue. Don't invalidate session.
            mSplashActivity.setSessionValidation(true);

            while(mSplashActivity.getIsTimerRunning() == true){
                // wait for icon on splash screen to finish showing
            }
            return null;
        }

        if (mResponseCode == 200 || mResponseCode == 404) {

            // session is valid or there was a network error
            mSplashActivity.setSessionValidation(true);

        }else{

            // session isn't valid. remove it.
            mSplashActivity.setSessionValidation(false);
        }

        while(mSplashActivity.getIsTimerRunning() == true){
            // wait for icon on splash screen to finish showing
        }

        return null;

    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        // Icon is done displaying for 2.5 seconds in SplashActivity.
        // Return to MainActivity.
        mSplashActivity.returnToMainActivity();
    }

}


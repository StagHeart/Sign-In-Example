package com.wesgue.signinexample.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.wesgue.signinexample.Cleints.APIClient;
import com.wesgue.signinexample.Custom.JSON.JSONReader;
import com.wesgue.signinexample.Databases.DatabaseHelper;
import com.wesgue.signinexample.Databases.Models.Session;
import com.wesgue.signinexample.Databases.Models.User;

import org.json.JSONException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Wesley Gue on 10/10/2016.
 * <p>
 * MainActivity  does not have any view. It transitions between
 * SplashActivity, SignInActivity, and RowListActivity
 */
public class MainActivity extends AppCompatActivity {

    // this flag with make sure Crashlyitics will not log your crashes while you are developing
    // TODO [FLAG] make false for production !!!
    private boolean debuggingDev = true;
    // TODO [FLAG] make false for production !!!

    // Used for returning from other activities.
    private static final int _SPLASH = 0; // for SplashActivity
    private static final int _SIGNIN = 1;  // for LoginActivity

    // Member Variables
    private DatabaseHelper mDatabase;  //  Database

    // Twitter API Client
    private String twitterIDKey = "Your Twitter ID Key";
    private String twitterSecretKey = "Your Twitter Secret Key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // For Fabric Crashlytics
        //Fabric Code [START]
        APIClient.getInstance().setDebugging(debuggingDev);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterIDKey, twitterSecretKey);

        if (debuggingDev) {
            // Won't send Crashes
            Fabric.with(this, new Twitter(authConfig));
        } else {
            // Will send Crashes
            Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        }
        //Fabric Code [END]

        // Create new Database
        mDatabase = new DatabaseHelper(this);
        mDatabase.close();


        // Run Splash Screen. Will return here after done.
        Intent intent = new Intent(this, SplashActivity.class);
        startActivityForResult(intent, _SPLASH);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Splash Screen Returned. Have user login if session isn't valid.
            case (_SPLASH): {
                if (resultCode == Activity.RESULT_OK) {

                    // check switch screen to display to user.
                    splashResponse(data.getExtras().getBoolean("IS_SESSION_VALID"));
                }
                break;
            }
            // User Logged in. Get upload behavior.
            case (_SIGNIN): {
                if (resultCode == Activity.RESULT_OK) {

                    //get user's information from email and server response
                    loginResponse(data.getStringExtra("EMAIL"), data.getStringExtra("RESPONSE"));
                }
                break;
            }
        }

    }


    // Override onBackPressed so the user can't back out of the login screen
    @Override
    public void onBackPressed() {
        return;
    }


    // If session isn't valid run LoginActivity.
    private void splashResponse(boolean isSessionValid) {
        if (isSessionValid) {

            runRowListActivity();
        } else {

            // Session isn't valid run SignInActivity
            runSignInActivity();
        }

    }


    // see if the user exist in the database. if not add them.
    // run the next activity accordingly
    private void loginResponse(String email, String response) {

        try {
            // Reading JSON Response
            String sessionId = JSONReader.sessionJSONReader(response);
            String userId = JSONReader.userJSONReader(response);

            // Make sure IDs aren't null before moving forward
            if (sessionId == null || userId == null) {

                // There's an ID null. Go back to Sign In Screen
                runSignInActivity();
            } else {

                // Check if User ID already exist before creating a new user
                DatabaseHelper db = new DatabaseHelper(this);
                if (db.checkIfUserIdExist(userId)) {

                    //  User exist.
                    // Set APIClient User amd Session ID
                    APIClient.getInstance().setmUserId(userId);
                    APIClient.getInstance().setmSessionId(sessionId);


                    // Continue to RowListActivity.
                    db.close();
                    runRowListActivity();

                } else {

                    // User doesn't exist. Add User to the database.
                    db.addUser(new User(userId, email, sessionId));
                    // Add Session to the database.
                    db.addSession((new Session(1, sessionId)));

                    // Set APIClient User and Session ID
                    APIClient.getInstance().setmUserId(userId);
                    APIClient.getInstance().setmSessionId(sessionId);

                    // Run RowListActivity to get users upload_pref
                    db.close();
                    runRowListActivity();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void runSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, _SIGNIN);
    }


    public void runRowListActivity() {
        Intent intent = new Intent(this, RowListActivity.class);
        startActivity(intent);
    }
}

package com.wesgue.signinexample.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.wesgue.signinexample.AsyncTasks.CheckSessionAsyncTask;
import com.wesgue.signinexample.Cleints.APIClient;
import com.wesgue.signinexample.Databases.DatabaseHelper;
import com.wesgue.signinexample.Databases.Models.User;
import com.wesgue.signinexample.R;

/**
 * Created by Wesley Gue on 10/10/2016.
 *
 * Displays App Logo for 2.5 seconds then returns to
 * MainActivity
 */
public class SplashActivity extends AppCompatActivity {

    // Member variables
    private boolean mIsTimerRunning = true; // used to tell if time is running
    private boolean mIsSessionValid = false; // sent to MainActivity after SplashActivity is done.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // See is a session exist in the database. If So validate it.
        DatabaseHelper db = new DatabaseHelper(this);
        if(db.doesSessionExist()) {

            db.close();
            // check if session is valid
            new CheckSessionAsyncTask(this).execute();

            // Display icon for 2.5 seconds.
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // check to see if Session is Valid
                    mIsTimerRunning = false;
                }
            }, 2500);
        }
        else{

            db.close();

            // Display icon for 2.5 seconds.
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // check to see if Session is Valid
                    mIsTimerRunning = false;
                    // return to MainActivity
                    returnToMainActivity();
                }
            }, 2500);

        }


    }


    // Override onBackPressed so the user can't back out of the splash screen
    @Override
    public void onBackPressed() {
        return;
    }


    // returns back to MainActivity
    public void returnToMainActivity() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("IS_SESSION_VALID", mIsSessionValid);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }


    // Called from CheckSessionAsyncTask. If true the session is valid and set the APIClients
    // fields to reflect that. If false set the Users Session ID to null and delete the session
    // itself from the database.
    public void setSessionValidation(boolean bool) {

        DatabaseHelper db = new DatabaseHelper(this);
        String sessionId = db.getSession(1).getSession_server_id();
        User user = db.getUserBySessionId(sessionId);
        if (bool) {

            // Session Valid. Set User and Session ID
            APIClient.getInstance().setmSessionId(db.getSession(1).getSession_server_id());
            APIClient.getInstance().setmUserId(user.getUser_id());

        } else {

            // Session invalid. Set Users Session to null and delete Session.
            db.setUserSessionToNull(user);
            db.deleteSession(db.getSession(1));

        }

        mIsSessionValid = bool;
        db.close();

    }

    // Called from CheckSessionAsyncTask. Used to make sure it allows the icon to be displayed
    // for 2.5 seconds before it calls returnToMainActivity.
    public boolean getIsTimerRunning() {
        return mIsTimerRunning;
    }
}

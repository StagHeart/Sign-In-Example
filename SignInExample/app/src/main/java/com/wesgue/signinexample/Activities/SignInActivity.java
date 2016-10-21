package com.wesgue.signinexample.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wesgue.signinexample.AsyncTasks.SignInAsyncTask;
import com.wesgue.signinexample.Custom.Font.FontTextView;
import com.wesgue.signinexample.R;

/**
 * Created by Wesley Gue on 10/21/2016.
 */

public class SignInActivity extends AppCompatActivity {

    //UI Field Variables
    private EditText _emailText; // Email Text Field
    private EditText _passwordText; // Password Text Field
    private Button _buttonSignIn; // Sign in button

    //Member Variables
    private ProgressDialog mProgressDialog; // Progress Spinner
    private String mEmail; // Users Email
    private String mPassword; // Users Password
    private String mResponse; // response from SignInAsync
    int mSdk = android.os.Build.VERSION.SDK_INT; // Users device SDK version


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // set UI Fields
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _buttonSignIn = (Button) findViewById(R.id.btn_sign_in);

        // Sign In Button Listener
        _buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try to login
                login();
            }
        });

    }


    // Override onBackPressed so the user can't back out of the login screen.
    @Override
    public void onBackPressed() {
        return;
    }


    // Set ProgressDialog, check if email and password fields aren't null.
    // If so try to login to the server.
    public void login() {

        // set ProgressDialog to custom theme
        mProgressDialog = new ProgressDialog(SignInActivity.this,
                R.style.AppTheme_Dark_Dialog);

        // Validate email and password is not null and email patterns match.
        if (!validate()) {

            // Fields are not valid. Display Errors.
            onLoginFailed();
            return;
        }
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Authenticating...");

        // set sign in button to false so user can't click it while
        // log in process is running
        _buttonSignIn.setEnabled(false);

        // retrieve email from field
        mEmail = _emailText.getText().toString();
        // retrieve password from field
        mPassword = _passwordText.getText().toString();

        // try to sign in via an AsyncTask
        new SignInAsyncTask(SignInActivity.this, mProgressDialog, mEmail, mPassword).execute();

    }


    // Confirm that the email and password field are not null or email patterns match.
    // Display proper errors if not.
    public boolean validate() {

        boolean valid = true; // returned at the end of method
        String email = _emailText.getText().toString(); // retrieve email from field
        String password = _passwordText.getText().toString(); // retrieve password from field

        // confirm that the email and password field are not null or email patterns match.
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            // email not confirmed. Display Errors
            _emailText.setError("enter a valid email address");
            valid = false; // returned at the end of method
            // check users device sdk version
            if (mSdk < Build.VERSION_CODES.JELLY_BEAN) {

                // set error field
                _emailText.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edit_field_error, null));
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    // set error field
                    _emailText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edit_field_error, null));
                }
            }
        } else {

            // set error field
            _emailText.setError(null);
        }

        if (password.isEmpty()) {

            // password field is empty. Set Errors
            _passwordText.setError("enter a valid email address");
            valid = false; // returned at the end of method
            // check users device sdk version
            if (mSdk < Build.VERSION_CODES.JELLY_BEAN) {

                // set error field
                _passwordText.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edit_field_error, null));
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    // set error field
                    _passwordText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edit_field_error, null));
                }
            }
        } else {

            // set error field
            _passwordText.setError(null);
        }

        return valid;

    }


    // Display Error Pop Up Dialog
    public void onLoginFailed() {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_sign_in_error);

        // set custom font views
        FontTextView fontX = (FontTextView) dialog.findViewById(R.id.font_x);
        fontX.setCustomFont(this.getApplicationContext(), "custom.ttf");
        fontX.setText("@");
        FontTextView fontXCircle = (FontTextView) dialog.findViewById(R.id.font_x_circle);
        fontXCircle.setCustomFont(this.getApplicationContext(), "custom.ttf");
        fontXCircle.setText("n");

        // set the custom dialog components
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        // Enable the Sign in button. The login process is over
        _buttonSignIn.setEnabled(true);

    }


    // Called from SignInAsyncTask. Checks the response from the server.
    public void setResponse(String result) {

        mResponse = result;
        // see if the response from the server is valid
        checkResponseValid();

    }


    // Checks if response from server is server is valid.
    // Dismisses progress dialog.
    public void checkResponseValid() {

        if (mResponse == null) {

            // response invalid, login failed
            onLoginFailed();
        } else {

            // response valid,
            onLoginSuccess();
        }
        // remove progress dialog from screen.
        mProgressDialog.dismiss();
    }


    // set Intent with email,password, and response. Return to MainActivity
    public void onLoginSuccess() {

        _buttonSignIn.setEnabled(true);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EMAIL", mEmail);
        resultIntent.putExtra("RESPONSE", mResponse);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }
}

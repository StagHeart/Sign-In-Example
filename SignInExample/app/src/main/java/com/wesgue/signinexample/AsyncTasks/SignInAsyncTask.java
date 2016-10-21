package com.wesgue.signinexample.AsyncTasks;


import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wesgue.signinexample.Activities.SignInActivity;
import com.wesgue.signinexample.Cleints.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wesley Gue on 10/11/2016.
 * Try to login user with email and password on Server
 */
public class SignInAsyncTask extends AsyncTask<Void, Void, String> {

    // Member Variables
    private SignInActivity mSignInActivity; // for setting response in SignInActivity
    private String mEmail; // Users email
    private String mPassword; // Users password
    private ProgressDialog mProgressDialog; // dialog spinner
    private String mResponse; // return to activity in onPostExecute


    // Constructor
    public SignInAsyncTask(SignInActivity signInActivity, ProgressDialog progressDialog, String email, String password) {
        mSignInActivity = signInActivity;
        mProgressDialog = progressDialog;
        mEmail = email;
        mPassword = password;
    }

    protected void onPreExecute() {
        // Display Dialog
        mProgressDialog.show();
    }


    @Override
    protected String doInBackground(Void... params) {

        try {
            // session url for the server.
            URL url = new URL(APIClient.getInstance().getBaseURL() + "/api/sessions");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");

            // header of the key
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());

            // write email and password to server
            writeStream(outputStream);


            // read response
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            readStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResponse;

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mSignInActivity != null)
            // set response in SignInActivity
            mSignInActivity.setResponse(result);

        if (mProgressDialog != null && mProgressDialog.isShowing())
            // remove dialog
            mProgressDialog.dismiss();

    }


    // Read Stream coming from server
    private Void readStream(InputStream in) throws IOException {

        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = read.readLine()) != null) {
            total.append(line);
        }
        mResponse = total.toString();

        return null;

    }


    // write stream to server
    private void writeStream(OutputStream out) throws IOException, JSONException {

        String output;
        JSONObject jsonObject = new JSONObject();
        // Sign in with Server Info
        JSONObject serverObject = new JSONObject();
        serverObject.put("uid", mEmail);
        serverObject.put("password", mPassword);
        jsonObject.put("obj", serverObject);
        output = jsonObject.toString();
        out.write(output.getBytes());
        out.flush();

    }
}

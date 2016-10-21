package com.wesgue.signinexample.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wesgue.signinexample.R;

public class RowListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row_list);
    }

    // Override onBackPressed so the user can't back out of the pool list screen
    @Override
    public void onBackPressed() {
        return;
    }
}

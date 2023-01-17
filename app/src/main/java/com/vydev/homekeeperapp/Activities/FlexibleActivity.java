package com.vydev.homekeeperapp.Activities;

import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class FlexibleActivity extends AppCompatActivity {
    protected void hideAppbar() {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
    }
}

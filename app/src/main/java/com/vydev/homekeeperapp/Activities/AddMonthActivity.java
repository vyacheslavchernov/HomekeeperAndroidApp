package com.vydev.homekeeperapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.color.DynamicColors;
import com.vydev.homekeeperapp.R;

public class AddMonthActivity extends FlexibleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        hideAppbar();

        setContentView(R.layout.activity_add_month);
    }
}
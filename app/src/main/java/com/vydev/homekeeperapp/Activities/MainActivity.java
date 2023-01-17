package com.vydev.homekeeperapp.Activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.color.DynamicColors;
import com.vydev.homekeeperapp.Api.RsBody.Core.ApiResponse;
import com.vydev.homekeeperapp.Api.Services;
import com.vydev.homekeeperapp.R;

import java.text.DecimalFormat;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FlexibleActivity {

    DecimalFormat df = new DecimalFormat("#.##");

    private final String RUB_CHAIR = " ₽";

    Context context = this;

    TextView lastTotalLabel;
    TextView lastDateLabel;

    View detailedView;

    private String lastMonthId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        hideAppbar();

        setContentView(R.layout.activity_main);

        lastTotalLabel = (TextView) findViewById(R.id.itemTitle);
        lastDateLabel = (TextView) findViewById(R.id.itemSubtitle);

        detailedView = (View) findViewById(R.id.detailedView);

        Services.getMonthDataService().getlast().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.body();
                if (apiResponse.getStatus().getCode() == 0) {
                    Map<String, String> payload = (Map<String, String>) apiResponse.getPayload();

                    lastDateLabel.setText(String.format("10.%s.%s", payload.get("month"), payload.get("year")));

                    lastMonthId = String.valueOf(payload.get("id"));
                    Services.getCalculatorService().calc(lastMonthId).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            ApiResponse apiResponse = response.body();
                            if (apiResponse.getStatus().getCode() == 0) {
                                Map<String, String> payload = (Map<String, String>) apiResponse.getPayload();

                                lastTotalLabel.setText(String.valueOf(df.format(payload.get("totalWithCommunalAndAdditional"))) + RUB_CHAIR);
                            } else {
                                new AlertDialog.Builder(context)
                                        .setTitle(apiResponse.getStatus().getErrors().get(0).getDescription())
                                        .setMessage(apiResponse.getStatus().getErrors().get(0).getTrace())
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                        }
                    });
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle(apiResponse.getStatus().getErrors().get(0).getDescription())
                            .setMessage(apiResponse.getStatus().getErrors().get(0).getTrace())
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
            }
        });

        detailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", lastMonthId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
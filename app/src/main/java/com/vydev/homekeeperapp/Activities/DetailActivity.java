package com.vydev.homekeeperapp.Activities;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.color.DynamicColors;
import com.vydev.homekeeperapp.Api.RsBody.Core.ApiResponse;
import com.vydev.homekeeperapp.Api.Services;
import com.vydev.homekeeperapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dlyt.yanndroid.oneui.widget.RoundFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends FlexibleActivity {

    private final String RUB_CHAIR = " ₽";

    DecimalFormat df = new DecimalFormat("#.##");

    Context context = this;
    private Bundle extras;

    LinearLayout itemsRoot;

    TextView monthDateLabel;

    Map<String, Object> monthData;
    Map<String, Object> monthCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        hideAppbar();

        setContentView(R.layout.activity_detail);

        extras = getIntent().getExtras();

        monthDateLabel = findViewById(R.id.MonthDateLabel);
        itemsRoot = findViewById(R.id.itemsRoot);


        Services.getMonthDataService().get(extras.getString("id")).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.body();
                if (apiResponse.getStatus().getCode() == 0) {
                    monthData = (Map<String, Object>) apiResponse.getPayload();

                    monthDateLabel.setText(String.format("10.%s.%s", monthData.get("month"), monthData.get("year")));

                    itemsRoot.addView(
                            createListBlock(
                                    String.valueOf(df.format(monthData.get("rent"))) + RUB_CHAIR,
                                    "Аренда",
                                    R.drawable.ic_round_house_48,
                                    R.layout.fragment_listblock_top,
                                    false,
                                    null
                            )
                    );

                    itemsRoot.addView(
                            createListBlock(
                                    String.valueOf(monthData.get("housemates")),
                                    "Количество жильцов",
                                    R.drawable.ic_round_person_48,
                                    R.layout.fragment_listblock_bottom,
                                    false,
                                    null
                            )
                    );

                    Services.getCalculatorService().calc(String.valueOf(monthData.get("id"))).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            ApiResponse apiResponse = response.body();
                            if (apiResponse.getStatus().getCode() == 0) {
                                monthCalc = (Map<String, Object>) apiResponse.getPayload();

                                //TOTALS
                                itemsRoot.addView(
                                        createListBlock(
                                                String.valueOf(df.format(monthCalc.get("totalWithCommunalAndAdditional"))) + RUB_CHAIR,
                                                "Общая сумма (полная)",
                                                R.drawable.ic_round_credit_card_48,
                                                R.layout.fragment_listblock_top,
                                                true,
                                                new View[]{
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(monthCalc.get("totalWithCommunal"))) + RUB_CHAIR,
                                                                "Общая сумма (только с коммунальными платежами)",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(monthCalc.get("totalWithCommunalAndNegativeAdditional"))) + RUB_CHAIR,
                                                                "Общая сумма (с коммунальными платежами и вычетами)",
                                                                R.drawable.ic_round_currency_ruble_48)
                                                }
                                        )
                                );

                                //COMMUNALS
                                Map<String, String> communal = (Map<String, String>) monthCalc.get("communal");
                                itemsRoot.addView(
                                        createListBlock(
                                                String.valueOf(df.format(communal.get("total"))) + RUB_CHAIR,
                                                "Коммунальные платежи",
                                                R.drawable.ic_baseline_hot_tub_48,
                                                R.layout.fragment_listblock_middle,
                                                true,
                                                new View[]{
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(communal.get("hotwater"))) + RUB_CHAIR,
                                                                "Горячая вода",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(communal.get("coldwater"))) + RUB_CHAIR,
                                                                "Холодная вода",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(communal.get("drainage"))) + RUB_CHAIR,
                                                                "Водоотведение",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(communal.get("electricity"))) + RUB_CHAIR,
                                                                "Электроэнергия",
                                                                R.drawable.ic_round_currency_ruble_48)
                                                }
                                        )
                                );

                                //ADDITIONAL DATA
                                ArrayList<View> addDatas = new ArrayList<>();
                                double addDataSum = 0d;

                                for (Map<String, Object> data : (List<Map<String, Object>>) monthData.get("additionalData")) {
                                    addDataSum += (Double) data.get("amount");
                                    addDatas.add(
                                            createListBoxSubitem(
                                                    String.valueOf(df.format(data.get("amount"))) + RUB_CHAIR,
                                                    String.valueOf(data.get("description")),
                                                    R.drawable.ic_round_currency_ruble_48)
                                    );
                                }

                                itemsRoot.addView(
                                        createListBlock(
                                                String.valueOf(df.format(addDataSum))  + RUB_CHAIR,
                                                "Дополнительные позиции",
                                                R.drawable.ic_round_format_list_bulleted_48,
                                                R.layout.fragment_listblock_middle,
                                                true,
                                                addDatas.toArray()
                                        )
                                );

                                //COUNTERS
                                Map<String, String> counters = (Map<String, String>) monthData.get("countersData");
                                itemsRoot.addView(
                                        createListBlock(
                                                "Счётчики",
                                                null,
                                                R.drawable.ic_round_timeline_48,
                                                R.layout.fragment_listblock_middle,
                                                true,
                                                new View[]{
                                                        createListBoxSubitem(
                                                                String.valueOf(counters.get("hotwater")),
                                                                "Горячая вода",
                                                                R.drawable.ic_round_grid_view_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(counters.get("coldwater")),
                                                                "Холодная вода",
                                                                R.drawable.ic_round_grid_view_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(counters.get("electricity")),
                                                                "Электроэнергия",
                                                                R.drawable.ic_round_grid_view_48)
                                                }
                                        )
                                );

                                //TARIFFS
                                Map<String, String> tariffs = (Map<String, String>) monthData.get("tariffsData");
                                itemsRoot.addView(
                                        createListBlock(
                                                "Тарифы",
                                                null,
                                                R.drawable.ic_round_account_balance_48,
                                                R.layout.fragment_listblock_bottom,
                                                true,
                                                new View[]{
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(tariffs.get("hotwater"))) + RUB_CHAIR,
                                                                "Горячая вода",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(tariffs.get("coldwater"))) + RUB_CHAIR,
                                                                "Холодная вода",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(tariffs.get("drainage"))) + RUB_CHAIR,
                                                                "Водоотведение",
                                                                R.drawable.ic_round_currency_ruble_48),
                                                        createListBoxSubitem(
                                                                String.valueOf(df.format(tariffs.get("electricity"))) + RUB_CHAIR,
                                                                "Электроэнергия",
                                                                R.drawable.ic_round_currency_ruble_48)
                                                }
                                        )
                                );

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
    }

    private View createListBlock(
            String title,
            String subtitle,
            int icon,
            int listBlockType,
            boolean expandable,
            Object[] subitems) {

        View listBlock = LayoutInflater.from(context).inflate(listBlockType, null);

        TextView itemTitle = listBlock.findViewById(R.id.itemTitle);
        TextView itemSubtitle = listBlock.findViewById(R.id.itemSubtitle);
        ImageView itemIcon = listBlock.findViewById(R.id.itemImage);
        RoundFrameLayout wrapper = listBlock.findViewById(R.id.wrapper);
        LinearLayout expander = listBlock.findViewById(R.id.expander);
        ImageView expandIcon = listBlock.findViewById(R.id.expandIcon);

        wrapper.setClickable(expandable);
        if (expandable) {
            expandIcon.setVisibility(View.VISIBLE);

            wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout expander = view.findViewById(R.id.expander);
                    ImageView expandIcon = view.findViewById(R.id.expandIcon);
                    if (expander.getVisibility() == GONE) {
                        expandIcon.animate().rotation(180).setDuration(300);
                        expander.setVisibility(View.VISIBLE);
                    } else {
                        expandIcon.animate().rotation(0).setDuration(300);
                        expander.setVisibility(View.GONE);
                    }
                }
            });
        }

        itemTitle.setText(title);

        if (subtitle == null) {
            itemSubtitle.setVisibility(GONE);
        } else {
            itemSubtitle.setText(subtitle);
        }

        itemIcon.setImageResource(icon);

        if (subitems != null) {
            for (Object subitem : subitems) {
                View v = (View) subitem;
                expander.addView(v);
            }
        }

        return listBlock;
    }

    private View createListBoxSubitem(String title, String subtitle, int icon) {
        View subItem = LayoutInflater.from(context).inflate(R.layout.fragment_listblock_subitem, null);

        TextView itemTitle = subItem.findViewById(R.id.itemTitle);
        TextView itemSubtitle = subItem.findViewById(R.id.itemSubtitle);
        ImageView itemIcon = subItem.findViewById(R.id.itemImage);

        itemTitle.setText(title);
        itemSubtitle.setText(subtitle);
        itemIcon.setImageResource(icon);

        return subItem;
    }
}
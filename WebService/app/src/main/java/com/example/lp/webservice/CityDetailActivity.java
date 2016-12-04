package com.example.lp.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CityDetailActivity extends AppCompatActivity {

    private TextView tvCityDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        tvCityDetails = (TextView) findViewById(R.id.tvCityDetails);

        String cityName = getIntent().getStringExtra("cityName");
        tvCityDetails.setText(cityName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

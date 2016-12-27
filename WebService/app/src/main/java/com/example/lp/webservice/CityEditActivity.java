package com.example.lp.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CityEditActivity extends AppCompatActivity {

    private TextView tvCityName;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_edit);
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        this.cityName = fetchCityNameFromExtras();
        displayCityNameTitle();
    }

    public String fetchCityNameFromExtras() {
        return getIntent().getStringExtra("cityName").trim();
    }

    public void displayCityNameTitle() {
        tvCityName.setText(cityName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

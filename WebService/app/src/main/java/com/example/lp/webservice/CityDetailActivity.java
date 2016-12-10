package com.example.lp.webservice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityDetailActivity extends AppCompatActivity {

    private TextView tvCityDetails;
    private TextView tvCityName;

    private String cityName;
    private City cityDetails;
    private JSONObject cityDetailsJsonObjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        tvCityName = (TextView) findViewById(R.id.tvCityName);

        this.cityName = fetchCityNameFromExtras();

        displayCityNameTitle();
        displayCityDetails();
    }

    public String fetchCityNameFromExtras() {
        return getIntent().getStringExtra("cityName").trim();
    }

    public void displayCityNameTitle() {
        tvCityName.setText(cityName);
        tvCityDetails = (TextView) findViewById(R.id.tvCityDetails);
    }

    public void displayCityDetails() {

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + cityName;
        //select Nom_Ville, Code_Postal, Code_INSEE, Code_Region, Latitude, Longitude, Eloignement from villes where Nom_Ville LIKE "%Ambl" limit 30;

        // The request always return a JsonArray
        JsonArrayRequest requestToFetchCityJsonArray = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray cityListJSONArrayResponse) {
                        fetchCityDetailsFromJSONArray(cityListJSONArrayResponse);
                        fillCityDetails();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ToastMessage.displayUnexpectedResponseInToast(getApplicationContext());
                    }
                });

        RequestQueue.getInstance(this).addToRequestQueue(requestToFetchCityJsonArray);
    }

    public void fetchCityDetailsFromJSONArray(JSONArray cityListJSONArray) {
        try {
            this.cityDetails = City.createFromJSONArray(cityListJSONArray);
        }

        catch (JSONException exception) {
            exception.printStackTrace();
            ToastMessage.displayJSONReadErrorInToast(getApplicationContext());
        }
    }

    public void fillCityDetails() {
        StringBuffer details = new StringBuffer();
            details.append("Code Postal : " + Integer.toString(this.cityDetails.getPostalCode()) + "\n");
            details.append("Code INSEE : " + Integer.toString(this.cityDetails.getInseeCode())+ "\n");
            details.append("Code région : " + this.cityDetails.getRegionCode() + "\n");
            details.append("Latitude : " + Double.toString(this.cityDetails.getLatitude()) + "\n");
            details.append("Longitude : " + Double.toString(this.cityDetails.getLongitude()) + "\n");
            details.append("Éloignement : " + Double.toString(this.cityDetails.getRemoteness()) + "\n");
            this.tvCityDetails.setText(details.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

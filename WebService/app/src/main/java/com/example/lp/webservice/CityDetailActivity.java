package com.example.lp.webservice;

import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityDetailActivity extends AppCompatActivity {

    private TextView tvCityDetails;
    private JSONObject cityDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        TextView tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvCityDetails = (TextView) findViewById(R.id.tvCityDetails);

        String cityName = getIntent().getStringExtra("cityName").trim();
        tvCityName.setText(cityName);

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + cityName;
        executeFromHttpRequest(url);
    }

    public void executeFromHttpRequest(String url) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fetchCityDetailsFromJsonResponse(response);
                        displayCityDetails();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public boolean fetchCityDetailsFromJsonResponse(String jsonResponse) {

        if (null != jsonResponse) {
            try {
                JSONArray cities = new JSONArray(jsonResponse);
                this.cityDetails = cities.getJSONObject(0);
            }

            catch (JSONException ex) {
                ex.printStackTrace();
                this.tvCityDetails.setText("Erreur : " + ex.getMessage());
                return false;
            }
        }

        return false;
    }

    public void displayCityDetails() {
        StringBuffer details = new StringBuffer();

        try {
            details.append("Code Postal : " + Integer.toString(this.cityDetails.getInt("Code_Postal")) + "\n");
            details.append("Code INSEE : " + Integer.toString(this.cityDetails.getInt("Code_INSEE"))+ "\n");
            details.append("Code région : " + this.cityDetails.getString("Code_Region") + "\n");
            details.append("Latitude : " + Double.toString(this.cityDetails.getDouble("Latitude")) + "\n");
            details.append("Longitude : " + Double.toString(this.cityDetails.getDouble("Longitude")) + "\n");
            details.append("Éloignement : " + Double.toString(this.cityDetails.getDouble("Eloignement")) + "\n");
            this.tvCityDetails.setText(details.toString());
        }

        catch (JSONException ex) {
            ex.printStackTrace();
            this.tvCityDetails.setText("Erreur : " + ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

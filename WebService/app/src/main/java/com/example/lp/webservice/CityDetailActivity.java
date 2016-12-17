package com.example.lp.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityDetailActivity extends AppCompatActivity {

    private TextView tvCityName;

    private ArrayList<TextView> cityDetailTvs;

    private String cityName;
    private City cityDetails;
    private JSONObject cityDetailsJsonObjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        tvCityName = (TextView) findViewById(R.id.tvCityName);

        if(NetworkChecker.isNetworkActivated(this)) {
            this.cityName = fetchCityNameFromExtras();
            findTextViews();
            displayCityNameTitle();
            displayCityDetails();
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }
    }

    public void findTextViews() {
        this.cityDetailTvs = new ArrayList<TextView>();
        this.cityDetailTvs.add( (TextView) findViewById(R.id.tvPostalCode) );
        this.cityDetailTvs.add( (TextView) findViewById(R.id.tvRegionCode) );
        this.cityDetailTvs.add( (TextView) findViewById(R.id.tvInseeCode) );
        this.cityDetailTvs.add( (TextView) findViewById(R.id.tvLatitude) );
        this.cityDetailTvs.add( (TextView) findViewById(R.id.tvLongitude) );
        this.cityDetailTvs.add( (TextView) findViewById(R.id.tvRemoteness) );
    }

    public String fetchCityNameFromExtras() {
        return getIntent().getStringExtra("cityName").trim();
    }

    public void displayCityNameTitle() {
        tvCityName.setText(cityName);
    }

    //test in db :
    // select Nom_Ville, Code_Postal, Code_INSEE, Code_Region, Latitude, Longitude, Eloignement
    // from villes
    // where Nom_Ville LIKE "%Ambl" limit 30;

    public void displayCityDetails() {

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + cityName;

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

        ArrayList<String> cityDetailsInfos = this.cityDetails.getCharacteristicsAsArray();
        for (int i = 0 ; i < this.cityDetailTvs.size() ; i++) {
            this.cityDetailTvs.get(i).setText(cityDetailsInfos.get(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

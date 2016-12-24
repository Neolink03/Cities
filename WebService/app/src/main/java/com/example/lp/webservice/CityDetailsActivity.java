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

public class CityDetailsActivity extends AppCompatActivity {

    private TextView tvCityName;

    private ArrayList<TextView> cityDetailTextViews;

    private String cityName;
    private City cityDetails;
    private JSONObject cityDetailsJsonObjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        tvCityName = (TextView) findViewById(R.id.tvCityName);

        if(NetworkChecker.isNetworkActivated(this)) {
            this.cityName = fetchCityNameFromExtras();
            displayCityNameTitle();
            displayCityDetails();
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }
    }

    public void displayGpsCoordinates() {

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

        loadCityDetailsTextViews();

        // The request always return a JsonArray
        JsonArrayRequest requestToFetchCityJsonArray = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray cityListJSONArrayResponse) {
                        loadCityDetailsFromJSONArray(cityListJSONArrayResponse);
                        loadCityDetails();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ToastMessage.displayUnexpectedResponse(getApplicationContext());
                    }
                });

        RequestQueue.getInstance(this).addToRequestQueue(requestToFetchCityJsonArray);
    }

    public void loadCityDetailsFromJSONArray(JSONArray cityListJSONArray) {
        try {
            this.cityDetails = City.createFromJSONArray(cityListJSONArray);
        }

        catch (JSONException exception) {
            exception.printStackTrace();
            ToastMessage.displayJSONReadError(getApplicationContext());
        }
    }

    public void loadCityDetails() {

        ArrayList<String> cityDetailsInfos = this.cityDetails.getCharacteristicsAsArray();
        for (int i = 0; i < this.cityDetailTextViews.size() ; i++) {
            this.cityDetailTextViews.get(i).setText(cityDetailsInfos.get(i));
        }
    }

    public void loadCityDetailsTextViews() {
        this.cityDetailTextViews = new ArrayList<TextView>();
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.postalCodeLabelTextView) );
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.regionCodeLabelTextView) );
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.inseeCodeLabelTextView) );
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.latitudeLabelTextView) );
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.longitudeLabelTextView) );
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.remotenessLabelTextView) );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

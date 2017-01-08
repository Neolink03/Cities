package com.example.lp.webservice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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
    private String cityName;
    private FloatingActionButton cityEditfloatingActionButton;

    private ArrayList<TextView> cityDetailTextViews;

    private City cityDetails;
    private JSONObject cityDetailsJsonObjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        cityEditfloatingActionButton = (FloatingActionButton) findViewById(R.id.cityEditFloatingActionButton);

        if(NetworkChecker.isNetworkActivated(this)) {
            //this.cityName = fetchCityNameFromExtras();
            //displayCityNameTitle();
            displayCityDetails();
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void deleteCity() {

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + this.cityDetails.getInseeCode();

        JsonArrayRequest deleteRequest = new JsonArrayRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray cityListJSONArrayResponse) {
                    System.out.println("ok");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ToastMessage.displayUnexpectedServerResponse(getApplicationContext());
                    }
                });

        RequestQueue.getInstance(this).addToRequestQueue(deleteRequest);
    }

    public void displayCityEditForm(View view) {
        Intent toCityEditActivity = new Intent(CityDetailsActivity.this, CityEditActivity.class);
        toCityEditActivity.putExtra("cityName", this.cityName);
        startActivity(toCityEditActivity);
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

        String inseeCode = getIntent().getStringExtra("inseeCode");
        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + inseeCode;

        loadCityDetailsTextViews();

        // The request always return a JsonArray
        JsonArrayRequest requestToFetchCityJsonArray = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray cityListJSONArrayResponse) {
                        loadCityDetailsFromJSONArray(cityListJSONArrayResponse);
                        loadCityDetails();
                        displayCityEditFabIfLoadingDatasSuccessful();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ToastMessage.displayUnexpectedServerResponse(getApplicationContext());
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

        if(null != this.cityDetails) {
            ArrayList<String> cityDetailsInfos = this.cityDetails.getCharacteristicsAsArray();
            String info;

            String details;
            for (int i = 0; i < this.cityDetailTextViews.size() ; i++) {
                info = cityDetailsInfos.get(i);
                this.cityDetailTextViews.get(i).setText(info);
            }
        }
    }

    public void displayCityEditFabIfLoadingDatasSuccessful() {
        if(null == this.cityDetails) {
            this.cityEditfloatingActionButton.setVisibility(View.INVISIBLE);
        }
        else {
            this.cityEditfloatingActionButton.setVisibility(View.VISIBLE);
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
        this.cityDetailTextViews.add( (TextView) findViewById(R.id.inhabitantNumberTextView) );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

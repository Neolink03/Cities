package com.example.lp.webservice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private FloatingActionButton cityEditfloatingActionButton;

    private TextView cityNameTitleTextView;
    private ArrayList<TextView> cityDetailTextViews;

    private City cityDetails;
    private JSONObject cityDetailsJsonObjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        this.cityNameTitleTextView = (TextView) findViewById(R.id.cityNameTitleTextView);
        cityEditfloatingActionButton = (FloatingActionButton) findViewById(R.id.cityEditFloatingActionButton);

        displayNameFromExtras();

        if(NetworkChecker.isNetworkActivated(this)) {
            displayCityDetails();
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkChecker.isNetworkActivated(this)) {
            displayCityDetails();
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editCityDetailsMenu:
                displayCityEditForm(null);
                return true;
            case R.id.deleteCityDetailsMenu:
                deleteCity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteCity() {

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + this.cityDetails.getInseeCode();

        final CityDetailsActivity self = this;
        JsonArrayRequest deleteRequest = new JsonArrayRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray cityListJSONArrayResponse) {
                        ToastMessage.citySucessfullyDeleted(self.cityDetails.getName(), self);
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ToastMessage.cityUnSucessfullyDeleted(self.cityDetails.getName(), self);
                    }
                });

        RequestQueue.getInstance(this).addToRequestQueue(deleteRequest);
    }

    public void displayCityEditForm(View view) {
        Intent toCityEditActivity = new Intent(CityDetailsActivity.this, CityEditActivity.class);

        ArrayList<String> caracteristics = this.cityDetails.getCharacteristicsAsArray();

        toCityEditActivity.putExtra("cityName", this.cityDetails.getName());
        toCityEditActivity.putExtra("inseeCode", caracteristics.get(0));
        toCityEditActivity.putExtra("postalCode", caracteristics.get(1));
        toCityEditActivity.putExtra("regionCode", caracteristics.get(2));
        toCityEditActivity.putExtra("latitude", caracteristics.get(3));
        toCityEditActivity.putExtra("longitude", caracteristics.get(4));
        toCityEditActivity.putExtra("remoteness", caracteristics.get(5));
        toCityEditActivity.putExtra("inhabitantNumber", caracteristics.get(6));

        toCityEditActivity.putExtra("title", getString(R.string.update_city_title_edit_form));
        toCityEditActivity.putExtra("actionOnSave", "update");

        startActivity(toCityEditActivity);
    }

    public void displayNameFromExtras() {
        this.cityNameTitleTextView.setText(getIntent().getStringExtra("cityName"));
    }

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

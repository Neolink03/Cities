package com.example.lp.webservice.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.lp.webservice.Domain.City;
import com.example.lp.webservice.Util.NetworkChecker;
import com.example.lp.webservice.R;
import com.example.lp.webservice.Util.RequestQueue;
import com.example.lp.webservice.Util.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityDetailsActivity extends AppCompatActivity {

    private final Alert alertMessage = new Alert();
    private TextView tvCityName;
    private FloatingActionButton cityEditfloatingActionButton;

    private TextView cityNameTitleTextView;
    private TextView cityDetails;
    private ArrayList<TextView> cityDetailTextViews;

    private City city;
    private JSONObject cityDetailsJsonObjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        this.cityNameTitleTextView = (TextView) findViewById(R.id.cityNameTitleTextView);
        this.cityDetails = (TextView) findViewById(R.id.city_details_textview);
        cityEditfloatingActionButton = (FloatingActionButton) findViewById(R.id.cityEditFloatingActionButton);

        displayNameFromExtras();

        if(NetworkChecker.isNetworkActivated(this)) {
            displayCityDetails();
        }
        else {
            this.alertMessage.noNetworkConnection(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkChecker.isNetworkActivated(this)) {
            displayCityDetails();
        }
        else {
            this.alertMessage.noNetworkConnection(this);
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

        switch (item.getItemId()) {
            case R.id.editCityDetailsMenu:
                displayCityEditForm(null);
                return true;
            case R.id.deleteCityDetailsMenu:
                deleteCity();
                return true;
            case R.id.edit_preferences_menu:
                displayPreferenceEditor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayPreferenceEditor() {
        Intent toPreferenceActivity = new Intent(CityDetailsActivity.this, PreferenceActivity.class);
        startActivity(toPreferenceActivity);
    }

    public String getFilters() {

        SharedPreferences settings = getSharedPreferences(PreferenceActivity.APP_PREFERENCES, 0);
        String filters = settings.getString(PreferenceActivity.FILTERS, null);

        return filters;
    }

    public void deleteCity() {

        if (NetworkChecker.isNetworkActivated(this)) {

            String ipServer = "http://10.0.2.1";
            String url = ipServer + "/villes/" + this.city.getInseeCode();

            final CityDetailsActivity self = this;
            JsonArrayRequest deleteRequest = new JsonArrayRequest
                    (Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray cityListJSONArrayResponse) {
                            Alert.citySucessfullyDeleted(self.city.getName(), self);
                            finish();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Alert.cityUnSucessfullyDeleted(self.city.getName(), self);
                        }
                    });

            RequestQueue.getInstance(this).addToRequestQueue(deleteRequest);
        }

        else {
            this.alertMessage.noNetworkConnection(this);
        }


    }

    public void displayCityEditForm(View view) {

        if (NetworkChecker.isNetworkActivated(this)) {

            Intent toCityEditActivity = new Intent(CityDetailsActivity.this, CityEditActivity.class);

            toCityEditActivity.putExtra("cityName", this.city.getName());
            toCityEditActivity.putExtra("inseeCode", Integer.toString(this.city.getInseeCode()));

            toCityEditActivity.putExtra("title", getString(R.string.update_city_title_edit_form));
            toCityEditActivity.putExtra("actionOnSave", "update");

            startActivity(toCityEditActivity);
        }

        else {
            this.alertMessage.noNetworkConnection(this);
        }


    }

    public void displayNameFromExtras() {
        this.cityNameTitleTextView.setText(getIntent().getStringExtra("cityName"));
    }

    public void displayCityDetails() {

        if (NetworkChecker.isNetworkActivated(this)) {

            String inseeCode = getIntent().getStringExtra("inseeCode");
            String ipServer = "http://10.0.2.1";
            String url = ipServer + "/villes/" + inseeCode + "/" + getFilters();

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
                            Alert.displayUnexpectedServerResponse(getApplicationContext());
                        }
                    });

            RequestQueue.getInstance(this).addToRequestQueue(requestToFetchCityJsonArray);
        }

        else {
            this.alertMessage.noNetworkConnection(this);
        }

    }

    public void loadCityDetailsFromJSONArray(JSONArray cityListJSONArray) {
        try {
            this.city = City.createFromJSONArray(cityListJSONArray);
        }

        catch (JSONException exception) {
            exception.printStackTrace();
            Alert.displayJSONReadError(getApplicationContext());
        }
    }

    public void loadCityDetails() {

        if(null != this.city) {
            ArrayList<String> details = city.getDetailsAsArray();
            ArrayList<String> labels = new ArrayList<>();
            labels.add(getString(R.string.inseeCode) + " : ");
            labels.add(getString(R.string.postalCode)  + " : ");
            labels.add(getString(R.string.regionCode)  + " : ");
            labels.add(getString(R.string.latitude)  + " : ");
            labels.add(getString(R.string.longitude)  + " : ");
            labels.add(getString(R.string.remoteness)  + " : ");
            labels.add(getString(R.string.inhabitantNumber)  + " : ");

            StringBuilder detailsText = new StringBuilder();

            for (int i = 0 ; i < details.size() ; i++) {

                if ( ! details.get(i).isEmpty()) {
                    detailsText.append(labels.get(i)).append(details.get(i)).append("\n");
                }
            }

            this.cityDetails.setText(detailsText.toString());
        }
    }

    public void displayCityEditFabIfLoadingDatasSuccessful() {
        if(null == this.city) {
            this.cityEditfloatingActionButton.setVisibility(View.INVISIBLE);
        }
        else {
            this.cityEditfloatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

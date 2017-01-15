package com.example.lp.webservice.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lp.webservice.Domain.City;
import com.example.lp.webservice.Util.NetworkChecker;
import com.example.lp.webservice.R;
import com.example.lp.webservice.Util.RequestQueue;
import com.example.lp.webservice.Util.RestApiResponse;
import com.example.lp.webservice.Util.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CityEditActivity extends AppCompatActivity {

    private final String UPDATE_ACTION = "update";
    private final String CREATE_ACTION = "create";

    private final Alert alertMessage = new Alert();

    private String actionOnSave;
    private City city;

    private TextView titleEditFormTextView;
    private String cityName;
    private String inseeCode;
    private FloatingActionButton cityEditSavefloatingActionButton;

    private EditText nameEditText;
    private EditText inseeCodeEditText;
    private EditText postalCodeEditText;
    private EditText regionCodeEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText remotenessEditText;
    private EditText inhabitantNumberEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_edit);
        cityEditSavefloatingActionButton = (FloatingActionButton) findViewById(R.id.cityEditSaveFloatingActionButton);
        loadEditTexts();
        getActionFromExtras();
        getCityNameFromExtras();
        showRequiredFieldLabel();

        if (this.actionOnSave.equals(UPDATE_ACTION)) {
            preFillFormWithCityDetails();
        }

        titleEditFormTextView = (TextView) findViewById(R.id.tvCityName);
        displayTitleFromExtras();
        this.cityEditSavefloatingActionButton.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_city_edit_menu:
                save(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadEditTexts() {
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.inseeCodeEditText = (EditText) findViewById(R.id.inseeCodeEditText);
        this.postalCodeEditText = (EditText) findViewById(R.id.postalCodeEditText);
        this.regionCodeEditText = (EditText) findViewById(R.id.regionCodeEditText);
        this.latitudeEditText = (EditText) findViewById(R.id.latitudeEditText);
        this.longitudeEditText = (EditText) findViewById(R.id.longitudeEditText);
        this.remotenessEditText = (EditText) findViewById(R.id.remotenessEditText);
        this.inhabitantNumberEditText = (EditText) findViewById(R.id.inhabitantNumberEditText);
    }

    private void showRequiredFieldLabel() {
        TextView nameEditLabelTextView = (TextView) findViewById(R.id.nameEditLabelTextView);

        if (nameEditLabelTextView != null) {
            nameEditLabelTextView.append(" *");
        }

        TextView inseeCodeEditLabelTextView = (TextView) findViewById(R.id.inseeCodeEditLabelTextView);
        if (inseeCodeEditLabelTextView != null) {
            inseeCodeEditLabelTextView.append(" *");
        }
    }

    public void getActionFromExtras() {

        this.actionOnSave = getIntent().getStringExtra("actionOnSave");

        if(this.actionOnSave == null) {
            finish();
            System.err.println("The action on save must be 'create' or 'update' and set as extra on the intent start activity");
            Alert.error(this);
        }
    }

    public void getCityNameFromExtras() {

        this.cityName = getIntent().getStringExtra("cityName");
    }

    public void getInseeCodeFromExtras() {

        this.inseeCode = getIntent().getStringExtra("inseeCode");

        if(this.inseeCode == null) {
            finish();
            System.err.println("Insee code not found");
            Alert.error(this);
        }
    }

    public boolean isNameAndInseeCodeFilled() {
        if(nameEditText.getText().toString().isEmpty()) {
            return false;
        }

        if(inseeCodeEditText.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }

    public void save(View button) {

        if(isNameAndInseeCodeFilled()) {
            if(this.actionOnSave.equals(CREATE_ACTION)) {
                createCity();
            }

            else if (this.actionOnSave.equals(UPDATE_ACTION)) {
                updateCity();
            }

            else {
                finish();
                System.err.println("The action on save must be 'create' or 'update' and set as extra on the intent start activity");
                Alert.error(this);
            }
        }

        else {
            this.alertMessage.requiredFieldsNotFilled(this);
        }

    }

    public String fetchCityNameFromExtras() {
        return getIntent().getStringExtra("cityName").trim();
    }

    public void displayTitleFromExtras() {
        titleEditFormTextView.setText(getIntent().getStringExtra("title"));
    }

    public Map<String, String> fetchInfos() {

        Map<String, String> params = new HashMap<String, String>();

        String name = this.nameEditText.getText().toString();
        String inseeCode = this.inseeCodeEditText.getText().toString();
        String postalCode = this.postalCodeEditText.getText().toString();
        String regionCode = this.regionCodeEditText.getText().toString();
        String latitude = this.latitudeEditText.getText().toString();
        String longitude = this.longitudeEditText.getText().toString();
        String remoteness = this.remotenessEditText.getText().toString();
        String inhabitantNumber = this.inhabitantNumberEditText.getText().toString();

        params.put(City.NAME_DB_COL, name);
        params.put(City.INSEE_CODE_DB_COL, inseeCode);

        if (! postalCode.isEmpty()) {
            params.put(City.POSTAL_CODE_DB_COL, postalCode) ;
        }

        if (! regionCode.isEmpty()) {
            params.put(City.REGION_CODE_DB_COL, regionCode);
        }

        if (! latitude.isEmpty()) {
            params.put(City.LATITUDE_DB_COL, latitude);
        }

        if (! longitude.isEmpty()) {
            params.put(City.LONGITUDE_DB_COL, longitude);
        }

        if (! remoteness.isEmpty()) {
            params.put(City.REMOTENESS_DB_COL, remoteness);
        }

        if (! inhabitantNumber.isEmpty()) {
            params.put(City.INHABITANT_NUMBER_DB_COL, inhabitantNumber);
        }

        return params;

    }

    public void createCity() {

        if (NetworkChecker.isNetworkActivated(this)) {

            String url = "http://10.0.2.1/villes/";
            Map<String, String> params = fetchInfos();

            try {
                JSONArray jsonRequestBody = new JSONArray("[ " + new JSONObject(params).toString() + " ]");
                System.out.println(jsonRequestBody.toString());

                final CityEditActivity self = this;

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, jsonRequestBody,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    RestApiResponse result = RestApiResponse.createFromJsonArray(response);
                                    System.out.println(result);

                                    if(result != null && result.isSuccessful()) {
                                        Alert.cityResultSave(self.cityName, getString(R.string.city_successfully_created), self);
                                        finish();
                                    }

                                    else {
                                        Alert.cityResultSave(self.cityName, getString(R.string.city_unsuccessfully_created), self);
                                    }

                                }

                                catch (JSONException e) {
                                    e.printStackTrace();
                                    Alert.displayJSONReadError(self);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Alert.cityResultSave(self.cityName, getString(R.string.city_unsuccessfully_created), self);
                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                RequestQueue.getInstance(this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            this.alertMessage.noNetworkConnection(this);
        }

    }

    public void preFillFormWithPreviousInfosFromExtras() {

        if(this.actionOnSave != null && this.actionOnSave.equals(UPDATE_ACTION)) {
            this.nameEditText.setText(getIntent().getStringExtra("cityName"));
            this.inseeCodeEditText.setText(getIntent().getStringExtra("inseeCode"));
            this.postalCodeEditText.setText(getIntent().getStringExtra("postalCode"));
            this.regionCodeEditText.setText(getIntent().getStringExtra("regionCode"));
            this.latitudeEditText.setText(getIntent().getStringExtra("latitude"));
            this.longitudeEditText.setText(getIntent().getStringExtra("longitude"));
            this.remotenessEditText.setText(getIntent().getStringExtra("remoteness"));
            this.inhabitantNumberEditText.setText(getIntent().getStringExtra("inhabitantNumber"));
        }

    }

    public void preFillFormWithCityDetails() {

        final CityEditActivity self = this;
        if (NetworkChecker.isNetworkActivated(this)) {

            getInseeCodeFromExtras();
            String ipServer = "http://10.0.2.1";
            String url = ipServer + "/villes/" + this.inseeCode;

            // The request always return a JsonArray
            JsonArrayRequest requestToFetchCityJsonArray = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray cityListJSONArrayResponse) {
                            loadCityDetailsFromJSONArray(cityListJSONArrayResponse);
                            preFillForm(self.city.getDetailsAsHashMap());

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

    public void preFillForm(HashMap<String, String> infos) {

        if(this.actionOnSave != null && this.actionOnSave.equals(UPDATE_ACTION)) {
            this.nameEditText.setText(infos.get(City.NAME_DB_COL));
            this.inseeCodeEditText.setText(infos.get(City.INSEE_CODE_DB_COL));
            this.postalCodeEditText.setText(infos.get(City.POSTAL_CODE_DB_COL));
            this.regionCodeEditText.setText(infos.get(City.REGION_CODE_DB_COL));
            this.latitudeEditText.setText(infos.get(City.LATITUDE_DB_COL));
            this.longitudeEditText.setText(infos.get(City.LONGITUDE_DB_COL));
            this.remotenessEditText.setText(infos.get(City.REMOTENESS_DB_COL));
            this.inhabitantNumberEditText.setText(infos.get(City.INHABITANT_NUMBER_DB_COL));
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

    public void updateCity() {

        if (NetworkChecker.isNetworkActivated(this)) {
            String ipServer = "http://10.0.2.1";
            String url = ipServer + "/villes/" + this.inseeCode;
            System.out.println("update");
            System.out.println(this.inseeCode);

            Map<String, String> params = fetchInfos();

            System.out.println(new JSONObject(params).toString());
            try {
                JSONArray jsonRequestBody = new JSONArray("[ " + new JSONObject(params).toString() + " ]");
                System.out.println(jsonRequestBody.toString());

                final CityEditActivity self = this;

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, jsonRequestBody,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    RestApiResponse result = RestApiResponse.createFromJsonArray(response);
                                    System.out.println(result);

                                    if(result != null && result.isSuccessful()) {
                                        finish();
                                        Alert.cityResultSave(self.cityName, Alert.UPDATE_SUCCESSFUL, self);
                                    }

                                    else {
                                        Alert.cityResultSave(self.cityName, Alert.UPDATE_UNSUCCESSFUL, self);
                                    }

                                }

                                catch (JSONException e) {
                                    e.printStackTrace();
                                    Alert.displayJSONReadError(self);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Alert.cityResultSave(self.cityName, Alert.UPDATE_UNSUCCESSFUL, self);
                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                RequestQueue.getInstance(this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            this.alertMessage.noNetworkConnection(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

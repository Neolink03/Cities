package com.example.lp.webservice;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CityEditActivity extends AppCompatActivity {

    private TextView tvCityName;
    private String cityName;
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

        if(NetworkChecker.isNetworkActivated(this)) {

            tvCityName = (TextView) findViewById(R.id.tvCityName);
            this.cityName = fetchCityNameFromExtras();
            displayCityNameTitle();
            this.cityEditSavefloatingActionButton.setVisibility(View.VISIBLE);
        }
        else {
            ToastMessage.noNetworkConnection(this);
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

    public String fetchCityNameFromExtras() {
        return getIntent().getStringExtra("cityName").trim();
    }

    public void displayCityNameTitle() {
        tvCityName.setText(cityName);
    }

    public void update() {
        String tag_json_obj = "json_obj_req";

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes/" + cityName;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject cityListJSONArrayResponse) {
                        System.out.println("ok");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ToastMessage.displayUnexpectedServerResponse(getApplicationContext());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nbr_habitants", "42");

                return params;
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        RequestQueue.getInstance(this).addToRequestQueue(request);

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

    public void createCity(View createCityButton) {
        String url = "http://10.0.2.1/villes/";

        Map<String, String> params = fetchInfos();

        System.out.println(new JSONObject(params).toString());
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
                                restApiResponse result = restApiResponse.createFromJsonArray(response);
                                System.out.println(result);

                                if(result != null && result.isSuccessful()) {
                                    ToastMessage.citySucessfullyCreated(self.nameEditText.getText().toString(), self);
                                    finish();
                                }

                                else {
                                    ToastMessage.cityUnSucessfullyCreated(self.nameEditText.getText().toString(), self);
                                }

                            }

                            catch (JSONException e) {
                                e.printStackTrace();
                                ToastMessage.displayJSONReadError(self);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    ToastMessage.cityUnSucessfullyCreated(self.nameEditText.getText().toString(), self);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

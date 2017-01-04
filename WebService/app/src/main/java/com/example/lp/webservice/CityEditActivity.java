package com.example.lp.webservice;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CityEditActivity extends AppCompatActivity {

    private TextView tvCityName;
    private String cityName;
    private FloatingActionButton cityEditSavefloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_edit);
        cityEditSavefloatingActionButton = (FloatingActionButton) findViewById(R.id.cityEditSaveFloatingActionButton);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

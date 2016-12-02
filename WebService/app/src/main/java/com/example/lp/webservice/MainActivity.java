package com.example.lp.webservice;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private TextView tvCityList;
    private JSONArray cities;
    //private RequestQueueSingleton queueSingleton;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Au début on affiche la liste
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCityList = (TextView) findViewById(R.id.tvCityList);
        this.cities = null;

        //queueSingleton = new RequestQueueSingleton(this);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes";
        executeFromHttpRequest(url);
    }

    public void executeFromHttpRequest(String url) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        createListFromJsonResponse(response);
                        fillCityList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvCityList.setText("Erreur lors de la récupération des données : " + error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public boolean createListFromJsonResponse(String jsonResponse) {
        if (null != jsonResponse) {

            try {
                this.cities = new JSONArray(jsonResponse);
            }

            catch (JSONException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public boolean fillCityList() {

        StringBuffer strToDisplay = new StringBuffer();
        try {
            for(int i = 0 ; i < this.cities.length() ; i++ ) {
                strToDisplay.append(this.cities.getJSONObject(i).getString("Nom_Ville") + "\n");
            }

            this.tvCityList.setText(strToDisplay.toString());
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return false;
        }

        return false;
    }
}

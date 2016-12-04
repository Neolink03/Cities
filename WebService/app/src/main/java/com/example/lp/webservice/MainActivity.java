package com.example.lp.webservice;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvCityList;
    private JSONArray cities;
    //private RequestQueueSingleton queueSingleton;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Au début on affiche la liste
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCityList = (ListView) findViewById(R.id.lvCityList);
        this.cities = null;

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
                        createListFromJsonResponse(response);
                        fillCityList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //tvSelectedCityInfos.setText("Erreur lors de la récupération des données : " + error);
            }
        });
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * Fetch the cities infos from the JSON response of the rest server
     * @param jsonResponse
     * @return boolean
     */
    public boolean createListFromJsonResponse(String jsonResponse) {
        if (null != jsonResponse) {
            try {
                this.cities = new JSONArray(jsonResponse);
            }

            catch (JSONException ex) {
                ex.printStackTrace();
                //tvSelectedCityInfos.setText("Erreur : " + ex.getMessage());
                return false;
            }
        }

        return false;
    }

    /**
     * Fill the listView with the list of cities
     * @return boolean
     */
    public boolean fillCityList() {

        try {
            // Convertinh datas from cities to ArrayList of String
            final ArrayList<String> list = new ArrayList<String>();
            for(int i = 0 ; i < this.cities.length() ; i++ ) {
                list.add(this.cities.getJSONObject(i).getString("Nom_Ville"));
            }

            // Converting list of cities to inputs of the ListView
            final ArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            lvCityList.setAdapter(adapter);

            // Handling click on an item of the list
            lvCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                    // Get position of the clicked item and display its infos
                    final String item = (String) parent.getItemAtPosition(position);
                    Intent cityDetailIntent = new Intent(MainActivity.this, CityDetailActivity.class);
                    cityDetailIntent.putExtra("cityName", item.toString());
                    startActivity(cityDetailIntent);
                }

            });

            return true;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            //tvSelectedCityInfos.setText("Erreur : " + ex.getMessage());
            return false;
        }
    }

    /**
     * To adapt an ArrayList content to a ListView
     */
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);

            // Puting the item of the ArrayList in the ListView
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}

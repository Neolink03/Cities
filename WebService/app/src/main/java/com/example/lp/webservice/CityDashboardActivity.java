package com.example.lp.webservice;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityDashboardActivity extends AppCompatActivity {

    private ListView lvCityList;
    private ArrayList<String> cityNameList;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Au début on affiche la liste
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCityList = (ListView) findViewById(R.id.lvCityList);
        displayListNameCityList();
    }

    public void displayListNameCityList() {
        String ipServer = "http://10.0.2.1";
        String url = ipServer + "/villes";
        JsonArrayRequest requestToFetchCityJsonArray = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray cityListJsonArray) {
                        fillCityNameListFromCityJsonArray(cityListJsonArray);
                        fillCityListView();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.getInstance(this).addToRequestQueue(requestToFetchCityJsonArray);
    }

    public void fillCityNameListFromCityJsonArray(JSONArray cityJsonArray) {

        try {
            this.cityNameList = CityList.createCityNameListFromJsonArray(cityJsonArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill the listView with the list of cities
     */
    public void fillCityListView() {

            // Converting list of cities to inputs of the ListView
            final ArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, this.cityNameList);
            lvCityList.setAdapter(adapter);

            // Handling click on an item of the list
            lvCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                    // Get position of the clicked item and display its infos
                    final String item = (String) parent.getItemAtPosition(position);
                    Intent cityDetailIntent = new Intent(CityDashboardActivity.this, CityDetailActivity.class);
                    cityDetailIntent.putExtra("cityName", item.toString());
                    startActivity(cityDetailIntent);
                }

            });
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

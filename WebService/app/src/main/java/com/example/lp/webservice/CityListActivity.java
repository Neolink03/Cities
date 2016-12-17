package com.example.lp.webservice;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class CityListActivity extends AppCompatActivity {

    private ListView cityList;
    private EditText citySearchBar;
    private ArrayList<String> cityNameList;

    public EditText getCitySearchBar() {
        return citySearchBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cityList = (ListView) findViewById(R.id.lvCityList);
        citySearchBar = (EditText) findViewById(R.id.tfCitySearch);

        displayListNameCityList("http://10.0.2.1/villes");
    }

    public void displayListNameCityList(String url) {

        if(NetworkChecker.isNetworkActivated(this)) {

            JsonArrayRequest requestToFetchCityJsonArray = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray cityListJsonArray) {
                            fillCityNameListFromCityJsonArray(cityListJsonArray);
                            fillCityListView();
                            setCityViewListener();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            ToastMessage.displayUnexpectedResponseInToast(getApplicationContext());
                        }
                    });

            RequestQueue.getInstance(this).addToRequestQueue(requestToFetchCityJsonArray);
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }


    }

    public void fillCityNameListFromCityJsonArray(JSONArray cityJSONArray) {

        try {
            this.cityNameList = CityList.createCityNameListFromJsonArray(cityJSONArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
            ToastMessage.displayJSONReadErrorInToast(getApplicationContext());
        }
    }

    /**
     * Fill the listView with the list of cities
     */
    public void fillCityListView() {

        if (!this.cityNameList.isEmpty()) {
            // Converting list of cities to inputs of the ListView
            final ArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, this.cityNameList);
            cityList.setAdapter(adapter);
        }
        else {
            ToastMessage.noCityFound(this);
        }
    }

    public void setCityViewListener() {
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                // Get position of the clicked item and display its infos
                final String item = (String) parent.getItemAtPosition(position);
                displayCityDetailOfClickedItem(item);

            }

            private void displayCityDetailOfClickedItem(String item) {
                Intent cityDetailIntent = new Intent(CityListActivity.this, CityDetailActivity.class);
                cityDetailIntent.putExtra("cityName", item);
                startActivity(cityDetailIntent);
            }

        });
    }

    public void searchForCityListOnSearchButtonClick(View view) {

        String searchedCity = citySearchBar.getText().toString();

        if(!searchedCity.isEmpty()) {
            displayListNameCityList("http://10.0.2.1/villes/" + searchedCity);
        }
        else {
            displayListNameCityList("http://10.0.2.1/villes");
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

package com.example.lp.webservice;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityListActivity extends AppCompatActivity {

    private ListView cityList;
    private EditText citySearchBar;
    private CityList cityNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cityList = (ListView) findViewById(R.id.cityListListView);
        citySearchBar = (EditText) findViewById(R.id.citySearchBarTextField);
        createCity(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchCityList(null);
    }

    public void createCity(View createCityButton) {
        String url = "http://10.0.2.1/villes/";

        Map<String, String> params = new HashMap<String, String>();
        params.put(City.NAME_DB_COL, "8002");
        params.put(City.POSTAL_CODE_DB_COL, "01000");
        params.put(City.INSEE_CODE_DB_COL, "8003");
        params.put(City.REGION_CODE_DB_COL, "5A");
        params.put(City.LATITUDE_DB_COL, "5");
        params.put(City.LONGITUDE_DB_COL, "6.5");
        params.put(City.REMOTENESS_DB_COL, "0.9");
        params.put(City.INHABITANT_NUMBER_DB_COL, "42");

        System.out.println(new JSONObject(params).toString());
        try {
            JSONArray jsonArrayBody = new JSONArray("[ " + new JSONObject(params).toString() + " ]");
            System.out.println(jsonArrayBody.toString());

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, jsonArrayBody,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
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

    public void searchCityList(View searchButton) {
        String searchedCity = citySearchBar.getText().toString();

        if(!searchedCity.isEmpty()) {
            displayListNameCityList(searchedCity);
        }
        else {
            displayListNameCityList("");
        }
    }

    public void displayListNameCityList(String city) {

        String url = "http://10.0.2.1/villes/" + city;

        if(NetworkChecker.isNetworkActivated(this)) {

            JsonArrayRequest fetchCityListRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray cityListJsonArray) {
                            loadCityListFromJsonArray(cityListJsonArray);
                            loadListView();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            ToastMessage.displayUnexpectedServerResponse(getApplicationContext());
                        }
                    });

            RequestQueue.getInstance(this).addToRequestQueue(fetchCityListRequest);
        }
        else {
            ToastMessage.noNetworkConnection(this);
        }
    }

    public void loadCityListFromJsonArray(JSONArray cityJSONArray) {

        try {
            this.cityNameList = CityList.createCityListFromJsonArray(cityJSONArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
            ToastMessage.displayJSONReadError(getApplicationContext());
        }
    }

    /**
     * Fill the listView with the list of cities
     */
    public void loadListView() {

        // Converting list of cities to inputs of the ListView
        if (!this.cityNameList.isEmpty()) {
            // Converting list of cities to inputs of the ListView
            final ArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, this.cityNameList.getNameCityList());
            cityList.setAdapter(adapter);
            setCityListViewListener();
        }
        else {
            ToastMessage.noCityFound(this);
        }
    }

    public void setCityListViewListener() {
        final CityListActivity self = this;
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                // Get position of the clicked city and display its details
                final String city = (String) parent.getItemAtPosition(position);
                displayCityDetails(self.cityNameList.getInseeCodeAtPosition(position));
            }

            private void displayCityDetails(int inseeCode) {
                Intent cityDetailIntent = new Intent(CityListActivity.this, CityDetailsActivity.class);
                cityDetailIntent.putExtra("inseeCode", Integer.toString(inseeCode));
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

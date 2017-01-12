package com.example.lp.webservice;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_list_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchCityList(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_city_list_menu:
                displayCityEditForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayCityEditForm() {
        Intent toCityEditActivity = new Intent(CityListActivity.this, CityEditActivity.class);
        toCityEditActivity.putExtra("cityName", "");
        toCityEditActivity.putExtra("actionOnSave", "create");
        startActivity(toCityEditActivity);
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

        String url = "http://10.0.2.1/villes/search/" + city;

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
        final ArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, this.cityNameList.getNameCityList());
        cityList.setAdapter(adapter);
        setCityListViewListener();

        if (this.cityNameList.isEmpty()) {
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

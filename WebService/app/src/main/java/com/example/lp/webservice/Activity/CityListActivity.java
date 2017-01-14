package com.example.lp.webservice.Activity;


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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lp.webservice.Util.CityList;
import com.example.lp.webservice.Util.NetworkChecker;
import com.example.lp.webservice.R;
import com.example.lp.webservice.Util.RequestQueue;
import com.example.lp.webservice.Util.Alert;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class CityListActivity extends AppCompatActivity {

    private ListView cityList;
    private EditText citySearchBar;
    private CityList cityNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayPreferenceEditor();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_city_list_menu:
                displayCityEditForm();
                return true;
            case R.id.edit_preferences_menu:
                this.displayPreferenceEditor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchCityList(null);
    }

    public void displayPreferenceEditor() {
        Intent toPreferenceActivity = new Intent(CityListActivity.this, PreferenceActivity.class);
        startActivity(toPreferenceActivity);
    }

    public void displayCityEditForm() {
        Intent toCityEditActivity = new Intent(CityListActivity.this, CityEditActivity.class);
        toCityEditActivity.putExtra("title", getString(R.string.create_city_title_edit_form));
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

        if(NetworkChecker.isNetworkActivated(this)) {

            String url = "http://10.0.2.1/villes/search/" + city;
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
                            Alert.displayUnexpectedServerResponse(getApplicationContext());
                        }
                    });

            RequestQueue.getInstance(this).addToRequestQueue(fetchCityListRequest);
        }
        else {
            Alert.noNetworkConnection(this);
        }
    }

    public void loadCityListFromJsonArray(JSONArray cityJSONArray) {

        try {
            this.cityNameList = CityList.createCityListFromJsonArray(cityJSONArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Alert.displayJSONReadError(getApplicationContext());
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
        displayCityDetailsOnListItemClick();

        if (this.cityNameList.isEmpty()) {
            Alert.noCityFound(this);
        }
    }

    public void displayCityDetailsOnListItemClick() {
        final CityListActivity self = this;
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                // Get position of the clicked city and display its details
                final String item = (String) parent.getItemAtPosition(position);
                displayCityDetails(self.cityNameList.getInseeCodeAtPosition(position), item);
            }

            private void displayCityDetails(int inseeCode, String cityName) {

                if (NetworkChecker.isNetworkActivated(self)) {
                    Intent cityDetailIntent = new Intent(CityListActivity.this, CityDetailsActivity.class);
                    cityDetailIntent.putExtra("cityName", cityName);
                    cityDetailIntent.putExtra("inseeCode", Integer.toString(inseeCode));
                    startActivity(cityDetailIntent);
                }

                else {
                    Alert.noNetworkConnection(self);
                }

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

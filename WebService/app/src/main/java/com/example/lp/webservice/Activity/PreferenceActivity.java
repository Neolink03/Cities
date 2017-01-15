package com.example.lp.webservice.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.example.lp.webservice.Domain.City;
import com.example.lp.webservice.R;
import com.example.lp.webservice.Util.Alert;

import java.util.HashMap;

public class PreferenceActivity extends AppCompatActivity {

    public final static String APP_PREFERENCES = "app_preferences";
    public final static String FILTERS = "filters";
    public final static String REQUIRED_FILTERS = City.NAME_DB_COL + "-" + City.INSEE_CODE_DB_COL;

    private SharedPreferences settings;

    private HashMap<Integer, String> filterListCheckBoxes;

    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_preference);

        settings = getSharedPreferences(APP_PREFERENCES, 0);
        loadFilterListCheckBoxes();
        displayCurrentSelectedFilters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_preferences_menu:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayCurrentSelectedFilters() {
        String selectedFilters = settings.getString(FILTERS, null);

        HashMap<String, Integer> checkBoxByFilter = new HashMap<>();
        checkBoxByFilter.put(City.POSTAL_CODE_DB_COL, R.id.postal_code_filter_checkbox);
        checkBoxByFilter.put(City.REGION_CODE_DB_COL, R.id.region_code_filter_checkbox);
        checkBoxByFilter.put(City.LATITUDE_DB_COL, R.id.gps_coordinates_filter_group_checkbox);
        checkBoxByFilter.put(City.LONGITUDE_DB_COL, R.id.gps_coordinates_filter_group_checkbox);
        checkBoxByFilter.put(City.REMOTENESS_DB_COL, R.id.gps_coordinates_filter_group_checkbox);
        checkBoxByFilter.put(City.INHABITANT_NUMBER_DB_COL, R.id.inhabitant_number_filter_checkbox);

        if (null != selectedFilters) {
            String[] selectedFiltersList = selectedFilters.split("-");

            for (String filter :
                    selectedFiltersList) {

                if ((! filter.equals(City.NAME_DB_COL))
                        && (! filter.equals(City.INSEE_CODE_DB_COL))) {

                    CheckBox checboxFilter = (CheckBox) findViewById(checkBoxByFilter.get(filter));

                    if (null != checboxFilter) {
                        checboxFilter.setChecked(true);
                    }
                }
            }
        }

    }

    public void loadFilterListCheckBoxes() {

        this.filterListCheckBoxes = new HashMap<>();
        this.filterListCheckBoxes.put(R.id.postal_code_filter_checkbox, City.POSTAL_CODE_DB_COL);
        this.filterListCheckBoxes.put(R.id.region_code_filter_checkbox, City.REGION_CODE_DB_COL);
        this.filterListCheckBoxes.put(R.id.gps_coordinates_filter_group_checkbox, City.LATITUDE_DB_COL + "-" + City.LONGITUDE_DB_COL + "-" + City.REMOTENESS_DB_COL);
        this.filterListCheckBoxes.put(R.id.inhabitant_number_filter_checkbox, City.POSTAL_CODE_DB_COL);
    }

    public String getSelectedFilters() {

        StringBuilder filters = new StringBuilder(REQUIRED_FILTERS);

        for (int checboxId:
                this.filterListCheckBoxes.keySet()) {
            CheckBox checkbox = (CheckBox) findViewById(checboxId);

            if((null != checkbox) && checkbox.isChecked()) {
                filters.append("-").append(this.filterListCheckBoxes.get(checboxId));
            }
        }
        
        return filters.toString();
    }

    public void save() {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(FILTERS, getSelectedFilters());
        editor.apply();
        Alert.preferencesSaved(this);
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }


}

package com.example.lp.webservice.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.lp.webservice.Domain.City;
import com.example.lp.webservice.R;

public class PreferenceActivity extends AppCompatActivity {

    public final static String APP_PREFERENCES = "app_preferences";
    public final static String FILTERS = "filters";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);

        setContentView(R.layout.activity_preference);
        settings = getSharedPreferences(APP_PREFERENCES, 0);

        save();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void save() {
        SharedPreferences.Editor editor = this.settings.edit();

        String requiredFields = "Nom_Ville-Code_INSEE-Code_Region";
        String filters = "Code_Postal" + "-" + City.LATITUDE_DB_COL + "-" + City.INHABITANT_NUMBER_DB_COL;

        editor.putString(FILTERS, requiredFields + "-" + filters);

        editor.apply();
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }


}

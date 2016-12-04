package com.example.lp.webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lp on 02/12/2016.
 */

public class CityList extends HashMap<String, City> {

    public CityList(String jsonResponse) {

        if (null != jsonResponse) {
            try {
                JSONArray cities = new JSONArray(jsonResponse);


            }

            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}

package com.example.lp.webservice;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by lp on 02/12/2016.
 */

public class CityList {

    public static ArrayList createCityNameListFromJsonArray(JSONArray cityListJsonArray) throws JSONException{

        ArrayList<String> cityNameList = new ArrayList<String>();

        for(int i = 0 ; i < cityListJsonArray.length() ; i++ ) {
            cityNameList.add(cityListJsonArray.getJSONObject(i).getString("Nom_Ville"));
        }

        return new ArrayList<String>(cityNameList);
    }
}

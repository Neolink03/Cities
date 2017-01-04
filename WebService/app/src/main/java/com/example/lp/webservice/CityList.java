package com.example.lp.webservice;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by Neolink03 on 02/12/2016.
 */

public class CityList {

    ArrayList<String> nameCityList;
    ArrayList<Integer> inseeCodeList;

    public static CityList createCityListFromJsonArray(JSONArray cityListJsonArray) throws JSONException{

        CityList cityList = new CityList();
        cityList.nameCityList = new ArrayList<>();
        cityList.inseeCodeList = new ArrayList<>();

        for(int i = 0 ; i < cityListJsonArray.length() ; i++ ) {
            cityList.nameCityList.add(cityListJsonArray.getJSONObject(i).getString("Nom_Ville"));
            cityList.inseeCodeList.add(cityListJsonArray.getJSONObject(i).getInt("Code_INSEE"));
        }

        return cityList;
    }

    public int getInseeCodeAtPosition(int position) {
        return inseeCodeList.get(position);
    }

    public ArrayList<String> getNameCityList() {
        return nameCityList;
    }

    public boolean isEmpty() {
        return (inseeCodeList.isEmpty());
    }
}

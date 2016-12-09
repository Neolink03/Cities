package com.example.lp.webservice;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jonathan on 04/12/16.
 */

public class City {

    private String name;
    private int postalCode;
    private int InseeCode;
    private String RegionCode;
    private float[] coordinates;

    public City(String name, int postalCode, int inseeCode, String regionCode, float[] coordinates) {
        this.name = name;
        this.postalCode = postalCode;
        InseeCode = inseeCode;
        RegionCode = regionCode;
        this.coordinates = coordinates;
    }

    /*
    public static City createFromJSONObject(JSONObject JsonCity) {



        return new City();
    }
    */
}

package com.example.lp.webservice;

import org.json.JSONArray;
import org.json.JSONException;
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
    private double latitude;
    private double longitude;
    private double remoteness;

    public City(String name, int postalCode, int inseeCode, String regionCode, double latitude, double longitude, double remoteness) {
        this.name = name;
        this.postalCode = postalCode;
        InseeCode = inseeCode;
        RegionCode = regionCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remoteness = remoteness;
    }

    public static City createFromJSONArray(JSONArray JSONArrayFromResponse) throws JSONException {

        JSONObject cityJSONObjet = JSONArrayFromResponse.getJSONObject(0);

        return new City(
                cityJSONObjet.getString("Nom_Ville"),
                cityJSONObjet.getInt("Code_Postal"),
                cityJSONObjet.getInt("Code_INSEE"),
                cityJSONObjet.getString("Code_Region"),
                cityJSONObjet.getDouble("Latitude"),
                cityJSONObjet.getDouble("Longitude"),
                cityJSONObjet.getDouble("Eloignement")
        );
    }

    public String getName() {
        return name;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public int getInseeCode() {
        return InseeCode;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRemoteness() {
        return remoteness;
    }
}

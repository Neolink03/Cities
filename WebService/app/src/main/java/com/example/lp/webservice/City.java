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
    private int inseeCode;
    private String regionCode;
    private double latitude;
    private double longitude;
    private double remoteness;
    private int inhabitantNumber;

    public City(String name, int postalCode, int inseeCode, String regionCode, double latitude, double longitude, double remoteness, int inhabitantNumber) {
        this.name = name;
        this.postalCode = postalCode;
        this.inseeCode = inseeCode;
        this.regionCode = regionCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remoteness = remoteness;
        this.inhabitantNumber = inhabitantNumber;
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
                cityJSONObjet.getDouble("Eloignement"),
                cityJSONObjet.getInt("nbre_habitants")
        );
    }

    public ArrayList<String> getCharacteristicsAsArray() {
        ArrayList<String> cityCharacteristics = new ArrayList<String>();
        cityCharacteristics.add( Integer.toString(this.postalCode) + "\n" );
        cityCharacteristics.add( Integer.toString(this.inseeCode)+ "\n");
        cityCharacteristics.add( this.regionCode + "\n");
        cityCharacteristics.add( Double.toString(this.latitude) + "\n");
        cityCharacteristics.add( Double.toString(this.longitude) + "\n");
        cityCharacteristics.add( Double.toString(this.remoteness) + "\n");
        cityCharacteristics.add( Integer.toString(this.inhabitantNumber)+ "\n");
        return cityCharacteristics;
    }

    public String getName() {
        return name;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public int getInseeCode() {
        return inseeCode;
    }

    public String getRegionCode() {
        return regionCode;
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

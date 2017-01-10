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

    private static final int INVALID_VALUE = -999999;

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
            ( ! cityJSONObjet.isNull("Nom_Ville") ) ? cityJSONObjet.getString("Nom_Ville") : "",
            ( ! cityJSONObjet.isNull("Code_Postal") ) ? cityJSONObjet.getInt("Code_Postal") : INVALID_VALUE,
            ( ! cityJSONObjet.isNull("Code_INSEE") ) ? cityJSONObjet.getInt("Code_INSEE") : INVALID_VALUE ,
            ( ! cityJSONObjet.isNull("Code_Region") ) ? cityJSONObjet.getString("Code_Region") : "",
            ( ! cityJSONObjet.isNull("Latitude") ) ? cityJSONObjet.getDouble("Latitude") : INVALID_VALUE,
            ( ! cityJSONObjet.isNull("Longitude") ) ? cityJSONObjet.getDouble("Longitude") : INVALID_VALUE,
            ( ! cityJSONObjet.isNull("Eloignement") ) ? cityJSONObjet.getDouble("Eloignement") : INVALID_VALUE,
            ( ! cityJSONObjet.isNull("nbre_habitants") ) ? cityJSONObjet.getInt("nbre_habitants") : INVALID_VALUE
        );
    }

    public ArrayList<String> getCharacteristicsAsArray() {
        ArrayList<String> cityCharacteristics = new ArrayList<String>();
        cityCharacteristics.add( ( (this.postalCode != INVALID_VALUE) ? Integer.toString(this.postalCode) : "") + "\n" );
        cityCharacteristics.add( this.regionCode + "\n");
        cityCharacteristics.add( ( (this.inseeCode != INVALID_VALUE) ? Integer.toString(this.inseeCode) : "") + "\n");
        cityCharacteristics.add( ( (this.latitude != INVALID_VALUE) ? Double.toString(this.latitude) : "") + "\n" );
        cityCharacteristics.add( ( (this.longitude != INVALID_VALUE) ? Double.toString(this.longitude) : "") + "\n" );
        cityCharacteristics.add( ( (this.remoteness != INVALID_VALUE) ? Double.toString(this.remoteness) : "") + "\n" );
        cityCharacteristics.add( ( (this.inhabitantNumber != INVALID_VALUE) ? Integer.toString(this.inhabitantNumber) : "") + "\n");
        return cityCharacteristics;
    }

    public String getName() {
        return name;
    }

    public int getInseeCode() {
        return inseeCode;
    }
}

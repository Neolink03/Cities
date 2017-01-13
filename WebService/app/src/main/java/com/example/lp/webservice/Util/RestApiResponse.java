package com.example.lp.webservice.Util;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by lp on 12/01/2017.
 */

public class RestApiResponse {

    private int resultCode;
    private String resultMessage;
    private final int SUCCESS = 0;

    private RestApiResponse(int resultCode, String resultMessage) {
        this.resultMessage = resultMessage;
        this.resultCode = resultCode;
    }

    public static RestApiResponse createFromJsonArray(JSONArray result) {

        try {

            int resultCode = 0;

            // resultCode is 2nd field of JsonArray result
            if(! result.isNull(1)) {
                resultCode = result.getInt(1);
            }

            return new RestApiResponse(
                resultCode,
                result.getString(2)
                );
        }
        catch (JSONException exception) {
            return null;
        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public RestApiResponse(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public String toString() {
        return this.resultCode + " : " + this.resultMessage;
    }

    public boolean isSuccessful() {
        return (this.resultCode == SUCCESS);
    }
}

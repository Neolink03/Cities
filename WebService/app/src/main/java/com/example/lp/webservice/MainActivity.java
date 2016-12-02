package com.example.lp.webservice;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    final String location = "39.6034810,-119.6822510";
    final String timestamp = "1480676276";
    final String key = "AIzaSyAeHQCeCI1DuiHVvvu3kS4rH-Jl8M1CJLM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        final TextView mTextView = (TextView) findViewById(R.id.tv1);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="https://maps.googleapis.com/maps/api/timezone/json?"
            +"location=" + location
            +"&timestamp=" + timestamp
            +"&key=" + key;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Il est : "+ calculateTime(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String calculateTime(String response) {
        if(null != response) {
            try {
                JSONObject responseJSONObject = new JSONObject(response);
                int dstOffset = responseJSONObject.getInt("dstOffset");
                int rawOffset = responseJSONObject.getInt("rawOffset");
                int res = Integer.parseInt(this.timestamp) + dstOffset + rawOffset;


                return this.formatDate((long) (res * 1000));

            }
            catch (final JSONException jsonE) {

            }
        }

        return null;
    }

    private String formatDate(long milliseconds) /* This is your topStory.getTime()*1000 */ {
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }
}

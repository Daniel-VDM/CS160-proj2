package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Detail extends AppCompatActivity {

    private static final String TAG = "Detail";

    LatLng currLoc;
    JSONObject jsonObjectSearch;
    JSONObject jsonObjectDetail;
    String carSelected;
    String id;
    String placeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        processIntent();
        getDetail();
    }

    private String read(String httpUrl) {
        String httpData = "";
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buf = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            httpData = buf.toString();
            reader.close();
        } catch (Exception e) {
            Log.e("HttpRequestHandler", Objects.requireNonNull(e.getMessage()));
        } finally {
            try {
                assert stream != null;
                stream.close();
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e("HttpRequestHandler", Objects.requireNonNull(e.getMessage()));
            }
        }
        return httpData;
    }

    private void getDetail() {
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/" +
                    "maps/api/place/details/json?")
                    .append("place_id=").append(placeId)
                    .append("&key=").append(MapsActivity.key);
            String response = read(googlePlacesUrl.toString());
            jsonObjectDetail = new JSONObject(response);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void processIntent() {
        try {
            Intent intent = getIntent();
            carSelected = intent.getStringExtra("CAR");
            String jsonObjectString = intent.getStringExtra("CURR_STATION");
            double lng = intent.getDoubleExtra("CURR_LONG", 0);
            double lat = intent.getDoubleExtra("CURR_LAT", 0);
            jsonObjectSearch = new JSONObject(jsonObjectString);
            currLoc = new LatLng(lat, lng);
            id = jsonObjectSearch.getString("id");
            placeId = jsonObjectSearch.getString("place_id");
        } catch (JSONException | NullPointerException e) {
            Log.e(TAG, e.toString());
        }
    }
}


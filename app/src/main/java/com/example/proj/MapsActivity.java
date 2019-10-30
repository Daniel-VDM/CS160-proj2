package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    String key = "AIzaSyCtNO_y0trt6tjgP8ApsdGIm1IK4rjZFeQ";

    JSONArray currStations;

    LatLng currLoc;
    LatLng lookLoc;
    String carSelected;
    String zipcode;
    Button searchButton;
    EditText zipcodeInput;
    Geocoder geocoder;
    MapView map;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // HACKMODE...
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        geocoder = new Geocoder(this);
        map = findViewById(R.id.map);
        searchButton = findViewById(R.id.maps_searchButton);
        zipcodeInput = findViewById(R.id.maps_zipcode);
        map.onCreate(savedInstanceState);

        processIntent();
        update();
    }

    private void processIntent() {
        Intent intent = getIntent();
        carSelected = intent.getStringExtra("CAR");
        zipcode = intent.getStringExtra("TARGET_ZIP");
        double lng = intent.getDoubleExtra("CURR_LONG", 0);
        double lat = intent.getDoubleExtra("CURR_LAT", 0);
        currLoc = new LatLng(lat, lng);
    }

    private void update(){
        if (zipcode.equals("")){
            lookLoc = currLoc;
        } else {
            try {
                List<Address> addrl = geocoder.getFromLocationName(zipcode, 1);
                Address addr = addrl.get(0);
                lookLoc = new LatLng(addr.getLatitude(), addr.getLongitude());
            } catch (IOException | IndexOutOfBoundsException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Unknown Area: " + zipcode,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
                lookLoc = currLoc;
            }
        }
        updateLocations();
        map.getMapAsync(this);
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

    private void updateLocations(){
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/" +
                "maps/api/place/nearbysearch/json?")
                .append("location=").append(lookLoc.latitude).append(",").append(lookLoc.longitude)
                .append("&radius=80467.2") // 50 miles in meters.
                .append("&keyword=charge")
                .append("&key=").append(key);
        String response = read(googlePlacesUrl.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            Random r = new Random();
            currStations = new JSONArray();
            while (jsonObject.has("next_page_token")){
                String nextPageToken = (String) jsonObject.get("next_page_token");
                JSONArray results = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < results.length(); i++){
                    // Add data for each station here
                    JSONObject station = (JSONObject)results.get(i);
                    double pricePerKwh = 0.08 + (0.16-0.08) * r.nextDouble();
                    station.put("price-per-kWh", pricePerKwh);
                    if(station.has("name")
                            && station.get("name").toString().contains("Tesla")){
                        station.put("range-per-hr-model3", 200);
                        station.put("range-per-hr-modelS", 160);
                        station.put("range-per-hr-i3", 216);
                    } else {
                        station.put("range-per-hr-model3", 45);
                        station.put("range-per-hr-modelS", 55);
                        station.put("range-per-hr-i3", 48);
                    }
                    currStations.put(station);
                }
                googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/" +
                        "maps/api/place/nearbysearch/json?")
                        .append("pagetoken=").append(nextPageToken)
                        .append("&key=").append(key);
                jsonObject = new JSONObject(read(googlePlacesUrl.toString()));
            }
            JSONArray results = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < results.length(); i++){
                // Add data for each station here
                JSONObject station = (JSONObject)results.get(i);
                double pricePerKwh = 0.08 + (0.16-0.08) * r.nextDouble();
                station.put("price-per-kWh", pricePerKwh);
                if(station.has("name")
                        && station.get("name").toString().contains("Tesla")){
                    station.put("range-per-hr-model3", 200);
                    station.put("range-per-hr-modelS", 160);
                    station.put("range-per-hr-i3", 216);
                } else {
                    station.put("range-per-hr-model3", 45);
                    station.put("range-per-hr-modelS", 55);
                    station.put("range-per-hr-i3", 48);
                }
                currStations.put(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    protected void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMinZoomPreference(12);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("You are here!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lookLoc));

        for(int i = 0; i < currStations.length(); i++){
            try {
                JSONObject station = (JSONObject) currStations.get(i);
                JSONObject geo = (JSONObject) station.get("geometry");
                JSONObject location = (JSONObject) geo.get("location");
                double lat = (double) location.get("lat");
                double lng = (double) location.get("lng");
                String name = "Charging Station";
                if (station.has("name")){
                    name = station.getString("name");
                }
                URL url = new URL("https://map.openchargemap.io/assets/images/icons/branding/AppIcon_128x128.png");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .icon(BitmapDescriptorFactory.fromBitmap(myBitmap))
                        .title(name));
            } catch (JSONException | IOException ignored) {
            }
        }
    }

}

package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng currLoc;
    LatLng lookLoc;
    String carSelected;
    String zipcode;
    Button searchButton;
    EditText zipcodeInput;
    MapView map;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);
        searchButton = findViewById(R.id.maps_searchButton);
        zipcodeInput = findViewById(R.id.maps_zipcode);

        processIntent();

        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
    }

    private void processIntent() {
        Intent intent = getIntent();
        carSelected = intent.getStringExtra("CAR");
        zipcode = intent.getStringExtra("TARGET_ZIP");
        double lng = intent.getDoubleExtra("CURR_LONG", 0);
        double lat = intent.getDoubleExtra("CURR_LAT", 0);
        currLoc = new LatLng(lat, lng);

        if (zipcode.equals("")){
            lookLoc = currLoc;
        } else {
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addrl = geocoder.getFromLocationName(zipcode, 1);
                Address addr = addrl.get(0);
                lookLoc = new LatLng(addr.getLatitude(), addr.getLongitude());
            } catch (IOException | IndexOutOfBoundsException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Unknown zip code: " + zipcode,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
                lookLoc = currLoc;
            }
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
        googleMap.addMarker(new MarkerOptions().position(currLoc).title("You are here!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lookLoc));
    }

}

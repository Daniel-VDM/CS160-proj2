package com.example.proj;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng currLoc;
    String carSelected;
    String zipcode;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        carSelected = intent.getStringExtra("CAR");
        zipcode = intent.getStringExtra("TARGET_ZIP");
        double lng = intent.getDoubleExtra("CURR_LONG", 0);
        double lat = intent.getDoubleExtra("CURR_LAT", 0);

        // TODO: handle logic for looking things up based on zipcode (get zip here!!!)
        currLoc = new LatLng(lat, lng);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        googleMap.setMinZoomPreference(13);
        mMap.addMarker(new MarkerOptions().position(currLoc).title("You are here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
    }
}

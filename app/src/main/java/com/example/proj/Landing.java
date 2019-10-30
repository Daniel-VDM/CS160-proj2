package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Landing extends AppCompatActivity implements OnMapReadyCallback {

    Button searchBuitton;
    MapView landingMap;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        searchBuitton = findViewById(R.id.searchButton);
        landingMap = findViewById(R.id.landing_mapView);

        searchBuitton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapActivity = new Intent(Landing.this, MapsActivity.class);
                startActivity(mapActivity);
            }
        });

        landingMap.onCreate(savedInstanceState);
        landingMap.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        landingMap.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        landingMap.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        landingMap.onStop();
    }
    @Override
    protected void onPause() {
        landingMap.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        landingMap.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        landingMap.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.addMarker(new MarkerOptions().position(ny).title("You are here!"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}

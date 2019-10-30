package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Landing extends AppCompatActivity implements OnMapReadyCallback {

    Button searchBuitton;
    MapView landingMap;
    EditText zipcodeInput;
    RadioButton model3;
    RadioButton modelS;
    RadioButton i3;
    String carSelected; // 'model3' OR 'modelS' OR 'i3'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        searchBuitton = findViewById(R.id.searchButton);
        landingMap = findViewById(R.id.landing_mapView);
        zipcodeInput = findViewById(R.id.landing_zipcodeInput);
        model3 = findViewById(R.id.landing_model3Sel);
        modelS = findViewById(R.id.landing_modelSSel);
        i3 = findViewById(R.id.landing_i3Sel);

        setListeners();

        landingMap.onCreate(savedInstanceState);
        landingMap.getMapAsync(this);
    }

    private void setListeners(){
        searchBuitton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get zipcode (if any THEN pass it through the intent)
                Intent mapActivity = new Intent(Landing.this, MapsActivity.class);
                startActivity(mapActivity);
            }
        });
        model3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    model3.setChecked(true);
                    modelS.setChecked(false);
                    i3.setChecked(false);
                    carSelected = "model3";
                }
            }
        });
        modelS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    modelS.setChecked(true);
                    model3.setChecked(false);
                    i3.setChecked(false);
                    carSelected = "modelS";
                }
            }
        });
        i3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    i3.setChecked(true);
                    model3.setChecked(false);
                    modelS.setChecked(false);
                    carSelected = "i3";
                }
            }
        });
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
        googleMap.setMinZoomPreference(15);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap.addMarker(new MarkerOptions().position(ny).title("You are here!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}

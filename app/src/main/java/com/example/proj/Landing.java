package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class Landing extends AppCompatActivity implements OnMapReadyCallback {

    Button searchBuotton;
    MapView landingMap;
    EditText zipcodeInput;
    RadioButton model3;
    RadioButton modelS;
    RadioButton i3;
    String carSelected; // 'model3' OR 'modelS' OR 'i3'
    LatLng currLoc;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        carSelected = null;

        searchBuotton = findViewById(R.id.searchButton);
        landingMap = findViewById(R.id.landing_mapView);
        zipcodeInput = findViewById(R.id.landing_zipcodeInput);
        model3 = findViewById(R.id.landing_model3Sel);
        modelS = findViewById(R.id.landing_modelSSel);
        i3 = findViewById(R.id.landing_i3Sel);

        setListeners();

        currLoc = new LatLng(0.7143528, -74.0059731);

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                requestLocationUpdate();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
            }
        });

        landingMap.onCreate(savedInstanceState);
        landingMap.getMapAsync(this);
    }

    private void requestLocationUpdate(){
        FusedLocationProviderClient fusedLocationClient = new FusedLocationProviderClient(this);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.setMinZoomPreference(13);
                            googleMap.addMarker(new MarkerOptions().position(currLoc).title("You are here!"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
                        }
                    }
                });
    }

    private void setListeners() {
        searchBuotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carSelected == null){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Please select a car",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 25);
                    toast.show();
                } else {
                    Intent mapActivity = new Intent(Landing.this, MapsActivity.class);
                    String zipCode = zipcodeInput.getText().toString();
                    mapActivity.putExtra("CAR", carSelected);
                    mapActivity.putExtra("CURR_LONG", currLoc.longitude);
                    mapActivity.putExtra("CURR_LAT", currLoc.latitude);
                    mapActivity.putExtra("TARGET_ZIP", zipCode);
                    startActivity(mapActivity);
                }
            }
        });
        model3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
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
                if (b) {
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
                if (b) {
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
        this.googleMap = googleMap;
        googleMap.setMinZoomPreference(15);
        googleMap.addMarker(new MarkerOptions().position(currLoc).title("You are here!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
    }
}

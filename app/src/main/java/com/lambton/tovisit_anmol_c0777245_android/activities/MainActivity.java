package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.lambton.tovisit_anmol_c0777245_android.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 3000;
    private static final int RADIUS = 1500;

    // use the fused location provider client
    private FusedLocationProviderClient mClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    private LatLng userLocation;

    FragmentManager fragmentManager;
    MapsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
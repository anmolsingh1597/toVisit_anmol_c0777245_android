package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlaces;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlacesRoomDb;

import java.util.ArrayList;
import java.util.List;

public class DirectionAndDistanceActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "DirectionAndDistanceActivity" ;
    private GoogleMap mMap;
    int placeID;

    FavoritePlacesRoomDb favoritePlacesRoomDb;
    List<FavoritePlaces> favoritePlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        placeID = getIntent().getIntExtra("placeID", 0);
        favoritePlaces = new ArrayList<>();
        favoritePlacesRoomDb = FavoritePlacesRoomDb.getINSTANCE(this);
        favoritePlaces = favoritePlacesRoomDb.favoritePlacesDao().getAllFavoritePlaces();

        mMap = googleMap;

        for(FavoritePlaces places: favoritePlaces){
            if(places.getId() == placeID){
                // Add a marker in Sydney and move the camera
                LatLng latLng = new LatLng(places.getLatitude(), places.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(places.getLocationName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }
        }


    }
}
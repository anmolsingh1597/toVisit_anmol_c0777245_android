package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlaces;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlacesRoomDb;
import com.lambton.tovisit_anmol_c0777245_android.volley.GetByVolley;
import com.lambton.tovisit_anmol_c0777245_android.volley.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectionAndDistanceActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String TAG = "DirectionAndDistanceActivity";
    private GoogleMap mMap;
    int placeID;
    FloatingActionButton directionFloatingButton;

    FavoritePlacesRoomDb favoritePlacesRoomDb;
    List<FavoritePlaces> favoritePlaces;

    LatLng destinationLatLng;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


        for(FavoritePlaces places: favoritePlaces){
            if(places.getId() == placeID){

                LatLng latLng = new LatLng(places.getLatitude(), places.getLongitude());
                destinationLatLng = latLng;
                mMap.addMarker(new MarkerOptions().position(latLng).title(places.getLocationName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }
        }
        floatingButtonAction();
    }

    private void floatingButtonAction() {

        directionFloatingButton = findViewById(R.id.fab_maps);
        directionFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionFromUserTodestination();
            }
        });
    }

    private void directionFromUserTodestination() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getDirectionUrl(destinationLatLng), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GetByVolley.getDirection(response, mMap, destinationLatLng);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private String getDirectionUrl(LatLng location) {
        StringBuilder googleDirectionUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionUrl.append("origin="+mMap.getMyLocation().getLatitude()+","+mMap.getMyLocation().getLongitude());
        googleDirectionUrl.append(("&destination="+location.latitude+","+location.longitude));
        googleDirectionUrl.append("&key="+getString(R.string.google_maps_key));
        Log.d(TAG, "getDirectionUrl: " + googleDirectionUrl);
        return googleDirectionUrl.toString();
    }

}
package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.fragment.MapsFragment;

import org.json.JSONObject;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragment = new MapsFragment();
        fragmentManager.beginTransaction()
                .add(R.id.myContainer, fragment)
                .commit();

        TabLayout tab = findViewById(R.id.tab);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getPosition());
                String url = "";
                switch (tab.getPosition()) {
                    case 0:
                        url = getPlaceUrl(userLocation.latitude, userLocation.longitude, "hospital");
                        break;
                    case 1:
                        url = getPlaceUrl(userLocation.latitude, userLocation.longitude, "restaurant");
                        break;
                }

                showNearbyPlaces(url);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getDestination(new IPassData() {
                    @Override
                    public void destinationSelected(final Location location, final GoogleMap map) {
                        /*Object[] dataTransfer = new Object[3];
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        String url = getDirectionUrl(latLng);
                        dataTransfer[0] = map;
                        dataTransfer[1] = url;
                        dataTransfer[2] = latLng;

                        GetDirectionsData getDirectionsData = new GetDirectionsData();
                        // execute asynchronously
                        getDirectionsData.execute(dataTransfer);*/

                        /*By Volley Library*/
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                getDirectionUrl(latLng), null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                GetByVolley.getDirection(response, map, location);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }
                });
            }
        });

        if (!hasPermission())
            requestLocationPermission();
        else
            startUpdateLocation();
    }
}


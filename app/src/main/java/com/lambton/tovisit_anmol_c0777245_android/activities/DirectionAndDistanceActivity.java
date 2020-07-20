package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlaces;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlacesRoomDb;
import com.lambton.tovisit_anmol_c0777245_android.volley.GetByVolley;
import com.lambton.tovisit_anmol_c0777245_android.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DirectionAndDistanceActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "DirectionAndDistanceActivity";
    private GoogleMap mMap;
    int placeID;
    FloatingActionButton directionFloatingButton;

    FavoritePlacesRoomDb favoritePlacesRoomDb;
    List<FavoritePlaces> favoritePlaces;

    LatLng destinationLatLng;

    String placeName;

    FloatingActionButton fabSeacrh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setHasOptionsMenu(true);
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
        mMap.setOnMarkerDragListener(DirectionAndDistanceActivity.this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

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


        for (FavoritePlaces places : favoritePlaces) {
            if (places.getId() == placeID) {

                LatLng latLng = new LatLng(places.getLatitude(), places.getLongitude());
                destinationLatLng = latLng;
                placeName = places.getLocationName();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(places.getLocationName())
                        .draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        }
        floatingButtonAction();
    }

    private void floatingButtonAction() {

        directionFloatingButton = findViewById(R.id.fab_maps);
        directionFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionFromUserToDestination();
            }
        });

        fabSeacrh =  findViewById(R.id.fab_map_search);
        fabSeacrh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace();
            }
            
        });
    }

    private void searchPlace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.alert_dialog_search, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText placeText = view.findViewById(R.id.search_place);

        view.findViewById(R.id.alert_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation(placeText.getText().toString());
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(placeText.getWindowToken(), 0);
                alertDialog.dismiss();
            
            }
        });


    }

    private void directionFromUserToDestination() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getDirectionUrl(destinationLatLng), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GetByVolley.getDirection(response, mMap, destinationLatLng, placeName);
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
        googleDirectionUrl.append("origin=" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude());
        googleDirectionUrl.append(("&destination=" + location.latitude + "," + location.longitude));
        googleDirectionUrl.append("&key=" + getString(R.string.google_maps_key));
        Log.d(TAG, "getDirectionUrl: " + googleDirectionUrl);
        return googleDirectionUrl.toString();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FavoritePlacesActivity.class));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");
        String currentDate = simpleDateFormat.format(cal.getTime());
        favoritePlacesRoomDb.favoritePlacesDao().updateFavoritePlaces(placeID, destinationLatLng.latitude, destinationLatLng.longitude, currentDate, placeName, 0);
        finish();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    private String locationName(Marker marker) {
        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        String address = "";
        Geocoder geocoder = new Geocoder(DirectionAndDistanceActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                if (addresses.get(0).getSubThoroughfare() != null)
                    address += addresses.get(0).getSubThoroughfare() + ", ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare() + ", ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + ", ";
                if (addresses.get(0).getAdminArea() != null)
                    address += addresses.get(0).getAdminArea();
                if (addresses.get(0).getPostalCode() != null)
                    address += "\n" + addresses.get(0).getPostalCode();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        destinationLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        placeName = locationName(marker);

    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = new MenuInflater(this);
        getMenuInflater().inflate(R.menu.direction_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.btnSearchDirection);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void searchLocation(String query) {
        mMap.clear();

        String location = query;
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

                if (addressList.size() == 0) {
                    Snackbar.make(findViewById(android.R.id.content), "No Location Found", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else {
                    Marker marker;
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    destinationLatLng = latLng;
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    placeName = locationName(marker);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
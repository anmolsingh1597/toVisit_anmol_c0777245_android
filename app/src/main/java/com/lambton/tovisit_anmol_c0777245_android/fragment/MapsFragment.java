package com.lambton.tovisit_anmol_c0777245_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.activities.MainActivity;
import com.lambton.tovisit_anmol_c0777245_android.dataPass.IPassData;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlaces;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlacesRoomDb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements  GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener{

    private static final String TAG = "MapsFragment";

    FusedLocationProviderClient mClient;
    private FavoritePlacesRoomDb favoritePlacesRoomDb;

    GoogleMap mMap;

    LatLng userLocation;

    public GoogleMap getmMap() {
        return mMap;
    }

    public LatLng getDestLocation() {
        return destLocation;
    }

    RadioGroup mapTypeRadioGroup;

    LatLng destLocation;
    Location destination;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(final GoogleMap googleMap) {

            // Room database
            favoritePlacesRoomDb = FavoritePlacesRoomDb.getINSTANCE(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap = googleMap;
            mMap.setOnMarkerDragListener(MapsFragment.this);
            mMap.setOnMarkerClickListener(MapsFragment.this);
            mMap.setMyLocationEnabled(true);

            mapTypeRadioGroup = getActivity().findViewById(R.id.maps_type_group);

            mapTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.default_map){
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }else if (checkedId == R.id.hybrid_map){
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }else if (checkedId == R.id.terrain_map){
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    }else if (checkedId == R.id.satellite_map){
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    }
                }
            });


            mClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(latLng));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                        ((MainActivity)getActivity()).showLaunchNearbyPlaces(latLng);
                    }
                }
            });

            // add long press gesture on map
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    destLocation = latLng;
                    destination = new Location("Your destination");
                    destination.setLatitude(latLng.latitude);
                    destination.setLongitude(latLng.longitude);
//                    setMarker(destLocation);
                    setMarker(destination);
                }
            });
        }
    };


    private void setMarker(Location destination) {
        LatLng destLatLng = new LatLng(destination.getLatitude(), destination.getLongitude());
        setMarker(destLatLng);
    }

    private void setMarker(LatLng location) {
        MarkerOptions options = new MarkerOptions().position(location)
                .title("Your Destination")
                .snippet("You are going there")
                .draggable(true);
        mMap.addMarker(options);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void setHomeMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    public void getDestination(IPassData callback) {
        callback.destinationSelected(destination, mMap);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        destination.setLatitude(marker.getPosition().latitude);
        destination.setLongitude(marker.getPosition().longitude);

        Log.d(TAG, "onMarkerDragEnd: " + destination.getLatitude());

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(destination.getLatitude(), destination.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                String address = "";
                if (addresses.get(0).getAdminArea() != null)
                    address += addresses.get(0).getAdminArea() + " ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + " ";
                if (addresses.get(0).getPostalCode() != null)
                    address += addresses.get(0).getPostalCode() + " ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare();
                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String locationName(Marker marker){
        LatLng latLng =  new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        String address = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                if (addresses.get(0).getAdminArea() != null)
                    address += addresses.get(0).getAdminArea() + " ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + " ";
                if (addresses.get(0).getPostalCode() != null)
                    address += addresses.get(0).getPostalCode() + " ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare();
                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to save this to your list of `Favorite Places`?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               saveToFavoritePlaces(marker);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                displaySnackBar("No inserted to Favorite Places list");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }

    private void saveToFavoritePlaces(Marker marker) {
        String address = locationName(marker);
        Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
        //getting current date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String currentDate = simpleDateFormat.format(cal.getTime());
        // insert into room db
        FavoritePlaces favoritePlaces = new FavoritePlaces(marker.getPosition().latitude, marker.getPosition().longitude,currentDate, address);
        favoritePlacesRoomDb.favoritePlacesDao().insertFavoritePlaces(favoritePlaces);
        
        displaySnackBar("Place inserted to favorite list");
        displayFavoriteListActivity();
    }

    private void displayFavoriteListActivity() {
    }

    private void displaySnackBar(String text){
        Snackbar.make(getActivity().findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}
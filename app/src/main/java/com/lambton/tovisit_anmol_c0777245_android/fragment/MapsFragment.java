package com.lambton.tovisit_anmol_c0777245_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lambton.tovisit_anmol_c0777245_android.R;

public class MapsFragment extends Fragment {

    private static final String TAG = "MapsFragment";

    FusedLocationProviderClient mClient;

    GoogleMap mMap;

    LatLng userLocation;

    public GoogleMap getmMap() {
        return mMap;
    }

    public LatLng getDestLocation() {
        return destLocation;
    }

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
        public void onMapReady(GoogleMap googleMap) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap = googleMap;
            mMap.setOnMarkerDragListener(MapsFragment.this);
            mMap.setMyLocationEnabled(true);

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
}
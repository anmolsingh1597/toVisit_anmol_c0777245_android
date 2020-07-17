package com.lambton.tovisit_anmol_c0777245_android.roomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "FavoritePlaces")
public class FavoritePlaces {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "latitude")
    private double latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private double longitude;

    @NonNull
    @ColumnInfo(name = "assignedDate")
    private String assignedDate;

    @NonNull
    @ColumnInfo(name = "locationName")
    private String locationName;

    public FavoritePlaces(@NonNull double latitude, double longitude, @NonNull String assignedDate, @NonNull String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.assignedDate = assignedDate;
        this.locationName = locationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(@NonNull String assignedDate) {
        this.assignedDate = assignedDate;
    }

    @NonNull
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(@NonNull String locationName) {
        this.locationName = locationName;
    }
}

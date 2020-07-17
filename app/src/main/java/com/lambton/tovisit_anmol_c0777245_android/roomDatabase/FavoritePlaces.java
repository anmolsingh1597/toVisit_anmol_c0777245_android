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
    @ColumnInfo(name = "latLng")
    private LatLng latLng;

    @NonNull
    @ColumnInfo(name = "assignedDate")
    private String assignedDate;

    @NonNull
    @ColumnInfo(name = "locationName")
    private String locationName;

    public FavoritePlaces(int id, @NonNull LatLng latLng, @NonNull String assignedDate, @NonNull String locationName) {
        this.id = id;
        this.latLng = latLng;
        this.assignedDate = assignedDate;
        this.locationName = locationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(@NonNull LatLng latLng) {
        this.latLng = latLng;
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

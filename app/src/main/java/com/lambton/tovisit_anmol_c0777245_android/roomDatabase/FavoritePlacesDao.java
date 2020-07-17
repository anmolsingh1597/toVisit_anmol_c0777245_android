package com.lambton.tovisit_anmol_c0777245_android.roomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

@Dao
public interface FavoritePlacesDao {

    @Insert
    void insertFavoritePlaces(FavoritePlaces favoritePlaces);

    @Query("DELETE FROM favoriteplaces")
    void deleteAllFavoritePlaces();

    @Query("DELETE FROM favoriteplaces WHERE id = :id")
    int favoritePlaces(int id);

    @Query("UPDATE favoriteplaces SET latLng = :latLng, assignedDate= :assignedDate, locationName = :locationName WHERE id = :id")
    int updateFavoritePlaces(int id, LatLng latLng, String assignedDate, String locationName);

    @Query("SELECT * FROM favoriteplaces ORDER BY locationName")
    List<FavoritePlaces> getAllFavoritePlaces();
}

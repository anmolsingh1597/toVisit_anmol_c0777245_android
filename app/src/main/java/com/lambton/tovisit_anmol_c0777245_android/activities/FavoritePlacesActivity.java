package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.adapter.FavoritePlacesAdapter;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlaces;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlacesRoomDb;

import java.util.ArrayList;
import java.util.List;

public class FavoritePlacesActivity extends AppCompatActivity {

    private FavoritePlacesRoomDb favoritePlacesRoomDb;
    RecyclerView favoritePlacesRecyclerView;
    List<FavoritePlaces> favoritePlacesList;
    FavoritePlacesAdapter favoritePlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

        initials();
        
    }

    private void initials() {
        favoritePlacesRecyclerView = findViewById(R.id.favorite_places_rv);
        favoritePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoritePlacesList = new ArrayList<>();
        favoritePlacesRoomDb = FavoritePlacesRoomDb.getINSTANCE(this);
        loadFavoritePlaces();
    }

    private void loadFavoritePlaces() {
        favoritePlacesList = favoritePlacesRoomDb.favoritePlacesDao().getAllFavoritePlaces();
        favoritePlacesAdapter = new FavoritePlacesAdapter(this,R.layout.favorite_place_list, favoritePlacesList);
        favoritePlacesRecyclerView.setAdapter(favoritePlacesAdapter);
    }
}
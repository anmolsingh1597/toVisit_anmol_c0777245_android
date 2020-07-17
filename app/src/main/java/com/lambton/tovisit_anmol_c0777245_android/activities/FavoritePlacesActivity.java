package com.lambton.tovisit_anmol_c0777245_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
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
        recyclerViewCellSwipe();

    }

    private void recyclerViewCellSwipe() {
        final ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    openEditMaps(position);
                } else if (direction == ItemTouchHelper.LEFT) {
                    deleteLocation(position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(favoritePlacesRecyclerView);
    }

    private void openEditMaps(int position) {
        Intent intent = new Intent(FavoritePlacesActivity.this, DirectionAndDistanceActivity.class);
        intent.putExtra("placeID", favoritePlacesList.get(position).getId());
        startActivity(intent);
        finish();
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
        favoritePlacesAdapter = new FavoritePlacesAdapter(this, R.layout.favorite_place_list, favoritePlacesList);
        favoritePlacesRecyclerView.setAdapter(favoritePlacesAdapter);
    }

    private void deleteLocation(final int position) {
        favoritePlacesRoomDb.favoritePlacesDao().deleteFavoritePlaces(favoritePlacesList.get(position).getId());
        loadFavoritePlaces();

        Snackbar.make(findViewById(android.R.id.content), "Value deleted from database", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void loadFavoritePlacesAgain() {
        favoritePlacesList = favoritePlacesRoomDb.favoritePlacesDao().getAllFavoritePlaces();
        favoritePlacesAdapter.notifyDataSetChanged();
    }
}
package com.lambton.tovisit_anmol_c0777245_android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.tovisit_anmol_c0777245_android.R;
import com.lambton.tovisit_anmol_c0777245_android.activities.DirectionAndDistanceActivity;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlaces;
import com.lambton.tovisit_anmol_c0777245_android.roomDatabase.FavoritePlacesRoomDb;

import java.util.ArrayList;
import java.util.List;

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.FavoritePlacesListViewHolder> {

    private static final String TAG = "FavoritePlacesAdapter";
    Context context;
    int layoutRes;
    List<FavoritePlaces> favoritePlacesList;
    List<FavoritePlaces> favoritePlacesListFull;
    FavoritePlacesRoomDb favoritePlacesRoomDb;

    //    MainActivity mainActivity;
    public FavoritePlacesAdapter(Context context, int resource, List<FavoritePlaces> favoritePlacesList) {
        this.favoritePlacesList = favoritePlacesList;
        this.layoutRes = resource;
        this.context = context;

        favoritePlacesRoomDb = favoritePlacesRoomDb.getINSTANCE(context);
        favoritePlacesListFull = new ArrayList<>(favoritePlacesList);
    }

    public class FavoritePlacesListViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView lat;
        TextView lng;
        TextView date;

        public FavoritePlacesListViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.tv_place_name);
            lat = itemView.findViewById(R.id.tv_latitude);
            lng = itemView.findViewById(R.id.tv_longitude);
            date = itemView.findViewById(R.id.tv_date);

        }
    }

    @NonNull
    @Override
    public FavoritePlacesAdapter.FavoritePlacesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutRes, null);

        FavoritePlacesListViewHolder favoritePlacesListViewHolder = new FavoritePlacesAdapter.FavoritePlacesListViewHolder(view);
        return favoritePlacesListViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePlacesAdapter.FavoritePlacesListViewHolder holder, int position) {
        final FavoritePlaces favoritePlaces = favoritePlacesList.get(position);
        holder.placeName.setText("Place: "+favoritePlaces.getLocationName());
        holder.lat.setText("Latitude: "+String.valueOf(favoritePlaces.getLatitude()));
        holder.lng.setText("Longitude: "+String.valueOf(favoritePlaces.getLatitude()));
        holder.date.setText("Date: "+favoritePlaces.getAssignedDate());
        holder.itemView.findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToMaps(favoritePlaces);
            }
        });
        
        holder.itemView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLocation(favoritePlaces);
            }
        });
    }

    private void deleteLocation(final FavoritePlaces favoritePlaces) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                favoritePlacesRoomDb.favoritePlacesDao().deleteFavoritePlaces(favoritePlaces.getId());
                loadFavoritePlaces();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Place:  " + favoritePlaces.getLocationName() + " is still in database", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadFavoritePlaces() {
        favoritePlacesList = favoritePlacesRoomDb.favoritePlacesDao().getAllFavoritePlaces();
        notifyDataSetChanged();
    }

    private void jumpToMaps(final FavoritePlaces favoritePlaces) {
        Intent intent = new Intent(context, DirectionAndDistanceActivity.class);
        intent.putExtra("placeID", favoritePlaces.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return favoritePlacesList.size();
    }


}

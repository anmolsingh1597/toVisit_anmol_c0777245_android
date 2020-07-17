package com.lambton.tovisit_anmol_c0777245_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.tovisit_anmol_c0777245_android.R;
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
        holder.placeName.setText(favoritePlaces.getLocationName());
        holder.lat.setText(String.valueOf(favoritePlaces.getLatitude()));
        holder.lng.setText(String.valueOf(favoritePlaces.getLatitude()));
        holder.date.setText(favoritePlaces.getAssignedDate());
    }

    @Override
    public int getItemCount() {
        return favoritePlacesList.size();
    }


}

package com.lambton.tovisit_anmol_c0777245_android.roomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoritePlaces.class}, version = 1, exportSchema = false)
public abstract class FavoritePlacesRoomDb extends RoomDatabase {

    private static final String DB_NAME = "favorite_places_database";

    public abstract FavoritePlacesDao favoritePlacesDao();

    private static volatile FavoritePlacesRoomDb INSTANCE;

    public static FavoritePlacesRoomDb getINSTANCE(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoritePlacesRoomDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;

    }
}

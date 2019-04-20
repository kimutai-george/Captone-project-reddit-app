package com.example.a4reddit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by George Kimutai on 4/08/2019.
 */


@Database(entities = {SubModel.class, BookedPost.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE
                    = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "sub_db")
                    .build();
        }
        return INSTANCE;
    }

    public abstract SubDao getAllSubName();

    public abstract BookedDAO getAllBookedPost();


}


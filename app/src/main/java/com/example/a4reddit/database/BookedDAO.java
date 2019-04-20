package com.example.a4reddit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by George Kimutai on 4/08/2019.
 */


@Dao
public interface BookedDAO {
    @Query("select * from BookedPost")
    LiveData<List<BookedPost>> getAllBookedPost();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPost(BookedPost bookedPost);
    @Delete
    void deletePost(BookedPost bookedPost);
}
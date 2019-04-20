package com.example.a4reddit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by George Kimutai on 4/08/2019.
 */


@Dao
public interface SubDao {

    @Query("select * from SubModel")
    LiveData<List<SubModel>> getAllSubName();
    @Insert(onConflict = REPLACE)
    void addSub(SubModel add);
    @Delete
    void deleteSub(SubModel delete);


}

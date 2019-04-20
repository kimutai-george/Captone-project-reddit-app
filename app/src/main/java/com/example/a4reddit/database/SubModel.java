package com.example.a4reddit.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by George Kimutai on 4/08/2019.
 */


@Entity
public class SubModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private final String nameGropu;
    public String getNameGropu() {
        return nameGropu;
    }
    public SubModel(String nameGropu) {
        this.nameGropu = nameGropu;
    }
}


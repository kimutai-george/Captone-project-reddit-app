package com.example.a4reddit.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by George Kimutai on 4/09/2019.
 */

public class MySubViewModel extends ViewModel {
    public MySubViewModel() {
    }

    public final MutableLiveData<List> entryListMy = new MutableLiveData<>();

    private final MutableLiveData<List> subsListMy = new MutableLiveData<>();


    public LiveData<List> getEntryList() {
        if (entryListMy == null) {

        }
        return entryListMy;
    }

    public MutableLiveData<List> getSubsList() {
        if (subsListMy == null) {

        }
        return subsListMy;
    }
}


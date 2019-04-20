package com.example.a4reddit.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by George Kimutai on 4/09/2019.
 */
public class AnyRespondViewHolder extends ViewModel {


    public AnyRespondViewHolder() {
    }

    public final MutableLiveData<List> entryList = new MutableLiveData<>();

    public final MutableLiveData<List> subsList = new MutableLiveData<>();


    public LiveData<List> getEntryList() {
        if (entryList == null) {

        }
        return entryList;
    }

    public MutableLiveData<List> getSubsList() {
        if (subsList == null) {

        }
        return subsList;
    }
}
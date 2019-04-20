package com.example.a4reddit.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.a4reddit.database.AppDatabase;
import com.example.a4reddit.database.SubModel;

import java.util.List;

/**
 * Created by George Kimutai on 4/09/2019.
 */

public class SubListViewModel extends AndroidViewModel {
    private final LiveData<List<SubModel>> nameOfGroup;
    private final AppDatabase appDatabase;

    public SubListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        nameOfGroup = appDatabase.getAllSubName().getAllSubName();
    }

    public LiveData<List<SubModel>> getNameOfGroup() {
        return nameOfGroup;

    }

    public void deleteItem(SubModel subModel) {
        new deleteAsyncTask(appDatabase).execute(subModel);
    }

    private static class deleteAsyncTask extends AsyncTask<SubModel, Void, Void> {
        private final AppDatabase appDatabase;

        deleteAsyncTask(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        protected Void doInBackground(final SubModel... subModels) {
            appDatabase.getAllSubName().deleteSub(subModels[0]);
            return null;
        }
    }
}

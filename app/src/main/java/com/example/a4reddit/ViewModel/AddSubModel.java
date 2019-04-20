package com.example.a4reddit.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.a4reddit.database.AppDatabase;
import com.example.a4reddit.database.SubModel;

/**
 * Created by George Kimutai on 4/09/2019.
 */
public class AddSubModel extends AndroidViewModel {

    private final AppDatabase appDatabase;

    public AddSubModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }

    public void addSub(SubModel subModel) {
        new addAsyncTask(appDatabase).execute(subModel);
    }

    private static class addAsyncTask extends AsyncTask<SubModel, Void, Void> {

        private final AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final SubModel... params) {
            db.getAllSubName().addSub(params[0]);
            return null;
        }

    }
}


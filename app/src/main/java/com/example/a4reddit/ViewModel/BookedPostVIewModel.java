package com.example.a4reddit.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.a4reddit.database.AppDatabase;
import com.example.a4reddit.database.BookedPost;

import java.util.List;

/**
 * Created by George Kimutai on 4/09/2019.
 */
public class BookedPostVIewModel extends AndroidViewModel {


    private final AppDatabase appDatabase;
    private final LiveData<List<BookedPost>> mBookedPost;

    public BookedPostVIewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        mBookedPost = appDatabase.getAllBookedPost().getAllBookedPost();

    }

    public LiveData<List<BookedPost>> getmBookedPost() {
        return mBookedPost;
    }

    public void deleteItem(BookedPost bookedPost) {
        new deleteBookedPost(appDatabase).execute(bookedPost);
    }

    private static class deleteBookedPost extends AsyncTask<BookedPost, Void, Void> {
        private final AppDatabase appDatabase;

        deleteBookedPost(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        protected Void doInBackground(final BookedPost... bookedPosts) {
            appDatabase.getAllBookedPost().deletePost(bookedPosts[0]);
            return null;
        }
    }

    public void addBooked(BookedPost bookedPost) {
        new addBookdedAsyncTask(appDatabase).execute(bookedPost);
    }

    private static class addBookdedAsyncTask extends AsyncTask<BookedPost, Void, Void> {

        private final AppDatabase db;

        addBookdedAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final BookedPost... params) {
            db.getAllBookedPost().addPost(params[0]);
            return null;
        }

    }

}

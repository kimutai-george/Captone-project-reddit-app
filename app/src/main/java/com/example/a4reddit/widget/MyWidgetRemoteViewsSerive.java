package com.example.a4reddit.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.a4reddit.R;
import com.example.a4reddit.fragments.MySubcreFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George Kimutai on 4/17/2019.
 */
public class MyWidgetRemoteViewsSerive extends RemoteViewsService {
    public static final String WIDGET_OBJECT = "WidgetObject";
    public static final String WIDGET_RECOGNIZE = "WidgetRecognize";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetRemoteViewFactory(this.getApplicationContext());
    }

    private class MyWidgetRemoteViewFactory implements RemoteViewsFactory {
        private final Context ct;
        private List<SendDataToWidget> mPost;

        MyWidgetRemoteViewFactory(Context applicationContext) {
            ct = applicationContext;
            mPost = new ArrayList<>();

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String json = preferences.getString(MySubcreFragment.SHARED_PREF_KEY, "");

            if (!json.equals("")) {
                Gson gson = new Gson();
                mPost = gson.fromJson(json, new TypeToken<ArrayList<SendDataToWidget>>() {
                }.getType());
            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mPost != null) {
                return mPost.size();
            } else return 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews rv = new RemoteViews(ct.getPackageName(), R.layout.widget_list_view);
            try {
                Bitmap bm = Picasso.get().load(mPost.get(i).getThumbnailURL()).get();
                rv.setImageViewBitmap(R.id.widget_list_imageView, bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
            rv.setTextViewText(R.id.widget_list_Date, mPost.get(i).getUpdated());

            rv.setTextViewText(R.id.widget_list_group, mPost.get(i).getGroup());
            rv.setTextViewText(R.id.widget_list_Subtitle, mPost.get(i).getAuthor());
            rv.setTextViewText(R.id.widget_list_Title, mPost.get(i).getTitle());
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(WIDGET_OBJECT, mPost.get(i));
            bundle.putBoolean(WIDGET_RECOGNIZE, true);
            intent.putExtras(bundle);

            rv.setOnClickFillInIntent(R.id.itemWidget, intent);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}

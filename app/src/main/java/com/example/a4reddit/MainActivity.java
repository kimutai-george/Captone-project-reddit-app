package com.example.a4reddit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.a4reddit.ViewModel.BookedPostVIewModel;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.database.BookedPost;
import com.example.a4reddit.fragments.GlobalFragments;
import com.example.a4reddit.fragments.MySubcreFragment;
import com.example.a4reddit.fragments.SearchFragment;
import com.example.a4reddit.widget.SendDataToWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by George Kimutai on 3/25/2019.
 */

public class MainActivity extends AppCompatActivity {
    //https://www.reddit.com/subreddits/
    private static final String TAG = "MainActivity";
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomnav_view)
    BottomNavigationView bottomNavigationView;
    private MenuItem menuItem;
    private BroadcastReceiver mBroad;
    private BookedPostVIewModel bookedPostVIewModel;
    private String[] foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bookedPostVIewModel = ViewModelProviders.of(this).get(BookedPostVIewModel.class);
        bookedPostVIewModel.getmBookedPost().observe(this, new Observer<List<BookedPost>>() {
            @Override
            public void onChanged(@Nullable List<BookedPost> bookedPosts) {

            }
        });
        mBroad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BookedPostVIewModel book = ViewModelProviders.of(MainActivity.this).get(BookedPostVIewModel.class);
                Post post = null;
                BookedPost bookedPost = null;
                SendDataToWidget sendDataToWidget = null;
                if (intent.getBooleanExtra(DetailActivity.IF_IS, false)) {
                    bookedPost = (BookedPost) intent.getSerializableExtra(DetailActivity.MAIN);
                } else if (intent.getBooleanExtra(DetailActivity.WIDGET, false)) {
                    sendDataToWidget = intent.getParcelableExtra(DetailActivity.MAIN);
                } else {
                    post = (Post) intent.getSerializableExtra(DetailActivity.MAIN);
                }
                boolean checkIfIs = false;
                boolean check = intent.getBooleanExtra(DetailActivity.CHECK, false);
                String comm;
                if (post != null) {
                    comm = post.getComments();
                } else if (bookedPost != null) {
                    comm = bookedPost.getComments();
                    Log.i(TAG, "onReceive: " + comm);
                } else {
                    comm = sendDataToWidget.getComments();
                }
                for (int x = 0; x < book.getmBookedPost().getValue().size(); x++) {
                    String comments = book.getmBookedPost().getValue().get(x).getComments();
                    if (comments.equals(comm)) {
                        checkIfIs = true;
                        bookedPost = book.getmBookedPost().getValue().get(x);
                    }
                }
                if (!checkIfIs) {
                    if (check) {
                        if (post != null) {
                            bookedPostVIewModel.addBooked(new BookedPost(post.getText(), post.getGroup(), post.getTitle(), post.getAuthor(), post.getUpdated(), post.getPostURL().get(0), post.getThumbnailURL(), post.getComments(), post.getYtLinks().get(0)));
                        } else {
                            bookedPostVIewModel.addBooked(new BookedPost(sendDataToWidget.getText(), sendDataToWidget.getGroup(), sendDataToWidget.getTitle(), sendDataToWidget.getAuthor(), sendDataToWidget.getUpdated(), sendDataToWidget.getPostURL(), sendDataToWidget.getThumbnailURL(), sendDataToWidget.getComments(), sendDataToWidget.getYtLinks()));
                        }
                    }
                } else {
                    if (bookedPost != null && !check) {
                        bookedPostVIewModel.deleteItem(bookedPost);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroad, new IntentFilter(DetailActivity.MainActivity));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.avtion_mySubreddits:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.avtion_global:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.avtion_searchNew:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(position).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        GlobalFragments globalFragments = new GlobalFragments();
        SearchFragment searchFragment = new SearchFragment();
        MySubcreFragment mySubcreFragment = new MySubcreFragment();
        adapter.addFragment(mySubcreFragment);
        adapter.addFragment(globalFragments);
        adapter.addFragment(searchFragment);
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroad);
        super.onDestroy();
    }
}


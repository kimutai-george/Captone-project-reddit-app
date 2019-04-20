package com.example.a4reddit.fragments;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a4reddit.Client.RetrofitClientInstance;
import com.example.a4reddit.DetailActivity;
import com.example.a4reddit.R;
import com.example.a4reddit.widget.SendDataToWidget;
import com.example.a4reddit.ViewModel.BookedPostVIewModel;
import com.example.a4reddit.ViewModel.MySubViewModel;
import com.example.a4reddit.ViewModel.SubListViewModel;
import com.example.a4reddit.widget.Widget;
import com.example.a4reddit.adapter.FavoriteViewAdapter;
import com.example.a4reddit.adapter.SubCreditAdapter;
import com.example.a4reddit.data.APIfeed;
import com.example.a4reddit.data.model.Feed;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.database.BookedPost;
import com.example.a4reddit.database.SubModel;
import com.example.a4reddit.interfaces.Mine_SubToDetail;
import com.example.a4reddit.interfaces.SendTOMainActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by George Kimutai on 3/29/2019.
 */
public class MySubcreFragment extends Fragment implements Mine_SubToDetail, SendTOMainActivity {
    private static final String TAG = "MySub";
    @BindView(R.id.RecyclerViewFavoriete)
    RecyclerView mRecyclerViewFavoriete;
    private FavoriteViewAdapter mFavorieteAdapter;
    @BindView(R.id.mySub_RecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBarMySub)
    ProgressBar mProgressBar;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.main_toolbar)
    Toolbar main_toolbar;

    private SubCreditAdapter mAdapter;
    public static final String BASE_URI = "https://www.reddit.com/r/";
    private RetrofitClientInstance retrofitClientInstance;
    private MySubViewModel mySubViewModel;
    private String mNamesOfCat = "";
    private String nameOfButton = "";
    private BookedPostVIewModel model;
    private List<BookedPost> mBook;
    public final static String SHARED_PREF_KEY = "KEY_FOR_PREFERENCES";



    public MySubcreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SubCreditAdapter(getContext(), this);
        mFavorieteAdapter = new FavoriteViewAdapter(getContext(), this);
        setHasOptionsMenu(true);


        SubListViewModel subListViewModel = ViewModelProviders.of(this).get(SubListViewModel.class);


        subListViewModel.getNameOfGroup().observe(this, new Observer<List<SubModel>>() {
            @Override
            public void onChanged(@Nullable List<SubModel> subModels) {


                for (int x = 0; x < subModels.size(); x++) {
                    if (x == subModels.size() - 1) {
                        mNamesOfCat = mNamesOfCat + subModels.get(x).getNameGropu();
                    } else {
                        //noinspection StringConcatenationInLoop
                        mNamesOfCat = mNamesOfCat + subModels.get(x).getNameGropu() + "+";
                    }
                }

                if (mNamesOfCat != null && !mNamesOfCat.isEmpty()) {
                    loadRetrofit(mNamesOfCat);
                }


            }
        });
        //swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_dark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String new_feed = mNamesOfCat + "/new";
                checkNet();
                Log.d(TAG,"onSwipe : " + new_feed);
                loadRetrofit(new_feed);
                Toast.makeText(getContext(),R.string.OnRefresh,Toast.LENGTH_LONG).show();
                swipeRefresh.setRefreshing(false);


            }
        });




        model = ViewModelProviders.of(this).get(BookedPostVIewModel.class);
        model.getmBookedPost().observe(this, new Observer<List<BookedPost>>() {
            @Override
            public void onChanged(@Nullable List<BookedPost> bookedPosts) {
                mRecyclerViewFavoriete.setAdapter(mFavorieteAdapter);
                mBook = bookedPosts;
                mFavorieteAdapter.takePost(bookedPosts);
                for (int x = 0; x < (bookedPosts != null ? bookedPosts.size() : 0); x++) {
                    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, bookedPosts.get(x).id);
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, bookedPosts.get(x).getComments());
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    firebaseAnalytics.setAnalyticsCollectionEnabled(true);
                    firebaseAnalytics.setMinimumSessionDuration(20000);
                    firebaseAnalytics.setSessionTimeoutDuration(500);
                    firebaseAnalytics.setUserId(String.valueOf(bookedPosts.get(x)));
                    firebaseAnalytics.setUserProperty("Comment", bookedPosts.get(x).getComments());
                }


            }
        });


        mySubViewModel = ViewModelProviders.of(this).get(MySubViewModel.class);
        mySubViewModel.getEntryList().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List list) {

                mProgressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(mAdapter);
                mAdapter.takePost(list);
                Log.i(TAG, "onChanged: " + mBook.size());
                sendToWidget(list);


            }
        });
        setToolBar();



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_sub, container, false);
        retrofitClientInstance = new RetrofitClientInstance();
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mRecyclerViewFavoriete.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewFavoriete.setHasFixedSize(true);
        setHasOptionsMenu(true);



        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        setToolBar();








        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nav_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void setToolBar(){

        ((AppCompatActivity)getActivity()).setSupportActionBar(main_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.My);
        main_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d(TAG,"onMenuItemClicked: clicked menu item: " + menuItem);

                switch(menuItem.getItemId()){
                    case R.id.newButton:

                        if (!mNamesOfCat.isEmpty() && mNamesOfCat != null) {
                            //  nameOfButton = String.valueOf(R.string.NEW);
                            String newButon = mNamesOfCat + R.string.NEW;
                            loadRetrofit(newButon);
                        } else {
                            Toast.makeText(getContext(), R.string.FIRS_SUBSC, Toast.LENGTH_SHORT).show();
                        }

                    case R.id.topButton:

                        if (!mNamesOfCat.isEmpty() && mNamesOfCat != null) {
                            //nameOfButton = String.valueOf(R.string.TOP);
                            String topButon = mNamesOfCat + R.string.TOP;
                            loadRetrofit(topButon);
                        } else {
                            Toast.makeText(getContext(), R.string.FIRS_SUBSC, Toast.LENGTH_SHORT).show();
                        }

                    case R.id.hotButton:

                        if (!mNamesOfCat.isEmpty() && mNamesOfCat != null) {
                            //nameOfButton = String.valueOf(R.string.HOT);
                            String hotButton = mNamesOfCat + R.string.HOT;
                            loadRetrofit(hotButton);
                        } else {
                            Toast.makeText(getContext(), R.string.FIRS_SUBSC, Toast.LENGTH_SHORT).show();
                        }

                    case R.id.SearchPopularButton:

                        if (!mNamesOfCat.isEmpty() && mNamesOfCat != null) {
                            // nameOfButton = String.valueOf(R.string.NEW);
                            String porpularButtom = mNamesOfCat + R.string.POPULAR;
                            loadRetrofit(porpularButtom);
                        } else {
                            Toast.makeText(getContext(), R.string.FIRS_SUBSC, Toast.LENGTH_SHORT).show();
                        }


                }

                return false;
            }
        });

    }


    private void loadRetrofit(String input) {
        APIfeed service = RetrofitClientInstance.getRetrofitMySub(BASE_URI).create(APIfeed.class);
        Call<Feed> feedCall = service.listFeed(input);
        feedCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<Post> respon;
                try {
                    respon = retrofitClientInstance.fetchDataFromXML(response.body().getEntires());
                } catch (NullPointerException e) {
                    respon = null;
                }

                if (respon == null) {
                    Toast.makeText(getContext(), R.string.Input_Is_Wrong, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mySubViewModel.entryListMy.setValue(respon);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                if (!checkNet()) {
                    Snackbar snackbar = Snackbar.make(swipeRefresh, R.string.CheckConnection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.Retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    mProgressBar.setVisibility(View.VISIBLE);
                                    loadRetrofit(mNamesOfCat + nameOfButton);
                                }
                            });
                    snackbar.show();
                }

            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mNamesOfCat = "";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void mMySubToDetail(Post post) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(GlobalFragments.NAME, post);
        intent.putExtra(GlobalFragments.LIST_OF_ROOM, (Serializable) model.getmBookedPost().getValue());
        getActivity().startActivity(intent, options.toBundle());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void mSendTOMainActivity(BookedPost post) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(GlobalFragments.NAME, post);
        intent.putExtra(DetailActivity.CHECK, true);
        getActivity().startActivity(intent, options.toBundle());
    }

    private boolean checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    private void sendToWidget(List<Post> post) {
        ArrayList<SendDataToWidget> mPost = new ArrayList<>();
        for (int x = 0; x < post.size(); x++) {
            String mAuthor = post.get(x).getAuthor();
            String mComment = post.get(x).getComments();
            String mPostURI = post.get(x).getPostURL().get(0);
            String mYT = post.get(x).getYtLinks().get(0);
            String mTitle = post.get(x).getTitle();
            String mPic = post.get(x).getThumbnailURL();
            String data = post.get(x).getUpdated();
            String mText = post.get(x).getText();
            String mGroup = post.get(x).getGroup();
            boolean mook = false;
            for (int z = 0; z < mBook.size(); z++) {

                if (mComment.equals(mBook.get(z).getComments())) {
                    mook = true;
                }
            }


            mPost.add(new SendDataToWidget(mText, mGroup, mTitle, mAuthor, data, mPostURI, mPic, mComment, mYT, mook));


        }
        Gson gson = new Gson();
        String json = gson.toJson(mPost);
        Log.i(TAG, "sendToWidget: " + json);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREF_KEY, json).apply();

        Intent intent = new Intent(getContext(), Widget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        getActivity().sendBroadcast(intent);


    }
}

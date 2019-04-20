package com.example.a4reddit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a4reddit.Client.RetrofitClientInstance;
import com.example.a4reddit.adapter.CommentsViewAdapter;
import com.example.a4reddit.data.APIfeed;
import com.example.a4reddit.data.model.Feed;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.database.BookedPost;
import com.example.a4reddit.fragments.GlobalFragments;
import com.example.a4reddit.widget.MyWidgetRemoteViewsSerive;
import com.example.a4reddit.widget.SendDataToWidget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by George Kimutai on 3/25/2019.
 */

public class DetailActivity extends YouTubeBaseActivity {
    @BindView(R.id.image_button_fav)
    Button mButton;
    @BindView(R.id.autor)
    TextView mAuthor;
    @BindView(R.id.progressBar_detail)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbarComments)
    Toolbar mToolbar;
    @BindView(R.id.detail_RecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.comment_imageView)
    ImageView imageView;
    @BindView(R.id.linkContainer)
    LinearLayout mContainer;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.group)
    TextView mGroup;
    @BindView(R.id.commentss_text)
    TextView mText;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    private CommentsViewAdapter mAdapter;
    private static final String TAG = "detailActivity";
    public static final String BASE_URI = "https://www.reddit.com/r/";
    private RetrofitClientInstance retrofitClientInstance;
    private BookedPost mBooked;
    private String linkPost = null;
    private Post post;
    @BindView(R.id.linkTV)
    TextView tvLink;
    private List<Post> respon;

    private YouTubePlayerView mPlayerView;
    private YouTubePlayer.OnInitializedListener mInitialize;
    public static final String NAMEOFLINK = "nameOfLink";
    public static final String MAIN = "main";
    public static final String IF_IS = "ifIs";
    public static final String WIDGET = "TRUE";
    public static final String MainActivity = "MainActivity";
    private final String BUTTON = "button";
    private final String COMMENTS = "comments";
    private final String POSITION = "position";
    public static final String CHECK = "check";
    private SendDataToWidget sendDataToWidget;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        android.transition.Transition transition = android.transition.TransitionInflater.from(this).inflateTransition(R.transition.transition);
        getWindow().setEnterTransition(transition);
        ButterKnife.bind(this);
        AdView mAdView = findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mPlayerView = findViewById(R.id.youTubePlayer);
        if (getIntent().getBooleanExtra(CHECK, false)) {
            mBooked = (BookedPost) getIntent().getSerializableExtra(GlobalFragments.NAME);
            mButton.setSelected(getIntent().getBooleanExtra(CHECK, false));

        } else if (getIntent().getExtras().getBoolean(MyWidgetRemoteViewsSerive.WIDGET_RECOGNIZE)) {
            sendDataToWidget = (SendDataToWidget) getIntent().getExtras().get(MyWidgetRemoteViewsSerive.WIDGET_OBJECT);

            if (sendDataToWidget.getmBook()) {
                mButton.setSelected(true);
            }


        } else {
            post = (Post) getIntent().getSerializableExtra(GlobalFragments.NAME);
            List<BookedPost> listOfRoom = (List<BookedPost>) getIntent().getSerializableExtra(GlobalFragments.LIST_OF_ROOM);
            if (listOfRoom != null) {
                for (int x = 0; x < listOfRoom.size(); x++) {
                    if (listOfRoom.get(x).getComments().equals(post.getComments())) {
                        mButton.setSelected(true);
                    }
                }
            }


        }
        mInitialize = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                String videoID = ytLinks(post.getYtLinks());
                if (videoID != null) {
                    youTubePlayer.loadVideo(videoID);

                }


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {


            }
        };
        init(post, mBooked, sendDataToWidget);
        retrofitClientInstance = new RetrofitClientInstance();
        if (savedInstanceState != null) {
            mButton.setSelected(savedInstanceState.getBoolean(BUTTON));
            respon = (List<Post>) savedInstanceState.getSerializable(COMMENTS);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.getPost((List<Post>) savedInstanceState.getSerializable(COMMENTS));
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVerticalScrollbarPosition(savedInstanceState.getInt(POSITION));

        } else {
            if (post != null) {
                loadRetrofit(post.getComments().substring(25));
            } else if (sendDataToWidget != null) {
                loadRetrofit(sendDataToWidget.getComments().substring(25));
            } else {
                loadRetrofit(mBooked.getComments().substring(25));
            }
        }


    }


    private void loadRetrofit(String input) {
        APIfeed service = RetrofitClientInstance.getComments(BASE_URI).create(APIfeed.class);
        Call<Feed> feedCall = service.listFeed(input);
        feedCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                try {
                    respon = retrofitClientInstance.fetchDataFromXML(response.body().getEntires());
                } catch (NullPointerException e) {
                    respon = null;
                }

                if (respon == null) {
                    Toast.makeText(DetailActivity.this, R.string.Input_Is_Wrong, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.getPost(respon);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                if (!checkNet()) {
                    Snackbar snackbar = Snackbar.make(mScrollView, R.string.CheckConnection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.Retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (post != null) {
                                        loadRetrofit(post.getComments().substring(25));
                                    } else {
                                        loadRetrofit(mBooked.getComments().substring(25));
                                    }
                                    mProgressBar.setVisibility(View.VISIBLE);
                                }
                            });
                    snackbar.show();
                }


            }
        });
    }

    private void init(Post post, BookedPost mBooked, SendDataToWidget sendDataToWidget) {
        String reddit = "reddit";
        if (post != null) {
            if (post.getPostURL() != null) {
                for (int x = 0; x < post.getPostURL().size(); x++) {
                    int link;
                    try {
                        link = post.getPostURL().get(x).indexOf(reddit);

                    } catch (NullPointerException e) {

                        link = -2;
                    }
                    if (link == -1) {
                        linkPost = post.getPostURL().get(x);

                    }
                }
            }

            if (linkPost == null) {
                mContainer.setVisibility(View.GONE);
            }

            if (post.getThumbnailURL() == null) {
                imageView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(post.getThumbnailURL()).into(imageView);
            }
            if (post.getAuthor() == null) {
                mAuthor.setVisibility(View.GONE);
            } else {
                mAuthor.setText(post.getAuthor());
            }
            if (post.getText() == null) {
                mText.setText(View.GONE);
            } else {
                mText.setText(post.getText());
            }


            if (post.getGroup() == null) {
                mGroup.setVisibility(View.GONE);
            } else {
                mGroup.setText(post.getGroup());
            }
            if (post.getUpdated() == null) {

                mDate.setVisibility(View.GONE);
            } else {
                mDate.setText(post.getUpdated());
            }


            if (post.getYtLinks().get(0) == null) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                mPlayerView.initialize(YouTubeConfig.getAPI(), mInitialize);
            }
        } else if (mBooked != null) {
            if (mBooked.getPostURL() != null) {
                int link;
                try {
                    link = mBooked.getPostURL().indexOf("reddit");

                } catch (NullPointerException e) {
                    Log.i(TAG, "init: " + e.getMessage());
                    link = -2;
                }
                if (link == -1) {
                    linkPost = mBooked.getPostURL();
                    Log.i(TAG, "onCreate: " + linkPost);
                }

            }

            if (linkPost == null) {
                mContainer.setVisibility(View.GONE);
            }

            if (mBooked.getThumbnailURL() == null) {
                imageView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(mBooked.getThumbnailURL()).into(imageView);
            }
            if (mBooked.getAuthor() == null) {
                mAuthor.setVisibility(View.GONE);
            } else {
                mAuthor.setText(mBooked.getAuthor());
            }
            if (mBooked.getText() == null) {
                mText.setText(View.GONE);
            } else {
                mText.setText(mBooked.getText());
            }


            if (mBooked.getGroup() == null) {
                mGroup.setVisibility(View.GONE);
            } else {
                mGroup.setText(mBooked.getGroup());
            }
            if (mBooked.getUpdated() == null) {

                mDate.setVisibility(View.GONE);
            } else {
                mDate.setText(mBooked.getUpdated());
            }

            if (mBooked.getYtLinks() == null) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                mPlayerView.initialize(YouTubeConfig.getAPI(), mInitialize);
            }
        } else {
            if (sendDataToWidget.getPostURL() != null) {
                int link;
                try {
                    link = sendDataToWidget.getPostURL().indexOf("reddit");

                } catch (NullPointerException e) {
                    Log.i(TAG, "init: " + e.getMessage());
                    link = -2;
                }
                if (link == -1) {
                    linkPost = sendDataToWidget.getPostURL();
                    Log.i(TAG, "onCreate: " + linkPost);
                }

            }

            if (linkPost == null) {
                mContainer.setVisibility(View.GONE);
            }

            if (sendDataToWidget.getThumbnailURL() == null) {
                imageView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(sendDataToWidget.getThumbnailURL()).into(imageView);
            }
            if (sendDataToWidget.getAuthor() == null) {
                mAuthor.setVisibility(View.GONE);
            } else {
                mAuthor.setText(sendDataToWidget.getAuthor());
            }
            if (sendDataToWidget.getText() == null) {
                mText.setText(View.GONE);
            } else {
                mText.setText(sendDataToWidget.getText());
            }


            if (sendDataToWidget.getGroup() == null) {
                mGroup.setVisibility(View.GONE);
            } else {
                mGroup.setText(sendDataToWidget.getGroup());
            }
            if (sendDataToWidget.getUpdated() == null) {

                mDate.setVisibility(View.GONE);
            } else {
                mDate.setText(sendDataToWidget.getUpdated());
            }

            if (sendDataToWidget.getYtLinks() == null) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                mPlayerView.initialize(YouTubeConfig.getAPI(), mInitialize);
            }
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mAdapter = new CommentsViewAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);


    }


    private String ytLinks(List<String> links) {
        String list = null;
        if (links.get(0) != null) {

            for (int x = 0; x < links.size(); x++) {
                int index = links.get(x).indexOf("v=");

                String link;
                if (index == -1)

                {
                    link = links.get(x).substring(17);

                } else {
                    link = links.get(x).substring(index + 2);

                }

                list = link;
            }
        }

        return list;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.linkTV)
    public void sendLink() {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra(NAMEOFLINK, linkPost);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.arrowBackComments)
    public void clickBac() {
        finishAfterTransition();


    }


    @OnClick(R.id.image_button_fav)
    public void clickImageButton() {

        if (mButton.isSelected()) {
            mButton.setSelected(false);
        } else {
            mButton.setSelected(true);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(BUTTON, mButton.isSelected());
        bundle.putSerializable(COMMENTS, (Serializable) respon);
        bundle.putInt(POSITION, mRecyclerView.getVerticalScrollbarPosition());
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (isFinishing()) {
            Intent intent = new Intent("MainActivity");
            if (post != null) {
                intent.putExtra(MAIN, post);
                intent.putExtra(IF_IS, getIntent().getBooleanExtra(CHECK, false));
            } else if (mBooked != null) {
                intent.putExtra(MAIN, mBooked);
                intent.putExtra(IF_IS, getIntent().getBooleanExtra(CHECK, false));
            } else {
                intent.putExtra(MAIN, sendDataToWidget);
                intent.putExtra(WIDGET, true);
            }
            intent.putExtra(CHECK, mButton.isSelected());
            Log.i(TAG, "onDestroy: " + mButton.isSelected());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }
    }


    private boolean checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }
}



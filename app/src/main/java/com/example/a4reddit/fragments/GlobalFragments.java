package com.example.a4reddit.fragments;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a4reddit.Client.RetrofitClientInstance;
import com.example.a4reddit.DetailActivity;
import com.example.a4reddit.R;
import com.example.a4reddit.ViewModel.AnyRespondViewHolder;
import com.example.a4reddit.ViewModel.BookedPostVIewModel;
import com.example.a4reddit.adapter.GlobalRecyclerViewAdapter;
import com.example.a4reddit.data.APIfeed;
import com.example.a4reddit.data.model.Feed;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.database.BookedPost;
import com.example.a4reddit.interfaces.GlobalAdapToFrag;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by George Kimutai on 3/29/2019.
 */


public class GlobalFragments extends Fragment implements GlobalAdapToFrag {
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.global_RecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.search_edit_text)
    EditText editText;
    @BindView(R.id.search_button)
    ImageButton imageButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.linearLine)
    LinearLayout linearLayout;
    @BindView(R.id.arrowBack)
    ImageView imageView;
    @BindView(R.id.main_toolbar)
    Toolbar main_toolbar;
    @BindView(R.id.mLinearGlobal)
    LinearLayout mLinearGlobal;
    private final String ifOpen = "ifOpen";
    private MenuItem menuItem;
    private GlobalRecyclerViewAdapter mAdapter;
    private static final String TAG = "GlobalFragment";
    public static final String BASE_URI = "https://www.reddit.com/r/";
    private AnyRespondViewHolder anyR;
    private RetrofitClientInstance retrofit;
    private String input = "popular";
    private boolean checkIfVis = true;
    private List<BookedPost> bookedPs;
    public static final String NAME = "Global";
    public static final String INPUT = "input";
    boolean setVisible = true;
    public static final String LIST_OF_ROOM = "listOfRoom";


    public GlobalFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        anyR = ViewModelProviders.of(this).get(AnyRespondViewHolder.class);
        anyR.getEntryList().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List posts) {

                mAdapter = new GlobalRecyclerViewAdapter(getContext(), GlobalFragments.this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.takePost(posts);
                progressBar.setVisibility(View.GONE);


            }
        });
        BookedPostVIewModel book = ViewModelProviders.of(this).get(BookedPostVIewModel.class);
        book.getmBookedPost().observe(this, new Observer<List<BookedPost>>() {


            @Override
            public void onChanged(@Nullable List<BookedPost> bookedPosts) {
                bookedPs = bookedPosts;

            }
        });



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_global_fragments, container, false);
        //textView = view.findViewById(R.id.text);
        ButterKnife.bind(this, view);


        if (savedInstanceState != null) {
            boolean check = savedInstanceState.getBoolean(ifOpen);
            if (check) {
                checkIfVis = check;

            }
            if (savedInstanceState.getString(INPUT) != null) {
                linearLayout.setVisibility(View.VISIBLE);
                editText.setText(savedInstanceState.getString(INPUT));
                setVisible = false;
            }
        } else {
            input = getString(R.string.POPULAR);
            loadEntryList(input);
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(main_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        retrofit = new RetrofitClientInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                menuItem.setVisible(true);
                checkIfVis = true;
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                menuItem.setVisible(true);
                checkIfVis = true;
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = editText.getText().toString();
                if (editText != null && !input.isEmpty()) {

                    loadEntryList(input);
                    editText.setText("");
                    progressBar.setVisibility(View.VISIBLE);

                    View view1 = getActivity().getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }


                }
            }
        });






        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String new_feed = input + getString(R.string.NEW);

                checkNet();
                loadEntryList(new_feed);
                Toast.makeText(getContext(),R.string.OnRefresh,Toast.LENGTH_LONG).show();
                swiperefresh.setRefreshing(false);


            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nav_menu, menu);
        menuItem = menu.findItem(R.id.search_action);
        super.onCreateOptionsMenu(menu, inflater);
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_action:
                menuItem.setVisible(false);
                linearLayout.setVisibility(View.VISIBLE);
                checkIfVis = false;
                return true;

            case R.id.newButton:

                    Log.d(TAG,"onClick : input: new :" + input );
                    progressBar.setVisibility(View.VISIBLE);
                    String newInput = input + getString(R.string.NEW);
                    Log.d(TAG,"onClick : newInput:" + newInput );
                    loadEntryList(newInput);
                    return true;


            case R.id.topButton:

                Log.d(TAG,"onClick : input : top :" + input );
                progressBar.setVisibility(View.VISIBLE);

                String topInput = input + getString(R.string.TOP);
                Log.d(TAG,"onClick : topInput:" + topInput );
                loadEntryList(topInput);
                return true;




            case R.id.hotButton:


                Log.d(TAG,"onClick : input : hot :" + input );
                progressBar.setVisibility(View.VISIBLE);
                String hotInput = input + getString(R.string.HOT);
                Log.d(TAG,"onClick : hotInput:" + hotInput );

                loadEntryList(hotInput);
                return true;



            case R.id.SearchPopularButton:


                progressBar.setVisibility(View.VISIBLE);
                String popularInput = input;

                loadEntryList(popularInput);



                return true;


        }

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem register = menu.findItem(R.id.search_action);
        if (!checkIfVis || !setVisible) {
            register.setVisible(false);
        } else {
            register.setVisible(true);
        }


    }


    private void loadEntryList(final String input) {
        APIfeed service = RetrofitClientInstance.getInstance(BASE_URI).create(APIfeed.class);
        Call<Feed> feedCall = service.listFeed(input);
        feedCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<Post> respon;
                try {
                    respon = retrofit.fetchDataFromXML(response.body().getEntires());
                } catch (NullPointerException e) {
                    respon = null;
                }
                try {
                    if (respon == null) {
                        Toast.makeText(getContext(), R.string.Input_Is_Wrong, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        anyR.entryList.setValue(respon);
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG,"onResponse : " + respon);
                    }
                }catch (NullPointerException e){
                    Log.d(TAG,"NullPointerException : " + e.getMessage());


                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (!checkNet()) {
                    Snackbar snackbar = Snackbar.make(mLinearGlobal, R.string.CheckConnection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.Retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    loadEntryList(input);

                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            });
                    snackbar.show();
                }

            }
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean(ifOpen, checkIfVis);
        if (!editText.getText().toString().isEmpty()) {
            outState.putString(INPUT, editText.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void toFragment(Post post) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(NAME, post);
        intent.putExtra(LIST_OF_ROOM, (Serializable) bookedPs);
        getActivity().startActivity(intent, options.toBundle());


    }

    private boolean checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }


}


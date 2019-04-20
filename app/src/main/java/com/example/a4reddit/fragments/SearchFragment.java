package com.example.a4reddit.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a4reddit.Client.RetrofitClientInstance;
import com.example.a4reddit.R;
import com.example.a4reddit.ViewModel.AddSubModel;
import com.example.a4reddit.ViewModel.AnyRespondViewHolder;
import com.example.a4reddit.ViewModel.SubListViewModel;
import com.example.a4reddit.adapter.SearchRecyclerviewAdapter;
import com.example.a4reddit.data.APIfeed;
import com.example.a4reddit.data.model.Feed;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.database.SubModel;
import com.example.a4reddit.interfaces.BackPress;

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
public class SearchFragment extends Fragment implements BackPress {
    @BindView(R.id.searchRecycler)
    RecyclerView recyclerView;
    @BindView(R.id.progressSearch)
    ProgressBar progresBar;
    @BindView(R.id.SearchbuttonContainer)
    LinearLayout linearLayout;
    @BindView(R.id.SearchPopularButton)
    Button popularButton;
    @BindView(R.id.SearchNewButton)
    Button newButton;
    @BindView(R.id.SearchDefaultButton)
    Button defaultButton;
    @BindView(R.id.mLineaLayoutSearch)
    LinearLayout mLinear;
    @BindView(R.id.main_toolbar)
    Toolbar main_toolbar;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefresh;
    private MenuItem menuItem;
    private static final String TAG = "SearchFragment";
    private AnyRespondViewHolder respondViewHolder;
    private RetrofitClientInstance retrofit;
    private SearchRecyclerviewAdapter mAdapter;
    private String input = "default";

    private SubListViewModel subListViewModel;
    private AddSubModel addSubModel;

    public SearchFragment() {

        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addSubModel = ViewModelProviders.of(this).get(AddSubModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SearchRecyclerviewAdapter(getContext(), new ArrayList<SubModel>(), SearchFragment.this);

        respondViewHolder = ViewModelProviders.of(this).get(AnyRespondViewHolder.class);
        respondViewHolder.getSubsList().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List posts) {
                recyclerView.setAdapter(mAdapter);
                mAdapter.takeList(posts);

                progresBar.setVisibility(View.GONE);
            }
        });
        setToolBar();


        subListViewModel = ViewModelProviders.of(this).get(SubListViewModel.class);
        subListViewModel.getNameOfGroup().observe(this, new Observer<List<SubModel>>() {
            @Override
            public void onChanged(@Nullable List<SubModel> subModels) {
                mAdapter.addGropu(subModels);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                input = "default";
                loadSubscr(input);
                Toast.makeText(getContext(),R.string.OnRefresh,Toast.LENGTH_LONG).show();
                swipeRefresh.setRefreshing(false);


            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresBar.setVisibility(View.VISIBLE);
                input = "new";
                loadSubscr(input);
            }
        });
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresBar.setVisibility(View.VISIBLE);
                input = "default";
                loadSubscr(input);
            }
        });

        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresBar.setVisibility(View.VISIBLE);
                input = "popular";
                loadSubscr(input);
            }
        });
        if (savedInstanceState == null) {
            progresBar.setVisibility(View.VISIBLE);
            loadSubscr(input);

        }
        init();
        setToolBar();


        return view;

    }


    private void loadSubscr(final String input) {
        String URL_BASE = "https://www.reddit.com/subreddits/";
        APIfeed service = RetrofitClientInstance.getRetrofitInstance(URL_BASE).create(APIfeed.class);
        Call<Feed> feedCall = service.getSubs(input);
        feedCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<Post> respon;
                try {
                    respon = retrofit.fetchDataFromXML(response.body().getEntires());
                } catch (NullPointerException e) {
                    respon = null;
                }

                if (respon == null) {
                    Toast.makeText(getContext(), R.string.Input_Is_Wrong, Toast.LENGTH_SHORT).show();
                    progresBar.setVisibility(View.GONE);
                } else {
                    progresBar.setVisibility(View.VISIBLE);
                    respondViewHolder.subsList.setValue(respon);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                progresBar.setVisibility(View.GONE);
                if (!checkNet()) {
                    Snackbar snackbar = Snackbar.make(mLinear, R.string.CheckConnection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.Retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loadSubscr(input);
                                    progresBar.setVisibility(View.VISIBLE);
                                }
                            });
                    snackbar.show();
                }

            }
        });
    }


    private void init() {
        retrofit = new RetrofitClientInstance();
        loadSubscr(input);

    }

    public void setToolBar(){

        ((AppCompatActivity)getActivity()).setSupportActionBar(main_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.subreddit);





    }



    @Override
    public void startNewActivity(String gropuName, View view, boolean ifChecked) {


        if (!ifChecked) {
            SubModel subModel = (SubModel) view.getTag();
            subListViewModel.deleteItem(subModel);


        } else {
            addSubModel.addSub(new SubModel(gropuName));

        }


    }

    private boolean checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }
}


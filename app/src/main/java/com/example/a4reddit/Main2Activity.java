package com.example.a4reddit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by George Kimutai on 3/29/2019.
 */
public class Main2Activity extends AppCompatActivity {
    @BindView(R.id.LinearLine2)
    LinearLayout mLiner;
    @BindView(R.id.webView)
    WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra(DetailActivity.NAMEOFLINK);
        if (checkNet()) {
            initializeWebView();
        } else {
            Snackbar snackbar = Snackbar.make(mLiner, "Check Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initializeWebView();
                        }
                    });
            snackbar.show();
        }


    }

    private class MyBrowser extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(String.valueOf(request.getUrl()));
            return true;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView() {
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @OnClick(R.id.arrowBackWebView)
    public void clickBac() {
        onBackPressed();
    }

    private boolean checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }
}



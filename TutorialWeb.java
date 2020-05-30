package com.lukosmeet.dateapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import im.delight.android.webview.AdvancedWebView;

public class TestActivity extends AppCompatActivity  implements AdvancedWebView.Listener {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  Intent intent = getIntent();
      //  String url = intent.getStringExtra("url");
      //  Log.d("TESTACTIVITY", url);


        setContentView(R.layout.activity_test);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(((AdvancedWebView)findViewById(R.id.test)), true);
        } else CookieManager.getInstance().setAcceptCookie(true);

        ((AdvancedWebView)findViewById(R.id.test)).setCookiesEnabled(true);
        ((AdvancedWebView)findViewById(R.id.test)).getSettings().setJavaScriptEnabled(true);
        ((AdvancedWebView)findViewById(R.id.test)).getSettings().setAllowContentAccess(true);
        ((AdvancedWebView)findViewById(R.id.test)).getSettings().setDomStorageEnabled(true);

        ((AdvancedWebView)findViewById(R.id.test)).setListener(this, this);
        if (savedInstanceState != null) {
            ((AdvancedWebView)findViewById(R.id.test)).restoreState(savedInstanceState);
            ((AdvancedWebView)findViewById(R.id.test)).setVisibility(View.VISIBLE);
        } else ((AdvancedWebView)findViewById(R.id.test)).loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((AdvancedWebView)findViewById(R.id.test)).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if(((AdvancedWebView)findViewById(R.id.test)).canGoBack()) ((AdvancedWebView)findViewById(R.id.test)).goBack(); else super.onBackPressed();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((AdvancedWebView)findViewById(R.id.test)).saveState(outState);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
        findViewById(R.id.test).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

        startActivity(new Intent(this, MainActivity.class));
        this.finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long testLength, String testDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}


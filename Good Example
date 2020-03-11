package com.football.cal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import im.delight.android.webview.AdvancedWebView;

public class TestActivity extends AppCompatActivity  {
    WebView test;


    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_CHOOSE = 2;
    public static String URL_STRING = ;
    private static String file_type     = image;     file types to be allowed for upload
    private boolean multiple_files      = true;          allowing multiple file upload
    private String cam_file_data = null;         for storing camera file information
    private ValueCallbackUri file_data;        dataheader received after file selection
    private ValueCallbackUri[] file_path;      received file(s) temp. location
    private final static int file_req_code = 1;

    @SuppressLint(SetJavaScriptEnabled)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test = findViewById(R.id.test);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        if (android.os.Build.VERSION.SDK_INT = 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(test, true);
        } else CookieManager.getInstance().setAcceptCookie(true);

        test.setWebChromeClient(new WebChromeClient(){

            -- handling input[type=file] requests for android API 21+ --
            public boolean onShowFileChooser(WebView webView, ValueCallbackUri[] filePathCallback, FileChooserParams fileChooserParams) {

                if(file_permission() && Build.VERSION.SDK_INT = 21) {
                    file_path = filePathCallback;
                    Intent takePictureIntent = null;
                    Intent takeVideoIntent = null;

                    boolean includeVideo = false;
                    boolean includePhoto = false;

                    -- checking the accept parameter to determine which intent(s) to include --
                    paramCheck
                    for (String acceptTypes  fileChooserParams.getAcceptTypes()) {
                        String[] splitTypes = acceptTypes.split(, +);  although it's an array, it still seems to be the whole value; split it out into chunks so that we can detect multiple values
                        for (String acceptType  splitTypes) {
                            switch (acceptType) {
                                case 
                                    includePhoto = true;
                                    includeVideo = true;
                                    break paramCheck;
                                case image
                                    includePhoto = true;
                                    break;
                                case video
                                    includeVideo = true;
                                    break;
                            }
                        }
                    }

                    if (fileChooserParams.getAcceptTypes().length == 0) {   no `accept` parameter was specified, allow both photo and video
                        includePhoto = true;
                        includeVideo = true;
                    }

                    if (includePhoto) {
                        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(TestActivity.this.getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = create_image();
                                takePictureIntent.putExtra(PhotoPath, cam_file_data);
                            } catch (IOException ex) {
                                Log.e(DEV, Image file creation failed, ex);
                            }
                            if (photoFile != null) {
                                cam_file_data = file + photoFile.getAbsolutePath();
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            } else {
                                cam_file_data = null;
                                takePictureIntent = null;
                            }
                        }
                    }

                    if (includeVideo) {
                        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(TestActivity.this.getPackageManager()) != null) {
                            File videoFile = null;
                            try {
                                videoFile = create_video();
                            } catch (IOException ex) {
                                Log.e(DEV, Video file creation failed, ex);
                            }
                            if (videoFile != null) {
                                cam_file_data = file + videoFile.getAbsolutePath();
                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                            } else {
                                cam_file_data = null;
                                takeVideoIntent = null;
                            }
                        }
                    }

                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType(file_type);
                    if (multiple_files) {
                        contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }

                    Intent[] intentArray;
                    if (takePictureIntent != null && takeVideoIntent != null) {
                        intentArray = new Intent[]{takePictureIntent, takeVideoIntent};
                    } else if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else if (takeVideoIntent != null) {
                        intentArray = new Intent[]{takeVideoIntent};
                    } else {
                        intentArray = new Intent[0];
                    }

                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, File chooser);
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, file_req_code);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                callback.onCustomViewHidden();
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
            }
        });
        test.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                startActivity(new Intent(TestActivity.this,MainActivity.class));
                TestActivity.this.finish();
                overridePendingTransition(0,0);
            }

            @Override
            @TargetApi(23)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(http)  url.startsWith(https)) {
                    view.loadUrl(url);
                    return true;
                }

                 Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                CookieSyncManager.getInstance().sync();
                test.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });
        test.getSettings().setSupportMultipleWindows(true);
        test.getSettings().setJavaScriptEnabled(true);
        test.getSettings().setAllowContentAccess(true);
        test.getSettings().setDomStorageEnabled(true);
        test.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        test.getSettings().setBuiltInZoomControls(true);
        test.getSettings().setUseWideViewPort(true);
        test.getSettings().setAppCacheEnabled(true);

        test.getSettings().setAllowFileAccess(true);
        test.getSettings().setPluginState(WebSettings.PluginState.ON);

        test.requestFocus(View.FOCUS_DOWNView.FOCUS_UP);
        test.getSettings().setLightTouchEnabled(true);
        test.setOnTouchListener((v, event) - {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN
                case MotionEvent.ACTION_UP
                    if (!v.hasFocus())
                    {
                        v.requestFocus();
                    }
                    break;
            }
            return false;
        });


        if (savedInstanceState != null) {
            test.restoreState(savedInstanceState);
            test.setVisibility(View.VISIBLE);
        } else test.loadUrl(getIntent().getStringExtra(test));
    }



    @Override
    public void onBackPressed() {
        if(test.canGoBack()) test.goBack(); else super.onBackPressed();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        test.saveState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT = 21){
            Uri[] results = null;

            -- if file request cancelled; exited camera. we need to send null value to make future attempts workable --
            if (resultCode == Activity.RESULT_CANCELED) {
                if (requestCode == file_req_code) {
                    file_path.onReceiveValue(null);
                    return;
                }
            }

            -- continue if response is positive --
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == file_req_code){
                    if(null == file_path){
                        return;
                    }

                    ClipData clipData;
                    String stringData;
                    try {
                        clipData = intent.getClipData();
                        stringData = intent.getDataString();
                    }catch (Exception e){
                        clipData = null;
                        stringData = null;
                    }

                    if (clipData == null && stringData == null && cam_file_data != null) {
                        results = new Uri[]{Uri.parse(cam_file_data)};
                    }else{
                        if (clipData != null) {  checking if multiple files selected or not
                            final int numSelectedFiles = clipData.getItemCount();
                            results = new Uri[numSelectedFiles];
                            for (int i = 0; i  clipData.getItemCount(); i++) {
                                results[i] = clipData.getItemAt(i).getUri();
                            }
                        } else {
                            results = new Uri[]{Uri.parse(stringData)};
                        }
                    }
                }
            }
            file_path.onReceiveValue(results);
            file_path = null;
        }else{
            if(requestCode == file_req_code){
                if(null == file_data) return;
                Uri result = intent == null  resultCode != RESULT_OK  null  intent.getData();
                file_data.onReceiveValue(result);
                file_data = null;
            }
        }
    }
    public boolean file_permission(){
        if(Build.VERSION.SDK_INT =23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(TestActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }else{
            return true;
        }
    }

    -- creating new image file here --
    private File create_image() throws IOException {
        @SuppressLint(SimpleDateFormat) String timeStamp = new SimpleDateFormat(yyyyMMdd_HHmmss).format(new Date());
        String imageFileName = img_+timeStamp+_;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,.jpg,storageDir);
    }

    -- creating new video file here --
    private File create_video() throws IOException {
        @SuppressLint(SimpleDateFormat)
        String file_name    = new SimpleDateFormat(yyyy_mm_ss).format(new Date());
        String new_name     = file_+file_name+_;
        File sd_directory   = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(new_name, .3gp, sd_directory);
    }


}


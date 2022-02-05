package com.example.hajj_fyp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;

public class BasicGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_guide);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Hajj Week Guide");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        WebView webView = findViewById(R.id.webview_hajjguide);
        webView.loadUrl("file:///android_asset/test.html");
    }
}
package com.example.hajj_fyp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<FAQ_Model> faq_modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqactivity);

        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("FAQ");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycleView);

        initData();
        setRecyclerview();
    }

    private void setRecyclerview() {
        FAQ_Adapter faq_adapter = new FAQ_Adapter(faq_modelList);
        recyclerView.setAdapter(faq_adapter);
        recyclerView.setHasFixedSize(true);
    }
    private void initData() {

        faq_modelList = new ArrayList<>();

        faq_modelList.add(new FAQ_Model("Android 10","best anseoiasajdbn aksndjas ljans"));
        faq_modelList.add(new FAQ_Model("Android 10","best anseoiasajdbn aksndjas ljans"));
        faq_modelList.add(new FAQ_Model("Android 10","best anseoiasajdbn aksndjas ljans"));
        faq_modelList.add(new FAQ_Model("Android 10","best anseoiasajdbn aksndjas ljans"));
        faq_modelList.add(new FAQ_Model("Android 10","best anseoiasajdbn aksndjas ljans"));
        faq_modelList.add(new FAQ_Model("Android 10","best anseoiasajdbn aksndjas ljans"));
    }
}
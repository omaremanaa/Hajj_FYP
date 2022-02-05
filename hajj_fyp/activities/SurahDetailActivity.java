package com.example.hajj_fyp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hajj_fyp.MenuActivity;
import com.example.hajj_fyp.R;
import com.example.hajj_fyp.adapter.SurahDetailAdapter;
import com.example.hajj_fyp.common.Common;
import com.example.hajj_fyp.model.SurahDetail;
import com.example.hajj_fyp.viewmodel.SurahDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class SurahDetailActivity extends AppCompatActivity {
    private TextView surahName, surahType, surahTranslation;
    private int no;
    private RecyclerView recyclerView;
    private List<SurahDetail> list;
    private SurahDetailAdapter adapter;
    private SurahDetailViewModel surahDetailViewModel;
    private String english = "english_hilali_khan";
    private String indonesia = "indonesian_affairs";
    private ImageButton refereshhh;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_detail);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Surah");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);

        surahName = findViewById(R.id.surah_name);
        surahType = findViewById(R.id.type);
        surahTranslation = findViewById(R.id.translation);
        recyclerView = findViewById(R.id.surah_detail_rv);

        no = getIntent().getIntExtra(Common.SURAH_NO,0);
        surahName.setText(getIntent().getStringExtra(Common.SURAH_NAME));
        surahType.setText(getIntent().getStringExtra(Common.SURAH_TYPE)+" "+
                getIntent().getIntExtra(Common.SURAH_TOTAL_AYA,0)+" AYA");
        surahTranslation.setText(getIntent().getStringExtra(Common.SURAH_TRANSLATION));

        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();

        surahTranslation(english,no);

    }

    private void surahTranslation(String lan, int id) {
        if(list.size()>0){
            list.clear();
        }

        surahDetailViewModel = new ViewModelProvider(this).get(SurahDetailViewModel.class);
        surahDetailViewModel.getSurahDetail(lan,id).observe(this, surahDetailResponse -> {


            for (int i = 0 ;i<surahDetailResponse.getList().size();i++){
                list.add(new SurahDetail(surahDetailResponse.getList().get(i).getId(),
                        surahDetailResponse.getList().get(i).getSura(),
                        surahDetailResponse.getList().get(i).getAya(),
                        surahDetailResponse.getList().get(i).getArabic_text(),
                        surahDetailResponse.getList().get(i).getTranslation(),
                        surahDetailResponse.getList().get(i).getFootnotes()));
            }

            if (list.size()!=0){
                adapter = new SurahDetailAdapter(this,list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
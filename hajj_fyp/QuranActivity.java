package com.example.hajj_fyp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.hajj_fyp.activities.SurahDetailActivity;
import com.example.hajj_fyp.adapter.SurahAdapter;
import com.example.hajj_fyp.common.Common;
import com.example.hajj_fyp.listener.SurahListener;
import com.example.hajj_fyp.model.Surah;
import com.example.hajj_fyp.viewmodel.SurahViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuranActivity extends AppCompatActivity implements SurahListener {
    private RecyclerView recyclerView;
    private SurahAdapter surahAdapter;
    private List<Surah> list;
    private SurahViewModel surahViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Quran");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        recyclerView = findViewById(R.id.surahRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        surahViewModel = new ViewModelProvider(this).get(SurahViewModel.class);

        surahViewModel.getSurah().observe(this,surahResponse -> {
            for(int i = 0;i<surahResponse.getList().size();i++){
                list.add(new Surah(surahResponse.getList().get(i).getNumber(),
                        String.valueOf(surahResponse.getList().get(i).getEnglishName()),
                        String.valueOf(surahResponse.getList().get(i).getName()),
                        String.valueOf(surahResponse.getList().get(i).getEnglishNameTranslation()),
                        surahResponse.getList().get(i).getNumberOfAyahs(),
                        String.valueOf(surahResponse.getList().get(i).getRevelationType())
                ));
            }

            if (list.size()!=0){
                surahAdapter = new SurahAdapter(this,list,this::onSurahListener);
                recyclerView.setAdapter(surahAdapter);
                surahAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onSurahListener(int position) {
        Intent intent = new Intent(this, SurahDetailActivity.class);
        intent.putExtra(Common.SURAH_NO,list.get(position).getNumber());
        intent.putExtra(Common.SURAH_NAME,list.get(position).getName());
        intent.putExtra(Common.SURAH_TOTAL_AYA,list.get(position).getNumberOfAyahs());
        intent.putExtra(Common.SURAH_TYPE,list.get(position).getRevelationType());
        intent.putExtra(Common.SURAH_TRANSLATION,list.get(position).getEnglishNameTranslation());
        startActivity(intent);
    }
}
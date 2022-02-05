package com.example.hajj_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hajj_fyp.activities.SurahDetailActivity;
import com.example.hajj_fyp.adapter.SurahAdapter;
import com.example.hajj_fyp.common.Common;
import com.example.hajj_fyp.listener.SurahListener;
import com.example.hajj_fyp.model.Surah;
import com.example.hajj_fyp.viewmodel.SurahViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuranFragment extends Fragment implements SurahListener {
    private RecyclerView recyclerView;
    private SurahAdapter surahAdapter;
    private List<Surah> list;
    private SurahViewModel surahViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quran, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        recyclerView = v.findViewById(R.id.surahRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        surahViewModel = new ViewModelProvider(this).get(SurahViewModel.class);

        surahViewModel.getSurah().observe(getActivity(),surahResponse -> {
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
                surahAdapter = new SurahAdapter(getContext(),list,this::onSurahListener);
                recyclerView.setAdapter(surahAdapter);
                surahAdapter.notifyDataSetChanged();

            }
        });
        return v;

    }

    @Override
    public void onSurahListener(int position) {
        Intent intent = new Intent(getActivity(), SurahDetailActivity.class);
        intent.putExtra(Common.SURAH_NO,list.get(position).getNumber());
        intent.putExtra(Common.SURAH_NAME,list.get(position).getName());
        intent.putExtra(Common.SURAH_TOTAL_AYA,list.get(position).getNumberOfAyahs());
        intent.putExtra(Common.SURAH_TYPE,list.get(position).getRevelationType());
        intent.putExtra(Common.SURAH_TRANSLATION,list.get(position).getEnglishNameTranslation());
        startActivity(intent);
    }
}

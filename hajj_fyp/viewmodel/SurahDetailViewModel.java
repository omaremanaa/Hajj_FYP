package com.example.hajj_fyp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hajj_fyp.repository.SurahDetailRepo;
import com.example.hajj_fyp.response.SurahDetailResponse;

public class SurahDetailViewModel extends ViewModel {

    private SurahDetailRepo surahDetailRepo;

    public SurahDetailViewModel(){
        surahDetailRepo = new SurahDetailRepo();
    }

    public LiveData<SurahDetailResponse> getSurahDetail(String lan, int id){
        return surahDetailRepo.getSurahDetail(lan, id);
    }
}

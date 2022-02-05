package com.example.hajj_fyp.network;

import com.example.hajj_fyp.response.SurahDetailResponse;
import com.example.hajj_fyp.response.SurahResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @GET("surah")
    Call<SurahResponse> getSurah();

    @GET("sura/{language}/{id}")
    Call<SurahDetailResponse> getSurahDetail(@Path("language")String lan,
                                             @Path("id")int surahId);
}

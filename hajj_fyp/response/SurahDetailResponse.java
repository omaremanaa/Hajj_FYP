package com.example.hajj_fyp.response;

import com.example.hajj_fyp.model.SurahDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurahDetailResponse {

    @SerializedName("result")
    private List<SurahDetail> list;

    public List<SurahDetail> getList() {
        return list;
    }

    public void setList(List<SurahDetail> list) {
        this.list = list;
    }
}

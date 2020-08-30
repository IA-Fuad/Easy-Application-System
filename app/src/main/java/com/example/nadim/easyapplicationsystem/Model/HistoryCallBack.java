package com.example.nadim.easyapplicationsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HistoryCallBack {

    @SerializedName("history")
    private ArrayList<FullApplication> fullApplications;

    public ArrayList<FullApplication> getFullApplications() {
        return fullApplications;
    }

}

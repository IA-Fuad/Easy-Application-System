package com.example.nadim.easyapplicationsystem;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiImplementation {

    private API api;
    private boolean flag;
    Retrofit retrofit;



   ApiImplementation(){

       String baseURL1 = "http://139.59.47.82:5000/";
       String baseURL2 = "https://feedback-server-dmbsmtksou.now.sh/";
       retrofit = new Retrofit.Builder().baseUrl(baseURL1).
                addConverterFactory(GsonConverterFactory.create()).build();

        api = retrofit.create(API.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}

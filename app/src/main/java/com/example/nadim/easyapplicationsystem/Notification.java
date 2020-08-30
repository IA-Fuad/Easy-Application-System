package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.FullApplication;
import com.example.nadim.easyapplicationsystem.Model.HistoryCallBack;
import com.example.nadim.easyapplicationsystem.Model.NotificationCallBack;
import com.example.nadim.easyapplicationsystem.Service.MyService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Notification extends AppCompatActivity {

    private ListView allNotificatoin;
    private API api;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayList<String> arrayList, fullApplication, applicationID;
    private int i;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        allNotificatoin = findViewById(R.id.allNotification);

        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        applicationID = new ArrayList<>();
        Gson gson = new Gson();
        if(sharedPreferences.contains("appl_id")){

            String appid = sharedPreferences.getString("appl_id", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            applicationID = gson.fromJson(appid, type);
        }


        // Toast.makeText(this, ""+appid, Toast.LENGTH_SHORT).show();

        // Toast.makeText(this, ""+sharedPreferences.getString("appl_id", ""), Toast.LENGTH_LONG).show();

        Call<NotificationCallBack> call;
        arrayList = new ArrayList<>();
        fullApplication = new ArrayList<>();

        if (applicationID.size() == 0){
            progressDialog.dismiss();
        }
//        Toast.makeText(this, ""+MyService.applicationID.size(), Toast.LENGTH_LONG).show();
        for (i=0; i<applicationID.size(); i++){


            String application_id = applicationID.get(i);
            Log.d("app_id", application_id);

            if(sharedPreferences.getString("user_type", "").equals("student")){
                call = api.getStudentNotification(application_id);
            }
            else{
                call = api.getFacultyNotification(application_id);
            }
            //  Toast.makeText(this, ""+application_id, Toast.LENGTH_SHORT).show();

            call.enqueue(new Callback<NotificationCallBack>() {
                @Override
                public void onResponse(Call<NotificationCallBack> call, Response<NotificationCallBack> response) {

                    if(!response.isSuccessful()){
                        Toast.makeText(Notification.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String title = response.body().getApplication().getApplication().getTitle();
                    int status = response.body().getApplication().getStatus();
                    arrayList.add(title+status);
                    fullApplication.add(response.body().getApplication().getBody_reason()+
                            response.body().getApplication().getBody_request());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, arrayList);
                    allNotificatoin.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<NotificationCallBack> call, Throwable t) {

                    Toast.makeText(Notification.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            progressDialog.dismiss();
        }


        allNotificatoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ShowApplication.class);
                intent.putExtra("application", fullApplication.get(position));
                startActivity(intent);
                arrayList.remove(position);
                applicationID.remove(position);
                Gson gson = new Gson();
                String appid = gson.toJson(applicationID);
                editor.putString("appl_id",appid);
                editor.apply();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, arrayList);
                allNotificatoin.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(sharedPreferences.getString("user_type", "").equals("student")){
            finish();
            startActivity(new Intent(Notification.this, Student_Home_Page.class));
        }
        else{
            finish();
            startActivity(new Intent(Notification.this, Faculty_Home_Page.class));
        }

    }
}

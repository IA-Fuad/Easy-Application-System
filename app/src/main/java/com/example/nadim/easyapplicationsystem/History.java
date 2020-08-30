package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.HistoryCallBack;
import com.example.nadim.easyapplicationsystem.Model.FullApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class History extends AppCompatActivity {

    private API api;
    private ListView history;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> fullApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__page);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        history = findViewById(R.id.history);
        fullApplication = new ArrayList<>();
        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        // Toast.makeText(this, ""+sharedPreferences.getString("user_type", ""), Toast.LENGTH_SHORT).show();
        Call<HistoryCallBack> call;
        if(sharedPreferences.getString("user_type", "").equals("student")){
            //Toast.makeText(this, "st", Toast.LENGTH_SHORT).show();
            call = api.getStudentHistory(sharedPreferences.getString("id", ""));
        }
        else{
            // Toast.makeText(this, "fc", Toast.LENGTH_SHORT).show();
            call = api.getFacultyHistory(sharedPreferences.getString("id", ""));
        }

        call.enqueue(new Callback<HistoryCallBack>() {
            @Override
            public void onResponse(Call<HistoryCallBack> call, Response<HistoryCallBack> response) {

                progressDialog.dismiss();

                if(!response.isSuccessful()){
                    Toast.makeText(History.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<FullApplication> allHistory = response.body().getFullApplications();
                Toast.makeText(History.this, ""+allHistory.size(), Toast.LENGTH_SHORT).show();
                //String code = response.body().getStatusCode();
                ArrayList<String> application = new ArrayList<>();


                for (FullApplication n: allHistory){

                    String status;
                    if(n.getStatus() == 0) status = "Pending";
                    else if(n.getStatus() == 1) status = "Accepted";
                    else status = "Rejected";

                    fullApplication.add(n.getApplication().getValue()+"\n"+n.getBody_reason()+"\n"+
                            n.getBody_request());
                    application.add(n.getApplication().getValue()+"\n"+status);
                }

                Collections.reverse(application);
                Collections.reverse(fullApplication);
                //   Toast.makeText(History.this, ""+application.size(), Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, application);
                history.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<HistoryCallBack> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(History.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ShowApplication.class);
                intent.putExtra("application", fullApplication.get(position));
                startActivity(intent);
            }
        });
    }
}

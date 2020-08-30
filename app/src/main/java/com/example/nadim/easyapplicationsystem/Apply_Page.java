package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.ApplicationSubmitionCallBack;
import com.example.nadim.easyapplicationsystem.Model.StudentApplicationParameter;
import com.example.nadim.easyapplicationsystem.Model.StudentLoginCallBack;
import com.example.nadim.easyapplicationsystem.Service.MyService;
import com.google.firebase.FirebaseApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Apply_Page extends AppCompatActivity {

    private TextView application;
    private Button submit;
    private String user_id, app_type, body, request, requestBy;
    private API api;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply__page);

        application = findViewById(R.id.application);
        submit = findViewById(R.id.submit);

        app_type = getIntent().getStringExtra("applicationType");
        body = getIntent().getStringExtra("body");
        request = getIntent().getStringExtra("request");
        user_id = getIntent().getStringExtra("user_id");

        application.setText(super.getIntent().getExtras().getString("application"));

        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        //FirebaseApp.initializeApp(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // startService(new Intent(getApplicationContext(), MyService.class));

                final ProgressDialog progressDialog = new ProgressDialog(Apply_Page.this);
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                // Toast.makeText(Apply_Page.this, app_type+"\n"+body+"\n"+request+"\n"+user_id, Toast.LENGTH_LONG).show();

                Call<ApplicationSubmitionCallBack> call = api.submitApplication(sharedPreferences.getString("token", ""),
                        new StudentApplicationParameter(app_type, body, request, user_id));

                call.enqueue(new Callback<ApplicationSubmitionCallBack>() {
                    @Override
                    public void onResponse(Call<ApplicationSubmitionCallBack> call, Response<ApplicationSubmitionCallBack> response) {

                        progressDialog.dismiss();
                        if (!response.isSuccessful()){

                            Toast.makeText(Apply_Page.this, ""+response.raw(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(Apply_Page.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Apply_Page.this, WaitText.class));

                    }

                    @Override
                    public void onFailure(Call<ApplicationSubmitionCallBack> call, Throwable t) {

                        progressDialog.dismiss();
                        Toast.makeText(Apply_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }
}

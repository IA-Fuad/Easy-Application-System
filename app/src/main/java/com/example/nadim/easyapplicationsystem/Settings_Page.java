package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.StudentUpdateCallBack;
import com.example.nadim.easyapplicationsystem.Model.User_Student;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Settings_Page extends AppCompatActivity {

    private EditText name, section, password, contact;
    private Button save;
    private API api;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__page);

        name = findViewById(R.id.name);
        section = findViewById(R.id.section);
        password = findViewById(R.id.password);
        contact = findViewById(R.id.contactNumber);
        save = findViewById(R.id.save);

        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        name.setText(sharedPreferences.getString("studentName", ""));
        section.setText(sharedPreferences.getString("section", ""));
        contact.setText(sharedPreferences.getString("contactNumber", ""));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(Settings_Page.this);
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);

                editor.putString("studentName", name.getText().toString());
                editor.putString("section", section.getText().toString());
                editor.putString("contactNumber", contact.getText().toString());
                editor.apply();


                Call<StudentUpdateCallBack> call = api.studentUpdate(sharedPreferences.getString("id", ""),
                        name.getText().toString(), section.getText().toString(), Integer.parseInt(contact.getText().toString()),
                        password.getText().toString());

                progressDialog.show();

                call.enqueue(new Callback<StudentUpdateCallBack>() {
                    @Override
                    public void onResponse(Call<StudentUpdateCallBack> call, Response<StudentUpdateCallBack> response) {

                        progressDialog.dismiss();
                        if (!response.isSuccessful()){

                            Toast.makeText(Settings_Page.this, response.message(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Toast.makeText(Settings_Page.this, response.body().getUser().getFullname(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Settings_Page.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Settings_Page.this, Student_Home_Page.class));

                    }

                    @Override
                    public void onFailure(Call<StudentUpdateCallBack> call, Throwable t) {

                        progressDialog.dismiss();
                        Toast.makeText(Settings_Page.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}

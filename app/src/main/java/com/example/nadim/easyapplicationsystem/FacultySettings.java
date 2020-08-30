package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.FacultyUpdateCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FacultySettings extends AppCompatActivity {

    private EditText name, position, password, contact;
    private Button save;
    private API api;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_settings);

        name = findViewById(R.id.name);
        position = findViewById(R.id.position);
        password = findViewById(R.id.password);
        contact = findViewById(R.id.contactNumber);
        save = findViewById(R.id.save);

        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        name.setText(sharedPreferences.getString("facultyName", ""));
        position.setText(sharedPreferences.getString("facultyPosition", ""));
        contact.setText(sharedPreferences.getString("contactNumber", ""));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(FacultySettings.this);
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                editor.putString("facultyName", name.getText().toString());
                editor.putString("facultyPosition", position.getText().toString());
                editor.putString("contactNumber", contact.getText().toString());
                editor.apply();

                Call<FacultyUpdateCallBack> call = api.facultyUpdate(sharedPreferences.getString("id", ""),
                        name.getText().toString(), position.getText().toString(), Integer.parseInt(contact.getText().toString()),
                        password.getText().toString());

                call.enqueue(new Callback<FacultyUpdateCallBack>() {
                    @Override
                    public void onResponse(Call<FacultyUpdateCallBack> call, Response<FacultyUpdateCallBack> response) {

                        progressDialog.dismiss();
                        if(!response.isSuccessful()){

                            Toast.makeText(FacultySettings.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Toast.makeText(FacultySettings.this, ""+response.body().getUser().getFullname(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(FacultySettings.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(FacultySettings.this, Faculty_Home_Page.class));

                    }

                    @Override
                    public void onFailure(Call<FacultyUpdateCallBack> call, Throwable t) {

                        progressDialog.dismiss();
                        Toast.makeText(FacultySettings.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}

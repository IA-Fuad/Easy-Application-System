package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.FacultyInfoModel;
import com.example.nadim.easyapplicationsystem.Model.RegistrationCallBack;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Faculty_Sign_Up_Page extends AppCompatActivity {

    String[] departments,positons;
    Spinner spinner1,spinnerd,spinnerp;
    ArrayList<String> user_names;
    private EditText name, facultyID, email, password, confirmPassword, contactNumber;
    private API api;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty__sign__up__page);

        //user_names = getResources().getStringArray(R.array.user_names);
        user_names = new ArrayList<String>();
        user_names.add("Faculty");
        user_names.add("Student");
        departments = getResources().getStringArray(R.array.departments);
        positons = getResources().getStringArray(R.array.positions);

        spinner1 = findViewById(R.id.spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1){
                    finish();
                    startActivity(new Intent(Faculty_Sign_Up_Page.this, Student_Sign_Up_Page.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerd = findViewById(R.id.spinnerd);
        spinnerp = findViewById(R.id.spinnerp);
        name = findViewById(R.id.name);
        facultyID = findViewById(R.id.facultyID);
        email = findViewById(R.id.email);
        contactNumber = findViewById(R.id.contactNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signUp);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.sample_view,R.id.textViewSample1,user_names);
        spinner1.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSample2, departments);
        spinnerd.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSample3, positons);
        spinnerp.setAdapter(adapter);

        final Retrofit retrofit = new ApiImplementation().getRetrofit();

        api = retrofit.create(API.class);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });
    }

    private void register(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Call<RegistrationCallBack> call = api.facultyRegister(new FacultyInfoModel(name.getText().toString(), facultyID.getText().toString(),
                contactNumber.getText().toString(), spinnerd.getSelectedItem().toString(),
                password.getText().toString(), email.getText().toString(), spinnerp.getSelectedItem().toString()));

        call.enqueue(new Callback<RegistrationCallBack>() {
            @Override
            public void onResponse(Call<RegistrationCallBack> call, Response<RegistrationCallBack> response) {

                progressDialog.dismiss();

                if (!response.isSuccessful()) {
                    Toast.makeText(Faculty_Sign_Up_Page.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

              //  Toast.makeText(Faculty_Sign_Up_Page.this, "", Toast.LENGTH_SHORT).show();

                if(response.body().getStatus_code() == 401){

                    Toast.makeText(Faculty_Sign_Up_Page.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Faculty_Sign_Up_Page.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Faculty_Sign_Up_Page.this, Login_Page.class));

            }

            @Override
            public void onFailure(Call<RegistrationCallBack> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(Faculty_Sign_Up_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

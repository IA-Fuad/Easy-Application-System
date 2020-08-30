package com.example.nadim.easyapplicationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.RegistrationCallBack;
import com.example.nadim.easyapplicationsystem.Model.StudentInfoModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Student_Sign_Up_Page extends AppCompatActivity {

    private String[] user_names, departments, batches, sectionList;
    private Spinner spinner1, spinnerd, spinnerb, section;
    private API api;
    private EditText name, studentID, email, password, confirmPassword, contactNumber;
    private Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__sign__up__page);

        user_names = getResources().getStringArray(R.array.user_names);
        departments = getResources().getStringArray(R.array.departments);
        batches = getResources().getStringArray(R.array.batches);
        sectionList = getResources().getStringArray(R.array.section);

        spinner1 = findViewById(R.id.spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    startActivity(new Intent(Student_Sign_Up_Page.this, Faculty_Sign_Up_Page.class));
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerd = findViewById(R.id.spinnerd);
        spinnerb = findViewById(R.id.spinnerb);
        section = findViewById(R.id.section);
        name = findViewById(R.id.name);
        studentID = findViewById(R.id.studentID);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        contactNumber = findViewById(R.id.contactNumber);
        signUp = findViewById(R.id.signUp);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSample1, user_names);
        spinner1.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSample2, departments);
        spinnerd.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSample3, batches);
        spinnerb.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSample3, sectionList);
        section.setAdapter(adapter);


        final Retrofit retrofit = new ApiImplementation().getRetrofit();

        api = retrofit.create(API.class);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {

        if(name.getText().toString().isEmpty() || studentID.getText().toString().isEmpty() ||
        email.getText().toString().isEmpty() || contactNumber.getText().toString().isEmpty() ||
        password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()){

            Toast.makeText(this, "All field must be filled up", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString())){

            Toast.makeText(this, "Password Confirmation did not match with Password", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Call<RegistrationCallBack> call = api.studentRegister(new StudentInfoModel(name.getText().toString(), studentID.getText().toString(),
                contactNumber.getText().toString(), email.getText().toString(), spinnerd.getSelectedItem().toString(),
                spinnerb.getSelectedItem().toString(), section.getSelectedItem().toString(), password.getText().toString()));

        call.enqueue(new Callback<RegistrationCallBack>() {
            @Override
            public void onResponse(Call<RegistrationCallBack> call, Response<RegistrationCallBack> response) {

                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(Student_Sign_Up_Page.this, response.message(), Toast.LENGTH_LONG).show();
                    return;
                }


               // Toast.makeText(Student_Sign_Up_Page.this, "", Toast.LENGTH_SHORT).show();

                if(response.body().getStatus_code() == 401){

                    Toast.makeText(Student_Sign_Up_Page.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Student_Sign_Up_Page.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Student_Sign_Up_Page.this, Login_Page.class));

            }

            @Override
            public void onFailure(Call<RegistrationCallBack> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Student_Sign_Up_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

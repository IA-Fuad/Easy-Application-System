package com.example.nadim.easyapplicationsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.ApplicationSubmitionCallBack;
import com.example.nadim.easyapplicationsystem.Model.FacultyApplicationParameter;
import com.example.nadim.easyapplicationsystem.Service.MyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FacultyApplyPage extends AppCompatActivity {

    private String type, typeID, body, date1, date2, address, contact, _id, token;
    private TextView applicationType, reason, dateFirst, dateLast, leaveAddress, contactNumber;
    private Button submit;
    private API api;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_apply_page);

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        type = getIntent().getStringExtra("applicationType");
        typeID = getIntent().getStringExtra("applicationTypeID");
        body = getIntent().getStringExtra("reason");
        date1 = getIntent().getStringExtra("dateFirst");
        date2 = getIntent().getStringExtra("dateLast");
        address = getIntent().getStringExtra("leaveAddress");
        contact = getIntent().getStringExtra("contactNumber");
       // _id = getIntent().getStringExtra("facultyUserId");
        token = getIntent().getStringExtra("token");
        _id = sharedPreferences.getString("id", "");

        applicationType = findViewById(R.id.applicationType);
        reason = findViewById(R.id.reason);
        dateFirst = findViewById(R.id.dateFirst);
        dateLast = findViewById(R.id.dateLast);
        leaveAddress = findViewById(R.id.leaveAddress);
        contactNumber = findViewById(R.id.contactNumber);
        submit = findViewById(R.id.submit);

        applicationType.setText(type);
        reason.setText(body);
        dateFirst.setText(date1);
        dateLast.setText(date2);
        leaveAddress.setText(address);
        contactNumber.setText(contact);

        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  startService(new Intent(getApplicationContext(), MyService.class));

                Log.d("facultyId", _id);

                Call<ApplicationSubmitionCallBack> call = api.submitApplicationFaculty(token,new FacultyApplicationParameter(
                        typeID, body, date1, date2, address, contact, _id));

                call.enqueue(new Callback<ApplicationSubmitionCallBack>() {
                    @Override
                    public void onResponse(Call<ApplicationSubmitionCallBack> call, Response<ApplicationSubmitionCallBack> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(FacultyApplyPage.this, response.message(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(FacultyApplyPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(FacultyApplyPage.this, WaitText.class));

                    }

                    @Override
                    public void onFailure(Call<ApplicationSubmitionCallBack> call, Throwable t) {

                        Toast.makeText(FacultyApplyPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }
}

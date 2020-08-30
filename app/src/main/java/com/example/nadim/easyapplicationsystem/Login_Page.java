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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.FacultyLoginCallBack;
import com.example.nadim.easyapplicationsystem.Model.StudentInfoModel;
import com.example.nadim.easyapplicationsystem.Model.StudentLoginCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login_Page extends AppCompatActivity {

    private TextView registerNow;
    private EditText userID, password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button loginButton;
    private API api;
    private RadioButton studentRadio, facultyRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        registerNow = findViewById(R.id.registerNow);

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this,Student_Sign_Up_Page.class);
                startActivity(intent);
            }
        });

        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        studentRadio = findViewById(R.id.studentRadio);
        facultyRadio = findViewById(R.id.facultyRadio);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(studentRadio.isChecked()){

                  //  Toast.makeText(Login_Page.this, "student", Toast.LENGTH_SHORT).show();

                    login(getApplicationContext(),
                            userID.getText().toString(),
                            password.getText().toString());
                }
                else if(facultyRadio.isChecked()){

                   // Toast.makeText(Login_Page.this, "faculty", Toast.LENGTH_SHORT).show();

                    loginFaculty(getApplicationContext(),
                            userID.getText().toString(),
                            password.getText().toString());

                }
                else{

                    Toast.makeText(Login_Page.this, "Radio Button is not checked", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sharedPreferences.contains("token")){
           // startActivity(new Intent(Login_Page.this, Student_Home_Page.class));
        }
    }

    public void login(final Context context, String userID, String password){

        if (userID.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "User ID or Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Call<StudentLoginCallBack> call = api.login(new UserLogin(userID,
                password));

        call.enqueue(new Callback<StudentLoginCallBack>() {
            @Override
            public void onResponse(Call<StudentLoginCallBack> call, Response<StudentLoginCallBack> response) {

                progressDialog.dismiss();

                if(!response.isSuccessful()){
                    Toast.makeText(context, "Response Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String token = response.body().getToken();
                if(token!=null){
                    Toast.makeText(context, response.body().getUser().getFullname(), Toast.LENGTH_LONG).show();
                    editor.putString("token", token);
                    editor.putString("id", response.body().getUser().get_id());
                    editor.putString("studentName", response.body().getUser().getFullname());
                    editor.putString("studentBatch", response.body().getUser().getBatch());
                    editor.putString("section", response.body().getUser().getSection());
                    editor.putString("studentID", response.body().getUser().getStudent_id());
                    editor.putString("department", response.body().getUser().getDepartment());
                    editor.putString("contactNumber", response.body().getUser().getContact_no());
                    editor.putString("user_type", response.body().getUser_type());
                    editor.apply();
                    finish();
                    startActivity(new Intent(Login_Page.this, Student_Home_Page.class));
                }
                else Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StudentLoginCallBack> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loginFaculty(final Context context, String userID, String password){

        if (userID.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "User ID or Password is empty", Toast.LENGTH_SHORT).show();
         //   return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Call<FacultyLoginCallBack> call = api.loginFaculty(new UserLogin(userID,
                password));

        call.enqueue(new Callback<FacultyLoginCallBack>() {
            @Override
            public void onResponse(Call<FacultyLoginCallBack> call, Response<FacultyLoginCallBack> response) {

                progressDialog.dismiss();

                if(!response.isSuccessful()){
                    Toast.makeText(context, "Response Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String token = response.body().getToken();
                if(token!=null){
                    Toast.makeText(context, response.body().getUser().getFullname(), Toast.LENGTH_LONG).show();
                    editor.putString("token", token);
                    editor.putString("facultyName", response.body().getUser().getFullname());
                    editor.putString("facultyDepartment", response.body().getUser().getDepartment());
                    editor.putString("email", response.body().getUser().getEmail());
                    editor.putString("facultyPosition", response.body().getUser().getPosition());
                    editor.putString("facultyID", response.body().getUser().getFaculty_id());
                    editor.putString("id", response.body().getUser().get_id());
                    editor.putString("contactNumber", response.body().getUser().getContact_no());
                    editor.putString("user_type", response.body().getUser_type());
                    editor.apply();
                    finish();
                    startActivity(new Intent(Login_Page.this, Faculty_Home_Page.class));
                }
                else Toast.makeText(context, response.body().getToken(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<FacultyLoginCallBack> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(Login_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}

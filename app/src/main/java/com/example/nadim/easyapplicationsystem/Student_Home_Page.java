package com.example.nadim.easyapplicationsystem;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Model.StudentLoginCallBack;
import com.example.nadim.easyapplicationsystem.Model.User_Student;
import com.example.nadim.easyapplicationsystem.Service.MyService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Student_Home_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    private EditText body, apply;
    private TextView subject, studentInfo, profileName;
    private Spinner applicationTypes;
    private Button demo, view;
    private API api;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> typeName, applicationTypeID;
    private String date_n;
    private int pos;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private MenuItem notificationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__home__page);

        drawlerLayout();


        applicationTypes = findViewById(R.id.applicationType);
        subject = findViewById(R.id.subject);
        body = findViewById(R.id.body);
        apply = findViewById(R.id.apply);
        studentInfo = findViewById(R.id.studentInfo);
        demo = findViewById(R.id.demo);
        view = findViewById(R.id.view);
     //   profileName = findViewById(R.id.profileName);

        Retrofit retrofit = new ApiImplementation().getRetrofit();

        api = retrofit.create(API.class);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        typeName = new ArrayList<String>();
        applicationTypeID = new ArrayList<>();

        String name = sharedPreferences.getString("studentName", "");
        String batch = sharedPreferences.getString("studentBatch", "");
        String section = sharedPreferences.getString("section", "");
        String studentID = sharedPreferences.getString("studentID", "");
        String department = sharedPreferences.getString("department", "");

        //applicationTypes.setBackgroundColor();
        //Log.d("header", ""+navigationView.getHeaderView(0).toString());
        View headerView = navigationView.getHeaderView(0);
        profileName = headerView.findViewById(R.id.profileName);
        profileName.setText(sharedPreferences.getString("studentName", null));

        typeName.add("Select Application Type ...");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, typeName);
        applicationTypes.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        types();

        Log.d("user_id", sharedPreferences.getString("id", null));
        // Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();

        Log.d("id", sharedPreferences.getString("id", ""));
        applicationTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0){
                    subject.setText(date_n+"\nThe Registrar,\nLeading University, Sylhet\nSubject: Application for "+typeName.get(position)+"\nSir,");
                }
                else{
                    subject.setText(date_n+"\nThe Registrar,\nLeading University, Sylhet\nSubject: Application for ... "+"\nSir,");
                }

                pos = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        studentInfo.setText("\nYours Faithfully\n"+name+"\nDepartment of "+department
                +"\n"+studentID+"\n"+batch+"\n"+section);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (applicationTypes.getSelectedItemPosition() == 0){
                    Toast.makeText(Student_Home_Page.this, "Select Application Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), Apply_Page.class);
                intent.putExtra("applicationType", applicationTypeID.get(pos-1));
                intent.putExtra("body", body.getText().toString());
                intent.putExtra("request", apply.getText().toString());
                intent.putExtra("user_id", sharedPreferences.getString("id", ""));
                intent.putExtra("application", subject.getText().toString()+
                        "\n"+body.getText().toString()+"\n\n"+apply.getText().toString()+"\n\n"+studentInfo.getText().toString());
                startActivity(intent);

            }
        });

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (applicationTypes.getSelectedItemPosition() == 0){
                    Toast.makeText(Student_Home_Page.this, "Select Application Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), Demo_Student_PP.class));

            }
        });


     //   startService(new Intent(getApplicationContext(), MyService.class));
    }

    private void drawlerLayout(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.student__home__page, menu);
        MenuItem notificationIcon = menu.findItem(R.id.action_settings);
        Log.d("item", ""+notificationIcon.getTitle().toString());

        ArrayList<String> applicationID = new ArrayList<>();
        Gson gson = new Gson();

        if(sharedPreferences.contains("appl_id")){

            String appid = sharedPreferences.getString("appl_id", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            applicationID = gson.fromJson(appid, type);

        }
        if (applicationID.size()>0){
            notificationIcon.setIcon(R.drawable.new_notification);
        }
        else{
            notificationIcon.setIcon(R.drawable.ic_notifications_active_black_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            finish();
            startActivity(new Intent(Student_Home_Page.this, Notification.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(Student_Home_Page.this, Student_Home_Page.class));

        } else if (id == R.id.nav_history) {

            startActivity(new Intent(Student_Home_Page.this, History.class));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(Student_Home_Page.this, Settings_Page.class));

        } else if(id == R.id.logOut){

            getSharedPreferences(getPackageName(), MODE_PRIVATE).edit().clear().commit();
            finish();
            startActivity(new Intent(Student_Home_Page.this, Login_Page.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void types(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String token = sharedPreferences.getString("token", "");

        Log.d("token", token);

        Call<TypesData> call = api.getTypes(sharedPreferences.getString("user_type", ""));
        ArrayList<TypesDataValue> value = new ArrayList<>();

        call.enqueue(new Callback<TypesData>() {

            @Override
            public void onResponse(Call<TypesData> call, Response<TypesData> response) {

                progressDialog.dismiss();

                //Toast.makeText(Student_Home_Page.this, "got inside", Toast.LENGTH_SHORT).show();

                if(!response.isSuccessful()){
                    Toast.makeText(Student_Home_Page.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    ArrayList<TypesDataValue> value = response.body().getTypes();
                    for (TypesDataValue v : value){

                        Log.d("type", v.getUser_type());
                        if(v.getTitle() != null){

                            typeName.add(v.getTitle());
                            applicationTypeID.add(v.get_id());
                        }
                        //  studentInfo.append(v.getTitle()+"\n"+typeName.size()+"\n");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, typeName);
                    applicationTypes.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                //findViewById(R.id.lo).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TypesData> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(Student_Home_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        // MenuItem item = notificationIcon.findItem(R.id.action_settings);

        // Log.d("item", ""+notificationIcon.getTitle().toString());
        if (sharedPreferences.contains("appl_id")){

          /*  MenuItem item = navigationView.getMenu().getItem(4);
            Log.d("item", item.getTitle().toString());*/


        }

        /*if(!sharedPreferences.contains("token")){

            finish();
            startActivity(new Intent(Student_Home_Page.this, Login_Page.class));
        }
        if(sharedPreferences.getString("user_type", "").equalsIgnoreCase("faculty")){

            finish();
            startActivity(new Intent(Student_Home_Page.this, Faculty_Home_Page.class));

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // stopService(new Intent(getApplicationContext(), MyService.class));
    }
}

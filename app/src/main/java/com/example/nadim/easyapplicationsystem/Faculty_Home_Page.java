package com.example.nadim.easyapplicationsystem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Service.MyService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Faculty_Home_Page extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private EditText reason, leaveAddress, contactNumber;
    private Button demo, view;
    private TextView dateFirst, dateLast, profileName;
    private ArrayList<String> typeName, applicationTypeID;
    private API api;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayAdapter<String> adapter;
    private Spinner applicationTypes;
    private int pos;
    private String user_type;
    Calendar myCalendar;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty__home__page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Retrofit retrofit = new ApiImplementation().getRetrofit();
        api = retrofit.create(API.class);
        typeName = new ArrayList<>();
        applicationTypeID = new ArrayList<>();

        reason = findViewById(R.id.reason);
        dateFirst = findViewById(R.id.dateFirst);
        dateLast = findViewById(R.id.dateLast);
        leaveAddress = findViewById(R.id.leaveAddress);
        contactNumber = findViewById(R.id.contactNumber);
        view = findViewById(R.id.view);
        demo = findViewById(R.id.demo);
        applicationTypes = findViewById(R.id.applicationType);
        user_type = sharedPreferences.getString("user_type", "");

        View headerView = navigationView.getHeaderView(0);
        profileName = headerView.findViewById(R.id.profileName);
        profileName.setText(sharedPreferences.getString("facultyName", null));
        contactNumber.setText(sharedPreferences.getString("contactNumber", null));
       // applicationTypes.setBackgroundColor(Color.LTGRAY);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFirst();
            }

        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelLast();
            }

        };


        dateFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(Faculty_Home_Page.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(Faculty_Home_Page.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        typeName.add("Select Application Type ...");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, typeName);
        applicationTypes.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        applicationTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        types();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (applicationTypes.getSelectedItemPosition() == 0){
                    Toast.makeText(Faculty_Home_Page.this, "Select Application Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), FacultyApplyPage.class);
                intent.putExtra("applicationType", applicationTypes.getSelectedItem().toString());
                intent.putExtra("applicationTypeID", applicationTypeID.get(pos-1));
                intent.putExtra("reason", reason.getText().toString());
                intent.putExtra("dateFirst", dateFirst.getText().toString());
                intent.putExtra("dateLast", dateLast.getText().toString());
                intent.putExtra("leaveAddress", leaveAddress.getText().toString());
                intent.putExtra("contactNumber", contactNumber.getText().toString());
                intent.putExtra("token", sharedPreferences.getString("token", ""));
                startActivity(intent);

            }
        });

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (applicationTypes.getSelectedItemPosition() == 0){
                    Toast.makeText(Faculty_Home_Page.this, "Select Application Type", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        //startService(new Intent(Faculty_Home_Page.this, MyService.class));
    }

    private void updateLabelFirst() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        dateFirst.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelLast() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        dateLast.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faculty__home__page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(Faculty_Home_Page.this, Notification.class));
            //item.setIcon(R.drawable.new_notification);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            startActivity(new Intent(Faculty_Home_Page.this, Faculty_Home_Page.class));
        } else if (id == R.id.nav_history) {

            startActivity(new Intent(Faculty_Home_Page.this, History.class));

        } else if (id == R.id.nav_setting) {

            startActivity(new Intent(Faculty_Home_Page.this, FacultySettings.class));

        } else if(id == R.id.logOut){

            getSharedPreferences(getPackageName(), MODE_PRIVATE).edit().clear().commit();
            finish();
            startActivity(new Intent(Faculty_Home_Page.this, Login_Page.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                    Toast.makeText(Faculty_Home_Page.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    ArrayList<TypesDataValue> value = response.body().getTypes();
                    for (TypesDataValue v : value){

                        typeName.add(v.getTitle());
                        applicationTypeID.add(v.get_id());
                        //  studentInfo.append(v.getTitle()+"\n"+typeName.size()+"\n");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sample_view, R.id.textViewSample1, typeName);
                    applicationTypes.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

//                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TypesData> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(Faculty_Home_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

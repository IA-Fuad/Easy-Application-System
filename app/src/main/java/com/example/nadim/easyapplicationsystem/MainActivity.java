package com.example.nadim.easyapplicationsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Service.MyService;
import com.example.nadim.easyapplicationsystem.Service.RestarterBroadcastReceiver;
import com.google.firebase.FirebaseApp;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseApp.initializeApp(MainActivity.this);
        startService(new Intent(getApplicationContext(), MyService.class));
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sharedPreferences.getString("user_type", "").equals("student")){
            finish();
            startActivity(new Intent(getApplicationContext(), Student_Home_Page.class));
        }
        else if(sharedPreferences.getString("user_type", "").equals("faculty")){
            finish();
            startActivity(new Intent(getApplicationContext(), Faculty_Home_Page.class));
        }
        else{
            finish();
            startActivity(new Intent(getApplicationContext(), Login_Page.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("main", "onDestroy: main");
        //sendBroadcast(new Intent(this, RestarterBroadcastReceiver.class));
    }
}

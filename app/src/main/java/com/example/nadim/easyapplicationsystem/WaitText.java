package com.example.nadim.easyapplicationsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WaitText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_text);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        final String user = sharedPreferences.getString("user_type", "");

        Button button = findViewById(R.id.ok);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.equalsIgnoreCase("student")){
                    finish();
                    startActivity(new Intent(WaitText.this, Student_Home_Page.class));
                }
                else{
                    finish();
                    startActivity(new Intent(WaitText.this, Faculty_Home_Page.class));
                }
            }
        });

    }
}

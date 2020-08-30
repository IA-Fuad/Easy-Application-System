package com.example.nadim.easyapplicationsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowApplication extends AppCompatActivity {

    private TextView application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_application);

        application = findViewById(R.id.application);
        application.setText(getIntent().getStringExtra("application"));

    }
}

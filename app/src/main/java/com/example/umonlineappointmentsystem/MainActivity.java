package com.example.umonlineappointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_dashboard = (Button) findViewById(R.id.btn_dashboard);

        btn_dashboard.setOnClickListener(view -> {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        });
    }
}
package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Rounding_PActivity extends AppCompatActivity {
    Button btnRefresh;
    TextView txtDoctorLocation;
    String strDoctorRounding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__p);
        btnRefresh = findViewById(R.id.btnRefresh);
        txtDoctorLocation = findViewById(R.id.txtDoctorLocation);
    }
}

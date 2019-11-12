package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnInfoRounding, btnChatting;
    String idGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInfoRounding = findViewById(R.id.btnInfoRounding);
        btnChatting = findViewById(R.id.btnChatting);

        Intent logIn_intent = getIntent();
        idGroup = logIn_intent.getExtras().getString("idGroup");
        Log.d("idGroup",idGroup);

        btnInfoRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idGroup.equals("Patient")) {
                    Intent intent = new Intent(MainActivity.this, Rounding_PActivity.class);
                    startActivity(intent);
                } else if(idGroup.equals("Doctor")) {
                    Intent intent = new Intent(MainActivity.this, Rounding_DActivity.class);
                    startActivity(intent);
                }
            }
        });
        btnChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                //startActivity(intent);
            }
        });
    }
}

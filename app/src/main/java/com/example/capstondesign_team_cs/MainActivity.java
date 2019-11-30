package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    Button btnInfoRounding, btnChatting;
    FirebaseAuth mAuth;
    Boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInfoRounding = findViewById(R.id.btnInfoRounding);
        btnChatting = findViewById(R.id.btnChatting);

        Intent logIn_intent = getIntent();
        state = logIn_intent.getExtras().getBoolean("state");
        Log.d(TAG + " state : ", state.toString());

        btnInfoRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state) {
                    Intent intent = new Intent(MainActivity.this, Rounding_DActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, Rounding_PActivity.class);
                    startActivity(intent);
                }
            }
        });
        btnChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                startActivity(intent);
            }
        });
    }
}

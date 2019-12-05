package com.example.capstondesign_team_cs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private Button btnInfoRounding, btnChatting, btnLogOut;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Boolean state;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnInfoRounding = findViewById(R.id.btnInfoRounding);
        btnChatting = findViewById(R.id.btnChatting);
        btnLogOut = findViewById(R.id.buttonLogOut);

        FirebaseUser user = mAuth.getCurrentUser();

        getAccountInfo(user);

        btnInfoRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state != null) {
                    if(state) {
                        Intent intent = new Intent(MainActivity.this, Rounding_DActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, Rounding_PActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "state is Null!");
                }
            }
        });
        btnChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                intent.putExtra("state", state);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Log.d(TAG, "Sign Out");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Log.d(TAG, "finish");
                finish();
            }
        });
    }

    private void getAccountInfo(FirebaseUser user) {
        if(user != null) {
            String email = user.getEmail();
            Log.d(TAG + "email", email);
            DocumentReference docRef = db.collection("Account").document(email);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG,"Success get userInfo");
                    final Boolean mState = documentSnapshot.getBoolean("State");
                    final String mPhone = documentSnapshot.getString("Phone");
                    Log.d(TAG + " mState, mPhone : ", mState + ", " + mPhone);
                    setExtraData(mState, mPhone);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Fail get accountInfo");
                }
            });
        }
    }

    public void setExtraData(Boolean state, String phone) {
        this.state = state;
        this.phone = phone;
    }
}

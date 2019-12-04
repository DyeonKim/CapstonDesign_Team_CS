package com.example.capstondesign_team_cs;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Rounding_PActivity extends AppCompatActivity {
    Button btnRefresh;
    TextView txtDoctorLocation;
    String strDoctorRounding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__p);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference drRef = db.collection("dr").document("Q0Ws10hupEXztzyQutcVKEf9pDn1");
        drRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                String location = document.getString("Location");
                txtDoctorLocation.setText(location + "입장");
            }
            else{
                Log.d("state","task fail");
            }
        });



        btnRefresh = findViewById(R.id.btnRefresh);
        txtDoctorLocation = findViewById(R.id.txtDoctorLocation);
    }
}
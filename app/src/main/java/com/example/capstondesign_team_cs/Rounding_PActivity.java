package com.example.capstondesign_team_cs;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Rounding_PActivity extends AppCompatActivity {
    Button btnRefresh;
    TextView txtDoctorLocation;
    String strDoctorRounding;
    static String d_id;
    static String phoneNum;
    static String minor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__p);
        btnClickListener();
        btnRefresh = findViewById(R.id.btnRefresh);
        txtDoctorLocation = findViewById(R.id.txtDoctorLocation);
    }


/*
    public String getRoomNum(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference roomRef = db.collection("Room").whereEqualTo("minor",)
    }

*/
    public void getPhoneNum(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            //phoneNum get 하기
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference accountRef = db.collection("Account").document(email);
            accountRef.get().addOnCompleteListener(task_phone -> {
                if (task_phone.isSuccessful()) {
                    DocumentSnapshot document_phone = task_phone.getResult();
                    phoneNum = document_phone.getString("Phone");
                } else {
                    Log.d("state", "phoneNum task fail");
                }
            });
        }
    }

    public void getDoctorId() {
        getPhoneNum();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference patientRef = db.collection("Patient").document(phoneNum);
        patientRef.get().addOnCompleteListener(task_patient -> {
            if (task_patient.isSuccessful()) {
                DocumentSnapshot document_patient = task_patient.getResult();
                d_id = document_patient.getString("D_id");
                //return d_id;//D_id get 완료
            } else {
                Log.d("state", "D_id task fail");
            }
        });
    }

    public void getMinor(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference drRef = db.collection("dr").document(d_id);
        drRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                minor = document.getString("Location");
            } else {
                Log.d("state", "minor task fail");
            }
        });
    }

    public Query getRoomNum(){
        getMinor();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference roomRef = db.collection("Room");
        Query room = roomRef.whereEqualTo("minor",minor);
        return room;
    }



    public void btnClickListener() {
        btnRefresh.setOnClickListener(v -> {
            getDoctorId();
            if (d_id == null) {
                txtDoctorLocation.setText("배정된 담당의가 없습니다.");
            } else {
                Query roomNum = getRoomNum();
                txtDoctorLocation.setText(roomNum + "회진 중");
            }
        });
    }
/*
    public void btnClickListener() {
        btnRefresh.setOnClickListener(v -> {
            getDoctorId();
            if (d_id == null) {
                txtDoctorLocation.setText("배정된 담당의가 없습니다.");
            } else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference drRef = db.collection("dr").document(d_id);
                drRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String location = document.getString("Location");
                        txtDoctorLocation.setText(location + "입장");
                    } else {
                        Log.d("state", "location task fail");
                    }
                });
            }
        });
    }
 */
}


    /*
    @Override //Location 받아오기 및 새로고침
    protected void onCreate(Bundle savedInstanceState) {
        getDoctorId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__p);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference drRef = db.collection("dr").document(d_id);
        drRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                String location = document.getString("Location");
                txtDoctorLocation.setText(location + "입장");
            }
            else{
                Log.d("state","location task fail");
            }
        });


        
        btnRefresh = findViewById(R.id.btnRefresh);
        txtDoctorLocation = findViewById(R.id.txtDoctorLocation);
    }
}
*/
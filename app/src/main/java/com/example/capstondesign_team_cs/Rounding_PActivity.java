package com.example.capstondesign_team_cs;

import android.content.Intent;
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
    static String d_id;
    static String phoneNum;
    static String minor;
    private String roomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__p);
        //
        btnRefresh = findViewById(R.id.btnRefresh);
        txtDoctorLocation = findViewById(R.id.txtDoctorLocation);
        btnClickListener();

    }

/*//폰 넘버 받기
    public void getPhoneNum(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            //phoneNum get 하기
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference accountRef = db.collection("Account").document(email);
            accountRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        phoneNum = document.getString("Phone");
                    } else {
                        Log.d("state", "phoneNum task fail");
                    }
                }
            });
        }
    }
*/

//get PhoneNum 인텐트로


    public void getDoctorId() {
        Intent paIntent = getIntent();
        phoneNum = paIntent.getExtras().getString("phone");
        Log.d("phoneNum", phoneNum);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference patientRef = db.collection("Patient").document(phoneNum);
        patientRef.get().addOnCompleteListener(task_patient -> {
            if (task_patient.isSuccessful()) {
                DocumentSnapshot document_patient = task_patient.getResult();
                String id = document_patient.getString("D_id");
                Log.d("D_id", id);
                setDoctorID(id);
            } else {
                Log.d("state", "D_id task fail");
            }
        });
    }

    public void setDoctorID(String id) {
        d_id = id;
    }

    public void getMinor(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference drRef = db.collection("Dr").document(d_id);
        drRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String mMinor = document.getString("Location");
                Log.d("minor", mMinor);
                setRoomNum(mMinor);
            } else {
                Log.d("state", "minor task fail");
            }
        });
    }

    public void setRoomNum(String num) {
        minor = num;
    }

    /*//getRoomNum 원본
    public Query getRoomNum(){
        getMinor();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference roomRef = db.collection("Room");
        Query room = roomRef.whereEqualTo("minor",minor);
        return room;
    }
     */

    /* 룸 넘버 주석처리 이거 아래 코드 있음
    public void getRoomNum(){
        getMinor();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference roomRef = db.collection("Room");
        roomRef.whereEqualTo("minor",minor).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, document.getId() + " => " + document.getData());
                        roomNum = document.getData();
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    private void getRoomNum() {
        getMinor();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference drRef = db.collection("Room").document(minor);
        drRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Log.d(TAG,"Success get Room Number");
                final String mRoom = documentSnapshot.getString("Number");
                setExtraData(mRoom);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.d(TAG,"Fail get Room Number");
            }
        });
    }

    public void setExtraData(String room) {
        this.roomNum = room;
    }

*/
    /*
    public void getRoomNum(){
        getMinor();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference drRef = db.collection("Room").document(minor);
        drRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("state", "room number task fail");
                DocumentSnapshot document = task.getResult();
                roomNum = document.getString("Number");
            } else {
            }
        });
    }
     */

    public void btnClickListener() {
        btnRefresh.setOnClickListener(v -> {
            getDoctorId();
            if (d_id == null) {
                txtDoctorLocation.setText("배정된 담당의가\n없습니다.");
            } else {
                //getRoomNum();
                getMinor();
                txtDoctorLocation.setText(minor + "회진 중");

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
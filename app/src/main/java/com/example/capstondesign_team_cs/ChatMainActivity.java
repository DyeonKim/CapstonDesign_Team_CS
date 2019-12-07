package com.example.capstondesign_team_cs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatMainActivity extends AppCompatActivity {

    LoginActivity login = new LoginActivity();
    UserInfo userInfo = new UserInfo();
    MainActivity main = new MainActivity();

    private static final String TAG = "Chat";
    private EditText user_edit;
    private TextView user_chat;
    private Button user_next, board_next;
    private ListView chat_list;
    private FirebaseFirestore db;
    static String phone;
    static String d_id;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("message");
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        user_chat = (TextView) findViewById(R.id.user_chat);
        user_edit = (EditText) findViewById(R.id.user_edit);
        user_next =  (Button) findViewById(R.id.user_next);
        chat_list = findViewById(R.id.chat_list);
        board_next = (Button) findViewById(R.id.board_next);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent paIntent = getIntent();
        phone = paIntent.getExtras().getString("phone");

        board_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change_view = new Intent(ChatMainActivity.this, BoardActivity.class);
                startActivity(change_view);

            }
        });



        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_edit.getText().toString().equals("")) {
                    Log.d("here?", "o");
                    return; }

                //user_chat.getText().toString()
                Intent intent = new Intent(ChatMainActivity.this, ChattingActivity.class);

                intent.putExtra("chatName", phone);
                intent.putExtra("userName", user_edit.getText().toString());
                user_chat.setText(phone);
                startActivity(intent);

                /*
               if (login.mUserInfo.getUserState() == true) {
                   intent.putExtra("chatName", login.mUserInfo.getUserPhone().toString());
                   intent.putExtra("userName", user_edit.getText().toString());
                   startActivity(intent);
               } else {
                    intent.putExtra("chatName", login.mUserInfo.getUserPhone().toString());
                    intent.putExtra("userName", user_edit.getText().toString());
                    startActivity(intent);
                } */
            }
        });

        showChatList();
    }

    private void showChatList() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);

        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(user_chat.getText().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDoctorId() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference patientRef = db.collection("Patient").document(phone);
        patientRef.get().addOnCompleteListener(task_patient -> {
            if (task_patient.isSuccessful()) {
                DocumentSnapshot document_patient = task_patient.getResult();
                d_id = document_patient.getString("D_id");
                //user_chat.setText(d_id);
                //return d_id;//D_id get 완료
            } else {
                Log.d("state", "D_id task fail");
            }
        });
    }
}

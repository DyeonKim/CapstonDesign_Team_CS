package com.example.capstondesign_team_cs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public class ChatMainActivity extends AppCompatActivity {

    LoginActivity login = new LoginActivity();

    private EditText user_edit;
    private EditText user_chat;
    private Button user_next;
    private ListView chat_list;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("message");
    private DatabaseReference dataRef = database.getReference("name");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        user_chat = (EditText) findViewById(R.id.user_chat);
        user_edit = (EditText) findViewById(R.id.user_edit);
        user_next =  (Button) findViewById(R.id.user_next);
        chat_list = (ListView) findViewById(R.id.chat_list);

        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_edit.getText().toString().equals("") || user_chat.getText().toString().equals(""))
                    return;
                //user_chat.getText().toString()
                Intent intent = new Intent(ChatMainActivity.this, ChattingActivity.class);

                if (login.idGroup.equals("Patient")) {
                    intent.putExtra("chatName", user_chat.getText());
                    intent.putExtra("userName", user_edit.getText().toString());
                    startActivity(intent);
                } else if (login.idGroup.equals("Dr")) {
                    intent.putExtra("chatName", user_chat.getText());
                    intent.putExtra("userName", user_edit.getText().toString());
                    startActivity(intent);
                }
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
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
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
}

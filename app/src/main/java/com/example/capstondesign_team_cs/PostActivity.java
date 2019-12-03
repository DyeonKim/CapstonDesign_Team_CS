package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTitle, mContents;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.post_title_edit);
        mContents = findViewById(R.id.post_contents_edit);

        findViewById(R.id.post_save_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser() != null) {
            String postId = mStore.collection(FireBaseId.post).document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(FireBaseId.documentId, mAuth.getCurrentUser().getUid());
            data.put(FireBaseId.title, mTitle.getText().toString());
            data.put(FireBaseId.contents, mContents.getText().toString());
            data.put(FireBaseId.timestamp, FieldValue.serverTimestamp());
            mStore.collection(FireBaseId.post).document(postId).set(data, SetOptions.merge());
            finish();
        }
    }
}

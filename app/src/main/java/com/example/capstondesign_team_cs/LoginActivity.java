package com.example.capstondesign_team_cs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "Login";
    private static final int SIGN_IN = 9001;
    private static final int CREATE_ACCOUNT = 9002;
    private int buttonCode = -1;

    UserInfo mUserInfo;

    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserInfo = new UserInfo();
        mEmailField = findViewById(R.id.userEmail);
        mPasswordField = findViewById(R.id.password);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnCreateAccount).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateSignInForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            buttonCode = SIGN_IN;
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateSignInForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            buttonCode = CREATE_ACCOUNT;
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            if (buttonCode == CREATE_ACCOUNT) {
                Map<String, Object> account = new HashMap<>();
                account.put("State", mUserInfo.getUserState());
                account.put("Name", mUserInfo.getUserName());
                account.put("Email", mUserInfo.getUserEmail());
                account.put("Phone", mUserInfo.getUserPhone());

                // Add a new document with a generated ID
                db.collection("Account").document(mUserInfo.getUserEmail())
                        .set(account)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + mUserInfo.getUserEmail());
                                Toast.makeText(getApplicationContext(),"계정 생성 완료", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        else if (buttonCode == SIGN_IN) {
            final String email = mAuth.getCurrentUser().getEmail();

            db.collection("Account").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                Boolean mState = document.getBoolean("State");
                                Log.i(TAG + " mState", mState.toString());
                                Log.i(TAG + "email", email);
                                Log.i(TAG + " mState", mState.toString());
                                Intent sign_intent = new Intent(getApplicationContext(), MainActivity.class);
                                sign_intent.putExtra("email", email);
                                sign_intent.putExtra("state", mState);
                                startActivity(sign_intent);
                            } else {
                                Log.d(TAG, "No Such Document");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        }
        } else {
            Log.d(TAG,"Error Sign In");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnCreateAccount) {
            setRegister();
        } else if (i == R.id.btnLogin) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    public void setRegister() {
        RegisterDialog registerDialog = new RegisterDialog(this);
        registerDialog.setDialogListener(new RegisterDialog.RegisterDialogListener() {
            @Override
            public void onPositiveClicked(Boolean state, String name, final String email, final String password, String phone) {
                mUserInfo.setUserInfo(state, name, email, phone);
                String role, id;
                if(state) {
                    role = "Dr";    id = "D_id";
                } else {
                    role = "Patient";   id = "P_id";
                }
                db.collection(role)
                        .whereEqualTo(id, phone)
                        .whereEqualTo("Name", name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    Log.d(TAG,"Exist User");
                                    createAccount(email, password);
                                } else {
                                    Log.d(TAG,"Not Exist User");
                                    Toast.makeText(getApplicationContext(), "병원에 정보가 없습니다.", Toast.LENGTH_LONG);
                                }
                            }
                        });
            }

            @Override
            public void onNegativeClicked() {

            }
        });
        registerDialog.show();
    }
}


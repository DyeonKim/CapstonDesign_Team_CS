package com.example.capstondesign_team_cs;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Login";
    private static final int SIGN_IN = 9001;
    private static final int CREATE_ACCOUNT = 9002;
    private int buttonCode = -1;

    UserInfo mUserInfo;

    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;
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
                        }).addOnFailureListener(new OnFailureListener() {
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
                                Intent sign_intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(sign_intent);
                                finish();
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
            public void onPositiveClicked(Boolean state, final String name, final String email, final String password, String phone) {
                mUserInfo.setUserInfo(state, name, email, phone);
                String role, id;
                if(state) {
                    role = "Dr";    id = "D_id";
                } else {
                    role = "Patient";   id = "P_id";
                }
                Log.i(TAG + " role, id ", role +"," + id);
                db.collection(role)
                        .whereEqualTo(id, phone)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(DocumentSnapshot document : queryDocumentSnapshots) {
                                    if(name.equals(document.get("Name"))) {
                                        Log.d(TAG,"Exist User");
                                        createAccount(email, password);
                                    } else {
                                        Log.d(TAG,"Not Exist User");
                                        Toast.makeText(getApplicationContext(), "병원에 정보가 없습니다.", Toast.LENGTH_LONG);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"Not Exist User");
                                Toast.makeText(getApplicationContext(), "병원에 정보가 없습니다.", Toast.LENGTH_LONG);
                            }
                        });
            }

            @Override
            public void onNegativeClicked() {

            }
        });

        registerDialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Window window = registerDialog.getWindow();
        int x = (int)(size.x * 0.8f);
        int y = (int)(size.y * 0.7f);
        window.setLayout(x, y);
        }
}
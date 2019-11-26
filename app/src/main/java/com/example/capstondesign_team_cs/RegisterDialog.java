package com.example.capstondesign_team_cs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegisterDialog extends Dialog implements View.OnClickListener {
    private Button positiveButton;
    private Button negativeButton;
    private RadioGroup stateGroup;
    private EditText editName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPhone;
    private Context context;
    boolean state = false;

    private RegisterDialogListener registerDialogListener;

    public RegisterDialog(Context context) {
        super(context);
        this.context = context;
    }

    interface RegisterDialogListener {
        void onPositiveClicked(Boolean state, String name, String email, String password, String phone);
        void onNegativeClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_register);

        stateGroup = findViewById(R.id.stateGroup);
        positiveButton = findViewById(R.id.buttonRegisterOK);
        negativeButton = findViewById(R.id.buttonRegisterCancel);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        stateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("checkedId : ", Integer.toString(checkedId));
                switch (checkedId) {
                    case R.id.statePatient:
                        Log.d("checkedId : ", Integer.toString(checkedId));
                        state = false;
                        break;
                    case R.id.stateDoctor:
                        Log.d("checkedId : ", Integer.toString(checkedId));
                        state = true;
                        break;
                    default:
                        RadioButton statePatient = findViewById(R.id.statePatient);
                        RadioButton stateDoctor = findViewById(R.id.stateDoctor);
                        statePatient.setError("Required.");
                        stateDoctor.setError("Required.");
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegisterOK:
                if(validateRegisterForm(editName, editEmail, editPassword)) {
                    String name = editName.getText().toString();
                    String email = editEmail.getText().toString();
                    String password = editPassword.getText().toString();
                    String phone = editPhone.getText().toString();
                    registerDialogListener.onPositiveClicked(state, name, email, password, phone);
                    dismiss();
                }
                break;
            case R.id.buttonRegisterCancel:
                cancel();
                break;
            default:
                break;
        }
    }

    private boolean validateRegisterForm(EditText nameField, EditText emailField, EditText passwordField) {
        boolean valid = true;

        String name = nameField.getText().toString();
        Log.d("이름", name);

        if(TextUtils.isEmpty(name)) {
            nameField.setError("Required");
            valid = false;
        } else {
            nameField.setError(null);
        }

        String email = emailField.getText().toString();
        Log.d("이름", email);
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        Log.d("이름", password);
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }
}

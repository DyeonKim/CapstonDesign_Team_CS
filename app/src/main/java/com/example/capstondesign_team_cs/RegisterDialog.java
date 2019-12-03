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
    private boolean state = false;

    private int mCheckedId = -1;

    private RegisterDialogListener registerDialogListener;

    public RegisterDialog(Context context) {
        super(context);
        this.context = context;
    }

    interface RegisterDialogListener {
        void onPositiveClicked(Boolean state, String name, String email, String password, String phone);
        void onNegativeClicked();
    }

    public void setDialogListener(RegisterDialogListener registerDialogListener) {
        this.registerDialogListener = registerDialogListener;
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
                        mCheckedId = checkedId;
                        break;
                    case R.id.stateDoctor:
                        Log.d("checkedId : ", Integer.toString(checkedId));
                        state = true;
                        mCheckedId = checkedId;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegisterOK:
                if(validateRegisterForm(editName, editEmail, editPassword, editPhone)) {
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

    private boolean validateRegisterForm(EditText nameField, EditText emailField, EditText passwordField, EditText phoneField) {
        boolean valid = true;

        if(mCheckedId != R.id.stateDoctor && mCheckedId != R.id.statePatient) {
            RadioButton stateDoctor = findViewById(R.id.stateDoctor);
            stateDoctor.setError("Required.");
            valid = false;
        } else {
            RadioButton stateDoctor = findViewById(R.id.stateDoctor);
            stateDoctor.setError(null);
        }
        String name = nameField.getText().toString();
        if(TextUtils.isEmpty(name)) {
            nameField.setError("Required");
            valid = false;
        } else {
            nameField.setError(null);
        }

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        String phone = phoneField.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            phoneField.setError("Required");
            valid =false;
        } else {
            phoneField.setError(null);
        }

        return valid;
    }
}

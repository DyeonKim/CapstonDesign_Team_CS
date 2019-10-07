package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Rounding_DActivity extends AppCompatActivity {
    Button btnStartRounding, btnCancelRounding;
    TextView txtInfoRounding;
    int nSelectReason = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__d);
        btnStartRounding = findViewById(R.id.btnStartRounding);
        btnCancelRounding = findViewById(R.id.btnCancelRounding);
        txtInfoRounding = findViewById(R.id.txtInfoRounding);

        btnCancelRounding.setEnabled(false);

        btnStartRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rounding_DActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog);
                alertDialogBuilder.setMessage("회진을 시작하시겠습니까?");
                alertDialogBuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtInfoRounding.setText("회진 중");
                        Toast.makeText(Rounding_DActivity.this,"회진을 시작합니다.", Toast.LENGTH_LONG).show();
                        btnStartRounding.setEnabled(false);
                        btnCancelRounding.setEnabled(true);
                    }
                });
                alertDialogBuilder.setNegativeButton("아니오", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        final String[] cancelReasons = {"회진이 끝났습니다.", "긴급수술로 인해 회진을 취소합니다."};
        btnCancelRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Rounding_DActivity.this);
                builder.setTitle("회진 취소 사유를 선택하세요")
                        .setSingleChoiceItems(cancelReasons, -1, new DialogInterface.OnClickListener() {
                        @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("which = ", Integer.toString(which));
                                nSelectReason = which;
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("nSelection = ", Integer.toString(nSelectReason));
                                //which = nSelectReason;
                                if(nSelectReason >= 0) {
                                    txtInfoRounding.setText("회진시간이 아닙니다.");
                                    Toast.makeText(getApplicationContext(),
                                            cancelReasons[nSelectReason], Toast.LENGTH_LONG).show();
                                    btnStartRounding.setEnabled(true);
                                    btnCancelRounding.setEnabled(false);
                                    nSelectReason = -1;
                                }
                                else {
                                    Toast.makeText(Rounding_DActivity.this,
                                            "이유를 선택하세요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNeutralButton("취소", null);
                builder.create();
                builder.show();
            }
        });
    }
}

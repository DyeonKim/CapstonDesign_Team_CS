package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.List;
import java.util.UUID;

public class Rounding_DActivity extends AppCompatActivity {
    private MinewBeaconManager mMinewBeaconManager;
    private boolean isScanning = false;
    final static int BT_REQUEST_ENABLE = 2;
    final static int PERMISSIONS = 100;

    Button btnStartRounding, btnCancelRounding;
    TextView txtInfoRounding;
    int nSelectReason = -1;

   private void checkPermissions() {
       ActivityCompat.requestPermissions(this,
               new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__d);

        btnStartRounding = findViewById(R.id.btnStartRounding);
        btnCancelRounding = findViewById(R.id.btnCancelRounding);
        txtInfoRounding = findViewById(R.id.txtInfoRounding);

        btnCancelRounding.setEnabled(false);

        checkPermissions();
        initManager();
        initListener();
    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(getApplicationContext());
    }

    private void initListener() {
        btnStartRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rounding_DActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog);
                alertDialogBuilder.setMessage("회진을 시작하시겠습니까?");
                alertDialogBuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initBeacon();
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
        btnCancelRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] cancelReasons = {"회진이 끝났습니다.", "긴급수술로 인해 회진을 취소합니다."};
                AlertDialog.Builder builder = new AlertDialog.Builder(Rounding_DActivity.this);
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
                                if(nSelectReason >= 0) {
                                    onDestroy();
                                    txtInfoRounding.setText("회진시간이 아닙니다.");
                                    Toast.makeText(getApplicationContext(), cancelReasons[nSelectReason], Toast.LENGTH_LONG).show();
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
        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {
                if(!minewBeacons.isEmpty()) {
                    String deviceName = minewBeacons.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                    Toast.makeText(getApplicationContext(), deviceName + "  in range", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                if(!minewBeacons.isEmpty()) {
                    for (MinewBeacon minewBeacon : minewBeacons) {
                        String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                        Toast.makeText(getApplicationContext(), deviceName + "  out range", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {
                switch (bluetoothState) {
                    case BluetoothStatePowerOff:
                        showBLEDialog();
                        break;
                    case BluetoothStatePowerOn:
                        break;
                }
            }


        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop scan
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }
    }

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, BT_REQUEST_ENABLE);
    }

    private void initBeacon() {
       if (mMinewBeaconManager != null) {
           BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
           switch (bluetoothState) {
               case BluetoothStateNotSupported:
                   Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다", Toast.LENGTH_SHORT).show();
                   finish();
                   break;
               case BluetoothStatePowerOff:
                   showBLEDialog();
                   break;
               case BluetoothStatePowerOn:
                   break;
           }
        }
       if (!isScanning) {
           isScanning = true;
           try {
               mMinewBeaconManager.startScan();
           } catch (Exception e) {
               e.printStackTrace();
           }
       } else {
           isScanning = false;
           if (mMinewBeaconManager != null) {
               mMinewBeaconManager.stopScan();
           }
       }
    }
}

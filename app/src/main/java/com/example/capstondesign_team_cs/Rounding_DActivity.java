package com.example.capstondesign_team_cs;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.List;
import java.util.UUID;

public class Rounding_DActivity extends AppCompatActivity {
    final static int PERMISSIONS = 100;
    int nSelectReason = -1;

    MinewBeaconManager beaconManager;
    final static int BT_REQUEST_ENABLE = 2;
    boolean isScanning = false;
    UUID[] uuid = new UUID[1];

    Button btnStartRounding, btnCancelRounding;
    TextView tvInfoRounding, tvRoom;

    private void checkPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__d);

        beaconManager = MinewBeaconManager.getInstance(getApplicationContext());
        uuid[0] = UUID.fromString("74278BDA-B644-4520-8F0C-720EAF059935");

        btnStartRounding = findViewById(R.id.btnStartRounding);
        btnCancelRounding = findViewById(R.id.btnCancelRounding);
        tvInfoRounding = findViewById(R.id.tvInfoRounding);
        tvRoom = findViewById(R.id.tvRoom);

        btnCancelRounding.setEnabled(false);

        checkPermissions();
        btnClickListener();
        setBeaconManager();

    }

    public void btnClickListener() {
        btnStartRounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rounding_DActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                alertDialogBuilder.setMessage("회진을 시작하시겠습니까?");
                alertDialogBuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initBluetooth();
                        beaconScan();
                        tvInfoRounding.setText("회진 중");
                        Toast.makeText(getApplicationContext(),"회진을 시작합니다.", Toast.LENGTH_LONG).show();
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
                                nSelectReason = which;
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(nSelectReason >= 0) {
                                    beaconScan();
                                    tvInfoRounding.setText("회진시간이 아닙니다.");
                                    tvRoom.setText("");
                                    Toast.makeText(getApplicationContext(), cancelReasons[nSelectReason], Toast.LENGTH_LONG).show();
                                    btnStartRounding.setEnabled(true);
                                    btnCancelRounding.setEnabled(false);
                                    nSelectReason = -1;
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "이유를 선택하세요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNeutralButton("취소", null);
                builder.create();
                builder.show();
            }
        });
    }

    public void setBeaconManager() {
        beaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> beacons) {
                for(MinewBeacon beacon : beacons) {
                    String strUUID = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID) .getStringValue();
                    if(strUUID != null && strUUID.equalsIgnoreCase(uuid[0].toString())) {
                        String deviceName = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                        Log.d("Appears", deviceName);
                    }
                }
            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> beacons) {
                for(MinewBeacon beacon : beacons) {
                    String strUUID = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID) .getStringValue();
                    if(strUUID != null && strUUID.equalsIgnoreCase(uuid[0].toString())) {
                        String deviceName = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                        Log.d("Disappear", deviceName);
                        tvRoom.setText((deviceName + "퇴장"));
                    }
                }
            }

            @Override
            public void onRangeBeacons(List<MinewBeacon> beacons) {
                for(MinewBeacon beacon : beacons) {
                    String strUUID = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue();
                    if(strUUID != null && strUUID.equalsIgnoreCase(uuid[0].toString())) {
                        if(beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue() > - 65) {
                            String deviceName = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                            Log.d("state", deviceName);
                            tvRoom.setText(deviceName + "입장");
                        }
                    }
                }
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

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, BT_REQUEST_ENABLE);
    }
    private void initBluetooth() {
        if (beaconManager != null) {
            BluetoothState bluetoothState = beaconManager.checkBluetoothState();
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
    }
    private void beaconScan() {
        if (!isScanning) {
            isScanning = true;
            try {
                beaconManager.startScan();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            isScanning = false;
            if (beaconManager != null) {
                beaconManager.stopScan();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop scan
        if (isScanning) {
            beaconManager.stopScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                break;
        }
    }
}

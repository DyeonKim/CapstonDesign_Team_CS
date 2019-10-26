package com.example.capstondesign_team_cs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.List;
import java.util.UUID;

public class Rounding_DActivity extends AppCompatActivity {
    BtnOnClickListener onClickListener;
    BluetoothAdapter mBluetoothAdapter;
    private MinewBeaconManager mMinewBeaconManager;
    private boolean isScanning;
    final static int BT_REQUEST_ENABLE = 1;
    //final static UUID BT_UUID = UUID.fromString("E2C56D85-DFFB-48D2-B060-DOF5A");

    Button btnStartRounding, btnCancelRounding;
    TextView txtInfoRounding;
    int nSelectReason = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounding__d);

        onClickListener = new BtnOnClickListener();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnStartRounding = findViewById(R.id.btnStartRounding);
        btnCancelRounding = findViewById(R.id.btnCancelRounding);
        txtInfoRounding = findViewById(R.id.txtInfoRounding);

        btnCancelRounding.setEnabled(false);

        btnStartRounding.setOnClickListener(onClickListener);
        btnCancelRounding.setOnClickListener(onClickListener);
        initManager();
        initBeaconListener();
        testSCanningBLE();


    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    private  void initBeaconListener() {
        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> list) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> list) {

            }

            @Override
            public void onRangeBeacons(List<MinewBeacon> list) {

            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {

            }
        });
    }

    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                //showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }

    private void testSCanningBLE() {
        if (mMinewBeaconManager != null) {
            checkBluetooth();
        }
        if (isScanning) {
            isScanning = false;
            if (mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
            }
        } else {
            isScanning = true;
            try {
                mMinewBeaconManager.startScan();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void bluetoothOn() {
        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStartRounding :
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rounding_DActivity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog);
                    alertDialogBuilder.setMessage("회진을 시작하시겠습니까?");
                    alertDialogBuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bluetoothOn();
                            txtInfoRounding.setText("회진 중");
                            Toast.makeText(Rounding_DActivity.this,"회진을 시작합니다.", Toast.LENGTH_LONG).show();
                            btnStartRounding.setEnabled(false);
                            btnCancelRounding.setEnabled(true);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("아니오", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;
                case R.id.btnCancelRounding :
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
                    break;
            }
        }
    }
}

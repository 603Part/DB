package com.iflytek.sybil.smarthome.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.iflytek.sybil.smarthome.R;
import com.iflytek.sybil.smarthome.utils.BluetoothService;
import com.iflytek.sybil.smarthome.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MacScanActivity extends AppCompatActivity {

    private String MAC1 = "FC:58:FA:31:86:CB";
    private String MAC = "FC:58:FA:11:86:AB";
    private Button btn_connect;
    private ProgressDialog progressDialog;

    private  BluetoothService mBluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_scan);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null)
            unbindService();
        Log.e("test", "onDestroy");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("test", "onStop");
        if (mBluetoothService != null){
            mBluetoothService.stopNotify(Constant.SeviceUUID, Constant.CharacteristicUUID);
            unbindService();
        }

    }

    private void initView() {

        progressDialog = new ProgressDialog(this);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, 12);
        }
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setScanCallback(callback);
            mBluetoothService.scanAndConnect5(MAC1);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    private BluetoothService.Callback callback = new BluetoothService.Callback() {
        @Override
        public void onStartScan() {
            btn_connect.setEnabled(false);

        }
        @Override
        public void onScanning(ScanResult result) {

        }

        @Override
        public void onScanComplete() {
            btn_connect.setEnabled(true);
        }

        @Override
        public void onConnecting() {
            progressDialog.show();
        }

        @Override
        public void onConnectFail() {

            btn_connect.setEnabled(true);
            progressDialog.dismiss();
            Toast.makeText(MacScanActivity.this, "连接失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisConnected() {

            btn_connect.setEnabled(true);
            progressDialog.dismiss();
            Toast.makeText(MacScanActivity.this, "连接断开", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServicesDiscovered() {
            progressDialog.dismiss();
            openNotiy();
        }
    };

    private void openNotiy() {
        mBluetoothService.notify(Constant.SeviceUUID, Constant.CharacteristicUUID, new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {

                dealMessage(characteristic);
            }

            @Override
            public void onFailure(BleException exception) {

            }

            @Override
            public void onInitiatedResult(boolean result) {
                Toast.makeText(MacScanActivity.this, "result", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dealMessage(BluetoothGattCharacteristic characteristic) {
        Toast.makeText(MacScanActivity.this, String.valueOf(HexUtil.encodeHex(characteristic.getValue())), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(MacScanActivity.this, ShowActivity.class);
//        intent.putExtra("message", String.valueOf(HexUtil.encodeHex(characteristic.getValue())));
//
//        startActivity(intent);
    }


    private void bindService() {
        Intent bindIntent = new Intent(this, BluetoothService.class);
        this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        this.unbindService(mFhrSCon);
    }

    private void onPermissionGranted(String permission) {

            switch (permission) {
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    if (mBluetoothService == null) {
                        bindService();
                    } else {
                        mBluetoothService.scanAndConnect5(MAC1);
                    }
                    break;
            }

    }
}

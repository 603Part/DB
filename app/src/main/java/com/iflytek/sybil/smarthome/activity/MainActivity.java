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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.iflytek.sybil.smarthome.R;
import com.iflytek.sybil.smarthome.adapter.ResultAdapter;
import com.iflytek.sybil.smarthome.utils.BluetoothService;
import com.iflytek.sybil.smarthome.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TitleActivity implements View.OnClickListener{


    private static BluetoothService mBluetoothService;

    private ListView listView_device;
    private ResultAdapter resultAdapter;

    private List<ScanResult> resultlist = new ArrayList();

    private Button btn_search;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }

    private void initView() {


        setTitle("连接设备");
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        resultAdapter = new ResultAdapter(this, resultlist);
        listView_device = (ListView) findViewById(R.id.listView_device);
        listView_device.setAdapter(resultAdapter);
        listView_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mBluetoothService != null){
                    mBluetoothService.cancelScan();
                    mBluetoothService.connectDevice(resultAdapter.getItem(position));
                    resultAdapter.clear();
                    resultAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public static void readMessage(final Context context) {
        mBluetoothService.read(Constant.SeviceUUID, Constant.UUID_READ, new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {

            }

            @Override
            public void onFailure(BleException exception) {

            }

            @Override
            public void onInitiatedResult(boolean result) {

            }
        });
    }

    public static void sendMessage(String message, final Context context){
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mBluetoothService.write(Constant.SeviceUUID, Constant.CharacteristicUUID, message, new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {

            }

            @Override
            public void onFailure(BleException exception) {
            }

            @Override
            public void onInitiatedResult(boolean result) {
            }
        });

    }

    private void bindService() {
        Intent bindIntent = new Intent(this, BluetoothService.class);
        this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        this.unbindService(mFhrSCon);
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setScanCallback(callback);
            mBluetoothService.scanDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private BluetoothService.Callback callback = new BluetoothService.Callback() {
        @Override
        public void onStartScan() {
            resultAdapter.clear();
            resultAdapter.notifyDataSetChanged();
            btn_search.setEnabled(false);
        }

        @Override
        public void onScanning(ScanResult scanResult) {
            resultAdapter.addResult(scanResult);
            resultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanComplete() {
            btn_search.setEnabled(true);
        }

        @Override
        public void onConnecting() {
            progressDialog.show();
        }

        @Override
        public void onConnectFail() {
            progressDialog.dismiss();
            btn_search.setEnabled(true);
            Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisConnected() {
            progressDialog.dismiss();
            resultAdapter.clear();
            resultAdapter.notifyDataSetChanged();
            btn_search.setEnabled(true);
            Toast.makeText(MainActivity.this, "连接断开", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServicesDiscovered() {
            progressDialog.dismiss();
            startActivity(new Intent(MainActivity.this, FirstActivity.class));
            //startActivity(new Intent(MainActivity.this, ControlActivity.class));

        }
    };


    public static BluetoothService getBluetoothService() {
        return mBluetoothService;
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

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (mBluetoothService == null) {
                    bindService();
                } else {
                    mBluetoothService.scanDevice();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                checkPermissions();
                break;
        }
    }
}

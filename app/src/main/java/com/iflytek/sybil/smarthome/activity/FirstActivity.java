package com.iflytek.sybil.smarthome.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.iflytek.sybil.smarthome.R;
import com.iflytek.sybil.smarthome.db.DBManager;
import com.iflytek.sybil.smarthome.utils.BluetoothService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstActivity extends TitleActivity implements AdapterView.OnItemClickListener{

    private BluetoothService mBluetoothService;
    private List<Map<String, Object>> data_list;

    private static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button In = (Button)this.findViewById(R.id.into);
        requestPermission();

        In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
        // 打开另一个activity，通过意图，意图作用是激活其他组件
                        Intent intent = new Intent();
                        intent.setClass(FirstActivity.this, SevenFourClipActivity.class);
        //发送意图.将意图发送给android系统，系统根据意图来激活组件
                        startActivity(intent);
                    }
                });
        showForwardView(R.string.txt_contect, true);//连接
        mBluetoothService = MainActivity.getBluetoothService();
        if (mBluetoothService != null){
            setTitle(mBluetoothService.getName());

            Log.e("test", mBluetoothService.getName());
        }else {
            setTitle("智能元件柜");
        }

    }

    private void requestPermission() {

        Log.i(TAG,"requestPermission");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"checkSelfPermission");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG,"shouldShowRequestPermissionRationale");


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

            } else {
                Log.i(TAG,"requestPermissions");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG,"onRequestPermissionsResult granted");
                    DBManager.copyDb(this, "");
                    DBManager.dbManager(this);
                } else {
                    Log.i(TAG,"onRequestPermissionsResult denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showWaringDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish();
                    }
                }).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                startActivity(new Intent(FirstActivity.this, SevenFourClipActivity.class));
                break;


        }
    }

    @Override
    protected void onForward(View forwardView) {
        startActivity(new Intent(FirstActivity.this, MainActivity.class));
    }
}

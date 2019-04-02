package com.iflytek.sybil.smarthome;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.iflytek.sybil.smarthome.activity.MainActivity;
import com.iflytek.sybil.smarthome.utils.BluetoothService;
import com.iflytek.sybil.smarthome.utils.Constant;

import java.io.UnsupportedEncodingException;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener{

    private BluetoothService mBluetoothService;
    private TextView txt_test, txt_jieshou;
    private Button btn_open, btn_close, btn_notify;
    //输出数据展示
    private StringBuilder mOutputInfo = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        mBluetoothService = MainActivity.getBluetoothService();
        txt_test = (TextView) findViewById(R.id.txt_test);
        txt_test.setText(mBluetoothService.getName());
        txt_jieshou = (TextView) findViewById(R.id.txt_jieshou);

        btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(this);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        btn_notify = (Button) findViewById(R.id.btn_notify);
        btn_notify.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open:
                sendMessage(Constant.READ_HUMITURE);
                break;
            case R.id.btn_close:
                sendMessage(Constant.KTICHEN_CLOSE);
                break;
            case R.id.btn_notify:
                readMessage();
                break;
        }
    }
    private void readMessage(){
        if (btn_notify.getText().toString().equals("打开通知")){
            btn_notify.setText("关闭通知");
            mBluetoothService.indicate(Constant.SeviceUUID, Constant.CharacteristicUUID, new BleCharacterCallback() {

                @Override
                public void onSuccess(final BluetoothGattCharacteristic characteristic) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = "";
                            try {
                                message  = new String(characteristic.getValue(),"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            mOutputInfo.append(message);
                            Log.e("test", "message" + message);
                            Log.e("test", "mOutputInfo" + mOutputInfo.toString());
                            txt_jieshou.setText(mOutputInfo.toString());

//                                        char c[] = txt_jieshou.getText().toString().toCharArray();
//                                        if (c[c.length-1] == '\n') {
//                                            Log.e("message", txt_jieshou.getText().toString());
//                                            String txt = txt_jieshou.getText().toString();
//                                            Log.e("message", txt);
//                                            txt_jieshou.setText("");
//
//                                            String weight = txt.substring(0, 3);
//                                            String height = txt.substring(3, 6);
//                                            String r = txt.substring(6, 9);
//                                            String heart = txt.substring(9, 12);
//
//                                            Toast.makeText(ControlActivity.this, weight + "-" + height + "-" + r + "-" + heart, Toast.LENGTH_SHORT).show();
//                                        }
                        }
                    });


                }

                @Override
                public void onFailure(BleException exception) {

                }

                @Override
                public void onInitiatedResult(boolean result) {

                }
            });
        }else {
            btn_notify.setText("打开通知");
            mBluetoothService.stopNotify(
                    Constant.SeviceUUID,
                    Constant.CharacteristicUUID);
        }
    }

    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
        }
        return sb.toString().toUpperCase().trim();
    }

    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    private void sendMessage(String message){
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

   /* private boolean subPackageOnce(BluetoothBuffer buffer) {
        if (null == buffer) return false;
        if (buffer.getBufferSize() >= 14) {
            byte[] rawBuffer =  buffer.getBuffer();
            //求包长
            int pkgSize;
            if (isHead(rawBuffer)){
                pkgSize = byteToInt(rawBuffer[2], rawBuffer[3]);
            }else {
                pkgSize = -1;
                for (int i = 0; i < rawBuffer.length-1; ++i){
                    if (rawBuffer[i] == -2 && rawBuffer[i+1] == 1){
                        buffer.releaseFrontBuffer(i);
                        return true;
                    }
                }
                return false;
            }
            //剥离数据
            if (pkgSize > 0 && pkgSize <= buffer.getBufferSize()) {
                byte[] bufferData = buffer.getFrontBuffer(pkgSize);
                long time = System.currentTimeMillis();
                buffer.releaseFrontBuffer(pkgSize);
                //在这处理数据

                return true;
            }
        }
        return false;
    }*/

}


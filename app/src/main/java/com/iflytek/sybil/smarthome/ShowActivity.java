package com.iflytek.sybil.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {

    TextView txt_show;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mIntent = getIntent();
        String message = mIntent.getStringExtra("message");

        txt_show = (TextView) findViewById(R.id.txt_show);

        txt_show.setText(message);
    }
}
package com.iflytek.sybil.smarthome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.sybil.smarthome.R;
import com.iflytek.sybil.smarthome.db.DBManager;
import com.iflytek.sybil.smarthome.utils.DataActivity;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends TitleActivity {

    Button SQRT;
    EditText typeET, nameET, inboxET, readET, weizhiET;
    private List<String> queryOne;
    boolean haveOneName,haveTwoName,haveThreeName = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        queryOne = DBManager.queryOneList();
    }

    private void initView() {
        setTitle("添加");
        showBackwardView(true);
        SQRT = (Button) findViewById(R.id.AddSqrt);
        SQRT.setOnClickListener(this);
        typeET = findViewById(R.id.typeET);
        nameET = findViewById(R.id.nameET);
        inboxET = findViewById(R.id.inboxET);
        readET = findViewById(R.id.readET);
        weizhiET = findViewById(R.id.weizhiET);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddSqrt:

                String type = typeET.getText().toString();
                String name = nameET.getText().toString();
                String inBox = inboxET.getText().toString();
                String read = readET.getText().toString();
                String weizhi = weizhiET.getText().toString();
                if (isEmpty(type)) {
                    Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(name)) {
                    Toast.makeText(this, "元件名不能为空", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (isEmpty(inBox)) {
                    Toast.makeText(this, "封装不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(read)) {
                    Toast.makeText(this, "播报号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(weizhi)) {
                    Toast.makeText(this, "柜号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "确定", Toast.LENGTH_SHORT).show();

                addData(type,name,inBox,read,weizhi);
//                DataActivity.AddHave();
                Intent intent = new Intent();
                intent.setClass(AddActivity.this, SevenFourClipActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void addData(String type, String name, String inBox, String read, String weizhi) {


        if (queryOne != null) {
            for (int i = 0; i < queryOne.size(); i++) {
                if (type.equals(queryOne.get(i).split(",")[1])) {
                    haveOneName = true;
                    List<String> queryTwo = DBManager.queryTwoList(Integer.parseInt(queryOne.get(i).split(",")[0]));
                    for (int j = 0; j < queryTwo.size(); j++) {
                        if (name.equals(queryTwo.get(j).split(",")[1])) {
                            haveTwoName = true;
                            List<String> queryThree = DBManager.queryThreeList(Integer.parseInt(queryTwo.get(j).split(",")[0]));
                            for (int k = 0; k < queryThree.size(); k++) {
                                if (inBox.equals(queryThree.get(k).split(",")[1])) {
                                    haveThreeName = true;
                                    Toast.makeText(this, "该元件已经存在", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            }

                        }

                    }
                }

            }

            if (!haveOneName) {
                DBManager.insertOne(type);
                int twoID = DBManager.insertTwo(type, name);
                DBManager.insertThree(twoID,inBox,read,weizhi);
            } else if (!haveTwoName) {
                int twoID = DBManager.insertTwo(type, name);
                DBManager.insertThree(twoID,inBox,read,weizhi);
            } else if (!haveThreeName) {
                int twoID = DBManager.queryTwoByName(DBManager.queryOneByName(type), name);
                DBManager.insertThree(twoID,inBox,read,weizhi);
            }
        }

    }


    private boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    private List<String> queryOneListParse(List<String> list) {
        List<String> oneListStrings = new ArrayList<>();
        for (String s : list) {
            String name = s.split(",")[1];
            oneListStrings.add(name);
        }
        return oneListStrings;
    }

    // 解析two数据
    private List<String> queryTwoListParse(List<String> list) {
        List<String> twoListStrings = new ArrayList<>();
        for (String s : list) {
            String name = s.split(",")[1];
            twoListStrings.add(name);
        }
        return twoListStrings;
    }
}


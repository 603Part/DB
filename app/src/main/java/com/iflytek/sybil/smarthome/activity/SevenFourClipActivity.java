package com.iflytek.sybil.smarthome.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.text.Html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.iflytek.sybil.smarthome.R;
import com.iflytek.sybil.smarthome.db.DBManager;
import com.iflytek.sybil.smarthome.db.ThreeBean;
import com.iflytek.sybil.smarthome.utils.Constant;
import com.iflytek.sybil.smarthome.utils.DataActivity;
import com.iflytek.sybil.smarthome.utils.Position;


public class SevenFourClipActivity extends TitleActivity implements View.OnClickListener {

    private Button Sqrt, Over, Add, JION,Delete;

    private Spinner firstSpinner = null;  // 一级
    private Spinner secondSpinner = null;     // 二级
    private Spinner thridSpinner = null;    // 三级
    ArrayAdapter<String> firstAdapter = null;  // 一级适配器
    ArrayAdapter<String> secondAdapter = null;    // 二级适配器
    ArrayAdapter<String> thirdAdapter = null;    // 三级适配器

    private TextView tv_position = null;
    private TextView tv_need = null;

    static int provincePosition = 3;
    String result = "";
    String str = "";
    String giveout = "";

    StringBuilder stringBuilder = new StringBuilder();
    int first, second, third;
    private List<String> queryOne;
    private List<String> queryTwo;
    private List<String> queryThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_four_clip);

        initView();
//        setSpinner();
        setSpinnerUseDB();
        showneedUseDB(DBManager.showNeed());

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SevenFourClipActivity.this, DeletActivity.class);
                SevenFourClipActivity.this.startActivity(intent);
            }
        });

    }

    private void initView() {
        setTitle("智能元件柜");
        showBackwardView(true);
        Sqrt = (Button) findViewById(R.id.sqrt);
        Sqrt.setOnClickListener(this);
        Over = (Button) findViewById(R.id.over);
        Over.setOnClickListener(this);
        Add = (Button) findViewById(R.id.add);
        Add.setOnClickListener(this);
        JION = (Button) findViewById(R.id.Jion);
        JION.setOnClickListener(this);
        Delete = (Button) findViewById(R.id.delet);
        JION.setOnClickListener(this);

    }

    private void showneed(){
        //stringBuilder.append("以下元件缺少：\n");
        for (int i = 0; i < DataActivity.Have.length; i++) {
            for (int j = 0; j < DataActivity.Have[i].length; j++) {
                for (int p = 0; p < DataActivity.Have[i][j].length; p++) {
                    if (DataActivity.Have[i][j][p].equals("1")) {
                        str="元件名："+DataActivity.city[i][j]+"  "+
                                "封装："+DataActivity.county[i][j][p]+"  "+
                                "柜号："+DataActivity.ID[i][j][p]+"\n";
                        stringBuilder.append(str);
                    }
                }
            }
        }
        tv_need.setText(stringBuilder);
    }

    private void showneedUseDB(String stringBuilder){
        //stringBuilder.append("以下元件缺少：\n");
        tv_need.setText(stringBuilder);
    }



    /**
     * 设置下拉框
     */
    private void setSpinnerUseDB() {
        firstSpinner = (Spinner) findViewById(R.id.spin_first);
        secondSpinner = (Spinner) findViewById(R.id.spin_second);
        thridSpinner = (Spinner) findViewById(R.id.spin_third);

        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_need = (TextView) findViewById(R.id.tv_need);
        queryOne = DBManager.queryOneList();
        List<String> oneParseList = queryOneListParse(queryOne); //用于展示，真正操作数据的还是带有id的queryOne
        //绑定适配器和值
        firstAdapter = new ArrayAdapter<>(SevenFourClipActivity.this, android.R.layout.simple_spinner_item, oneParseList);
        firstSpinner.setAdapter(firstAdapter);
        firstSpinner.setSelection(3, true);  //设置默认选中项，此处为默认选中第4个值
        queryTwo = DBManager.queryTwoList(Integer.parseInt(queryOne.get(3).split(",")[0]));
        secondAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this,
                android.R.layout.simple_spinner_item,queryTwoListParse(queryTwo));
        secondSpinner.setAdapter(secondAdapter);
        secondSpinner.setSelection(0, true);  //默认选中第0个
        List<String> strings = DBManager.queryThreeList(Integer.parseInt(queryTwo.get(0).split(",")[0]));
        thirdAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this,
                android.R.layout.simple_spinner_item, queryThreeListParseSimple(strings));
        thridSpinner.setAdapter(thirdAdapter);
        thridSpinner.setSelection(0, true);

        // 一级下拉框监听
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号
                queryTwo = DBManager.queryTwoList(Integer.parseInt(queryOne.get(position).split(",")[0]));
                List<String> twoParseList = queryTwoListParse(queryTwo);
                //将地级适配器的值改变为city[position]中的值
                secondAdapter = new ArrayAdapter<String>(
                        SevenFourClipActivity.this, android.R.layout.simple_spinner_item, twoParseList);
                // 设置二级下拉列表的选项内容适配器
                secondSpinner.setAdapter(secondAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
                first = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        // 二级下拉监听
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                queryThree = DBManager.queryThreeList(Integer.parseInt(queryTwo.get(position).split(",")[0]));
                List<String> queryThreeListParseSimple = queryThreeListParseSimple(queryThree);
                thirdAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this,
                        android.R.layout.simple_spinner_item, queryThreeListParseSimple);
                thridSpinner.setAdapter(thirdAdapter);
                second = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        // 三级下拉框监听
        thridSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<ThreeBean> threeBeans = queryThreeListParse(queryThree);
                ThreeBean threeBean = threeBeans.get(position);
                findPosition(threeBean);
                third = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * 设置下拉框
     */
    private void setSpinner() {
        firstSpinner = (Spinner) findViewById(R.id.spin_first);
        secondSpinner = (Spinner) findViewById(R.id.spin_second);
        thridSpinner = (Spinner) findViewById(R.id.spin_third);

        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_need = (TextView) findViewById(R.id.tv_need);

        //绑定适配器和值
        firstAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this, android.R.layout.simple_spinner_item, DataActivity.province);
        firstSpinner.setAdapter(firstAdapter);
        firstSpinner.setSelection(3, true);  //设置默认选中项，此处为默认选中第4个值

        secondAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this,
                android.R.layout.simple_spinner_item, DataActivity.city[3]);
        secondSpinner.setAdapter(secondAdapter);
        secondSpinner.setSelection(0, true);  //默认选中第0个

        thirdAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this,
                android.R.layout.simple_spinner_item, DataActivity.county[3][0]);
        thridSpinner.setAdapter(thirdAdapter);
        thridSpinner.setSelection(0, true);

        // 一级下拉框监听
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                secondAdapter = new ArrayAdapter<String>(
                        SevenFourClipActivity.this, android.R.layout.simple_spinner_item, DataActivity.city[position]);
                // 设置二级下拉列表的选项内容适配器
                secondSpinner.setAdapter(secondAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
                first = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        // 二级下拉监听
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                thirdAdapter = new ArrayAdapter<String>(SevenFourClipActivity.this,
                        android.R.layout.simple_spinner_item, DataActivity.county[provincePosition][position]);
                thridSpinner.setAdapter(thirdAdapter);
                second = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        // 三级下拉框监听
        thridSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = thridSpinner.getSelectedItem().toString();

//                findPosition(text);
                third = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }
        return r;
    }

    /*
     * 字符串转字节数组
     */
    public static byte[] string2Bytes(String s) {
        byte[] r = s.getBytes();
        return r;
    }

    /*
     * 字符串转16进制字符串
     */
    public static String string2HexString(String s) {
        String r = bytes2HexString(string2Bytes(s));
        return r;
    }

    public static int stringToten(String s) {
        int sum = 0;
        byte[] r = string2Bytes(s);
        for (int i = 0; i < r.length; i++)
            sum = sum * 10 + r[i] - '0';
        return sum;
    }

    public static String intToString(int s) {
        String r = "";
        int i = 100;
        while (i != 0) {
            if (s / i > 0)
                r += (s / i) + '0';
            i = i / 10;
        }
        return r;
    };

    public static String Shu(String s) {
        String r;
        if (s.length() >= 2)
            r = s;
        else
            r = "0" + s;
        return r;
    }

    private void findPosition(ThreeBean tb) {
        tv_position.setVisibility(View.VISIBLE);
        tv_need.setVisibility(View.VISIBLE);

        String three_position = tb.getThree_position()+"";
        if (tb.getHave() == 1)
        {
            tv_position.setText(Html.fromHtml(Html.fromHtml("该元件在第"+"<font color='#FF0000'>"+three_position)+"</font>"+
                    ",并且元件"+"<font color='#FF0000'>"+"不足"+"</font>"+"请及时不齐。"));
        }
        else
        {
            tv_position.setText(Html.fromHtml(Html.fromHtml("该元件在第"+"<font color='#FF0000'>"+three_position)+"</font>"));
        }
        giveout="43434954"+"2C"+string2HexString(Shu(tb.getBobao()))+"2C"+
                string2HexString(Shu(tb.getBobao()))+"0D0A";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sqrt:
                if (Sqrt.getText().toString().equals("确定")) {
                    try {
                        MainActivity.sendMessage(giveout, SevenFourClipActivity.this);
                    } catch (Exception e) {
                        Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.over:
                if (Over.getText().toString().equals("结束本次使用")) {
                    try {
                        MainActivity.sendMessage(Constant.Over, SevenFourClipActivity.this);
                    } catch (Exception e) {
                        Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.add:
                Add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg1) {
                        // 打开另一个activity，通过意图，意图作用是激活其他组件
                        Intent intent02 = new Intent();
                        intent02.setClass(SevenFourClipActivity.this, AddActivity.class);
                        //发送意图.将意图发送给android系统，系统根据意图来激活组件
                        startActivity(intent02);
                    }
                });
                break;

            case R.id.Jion:
                if (Over.getText().toString().equals("刷新")) {
                    try {
                        MainActivity.sendMessage(Constant.Jion, SevenFourClipActivity.this);
                        for (int i = 0; i < 256; i++) {
                            MainActivity.readMessage(SevenFourClipActivity.this);
                            if (Constant.UUID_READ == "31")
                                DataActivity.Have[i / 64][(i / 8) % 8][i % 8] = "0";
                            else
                                DataActivity.Have[i / 64][(i / 8) % 8][i % 8] = "1";
                        }
                        showneedUseDB(DBManager.showNeed());
                    } catch (Exception e) {
                        Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onBackward(View backwardView) {

    }

    // 解析数据
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

    private List<ThreeBean> queryThreeListParse(List<String> list) {
        List<ThreeBean> twoListStrings = new ArrayList<>();
        for (String s : list) {
            ThreeBean tb = new ThreeBean();
            String id = s.split(",")[0];
            String name = s.split(",")[1];
            String three_position = s.split(",")[2];
            String have = s.split(",")[3];
            String two_id = s.split(",")[4];
            String bobao = s.split(",")[5];


            tb.setThree_id(Integer.parseInt(id));
            tb.setName(name);
            tb.setThree_position(Integer.parseInt(three_position));
            tb.setHave(Integer.parseInt(have));
            tb.setTwo_id(Integer.parseInt(two_id));
            tb.setBobao(bobao);

            twoListStrings.add(tb);
        }
        return twoListStrings;
    }
    private List<String> queryThreeListParseSimple(List<String> list) {
        List<String> twoListStrings = new ArrayList<>();
        for (String s : list) {
            String name = s.split(",")[1];
            twoListStrings.add(name);
        }
        return twoListStrings;
    }
}



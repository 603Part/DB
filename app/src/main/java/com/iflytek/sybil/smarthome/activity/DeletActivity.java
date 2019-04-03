package com.iflytek.sybil.smarthome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.iflytek.sybil.smarthome.R;
import com.iflytek.sybil.smarthome.db.DBManager;
import com.iflytek.sybil.smarthome.db.ThreeBean;
import com.iflytek.sybil.smarthome.utils.DataActivity;

import java.util.ArrayList;
import java.util.List;

public class DeletActivity extends TitleActivity {

    private Button Delet_SQRT;

    private Spinner DeletfirstSpinner = null;  // 一级
    private Spinner DeletsecondSpinner = null;     // 二级
    private Spinner DeletthridSpinner = null;    // 三级
    ArrayAdapter<String> DeletfirstAdapter = null;  // 一级适配器
    ArrayAdapter<String> DeletsecondAdapter = null;    // 二级适配器
    ArrayAdapter<String> DeletthirdAdapter = null;    // 三级适配器


    private int parentID = 0;
    private int twoID = 0;
    private int threeID = 0;

    private List<String> queryOne;
    private List<String> queryTwo;
    private List<String> queryThree;

    private boolean isTwoArrayEmpty,isThreeArrayEmpty = false;

    private TextView Delettv_position = null;

    static int provincePosition = 3;
    String result = "";

    int Deletfirst, Deletsecond, Deletthird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delet);
        initView();
        setSpinnerUseDB();
//        setSpinner();
    }

    private void initView() {
        setTitle("删除");
        showBackwardView(true);
        Delet_SQRT = (Button) findViewById(R.id.delet_sqrt);
        Delet_SQRT.setOnClickListener(this);

    }


    private void setSpinnerUseDB() {
        DeletfirstSpinner = (Spinner) findViewById(R.id.delet_first);
        DeletsecondSpinner = (Spinner) findViewById(R.id.delet_second);
        DeletthridSpinner = (Spinner) findViewById(R.id.delet_third);
        Delettv_position = (TextView) findViewById(R.id.delet_tv_position);

        queryOne = DBManager.queryOneList();
        List<String> oneParseList = queryOneListParse(queryOne); //用于展示，真正操作数据的还是带有id的queryOne
        //绑定适配器和值
        DeletfirstAdapter = new ArrayAdapter<String>(DeletActivity.this, android.R.layout.simple_spinner_item, oneParseList);
        DeletfirstSpinner.setAdapter(DeletfirstAdapter);
        DeletfirstSpinner.setSelection(3, true);  //设置默认选中项，此处为默认选中第4个值

        parentID = Integer.parseInt(queryOne.get(3).split(",")[0]);
        queryTwo = DBManager.queryTwoList(parentID);
        queryThree = DBManager.queryThreeList(Integer.parseInt(queryTwo.get(0).split(",")[0]));
        DeletsecondAdapter = new ArrayAdapter<String>(DeletActivity.this,
                android.R.layout.simple_spinner_item,queryTwoListParse(queryTwo));
        DeletsecondSpinner.setAdapter(DeletsecondAdapter);
        DeletsecondSpinner.setSelection(0, true);  //默认选中第0个

        twoID = Integer.parseInt(queryTwo.get(0).split(",")[0]);
        List<String> strings = DBManager.queryThreeList(twoID);
        threeID = Integer.parseInt(queryThree.get(0).split(",")[0]);
        DeletthirdAdapter = new ArrayAdapter<String>(DeletActivity.this,
                android.R.layout.simple_spinner_item, queryThreeListParseSimple(strings));
        DeletthridSpinner.setAdapter(DeletthirdAdapter);
        DeletthridSpinner.setSelection(0, true);

        // 一级下拉框监听
        DeletfirstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号
                isTwoArrayEmpty = false;
                parentID = Integer.parseInt(queryOne.get(position).split(",")[0]);
                queryTwo = DBManager.queryTwoList(parentID);

                List<String> twoParseList = queryTwoListParse(queryTwo);
                if (twoParseList.size() == 1) {
                    isTwoArrayEmpty = true;
                }
                //将地级适配器的值改变为city[position]中的值
                DeletsecondAdapter = new ArrayAdapter<String>(
                        DeletActivity.this, android.R.layout.simple_spinner_item, twoParseList);
                // 设置二级下拉列表的选项内容适配器
                DeletsecondSpinner.setAdapter(DeletsecondAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
//                first = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        // 二级下拉监听
        DeletsecondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                isThreeArrayEmpty = false;
                twoID = Integer.parseInt(queryTwo.get(position).split(",")[0]);
                queryThree = DBManager.queryThreeList(twoID);
                if (queryThree.size() == 1) {
                    isThreeArrayEmpty = true;
                }
                List<String> queryThreeListParseSimple = queryThreeListParseSimple(queryThree);
                DeletthirdAdapter = new ArrayAdapter<String>(DeletActivity.this,
                        android.R.layout.simple_spinner_item, queryThreeListParseSimple);
                DeletthridSpinner.setAdapter(DeletthirdAdapter);
//                second = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        // 三级下拉框监听
        DeletthridSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<ThreeBean> threeBeans = queryThreeListParse(queryThree);
                threeID = threeBeans.get(position).getThree_id();
                ThreeBean threeBean = threeBeans.get(position);
//                findPosition(threeBean);
//                third = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * 设置下拉框
     */
//    private void setSpinner() {
//        DeletfirstSpinner = (Spinner) findViewById(R.id.delet_first);
//        DeletsecondSpinner = (Spinner) findViewById(R.id.delet_second);
//        DeletthridSpinner = (Spinner) findViewById(R.id.delet_third);
//        Delettv_position = (TextView) findViewById(R.id.delet_tv_position);
//
//        //绑定适配器和值
//        DeletfirstAdapter = new ArrayAdapter<String>(DeletActivity.this, android.R.layout.simple_spinner_item, DataActivity.province);
//        DeletfirstSpinner.setAdapter(DeletfirstAdapter);
//        DeletfirstSpinner.setSelection(3, true);  //设置默认选中项，此处为默认选中第4个值
//
//        DeletsecondAdapter = new ArrayAdapter<String>(DeletActivity.this,
//                android.R.layout.simple_spinner_item, DataActivity.city[3]);
//        DeletsecondSpinner.setAdapter(DeletsecondAdapter);
//        DeletsecondSpinner.setSelection(0, true);  //默认选中第0个
//
//        DeletthirdAdapter = new ArrayAdapter<String>(DeletActivity.this,
//                android.R.layout.simple_spinner_item, DataActivity.county[3][0]);
//        DeletthridSpinner.setAdapter(DeletthirdAdapter);
//        DeletthridSpinner.setSelection(0, true);
//
//        // 一级下拉框监听
//        DeletfirstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                //position为当前省级选中的值的序号
//
//                //将地级适配器的值改变为city[position]中的值
//                DeletsecondAdapter = new ArrayAdapter<String>(
//                        DeletActivity.this, android.R.layout.simple_spinner_item, DataActivity.city[position]);
//                // 设置二级下拉列表的选项内容适配器
//                DeletsecondSpinner.setAdapter(DeletsecondAdapter);
//                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
//                Deletfirst = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//
//        });
//
//
//        // 二级下拉监听
//        DeletsecondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int position, long arg3) {
//                DeletthirdAdapter = new ArrayAdapter<String>(DeletActivity.this,
//                        android.R.layout.simple_spinner_item, DataActivity.county[provincePosition][position]);
//                DeletthridSpinner.setAdapter(DeletthirdAdapter);
//                Deletsecond = position;
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });
//
//        // 三级下拉框监听
//        DeletthridSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String text = DeletthridSpinner.getSelectedItem().toString();
//
////                findPosition(text);
//                Deletthird = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }


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
    }

    ;

    public static String Shu(String s) {
        String r;
        if (s.length() >= 2)
            r = s;
        else
            r = "0" + s;
        return r;
    }


    private void findPosition(ThreeBean tb) {
        Delettv_position.setVisibility(View.VISIBLE);
        result = "类型：" + DataActivity.province[Deletfirst] + "\n" +
                "元器件：" + DataActivity.city[Deletfirst][Deletsecond] + "\n" +
                "封装：" + DataActivity.county[Deletfirst][Deletsecond][Deletthird] + "\n";

        Delettv_position.setText(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delet_sqrt:
                try {
                    DBManager.remove(threeID);
                    if (isThreeArrayEmpty) {
                        DBManager.remove(twoID,parentID);
                        if (isTwoArrayEmpty) {
                            DBManager.removeOne(parentID);
                        }
                    }

//                    DataActivity.Delet(Deletfirst,Deletsecond,Deletthird);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(DeletActivity.this, SevenFourClipActivity.class);
                startActivity(intent);
                break;

        }

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
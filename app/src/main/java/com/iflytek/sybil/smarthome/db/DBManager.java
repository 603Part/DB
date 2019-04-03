package com.iflytek.sybil.smarthome.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static String DB_NAME = "db.db";
    private Context mContext;
    private static SQLiteDatabase manager;

    public static void copyDb(Context mContext,String tab_name) {
        InputStream in = null;
        FileOutputStream out = null;

        String path = "/data/data/"+mContext.getPackageName()+"/databases";
        File file = new File(path + "/" +DB_NAME);
        try{
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
                in = mContext.getAssets().open(DB_NAME);
                out = new FileOutputStream(file);
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1) {
                    out.write(buf,0,length);
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {

            }
        }
    }


    public static SQLiteDatabase dbManager(Context mContext) {
        String dbPath = "/data/data/" + mContext.getPackageName()
                + "/databases/" + DB_NAME;
        manager = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        return manager;
    }

    private static final String TAG = "DBManager";
    public static void query() {
        Cursor cursor = manager.rawQuery("SELECT * FROM one",null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Log.d(TAG, "query: " + id + ", " + name);
        }

        cursor.close();
    }

    /**
     * 获取第一级菜单
     */
    public static List<String> queryOneList() {
        Cursor cursor = manager.rawQuery("SELECT * FROM one",null);
        List<String> oneList = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            oneList.add(id + "," + name);
            Log.d(TAG, "query: " + id + ", " + name);
        }
        return oneList;
    }

    /**
     * 获取第二级
     */
    public static List<String> queryTwoList(int id) {
        Cursor cursor = manager.rawQuery("SELECT * FROM two WHERE parent_id = " + id,null);
        List<String> twoList = new ArrayList<>();
        while(cursor.moveToNext()){
            int two_id = cursor.getInt(0);
            String name = cursor.getString(1);
            int parent_id = cursor.getInt(2);
            twoList.add(two_id + "," + name+","+parent_id);
            Log.d(TAG, "query: " + two_id + ", " + name);
        }
        cursor.close();
        return twoList;
    }

    /**
     * 获取第三级菜单
     */
    public static List<String> queryThreeList(int id) {
        Cursor cursor = manager.rawQuery("SELECT * FROM three WHERE two_id = " + id,null);
        List<String> threeList = new ArrayList<>();
        while(cursor.moveToNext()){
            int threeId = cursor.getInt(0);
            String name = cursor.getString(1);
            int threePosition = cursor.getInt(2); //柜门
            int have = cursor.getInt(3);
            int two_id = cursor.getInt(4);
            String bobao = cursor.getString(5);
            String all = String.format("%d,%s,%d,%d,%d,%s", threeId, name, threePosition, have, two_id,bobao);
            Log.d(TAG, "queryThreeList: " + all);
            threeList.add(all);
        }
        cursor.close();
        return threeList;
    }

    public static String showNeed() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
//        SELECT two.name,three_name,three_position FROM two,three WHERE three.have=1 AND three.two_id = two.id
        Cursor cursor = manager.rawQuery("SELECT two.name,three_name,three_position FROM two,three WHERE three.have=1 AND three.two_id = two.id"
                , null);
        while (cursor.moveToNext()) {
            String str = "元件名：" + cursor.getString(0) + "  "+
                    "封装："+cursor.getString(1)+"  "+
                    "柜号："+cursor.getString(2)+"\n";
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static boolean remove(int three_id) {
        manager.beginTransaction();
        boolean b = false;
        try {
            manager.execSQL("DELETE FROM three WHERE three_id =" + three_id);
            manager.setTransactionSuccessful();
            b = true;
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        } finally {
            manager.endTransaction();
            return b;
        }
    }

    public static boolean remove(int two_id,int parent_id) {
        manager.beginTransaction();
        boolean b = false;
        try {
            manager.execSQL("DELETE FROM three WHERE two_id =" + two_id);
            manager.execSQL("DELETE FROM two WHERE parent_id = '"+parent_id+"' AND id = '"+two_id+"'");
//            manager.execSQL("DELETE FROM one WHERE id =" + parent_id);
            manager.setTransactionSuccessful();
            b = true;
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        } finally {
            manager.endTransaction();
            return b;
        }
    }

    public static boolean removeOne(int parent_id) {
        manager.beginTransaction();
        boolean b = false;
        try {
            manager.execSQL("DELETE FROM one WHERE id =" + parent_id);
            manager.setTransactionSuccessful();
            b = true;
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        } finally {
            manager.endTransaction();
            return b;
        }
    }

    public static void insertOne(String type) {
        try {
            manager.execSQL("INSERT INTO one values(NULL,'"+type+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int insertTwo(String type,String name) {
        int twoID = -1;
        try {
            int id = queryOneByName(type);
            manager.execSQL("INSERT INTO two values(NULL,'"+name+"','"+id+"',NULL,NULL)");
            twoID = queryTwoByName(id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return twoID;
    }

    public static void insertThree(int twoID,String name,String bohao,String position) {
        try {
            manager.execSQL("INSERT INTO three values(NULL,'" + name + "','" + position + "','" + twoID + "','0','" + bohao + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int queryOneByName(String type) {
        Cursor cursor = manager.rawQuery("SELECT * FROM one where name = '"+type+"'",null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        return id;
    }

    public static int queryTwoByName(int parentID,String name) {
        Cursor cursor = manager.rawQuery("SELECT * FROM two where name = '"+name+"' AND parent_id = '"+parentID+"'",null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        return id;
    }
}

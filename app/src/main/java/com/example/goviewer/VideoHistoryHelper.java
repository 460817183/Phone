package com.example.goviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shinelon on 2017/4/17.
 */

public class VideoHistoryHelper extends SQLiteOpenHelper{
    public static String TABLE_NAME="history";
    private VideoHistoryHelper(Context context) {
        super(context,TABLE_NAME, null, 1);
    }
    private static VideoHistoryHelper instance;
    public static synchronized VideoHistoryHelper getInstance(Context context){
        if(instance==null){
            instance=new VideoHistoryHelper(context);
        }

        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE_NAME + "("
                + "userid TEXT PRIMARY KEY,"
                + "videoid TEXT,"
                + "time INTEGER);";

        Log.e("table oncreate", "create table");
        db.execSQL(sql); 		//创建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


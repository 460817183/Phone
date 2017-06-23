package com.example.goviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Shinelon on 2017/4/17.
 */

public class VideoHistoryUtils {

    private static SQLiteDatabase db;
    private VideoHistoryHelper helper;
    public VideoHistoryUtils(Context context){
        helper= VideoHistoryHelper.getInstance(context);
        if(db==null){
            db=helper.getWritableDatabase();
        }

    }

    public synchronized void insertHistory(String userid,String videoId,long time){
        if(!db.isOpen()){
            db=helper.getWritableDatabase();
        }
        L.e("插入或更新历史记录userid:"+userid+"   videoid:"+videoId+"    time:"+time);
        ContentValues values=new ContentValues();
        long result;
        Cursor query = db.query(VideoHistoryHelper.TABLE_NAME, null, "userid=? and videoid=?", new String[]{userid, videoId}, null, null, null);
        if(query.moveToNext()){
            query.close();
            values.put("time",time);
            result=db.update(VideoHistoryHelper.TABLE_NAME,values,"userid=? and videoid=?",new String[]{userid, videoId});
        }else{
            values.put("userid",userid);
            values.put("videoid",videoId);
            values.put("time",time);
            result=db.insert(VideoHistoryHelper.TABLE_NAME,"time",values);
        }
        close();
    }
    public synchronized long queryHistory(String userid,String videoId){
        if(!db.isOpen()){
            db=helper.getWritableDatabase();
        }
        Cursor query=null;
        try{
            query = db.query(VideoHistoryHelper.TABLE_NAME, null, "userid=? and videoid=?", new String[]{userid, videoId}, null, null, null);
            L.e("查询历史记录:userid:"+userid+"   videoid:"+videoId);
            if(query.moveToNext()){
                L.e("进来了");
                L.e("查询到:"+query.getLong(query.getColumnIndex("time")));
                return query.getLong(query.getColumnIndex("time"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(query!=null){
                query.close();
            }
            close();
        };
        return 0;
    }

    public synchronized void deleteHistory(String userid,String videoId){
        if(!db.isOpen()){
            db=helper.getWritableDatabase();
        }
        db.delete(VideoHistoryHelper.TABLE_NAME,"userid=? and videoid=?",new String[]{userid, videoId});
        close();
        L.e("删除历史记录");
    }
    public synchronized void close(){
        if(db.isOpen()){
            db.close();
            L.e("关闭数据库");
        }

    }
}

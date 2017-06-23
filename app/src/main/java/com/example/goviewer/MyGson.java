package com.example.goviewer;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by xiao on 2016/11/2.
 */

public class MyGson {
    public static Gson gson=new Gson();

    /**
     * 解析json
     * */
    public static <T> T  fromJson (String json,Class<T> clazz){
            return gson.fromJson(json,clazz);
    }
    /**
     * 解析json数组
     * */
    public static <T> ArrayList<T>  fromJsonArray (String json,Class<T> clazz){
        ArrayList<T> list=new ArrayList<T>();
        try {
            JSONArray array=new JSONArray(json);
            T t;
            for (int i=0;i<array.length();i++){
                t=gson.fromJson(array.getJSONObject(i).toString(),clazz);
                list.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}

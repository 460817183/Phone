package com.example.goviewer;

import com.uzmap.pkg.uzcore.UZResourcesIDFinder;

/**
 * Created by xqf on 2017/5/24.
 */

public class ApiUtils {
    public static final boolean isAndroid=false;

    public static int getResDrawableID(int resId,String resString){
        if(isAndroid){
            return resId;
        }else{
            return UZResourcesIDFinder.getResDrawableID(resString);
        }
    }
    public static int getResIdID(int resId,String resString){
        if(isAndroid){
            return resId;
        }else{
            return UZResourcesIDFinder.getResIdID(resString);
        }
    }
    public static int getResLayoutID(int resId,String resString){
        if(isAndroid){
            return resId;
        }else{
            return UZResourcesIDFinder.getResLayoutID(resString);
        }
    }
    public static int getResRawID(int resId,String resString){
        if(isAndroid){
            return resId;
        }else{
            return UZResourcesIDFinder.getResRawID(resString);
        }
    }
    public static int getResStyleID(int resId,String resString){
        if(isAndroid){
            return resId;
        }else{
            return UZResourcesIDFinder.getResStyleID(resString);
        }
    }
}

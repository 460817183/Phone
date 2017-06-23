package com.example.goviewer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by xiao on 2016/10/31.
 */

public class MyHttpUtils {
    /**
     * 发起请求自选请求方式
     * @param method 0是get请求 1是post请求
     * */
    public static void request(int method, RequestParams params, final Handler handler, final int what){
        params.setCacheMaxAge(0);
        params.setConnectTimeout(20 * 1000);
        Callback.CommonCallback<String> callback= new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(handler!=null){
                    Message message = handler.obtainMessage();
                    message.what=what;
                    message.obj=result;
                    message.sendToTarget();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(handler!=null) {
                    Log.e("test","网络请求出错:"+ex.getMessage());
                    Message message = handler.obtainMessage();
                    message.what = Constants.HTTP_ERRORCODE;
                    message.obj = ex;
                    message.sendToTarget();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        if(method==1){
            x.http().post(params,callback);
        }else {
            x.http().get(params,callback);
        }
    }

    /**
     * 发起GET请求
     * */
    public static void request(final RequestParams params, final Handler handler, final int what){
        params.setCacheMaxAge(0);
        params.setConnectTimeout(20 * 1000);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if(handler!=null) {
                    Message message = handler.obtainMessage();
                    message.what = what;
                    message.obj = result;
                    message.sendToTarget();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(handler!=null) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.HTTP_ERRORCODE;
                    message.obj = ex;
                    message.sendToTarget();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /*
    * @params saveState   1 进入的时候      2 退出的时候
    * */
    public static void saveUserHistory(Context context, final Handler handler, String stat, final int saveState){
        RequestParams params=new RequestParams(Constants.BASE_URL+Constants.SAVE_USER_HISTORY);
        if(saveState==1){
            params.addBodyParameter("stat",stat+"");
            params.addBodyParameter("userId", UserInfo.getUserId(context));
        }else{
            params.addBodyParameter("id",stat+"");
        }
        params.setCacheMaxAge(0);
        params.setConnectTimeout(20 * 1000);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(saveState==1){
                    L.e("进入时轨迹返回:"+result);
                }else{
                    L.e("退出时轨迹返回:"+result);
                }
                if(handler!=null) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.TRAJECTORY;
                    message.obj = result;
                    message.sendToTarget();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(handler!=null) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.HTTP_ERRORCODE;
                    message.obj = ex;
                    message.sendToTarget();
                    ex.printStackTrace();
                }
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }



    public static void request(RequestParams params, final OnRequestListener listener){
        params.setCacheMaxAge(0);
        params.setConnectTimeout(20 * 1000);
        Callback.CommonCallback<String> callback= new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                listener.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                listener.onFail(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        x.http().get(params,callback);

    }
}

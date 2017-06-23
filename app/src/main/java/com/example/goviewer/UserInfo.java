package com.example.goviewer;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by Wsh on 2016/7/8.
 */
public class UserInfo {
    private final static String version = "72";
    public static String userId = "-1";
    public static boolean isSdk=false;
    public static String userName = "-1";

    public static String userAccount = "-1";

    public static String userPwd = "-1";

    public static String userFastLogin = "-1";

    public static String getUserId(Context context) {

            DatabaseUtil databaseUtil=new DatabaseUtil(context);
            if("-1".equals(userId)||userId==null){
                if (databaseUtil.queryAll().size() != 0) {
                    Person person = databaseUtil.queryByid(1);
                    UserInfo.setUserId(person.getPassword());
                    UserInfo.setUserAccount(person.getAccount());
                }
            }


        return userId;
    }
    public static String getUserId() {
        return userId;
    }


    public static void setUserId(String userId) {
        UserInfo.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfo.userName = userName;
    }

    public static String getUserAccount() {
        return userAccount;
    }

    public static void setUserAccount(String userAccount) {
        UserInfo.userAccount = userAccount;
    }

    public static void setUserPwd(String userPwd) {
        UserInfo.userPwd = userPwd;
    }

    public static String getUserPwd() {
        return userPwd;
    }

    public static String getVersion() {
        return version;
    }

    public static void setUserFastLogin(String userFastLogin) {
        UserInfo.userFastLogin = userFastLogin;
    }

    public static String getUserFastLogin() {
        return userFastLogin;
    }
}

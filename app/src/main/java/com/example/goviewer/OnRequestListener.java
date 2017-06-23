package com.example.goviewer;

/**
 * Created by Shinelon on 2017/3/13.
 */

public interface OnRequestListener<T> {
    void onSuccess(T result);
    void onFail(String error);
}

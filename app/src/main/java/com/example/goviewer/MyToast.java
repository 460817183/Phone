package com.example.goviewer;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	private static Toast toast;
	private MyToast(){}
	public static synchronized void show(Context context,String text){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}else{
			toast.setText(text);
		}
		toast.show();
	}
	public static synchronized void showLong(Context context,String text){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_LONG);
		}else{
			toast.setText(text);
		}
		toast.show();
	}
}

package com.example.goviewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by xqf on 2017/5/22.
 */

public class BaseActivity extends Activity{
    protected Activity context;
    /*AutoLayout*/
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT))
        {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT))
        {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT))
        {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;
        Log.e("test","onCreateView");
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.e("test","super.onCreate");
    }
}

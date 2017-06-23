package com.example.goviewer;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by xiao on 2016/10/28.
 */

public class AnimationUtils {
    /**
     * @param view      要执行动画的控件
     * @param scaleType true表示放大  false表示缩小
     */
    public static void startScaleXYAnimation(View view, boolean scaleType) {
        ScaleAnimation scaleAnimation;
        if (scaleType) {
            scaleAnimation = new ScaleAnimation(1.0f, 1.04f, 1.0f, 1.04f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            scaleAnimation = new ScaleAnimation(1.04f, 1, 1.04f, 1,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        }
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }

    /**
     * 选项gridview图片的选中动画
     */
    public static void chooseGrid(View view) {
        ScaleAnimation scaleAnimation;

        scaleAnimation = new ScaleAnimation(0.98f, 1.00f, 0.98f, 1.00f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }


    /**
     * @param view 要执行动画的控件
     * @param time 执行动画的时间
     * @param x    横向相对于自己的起始位置 0.0-1.0
     * @param x1   横向相对于自己的结束位置
     * @param y    纵向相对于自己的起始位置
     * @param y1   纵向相对于自己的结束位置
     **/
    public static void startTranslateAnimation(View view, float x, float x1, float y, float y1, int time) {
        TranslateAnimation translateAnimation;
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, x,
                Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, y,
                Animation.RELATIVE_TO_SELF, y1);
        translateAnimation.setDuration(time);
        view.startAnimation(translateAnimation);
    }

    /**
     * //@param view 要执行动画的控件
     * //@param time 执行动画的时间
     //@param x    横向相对于自己的起始位置 0.0-1.0
     //@param x1   横向相对于自己的结束位置
     // @param y    纵向相对于自己的起始位置
     // @param y1   纵向相对于自己的结束位置
     **/
    public static void startTranslateAndScaleAnimation(View view,boolean scaleType) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation translateAnimation;
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 2);
        translateAnimation.setDuration(300);
        set.addAnimation(translateAnimation);

        ScaleAnimation scaleAnimation;
        if (scaleType) {
            scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                    Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f);
        } else {
            scaleAnimation = new ScaleAnimation(1.04f, 1, 1.04f, 1,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        }
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        set.addAnimation(scaleAnimation);
        set.setFillAfter(true);

        view.startAnimation(set);
    }


    public static void hailuoAnimation(final View v, final Animation.AnimationListener listener){
        AnimationSet set=new AnimationSet(true);
        int bigLength=v.getWidth();
        int x= (int) (903* AutoUtils.getPercentWidth1px()-bigLength*(1-73f/242f)/2+73* AutoUtils.getPercentWidth1px());
        int y= (int) (445* AutoUtils.getPercentHeight1px()-bigLength*(1-73f/242f)/2+73* AutoUtils.getPercentHeight1px());
        TranslateAnimation translateAnimation=new TranslateAnimation(0,x,0,y);
        ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f, 73f/242f, 1.0f, 73f/242f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimation);
        set.setDuration(2000);
        set.setFillAfter(true);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(listener!=null){
                            listener.onAnimationStart(animation);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(listener!=null){
                            listener.onAnimationEnd(animation);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        if(listener!=null){
                            listener.onAnimationRepeat(animation);
                        }
                    }
                });
                v.startAnimation(alphaAnimation);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }

    /*
    *
    * @params orientation 方向  true 向左切换  false  向右切换
    * */
    public static void switchPage(final View view1,final View view2, boolean orientation){
        TranslateAnimation translateAnimation1;
        TranslateAnimation translateAnimation2;
        if(orientation){
            translateAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view1.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{

            translateAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            translateAnimation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view1.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        translateAnimation1.setDuration(300);
        translateAnimation2.setDuration(300);
        view2.setVisibility(View.VISIBLE);
        view1.startAnimation(translateAnimation1);
        view2.startAnimation(translateAnimation2);

    }


    /*
    *
    * @params orientation 方向  true 向上切换  false  向下切换
    * */
    public static void switchHomepPage(final View view1,final View view2, boolean orientation){
        TranslateAnimation translateAnimation1;
        TranslateAnimation translateAnimation2;
        if(orientation){
            translateAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, -1);
            translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);
            translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view1.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{

            translateAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 1);
            translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1,
                    Animation.RELATIVE_TO_SELF, 0);
            translateAnimation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view1.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        translateAnimation1.setDuration(300);
        translateAnimation2.setDuration(300);
        view2.setVisibility(View.VISIBLE);
        view1.startAnimation(translateAnimation1);
        view2.startAnimation(translateAnimation2);

    }


    public static void startRotateAnimation(View view){
        RotateAnimation rotateAnimation=new RotateAnimation(0f,  360f,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10*1000);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
    }


}

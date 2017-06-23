package com.example.goviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by xiao on 2016/12/6.
 */

public class MyVideo extends RelativeLayout{
    public MyVideo(Context context) {
        super(context);
    }

    private VideoView myvideoview;
    SeekBar seekbar;//进度条
    private LinearLayout controller;  //视频控制控件
    private TextView time_current;   //当前时间
    private TextView time;            // 总时间
    private ImageButton reloading; //重新播放
    private ImageButton rew; //快退
    private ImageButton pause; //播放/暂停
    private ImageButton ffwd; //快进
    private ImageButton collection; //收藏
    private ImageView video_loading; //正在加载
    private boolean isAttachedToWindow;   //控件是否加载
    int showTimeoutMs = 3000;         //控制控件显示时间
    private boolean dragging;   //进度条是否在拖拽状态
    private Formatter formatter;
    private StringBuilder formatBuilder;
    private boolean isCollection;
    private boolean canShowLoading=true;
    private boolean canShowController=true;
    private boolean isUserPause=false;
    private boolean isPrepared=false;  //是否准备完毕
    private int currentPosition;//当前位置
    private int duration=0;//视频长度
    private int errorPosition=0;//发生-1004错误的位置
    private int errorDuration=0;//发生-1004视频的长度
    private Uri uri=null;//视频的Uri
    public MyVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        init(context);
        initListener();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        },1000);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateProgress();
                postDelayed(this,1000);
            }
        }, 1000);
    }
    private void initListener() {
        myvideoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(final MediaPlayer mp, int what, int extra) {
                errorPosition=mp.getCurrentPosition();
                errorDuration=mp.getDuration();
                if(extra==-1004){
                    L.e("视频播放出错了");
                    isPrepared=false;
                    myvideoview.setVideoURI(uri);
                    myvideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer arg0) {
                            // TODO Auto-generated method stub
                            myvideoview.start();
                            myvideoview.seekTo(errorPosition);

                            time.setText(stringForTime(errorDuration));
                            time_current.setText(stringForTime(errorPosition));
                            mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                                @Override
                                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                                    seekbar.setSecondaryProgress(percent*10);
                                }
                            });
                            hideLoading();
                            updatePlayPauseButton();
                            updateProgress();
                            isPrepared=true;
                        }
                    });
                }
                return true;
            }
        });
        myvideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared=true;
                mp.start();
                time.setText(stringForTime(mp.getDuration()));
                time_current.setText(stringForTime(mp.getCurrentPosition()));
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        seekbar.setSecondaryProgress(percent*10);
                    }
                });
                hideLoading();
                updatePlayPauseButton();
                updateProgress();
                if(listener!=null){
                    listener.onPrepared(mp);
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new MyListener());
        reloading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                reloading();
            }
        });
        rew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                rew();
            }
        });
        ffwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ffwd();
            }
        });
        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myvideoview.isPlaying()){
                    myvideoview.pause();
                    isUserPause=true;
                }else{
                    myvideoview.start();
                    isUserPause=false;
                }
                hideAfterTimeout();
                updatePlayPauseButton();
            }
        });
        collection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(collectionListener!=null){
                    collectionListener.onClick(view);
                    hideAfterTimeout();
                }
            }
        });
        pause.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (myvideoview.isPlaying()&&myvideoview.getDuration()!=0) {
                        pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_pause1,"video_pause1"));//R.drawable.video_pause1);//
                    } else {
                        pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_play1,"video_play1"));//R.drawable.video_play1);//
                    }
                } else {
                    if (myvideoview.isPlaying()&&myvideoview.getDuration()!=0) {
                        pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_pause,"video_pause"));//R.drawable.video_pause);//
                    } else {
                        pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_play,"video_play"));//R.drawable.video_play);//
                    }
                }
            }
        });
        reloading.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    reloading.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_reloading1,"video_reloading1"));//R.drawable.video_reloading1);//
                } else {
                    reloading.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_reloading,"video_reloading"));//R.drawable.video_reloading);//
                }
            }
        });
        rew.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    rew.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_rew1,"video_rew1"));//R.drawable.video_rew1);//
                } else {
                    rew.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_rew,"video_rew"));//R.drawable.video_rew);//
                }
            }
        });
        ffwd.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ffwd.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_ffwd1,"video_ffwd1"));//R.drawable.video_ffwd1);//
                } else {
                    ffwd.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_ffwd,"video_ffwd"));//R.drawable.video_ffwd);//
                }
            }
        });
        collection.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (isCollection) {
                        collection.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_collectioned1,"video_collectioned1"));//R.drawable.video_collectioned1);//
                    } else {
                        collection.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_collection1,"video_collection1"));//R.drawable.video_collection1);//
                    }
                } else {
                    if (isCollection) {
                        collection.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_collectioned,"video_collectioned"));//R.drawable.video_collectioned);//
                    } else {
                        collection.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_collection,"video_collection"));//R.drawable.video_collection);//
                    }
                }
            }
        });
    }

    //重新播放
    private void reloading() {
        if (myvideoview != null) {
            myvideoview.seekTo(0);
            postDelayed(updateProgressAction,1000);
            hideAfterTimeout();
        }
    }
    //快退
    private void rew() {
        if (myvideoview != null) {
            if (myvideoview.getCurrentPosition() > 30 * 1000) {
                myvideoview.seekTo(myvideoview.getCurrentPosition() - 30 * 1000);
            }
            postDelayed(updateProgressAction,1000);
            hideAfterTimeout();
        }
    }
    //快进
    private void ffwd() {
        if (myvideoview != null) {
            if (myvideoview.getCurrentPosition() < myvideoview.getDuration() - 30 * 1000) {
                myvideoview.seekTo(myvideoview.getCurrentPosition() + 30 * 1000);
            }
            postDelayed(updateProgressAction,1000);
            hideAfterTimeout();
        }
    }


    private void init(Context context) {
        View view=View.inflate(context, ApiUtils.getResLayoutID(R.layout.ui_myvideo,"ui_myvideo"),null);//R.layout.ui_myvideo,this);//
        myvideoview= (VideoView) view.findViewById(ApiUtils.getResIdID(R.id.myvideoview,"myvideoview"));//R.id.myvideoview);//
        seekbar = (SeekBar) view.findViewById(ApiUtils.getResIdID(R.id.seekBar,"seekBar"));//R.id.seekBar);//
        controller = (LinearLayout) view.findViewById(ApiUtils.getResIdID(R.id.controller,"controller"));//R.id.controller);//
        time_current = (TextView) view.findViewById(ApiUtils.getResIdID(R.id.time_current,"time_current"));//R.id.time_current);//
        time = (TextView) view.findViewById(ApiUtils.getResIdID(R.id.time,"time"));//R.id.time);//
        reloading = (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.reloading,"reloading"));//R.id.reloading);//
        rew = (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.rew,"rew"));//R.id.rew);//
        pause = (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.pause,"pause"));//R.id.pause);//
        ffwd = (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.ffwd,"ffwd"));//R.id.ffwd);//
        collection = (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.collection,"collection"));//R.id.collection);//
        video_loading = (ImageView) view.findViewById(ApiUtils.getResIdID(R.id.video_loading,"video_loading"));//R.id.video_loading);//
        AutoUtils.auto(view);
        addView(view);
    }
    private OnClickListener collectionListener;
    //设置收藏的监听
    public void setCollectionClick(OnClickListener listener) {
        collectionListener=listener;
    }

    private void updatePlayPauseButton() {
        if (controller.getVisibility()==GONE || !isAttachedToWindow) {
            return;
        }
        if (myvideoview.isPlaying()) {
            if(pause.hasFocus()){
                pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_pause1,"video_pause1"));//R.drawable.video_pause1);//
            }else{
                pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_pause,"video_pause"));//R.drawable.video_pause);//
            }
        } else {
            if(pause.hasFocus()){
                pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_play1,"video_play1"));//R.drawable.video_play1);//
            }else{
                pause.setImageResource(ApiUtils.getResDrawableID(R.drawable.video_play,"video_play"));//R.drawable.video_play);//
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show();

        return super.onTouchEvent(event);
    }

    private String stringForTime(long timeMs) {
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }
    private boolean isCanUpdateProgress=true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(controller.getVisibility()==VISIBLE){
            hideAfterTimeout();
        }else{
            show();
        }
        return super.onKeyDown(keyCode, event);
    }
    public Runnable progressRunnable=new Runnable() {
        @Override
        public void run() {
            isCanUpdateProgress=true;
        }
    };
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(seekbar.hasFocus()){
            if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT||keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
                isCanUpdateProgress=false;
                removeCallbacks(progressRunnable);
                postDelayed(progressRunnable,800);
            }
        }else{
            isCanUpdateProgress=true;
        }
        return super.onKeyUp(keyCode, event);
    }
    private final class MyListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {

            if (fromUser) {
                hideAfterTimeout();
                removeCallbacks(updateProgressAction);

//                MyToast.show(getContext(),"seekto:"+(i/1000*myvideoview.getDuration())+"  getDuration:"+myvideoview.getDuration());
                myvideoview.seekTo(i*myvideoview.getDuration()/1000);
                time_current.setText(stringForTime(positionValue(i)));
                isCanUpdateProgress=false;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            dragging = true;
            removeCallbacks(hideAction);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            myvideoview.seekTo((int) positionValue(seekBar.getProgress()));
            hideAfterTimeout();
        }
    }
    private long positionValue(int progress) {
        long duration = myvideoview == null ? 0 : myvideoview.getDuration();
        return (duration * progress) / 1000;
    }
    public void show(){
        if (controller.getVisibility()!=VISIBLE&&canShowController) {
            updateProgress();
            controller.setVisibility(VISIBLE);
            pause.requestFocus();
        }else{
            hideController();
        }
//        pause.requestFocus();
        hideAfterTimeout();
    }

    public boolean isControllerShow(){
        if (controller.getVisibility()==VISIBLE) {
            return true;
        }else{
            return false;
        }
    }
    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        if (showTimeoutMs > 0&&isAttachedToWindow) {
            postDelayed(hideAction, showTimeoutMs);
        }
    }
    private Runnable hideAction = new Runnable() {
        @Override
        public void run() {
            if (controller.getVisibility() == VISIBLE) {
                controller.setVisibility(GONE);
                isCanUpdateProgress=true;
            }
            removeCallbacks(hideAction);
            removeCallbacks(updateProgressAction);
        }
    };
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    public void postUpdateProgress(){
        post(updateProgressAction);
    }

    private void updateProgress(){
        if (controller.getVisibility() == GONE || !isAttachedToWindow||dragging||!myvideoview.isPlaying()||!isCanUpdateProgress) {
            return;
        }
        long duration = myvideoview == null ? 0 : myvideoview.getDuration();
        long position = myvideoview == null ? 0 : myvideoview.getCurrentPosition();
        time.setText(stringForTime(duration));
        if (!dragging) {
            time_current.setText(stringForTime(position));
        }
        if (!dragging) {
            seekbar.setProgress(progressBarValue(position));
        }
        long bufferedPosition = myvideoview == null ? 0 : myvideoview.getBufferPercentage();
        seekbar.setSecondaryProgress(progressBarValue(bufferedPosition));
        removeCallbacks(updateProgressAction);
    }
    private int progressBarValue(long position) {
        long duration = myvideoview == null ? 0 : myvideoview.getDuration();
        return  duration == 0 ? 0
                : (int) ((position * 1000) / duration);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        myvideoview= (VideoView) findViewById(ApiUtils.getResIdID(R.id.myvideoview,"myvideoview"));//R.id.myvideoview);//
        L.e("onAttachedToWindow");
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        L.e("onDetachedFromWindow");
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
        Bitmap bitmap=collection.getDrawingCache();
        if(bitmap!=null
                &&!bitmap.isRecycled()){
            bitmap.recycle();
        }
        try {
            if(myvideoview.isPlaying()){
                myvideoview.pause();
            }
            myvideoview.stopPlayback();
            myvideoview=null;
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            myvideoview.stopPlayback();
            myvideoview=null;
        }catch (Exception e){
e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        if(myvideoview!=null){
            try {
                return myvideoview.isPlaying();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }


    }

    public void start(){
        if(myvideoview!=null){
            myvideoview.start();
        }
        postDelayed(updateProgressAction,1000);
    };
    public void pause(){
        if(myvideoview!=null){
            myvideoview.pause();
        }
        postDelayed(updateProgressAction,1000);
    }

    public void hideController(){
        controller.setVisibility(GONE);
        isCanUpdateProgress=true;
    }
    public void hideLoading(){
        video_loading.setVisibility(GONE);
        video_loading.clearAnimation();
    }
    public void showLoading(){

        if(canShowLoading) {
            video_loading.setVisibility(VISIBLE);
        }
        else{
            hideLoading();
        }
    }

    public int getCurrentPosition(){
        if(isPrepared){
            currentPosition=myvideoview.getCurrentPosition();
        }
        return currentPosition;
    }
    public int getDuration(){
        if(isPrepared){
            duration=myvideoview.getDuration();
        }
        return  duration;
    }



    public void setVideoURI(Uri uri){
        this.uri=uri;
        myvideoview.setVideoURI(uri);
    }
    public void setCanShowControllerAndLoading(boolean b){
        canShowController=b;
        canShowLoading=b;
    }

    public void updateCollection(boolean b){
        isCollection=b;
        if(b){
            MyBitmapUtils.display(collection,"drawable://"+ApiUtils.getResDrawableID(R.drawable.video_collectioned1,"video_collectioned1"));//R.drawable.video_collectioned1);//
        }else{
            if(collection.hasFocus()){
                MyBitmapUtils.display(collection,"drawable://"+ApiUtils.getResDrawableID(R.drawable.video_collection1,"video_collection1"));//R.drawable.video_collection1);//
            }else{
                MyBitmapUtils.display(collection,"drawable://"+ApiUtils.getResDrawableID(R.drawable.video_collection,"video_collection"));//R.drawable.video_collection);//
            }
        }
    }
    public boolean isUserPause(){
        return isUserPause;
    }

    public void stopPlayback(){
        if(myvideoview!=null){
            try {
                myvideoview.stopPlayback();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void seekTo(long time){
        if(myvideoview!=null){
            myvideoview.seekTo((int) time);
        }
    }
    MediaPlayer.OnPreparedListener listener;
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener){
        this.listener=listener;
    }
}

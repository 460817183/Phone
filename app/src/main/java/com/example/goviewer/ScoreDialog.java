package com.example.goviewer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinelon on 2017/5/5.
 */

public class ScoreDialog extends Dialog{
    private ScoreAndFlow scoreAndFlow;
    private ImageView rotate_bg;
    private TextView score;
    private TextView personal_ranking;
    private ImageView flower;
    private ListView score_list;
    private ImageButton wrong_record;
    private ImageButton recommend_practice;
    private ImageButton goto_homepage;
    private ScoreAdapter adapter;
    private String scoreId;
    private String videoId;
    private Activity context;
    public ScoreDialog(@NonNull Activity context, ScoreAndFlow scoreAndFlow, String scoreId, String videoId) {
        super(context, ApiUtils.getResStyleID(R.style.mydialog,"mydialog"));//R.style.mydialog);
        this.context=context;
        this.scoreId=scoreId;
        this.videoId=videoId;
        this.scoreAndFlow=scoreAndFlow;
        initView();
        setListener();
    }
    private void setListener() {
        wrong_record.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AnimationUtils.startScaleXYAnimation(wrong_record,hasFocus);
                if(hasFocus){
                    MyBitmapUtils.display(wrong_record,ApiUtils.getResDrawableID(R.drawable.wrong_record_1,"wrong_record_1"));//R.drawable.wrong_record_1);
                }else{
                    MyBitmapUtils.display(wrong_record,ApiUtils.getResDrawableID(R.drawable.wrong_record,"wrong_record"));//R.drawable.wrong_record);
                }
            }
        });
        recommend_practice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AnimationUtils.startScaleXYAnimation(recommend_practice,hasFocus);
                if(hasFocus){
                    MyBitmapUtils.display(recommend_practice,ApiUtils.getResDrawableID(R.drawable.recommend_practice_1,"recommend_practice_1"));//R.drawable.recommend_practice_1);
                }else{
                    MyBitmapUtils.display(recommend_practice,ApiUtils.getResDrawableID(R.drawable.recommend_practice,"recommend_practice"));//R.drawable.recommend_practice);
                }
            }
        });
        goto_homepage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AnimationUtils.startScaleXYAnimation(goto_homepage,hasFocus);
                if(hasFocus){
                    MyBitmapUtils.display(goto_homepage,ApiUtils.getResDrawableID(R.drawable.score_dialog_goto_homepage_1,"score_dialog_goto_homepage_1"));//R.drawable.score_dialog_goto_homepage_1);
                }else{
                    MyBitmapUtils.display(goto_homepage,ApiUtils.getResDrawableID(R.drawable.score_dialog_goto_homepage,"score_dialog_goto_homepage"));//R.drawable.score_dialog_goto_homepage);
                }
            }
        });
        wrong_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("scoreId",scoreId);
                context.setResult(1,intent);
                dismiss();
                context.finish();
            }
        });
        recommend_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("score",scoreAndFlow.getScores()+"");
                intent.putExtra("videoId",videoId);
                context.setResult(2,intent);
                dismiss();
                context.finish();
            }
        });
        goto_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.setResult(3);
                dismiss();
                context.finish();
            }
        });
    }

    private void initView() {
        View view= View.inflate(getContext(),ApiUtils.getResLayoutID(R.layout.dialog_score,"dialog_score"),null);//R.layout.dialog_score,null);
        rotate_bg= (ImageView) view.findViewById(ApiUtils.getResIdID(R.id.rotate_bg,"rotate_bg"));//R.id.rotate_bg);
        score= (TextView) view.findViewById(ApiUtils.getResIdID(R.id.score,"score"));//R.id.score);
        flower= (ImageView) view.findViewById(ApiUtils.getResIdID(R.id.flower,"flower"));//R.id.flower);
        personal_ranking= (TextView) view.findViewById(ApiUtils.getResIdID(R.id.personal_ranking,"personal_ranking"));//R.id.personal_ranking);
        score_list= (ListView) view.findViewById(ApiUtils.getResIdID(R.id.score_list,"score_list"));//R.id.score_list);
        wrong_record= (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.wrong_record,"wrong_record"));//R.id.wrong_record);
        recommend_practice= (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.recommend_practice,"recommend_practice"));//R.id.recommend_practice);
        goto_homepage= (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.goto_homepage,"goto_homepage"));//R.id.goto_homepage);
        score.setText(""+scoreAndFlow.getScores());
        personal_ranking.setText("No."+scoreAndFlow.getPmNum());
        if ("1".equals(scoreAndFlow.getStat())) {
            MyBitmapUtils.display(flower,ApiUtils.getResDrawableID(R.drawable.score_dialog_flower,"score_dialog_flower"));//R.drawable.score_dialog_flower);
        }else{
            MyBitmapUtils.display(flower,ApiUtils.getResDrawableID(R.drawable.score_dialog_no_flower,"score_dialog_no_flower"));//R.drawable.score_dialog_no_flower);
        }
        score_list.setFocusable(false);
        if(scoreAndFlow.getPm().size()>3){
            List<ScoreAndFlow.Pm> pms=new ArrayList<ScoreAndFlow.Pm>();
            pms.add(scoreAndFlow.getPm().get(0));
            pms.add(scoreAndFlow.getPm().get(1));
            pms.add(scoreAndFlow.getPm().get(2));
            adapter=new ScoreAdapter(getContext(),pms,ApiUtils.getResLayoutID(R.layout.item_score_dialog,"item_score_dialog"));//R.layout.item_score_dialog);
        }else{
            adapter=new ScoreAdapter(getContext(),scoreAndFlow.getPm(),ApiUtils.getResLayoutID(R.layout.item_score_dialog,"item_score_dialog"));//R.layout.item_score_dialog);
        }

        score_list.setAdapter(adapter);
        AutoUtils.auto(view);
        setContentView(view);

    }
    @Override
    public void show() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        super.show();
        AnimationUtils.startRotateAnimation(rotate_bg);
        if(scoreAndFlow.getHonorStat()==1){
            FirstAchievementDialog dialog=new FirstAchievementDialog(getContext(),scoreAndFlow.getHonor());
            dialog.show();
        }else{
            goto_homepage.requestFocus();
        }
    }
}

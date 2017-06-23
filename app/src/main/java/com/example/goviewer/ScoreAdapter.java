package com.example.goviewer;

import android.content.Context;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Shinelon on 2017/5/5.
 */

public class ScoreAdapter extends MyAdapter<ScoreAndFlow.Pm>{
    public ScoreAdapter(Context context, List<ScoreAndFlow.Pm> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(MyViewHolder holder, ScoreAndFlow.Pm p, int position) {
        TextView pm=holder.getView(ApiUtils.getResIdID(R.id.pm,"pm"));//R.id.pm);
        TextView userNm=holder.getView(ApiUtils.getResIdID(R.id.username,"username"));//R.id.username);
        TextView score=holder.getView(ApiUtils.getResIdID(R.id.score,"score"));//R.id.score);
        pm.setText("No."+p.getPm());
        userNm.setText(p.getUserNm());
        score.setText(""+p.getScore());
    }
}

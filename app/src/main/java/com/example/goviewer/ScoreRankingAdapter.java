package com.example.goviewer;

import java.util.List;

import com.uzmap.pkg.uzcore.UZResourcesIDFinder;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by xiao on 2016/11/9.
 */

public class ScoreRankingAdapter extends MyAdapter<ScoreAndFlow.Pm> {
    public ScoreRankingAdapter(Context context, List<ScoreAndFlow.Pm> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(MyViewHolder holder, ScoreAndFlow.Pm pm, int position) {
        TextView ranking = holder.getView(ApiUtils.getResIdID(R.id.score_ranking_tv,"score_ranking_tv"));//R.id.score_ranking_tv);//
        TextView id = holder.getView(ApiUtils.getResIdID(R.id.score_ranking_id,"score_ranking_id"));//R.id.score_ranking_id);//
        
        ranking.setText("ON." + pm.getPm());
        id.setText(pm.getUserNm());
       
    }
}

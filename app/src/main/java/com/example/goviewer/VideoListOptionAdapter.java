package com.example.goviewer;

import java.util.List;


import android.content.Context;
import android.widget.TextView;


/**
 * Created by xiao on 2016/11/8.
 * 视频中间提问答案的adapter
 */

public class VideoListOptionAdapter extends MyAdapter<OptionBean> {
    public VideoListOptionAdapter(Context context, List<OptionBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(MyViewHolder holder, OptionBean optionBean, int position) {
        TextView tv = holder.getView(ApiUtils.getResIdID(R.id.item_choose_text,"item_choose_text"));//R.id.item_choose_text);//
        TextView title = holder.getView(ApiUtils.getResIdID(R.id.choose_title,"choose_title"));//R.id.choose_title);//
        String s=optionBean.getContent();
        int i=s.indexOf("(");
        if(i!=-1){
            tv.setText(s.substring(0,i)+"\n"+s.substring(i));
        }else{
            tv.setText(optionBean.getContent());
        }
        switch (position){
            case 0:title.setText("A.");break;
            case 1:title.setText("B.");break;
            case 2:title.setText("C.");break;
            case 3:title.setText("D.");break;
            case 4:title.setText("E.");break;
            case 5:title.setText("F.");break;
            case 6:title.setText("G.");break;
            case 7:title.setText("H.");break;
        }
       
    }
}

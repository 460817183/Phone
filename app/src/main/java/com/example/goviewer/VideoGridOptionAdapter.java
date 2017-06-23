package com.example.goviewer;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by xiao on 2016/11/8.
 * 视频中间提问答案的adapter
 */
public class VideoGridOptionAdapter extends MyAdapter<OptionBean> {
    public VideoGridOptionAdapter(Context context, List<OptionBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }
    @Override
    public void convert(MyViewHolder holder, OptionBean optionBean, int position) {
        TextView title = holder.getView(ApiUtils.getResIdID(R.id.choose_title,"choose_title"));//R.id.choose_title);//
        ImageView image = holder.getView(ApiUtils.getResIdID(R.id.image,"image"));//R.id.image);//
        ImageView smile=holder.getView(ApiUtils.getResIdID(R.id.smile,"smile"));//R.id.smile);//
        smile.setVisibility(View.GONE);
        if (position == 0) {
            title.setText("A");
        } else if (position == 1) {
            title.setText("B");
        } else if (position == 2) {
            title.setText("C");
        } else {
            title.setText("D");
        }
        MyBitmapUtils.display(image, optionBean.getImg());
        
    }
}

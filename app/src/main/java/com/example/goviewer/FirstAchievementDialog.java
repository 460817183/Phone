package com.example.goviewer;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Shinelon on 2017/4/5.
 */

public class FirstAchievementDialog extends Dialog{
    public FirstAchievementDialog(@NonNull Context context) {
        super(context,ApiUtils.getResStyleID(R.style.mydialog,"mydialog"));
        initView();
    }
    public FirstAchievementDialog(@NonNull Context context, AchievementBean.Honor honor) {
        super(context, ApiUtils.getResStyleID(R.style.mydialog,"mydialog"));
        initView(honor);
    }

    ImageView bg;
    TextView desc;
    ImageButton receive;
    private void initView(AchievementBean.Honor honor) {
        initView();
        /*L.e("成就图标url:"+honor.getImg());
        MyBitmapUtils.display(achievement_image,honor.getImg());
        desc.setText(honor.getDesc());*/
    }


    private void initView() {
        View view= View.inflate(getContext(),ApiUtils.getResLayoutID(R.layout.dialog_first_achievement,"dialog_first_achievement"),null);
        bg= (ImageView) view.findViewById(ApiUtils.getResIdID(R.id.bg,"bg"));//R.id.bg);
        desc= (TextView) view.findViewById(ApiUtils.getResIdID(R.id.desc,"desc"));//R.id.desc);
        MyBitmapUtils.display(bg,ApiUtils.getResDrawableID(R.drawable.first_achievement_bg,"first_achievement_bg"));//R.drawable.first_achievement_bg);
        AnimationUtils.startRotateAnimation(bg);
        AutoUtils.auto(view);
        setContentView(view);
        receive= (ImageButton) view.findViewById(ApiUtils.getResIdID(R.id.receive,"receive"));//R.id.receive);
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        receive.requestFocus();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        /*RequestParams params=new RequestParams(Constants.BASE_URL+Constants.SELECT_ACHIEVEMENT);
        params.addBodyParameter("id",honor.getId());
        params.addBodyParameter("userId", UserInfo.getUserId());
        params.addBodyParameter("type", honor.getType());
        MyHttpUtils.request(params, new OnRequestListener<String>() {
            @Override
            public void onSuccess(String result) {
                L.e("领取勋章返回:"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getBoolean("status")){
                        MyToast.show("领取成功！");
                    }else{
                        MyToast.show("领取失败！"+jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                MyToast.show("领取失败，请检查网络。。。");
                L.e("勋章领取失败:"+error);
            }
        });*/

    }
}

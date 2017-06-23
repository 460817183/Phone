package com.example.goviewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;


public class GoviewerModule extends UZModule {

    public GoviewerModule(UZWebView webView) {
        super(webView);
        // TODO Auto-generated constructor stub
    }

    private UZModuleContext jsResult;
    private Handler handler=new Handler();
    public void jsmethod_startActivity(UZModuleContext context) {
        Intent intent = new Intent(getContext(), VideoActivity.class);
        intent.putExtra("videoPath", context.optString("videoPath"));
        intent.putExtra("videoId", context.optString("videoId"));
        intent.putExtra("userId", context.optString("userId"));
        intent.putExtra("isDisney", context.optBoolean("isDisney", false));
        intent.putExtra("isYunhailuo", context.optBoolean("isYunhailuo", false));
        startActivityForResult(intent, 1);
        jsResult = context;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode != 0) {
            Toast.makeText(getContext(), "context:" + getContext(), Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("resultCode", resultCode);
                switch (resultCode) {
                    case 1:
                        jsonObject.put("scoreId", data.getStringExtra("scoreId"));
                        break;
                    case 2:
                        jsonObject.put("score", data.getStringExtra("score"));
                        jsonObject.put("videoId", data.getStringExtra("videoId"));
                        break;
                }
                jsonObject.put("resultCode", resultCode);
                jsResult.success(jsonObject, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public static SpeechRecognizer mIat;
    public RecognizerListener mRecoListener;
    public static boolean isStop;

    public void jsmethod_stopVoice(final UZModuleContext context) {

        isStop = true;
        mIat.stopListening();
    }






    public void jsmethod_startVoice(final UZModuleContext context) {
        if (mIat == null) {
            initSpeech();
        }
        isStop = false;
        mRecoListener = new RecognizerListener() {
            @Override
            public void onBeginOfSpeech() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(SpeechError arg0) {
                // TODO Auto-generated method stub
                if (!isStop) {
                    mIat.startListening(mRecoListener);

                }

            }

            @Override
            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onResult(RecognizerResult results, boolean arg1) {
                // TODO Auto-generated method stub
                mIat.stopListening();
                if (isStop) {
//	        		MyToast.show(getContext(), "停止识别了");
                    return;
                } else {

                    final String text = JsonParser.parseIatResult(results
                            .getResultString());
                    if (text.trim().length() != 1) {
                        JSONObject result = new JSONObject();
                        try {
                            result.put("result", text);
                            context.success(result, false);
                            MyToast.show(getContext(), "识别出" + text + "并返回给js");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        mIat.startListening(mRecoListener);
                    }
                }


            }

            @Override
            public void onVolumeChanged(int arg0, byte[] arg1) {
                // TODO Auto-generated method stub
                MyToast.show(getContext(), "正在识别。。。");
            }
        };
        mIat.startListening(mRecoListener);
    }

    private void initSpeech() {
        SpeechUtility.createUtility(getContext().getApplicationContext(), "appid=58381105");//580587c2
        mIat = SpeechRecognizer.createRecognizer(getContext(), new InitListener() {
            @Override
            public void onInit(int code) {
                Log.e("test", "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(getContext(), "语音功能初始化失败，错误码：" + code, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        mIat.setParameter(SpeechConstant.VAD_BOS, "15000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "15000");
        Bundle bundle=new Bundle();
    }


}

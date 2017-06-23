package com.example.goviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {

    AudioRecordManager manager;
    Button button;
    Button startvoice;
    Button stopvoice;
    SpeechRecognizer speechRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=new AudioRecordManager();
        startvoice= (Button) findViewById(R.id.startvoice);
        stopvoice= (Button) findViewById(R.id.stopvoice);
        SpeechUtility.createUtility(getApplicationContext(), "appid=58381105");//580587c2
        speechRecognizer= SpeechRecognizer.createRecognizer(this,null);
        speechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");
        speechRecognizer.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        speechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
               /* Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                intent.putExtra("videoPath","http://tg-disney.oss-cn-hangzhou.aliyuncs.com/miaomiaosenlin01.mp4");
                intent.putExtra("videoId","68");
                intent.putExtra("userId","14655");
                intent.putExtra("isDisney",true);
                intent.putExtra("isYunhailuo",false);
                startActivity(intent);*/
               startVideoActivity();
			}
		});

        startvoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoice();
            }
        });
        stopvoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopVoice();
            }
        });
    }

    String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/VoiceTest/test.pcm";
    public void startVoice(){

        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/VoiceTest");
        if(!file.exists()){
            file.mkdir();
        }
        File file1=new File(file,"test.pcm");
        manager.startRecord(file1.getAbsolutePath());

        Log.e("test","调用了开始录音");
    }
    public void stopVoice(){
        manager.stopRecord();
        speechRecognizer.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
                Log.e("test","onVolumeChanged");
            }

            @Override
            public void onBeginOfSpeech() {
                Log.e("test","onBeginOfSpeech");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("test","onEndOfSpeech");
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if(b){
                    Log.e("test","识别出来:"+JsonParser.parseIatResult(recognizerResult
                            .getResultString()));
                }

            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e("test","onError:"+speechError.getErrorDescription());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                Log.e("test","onEvent");
            }
        });
        File file=new File(filePath);
        try {
            FileInputStream inputStream=new FileInputStream(file);
            byte[] bytes=new byte[inputStream.available()];
            inputStream.read(bytes);
            speechRecognizer.writeAudio(bytes,0,bytes.length);
            speechRecognizer.stopListening();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("test","调用了结束录音");
    }

    public void startVideoActivity(){
    	Intent intent=new Intent(this,VideoActivity.class);
    	intent.putExtra("videoPath", "http://tg-disney.oss-cn-hangzhou.aliyuncs.com/2016%E5%B9%B411%E6%9C%8823%E6%97%A5/yuny1.mp4");
    	intent.putExtra("videoId", "89");
    	intent.putExtra("userId", "14655");
    	startActivity(intent);
    }
}

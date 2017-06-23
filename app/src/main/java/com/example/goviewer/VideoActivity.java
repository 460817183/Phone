package com.example.goviewer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import pl.droidsonroids.gif.GifImageView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import static android.view.View.GONE;

public class VideoActivity extends BaseActivity {

    public static boolean isCreated;
    MyVideo videoview;   //视频播放控件

    RelativeLayout ll_show;  //答题的界面

    RelativeLayout ll_say; //语音答题界面
    ImageView skip_yuyin;//讯飞版 跳过语音按钮
    RelativeLayout ll_text; //文字或者图片答题的界面
    ImageView yuyin;  //语音图标

    //苹果界面
    RelativeLayout rl_video_disney_apple;
    private boolean isOnce = true;
    //苹果图片
    ImageView video_disney_apple_img;

    ImageView ll_show_bg; //答题界面的背景
    TextView tv_tpName; //题目
    TextView tv_tpName_cn;//题目里面的中文
    GridView choose_gridView;  //图片答案的选项
    ListView choose_listview; //文字答案的选项
    ImageView error; //选择错误时显示的图片
    GifImageView error_gif;//选错时候的gif图片
    ImageView right; //选择正确时显示的图片
    GifImageView right_gif;//选对时候的gif图片
    private List<TopicBean> topics;   //话题列表
    private TopicBean voiceTopic; //语音问答的题目
    private List<OptionBean> options;  // 话题列表index下标的问题答案列表
    private VideoListOptionAdapter listadapter;
    private VideoGridOptionAdapter gridadapter;
    private List<EduScoreAddBean> mappings; //题目id与分数子表的对应关系列表

    private List<TopicBean> inClass; //课间题目列表
    private List<TopicBean> afterClass;//课后题目列表

    private boolean isEnd;//视频是否结束
    private int afterIndex = 0;//课后问题的下标
    private long currentTime;//当前时间
    private ScoreAndFlow scoreAndFlow; //评分表数据
    private MediaPlayer mediaPlayer;
    private long reallyPlayTime = 0;//实际播放时间  去除了快进
    int old_duration;//播放到的时间   用来判断视频是否暂停
    MediaPlayer soMp;//回答正确声音
    MediaPlayer coMp;//回答错误声音
    Runnable runnable;
    private JSONObject addToHistoryBack;
    Timer timer;
    TimerTask timerTask;

    /*云海螺*/
    RelativeLayout yunhailuo;//云海螺整个页面
    RelativeLayout hailuo_score;//回答正确加分界面
    TextView hailuo_question_score_add;//答对增加分数
    TextView yunhailuo_score_sum;//分数总和
    TextView current_question;//当前题目下标
    TextView question_num; //题目总数
    ImageView star1;//第一颗星
    ImageView star2;//第二课星
    ImageView star3;//第三颗星
    float single_question_score;//单题分数
    float yunhailuo_score_sum_num = 0;//答题分数总计
    int current_question_num;     //当前题目下标
    int answer_right_num = 0;

    long xunfeiTime;  //讯飞语音遥控答题开始时间
    VideoHistoryUtils videoHistoryUtils;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mHandler == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    L.e("返回:" + msg.obj.toString());
                    break;
                case 1:
                    //获取话题返回
                    L.e("获取话题返回:" + msg.obj.toString());
                    topics = MyGson.fromJsonArray(msg.obj.toString(), TopicBean.class);
                    if (getIntent().getBooleanExtra("isYunhailuo", false)) {
                        question_num.setText("/" + topics.size());
                        single_question_score = 100f / topics.size();
                    }
                    /*if (MyApplication.isXunfei) {
                        String mCloudGrammar = "#ABNF 1.0 UTF-8;language en_us;mode voice;root $main;$main = ";
                        for (int i = 0; i < topics.size(); i++) {
                            mCloudGrammar += topics.get(i).getTopicName();
                        }
                        mCloudGrammar = mCloudGrammar.substring(0, mCloudGrammar.length() - 1);
                        mCloudGrammar += ";";
                        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
                        mIat.buildGrammar("abnf", mCloudGrammar, new GrammarListener() {
                            @Override
                            public void onBuildFinish(String s, SpeechError speechError) {
                                if (speechError == null) {
                                    if (!TextUtils.isEmpty(s)) {
                //构建语法成功，请保存grammarId用于识别
                                        grammarId = s;
                                    } else {
                                        L.e("语法构建失败,错误码：" + speechError.getErrorCode());
                                    }
                                }
                            }
                        });
                    }*/
                    L.e("返回话题数量:" + topics.size());
                    getEduScoreAdd();
                    inClass = new ArrayList<TopicBean>();
                    afterClass = new ArrayList<TopicBean>();
                    for (int i = 0; i < topics.size(); i++) {
                        if ("1".equals(topics.get(i).getParamType())) {
                            inClass.add(topics.get(i));
                        } else {
                            afterClass.add(topics.get(i));
                        }
                    }
                    if (inClass.size() == 0) {
                        MyToast.show(context, "该视频没有课间互动");

                    }
                    timer = new Timer();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (videoview.isPlaying() && currentTime > videoview.getCurrentPosition() + 5000) {
                                //如果视频回退了   结束五秒前依然要弹出题目
                                isEnd = false;
                                for (int i = 0; i < inClass.size(); i++) {
                                    if (videoview.getCurrentPosition() / 1000 < inClass.get(i).getPlayStat()) {
                                        inClass.get(i).setPlayed(false);
                                    } else {
                                        inClass.get(i).setPlayed(true);
                                    }
                                }
                            }
                            if (videoview.isPlaying()) {
                                currentTime = videoview.getCurrentPosition();
                                //循环判断   是否需要弹出课间练习题
                                for (int i = 0; i < inClass.size(); i++) {
                                    final TopicBean topicBean = inClass.get(i);
                                    /**正在播放状态
                                     * 循环遍历课间问答列表
                                     * 如果播放时间时间与某题相等  判断是否是正在回答的题目  如果不是
                                     * 弹出题目  暂停播放  并记录当前题目的id
                                     * 题目回答正确后  继续播放
                                     */
                                    if (videoview.getCurrentPosition() / 1000 == topicBean.getPlayStat() && !topicBean.isPlayed() && (ll_show.getVisibility() == GONE) && videoview.getCurrentPosition() != historyTime) {
                                        historyTime = 0;
                                        final int a = i;
                                        voiceTopic = topicBean;
                                        voiceTopic.setPlayed(true);
                                        if (voiceTopic.getIsSuspend() == 1) {
                                            videoview.start();
                                            tv_tpName.setVisibility(GONE);
                                        } else {
                                            videoview.pause();
                                            tv_tpName.setVisibility(View.VISIBLE);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setTitle(voiceTopic.getTopicName(), a + 1);
                                                switchRightOrError(0);
                                                if ("1".equals(voiceTopic.getStat())) {
                                                    ll_show.setVisibility(View.VISIBLE);
                                                    videoview.setCanShowControllerAndLoading(false);
                                                    videoview.hideLoading();
                                                    ll_say.setVisibility(View.VISIBLE);
                                                    startVoiceAnimation();
                                                    ll_text.setVisibility(GONE);
                                                    MyToast.show(context, "请讲话");
                                                    xunfeiTime = System.currentTimeMillis();
                                                    mIat.startListening(mRecoListener);
                                                    skip_yuyin.setVisibility(View.VISIBLE);
                                                    skip_yuyin.requestFocus();
                                                    skip_yuyin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                        @Override
                                                        public void onFocusChange(View v, boolean hasFocus) {
                                                            AnimationUtils.startScaleXYAnimation(skip_yuyin, hasFocus);
                                                        }
                                                    });
                                                    skip_yuyin.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mHandler.post(xunfeiAnswerError);
                                                        }
                                                    });
                                                } else {
                                                    getOptions(voiceTopic.getTopicId());
                                                    ll_say.setVisibility(GONE);
                                                    ll_text.setVisibility(View.VISIBLE);
                                                }
                                                if (choose_gridView.getAdapter() != null) {
                                                    choose_gridView.requestFocus();
                                                }
                                            }
                                        });
                                        break;
                                    }
                                }
                                //如果在播放期间   播放到了视频最后五秒  弹出课后练习题
                                if (videoview.getDuration() != -1 && (videoview.getDuration() - videoview.getCurrentPosition()) / 1000 < 5 && !isEnd) {
                                    L.e("getDuration:" + videoview.getDuration() + "    getCurrentPosition:" + videoview.getCurrentPosition());
                                    isEnd = true;
                                    mHandler.removeCallbacks(runnable);
                                    if (inClass.size() > 0 && !inClass.get(inClass.size() - 1).isPlayed()) {
//                                        return;
                                    }
                                    /*if (ll_show.getVisibility() == GONE) {
                                        for (int i = 0; i < inClass.size(); i++) {
                                            if (!inClass.get(i).isPlayed()) {
                                                getOptions(inClass.get(i).getTopicId());
                                                return;
                                            }
                                        }
                                    }*/

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            videoview.pause();
                                            if (isOnce && getIntent().getBooleanExtra("isDisney", false)) {
                                                if (videoview.getDuration() * 0.8 < reallyPlayTime) {
                                                    isOnce = false;
                                                    rl_video_disney_apple.setVisibility(View.VISIBLE);
                                                    ll_show.setVisibility(GONE);
                                                    videoview.setCanShowControllerAndLoading(false);
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Net net = new Net();
                                                            net.addApple("1");
                                                        }
                                                    }).start();
                                                }
                                            }
                                            mHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    rl_video_disney_apple.setVisibility(GONE);
                                                    videoview.setCanShowControllerAndLoading(true);
                                                    if (afterClass.size() == 0) {
                                                        getScoreAndFlow();
                                                        return;
                                                    } else {
                                                        getQuestionAfterClass();
                                                    }
                                                }
                                            }, 2000);
                                        }
                                    });
                                    //视频即将结束
                                }
                            }
                        }
                    };
                    timer.scheduleAtFixedRate(timerTask, 0, 500);
                    runnable = new Runnable() {
                        public void run() {
                            int duration = videoview.getCurrentPosition();
                            L.e("old_duration:" + old_duration + "   duration:" + duration);//(old_duration == duration)+":"+(videoview.isPlaying()));
                            if (old_duration == duration || !videoview.isPlaying()) {
                                if (ll_show.getVisibility() == GONE && rl_video_disney_apple.getVisibility() == GONE && !isEnd) {
//                                    video_loading.setVisibility(View.VISIBLE);

                                    videoview.showLoading();
                                    if (!videoview.isUserPause()) {
                                        mHandler.postDelayed(noUserPause, 30 * 1000);
                                    } else {
                                        mHandler.removeCallbacks(noUserPause);
                                    }
                                } else {
//                                    video_loading.setVisibility(View.GONE);
                                    videoview.hideLoading();
                                    mHandler.removeCallbacks(noUserPause);
                                }
                            } else {
                                reallyPlayTime += 1000;
                                L.e("播放总时间:" + reallyPlayTime);

                                videoview.hideLoading();
                                mHandler.removeCallbacks(noUserPause);
                            }
                            old_duration = duration;
                            mHandler.postDelayed(runnable, 1000);
                        }
                    };
                    mHandler.postDelayed(runnable, 0);
                    break;
                case 2:
                    //获取选项返回
                    options = MyGson.fromJsonArray(msg.obj.toString(), OptionBean.class);
                    ll_show.setVisibility(View.VISIBLE);
                    videoview.setCanShowControllerAndLoading(false);
//                    video_loading.setVisibility(View.GONE);
                    videoview.hideLoading();
                    videoview.hideController();
                    try {
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }
                            mediaPlayer.release();
                        }
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(voiceTopic.getVoice());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (options.size() > 0) {
                        if (TextUtils.isEmpty(options.get(0).getImg())) {
                            listadapter = new VideoListOptionAdapter(context, options, getIntent().getBooleanExtra("isDisney", false) ? ApiUtils.getResLayoutID(R.layout.item_choose_list,"item_choose_list")
                                     : ApiUtils.getResLayoutID(R.layout.item_choose_list1,"item_choose_list1"));
                            choose_listview.setAdapter(listadapter);
                            choose_listview.setVisibility(View.VISIBLE);
                            choose_gridView.setVisibility(GONE);
                            choose_listview.setEnabled(true);
                            choose_listview.requestFocus();
                        } else {
                            gridadapter = new VideoGridOptionAdapter(context, options, getIntent().getBooleanExtra("isDisney", false) ? ApiUtils.getResLayoutID(R.layout.item_choose_grid,"item_choose_grid")
                                     : ApiUtils.getResLayoutID(R.layout.item_choose_grid1,"item_choose_grid1"));//
                            choose_gridView.setAdapter(gridadapter);
                            choose_listview.setVisibility(GONE);
                            choose_gridView.setVisibility(View.VISIBLE);
                            choose_gridView.setEnabled(true);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    choose_gridView.requestFocus();
                                    choose_gridView.setSelection(1);
                                    choose_gridView.setSelection(0);
                                }
                            }, 200);
                        }
                        switchRightOrError(0);
                    }
                    break;
                case 3:
                    L.e("获取评分子表返回:" + msg.obj.toString());
                    mappings = MyGson.fromJsonArray(msg.obj.toString(), EduScoreAddBean.class);
                    break;
                case 4:
                    L.e("获取评分表返回:" + msg.obj.toString());
                    scoreAndFlow = MyGson.fromJson(msg.obj.toString(), ScoreAndFlow.class);
                    videoview.setCanShowControllerAndLoading(false);
                    ScoreDialog dialog = new ScoreDialog(context, scoreAndFlow, mappings.get(0).getScoreId() + "", getIntent().getStringExtra("videoId"));
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });
                    dialog.show();
                    break;
                case 5:
                    //语音问答返回
                    L.e("语音问答返回:" + msg.obj.toString());
                    try {
                        JSONObject json = new JSONObject(msg.obj.toString());
                        if ("1".equals(json.getString("stat"))) {
                            switchRightOrError(2);
                            MyToast.show(context, "回答正确,你真棒");
                            if (getIntent().getBooleanExtra("isYunhailuo", false) && topics.get(current_question_num - 1).getIsRight() == 0) {
                                hailuo_score.setVisibility(View.VISIBLE);
                                answer_right_num++;
                                final int current_question_score = (int) (Math.round(100f * answer_right_num / topics.size()) - yunhailuo_score_sum_num);
                                hailuo_question_score_add.setText("+" + current_question_score);
                                AnimationUtils.hailuoAnimation(hailuo_score, new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        hailuo_score.setVisibility(GONE);
                                        yunhailuo_score_sum_num += (int) current_question_score;
                                        if (yunhailuo_score_sum_num > 100) {
                                            yunhailuo_score_sum_num = 100;
                                        }
                                        setStarAndYunhailuo_score_sum_margin();
                                        yunhailuo_score_sum.setText("" + (int) yunhailuo_score_sum_num);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                            if (topics.get(current_question_num - 1).getIsRight() == 0) {
                                saveAnswer(1, getScoreSonId(Integer.parseInt(voiceTopic.getTopicId())), null);
                            }
                            topics.get(current_question_num - 1).setIsRight(2);
                            playMp(true);
                        } else {
                            if (topics.get(current_question_num - 1).getIsRight() == 0) {
                                saveAnswer(0, getScoreSonId(Integer.parseInt(voiceTopic.getTopicId())), null);
                            }
                            topics.get(current_question_num - 1).setIsRight(1);
                            playMp(false);
                            switchRightOrError(1);
                        }
                        /*if (isEnd) {
                            if (isOnce && getIntent().getBooleanExtra("isDisney", false)) {
                                if (videoview.getDuration() * 0.8 < reallyPlayTime) {
                                    isOnce = false;
                                    rl_video_disney_apple.setVisibility(View.VISIBLE);
                                    ll_show.setVisibility(GONE);
                                    videoview.setCanShowControllerAndLoading(true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Net net = new Net();
                                            net.addApple("1");
                                        }
                                    }).start();
                                }

                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            rl_video_disney_apple.setVisibility(GONE);
                                            if (afterIndex < afterClass.size()) {
                                                getQuestionAfterClass();
                                            } else {
                                                MyToast.show(context, "正在获取评分数据...");
                                                getScoreAndFlow();
                                            }
                                        }
                                    });
                                }
                            }, 2000);
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    L.e("addtoHistory:" + msg.obj.toString());
                    try {
                        addToHistoryBack = new JSONObject(msg.obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    L.e("收藏返回:" + msg.obj.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if ("true".equals(jsonObject.getString("status"))) {
                            videoview.updateCollection(true);
                        } else {
                            videoview.updateCollection(false);
                        }
                        MyToast.show(context, jsonObject.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    L.e("是否收藏返回:" + msg.obj.toString());
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if ("close".equals(jsonObject.getString("status")) && videoview != null) {
                            videoview.updateCollection(false);
                        } else {
                            videoview.updateCollection(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    MyToast.show(context, "网络请求出错！");
                    break;
            }
        }
    };

    //获取答案列表
    private void getOptions(String topicId) {
//        L.e("调用了获取答案  topicId:" + topicId);
        RequestParams params = new RequestParams(Constants.BASE_URL + Constants.GET_OPTIONS);
        params.addBodyParameter("topicId", "" + topicId);
        MyHttpUtils.request(params, mHandler, 2);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        videoview.onKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    //记录回答
    private void saveAnswer(int stat, int scoreSonId, String optionId) {
        RequestParams params = new RequestParams(Constants.BASE_URL + Constants.UPDATE_SON);
        params.addBodyParameter("userId", "" + UserInfo.getUserId(context));
        params.addBodyParameter("stat", "" + stat);
        params.addBodyParameter("id", "" + scoreSonId);
        if (optionId != null) {
            params.addBodyParameter("optionsId", optionId);
        }
        MyHttpUtils.request(params, mHandler, 0);
    }

    //获取评分奖励
    private void getEduScoreAdd() {
        RequestParams params = new RequestParams(Constants.BASE_URL + Constants.EDU_SCORE_ADD);
        params.addBodyParameter("typeid", "" + getIntent().getStringExtra("typeId"));
        params.addBodyParameter("videoId", "" + getIntent().getStringExtra("videoId"));
        params.addBodyParameter("userId", "" + UserInfo.getUserId(context));
        MyHttpUtils.request(params, mHandler, 3);
    }

    //添加到历史记录
    private void addToHistory() {
        RequestParams params = new RequestParams(Constants.BASE_URL + Constants.ADD_TO_HISTORY);
        params.addBodyParameter("videoId", "" + getIntent().getStringExtra("videoId"));
        params.addBodyParameter("userId", "" + UserInfo.getUserId(context));
        MyHttpUtils.request(params, mHandler, 6);
    }

    //获取评分子表ID
    private int getScoreSonId(int topicId) {
        for (int i = 0; i < mappings.size(); i++) {
            if (mappings.get(i).getQuestionId() == topicId) {
                return mappings.get(i).getScoreSonId();
            }
        }
        return 0;
    }

    //问题回答结束  获取评分表
    private void getScoreAndFlow() {
        if (mappings != null && mappings.size() != 0) {
            RequestParams params = new RequestParams(Constants.BASE_URL + Constants.GET_SCORE_AND_FLOWER);
            params.addBodyParameter("scoreId", "" + mappings.get(0).getScoreId());
            params.addBodyParameter("type", "0");
            params.addBodyParameter("userId", "" + UserInfo.getUserId(context));
            params.addBodyParameter("videoId", getIntent().getStringExtra("videoId"));
            L.e("获取评分表:userid;" + UserInfo.getUserId(context) + "  videoId:" + getIntent().getStringExtra("videoId") + "scoreId:" + mappings.get(0).getScoreId());
            MyHttpUtils.request(params, mHandler, 4);
        } else {
            MyToast.show(getApplicationContext(), "获取评分表时网络出错");
        }
    }

    SpeechRecognizer mIat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.Ext.init(getApplication());
        Log.e("test","onCreate");
        UserInfo.setUserId(getIntent().getStringExtra("userId"));
        initImageLoader();
        initSpeech();
        context = this;
        videoHistoryUtils = new VideoHistoryUtils(context);
        isCreated = true;
        SpeechUtility.createUtility(this, "appid=58381105");
        if (getIntent().getBooleanExtra("isDisney", false)) {
            setContentView(ApiUtils.getResLayoutID(R.layout.activity_new_video,"activity_new_video"));
            findView();

            MyBitmapUtils.display(ll_show_bg, "drawable://" +ApiUtils.getResDrawableID(R.drawable.choose_choose_1,"choose_choose_1"));
        } else {
            setContentView(ApiUtils.getResLayoutID(R.layout.activity_new_video1,"activity_new_video1"));//
            findView();
            MyBitmapUtils.display(ll_show_bg, "drawable://" + ApiUtils.getResDrawableID(R.drawable.choose_choose_2,"choose_choose_2"));
            if (getIntent().getBooleanExtra("isYunhailuo", false)) {
                yunhailuo = (RelativeLayout) findViewById(ApiUtils.getResIdID(R.id.yunhailuo,"yunhailuo"));
                hailuo_score = (RelativeLayout) findViewById(ApiUtils.getResIdID(R.id.hailuo_score,"hailuo_score"));
                hailuo_question_score_add = (TextView) findViewById(ApiUtils.getResIdID(R.id.hailuo_question_score_add,"hailuo_question_score_add"));
                yunhailuo_score_sum = (TextView) findViewById(ApiUtils.getResIdID(R.id.yunhailuo_score_sum,"yunhailuo_score_sum"));
                current_question = (TextView) findViewById(ApiUtils.getResIdID(R.id.current_question,"current_question"));
                question_num = (TextView) findViewById(ApiUtils.getResIdID(R.id.question_num,"question_num"));
                star1 = (ImageView) findViewById(ApiUtils.getResIdID(R.id.star1,"star1"));
                star2 = (ImageView) findViewById(ApiUtils.getResIdID(R.id.star2,"star2"));
                star3 = (ImageView) findViewById(ApiUtils.getResIdID(R.id.star3,"star3"));
                yunhailuo.setVisibility(View.VISIBLE);
            }
        }
        mediaPlayer = new MediaPlayer();
        addToHistory();
        getCollectStatus();
        initView();
        initData();
        setListener();
    }
    private void initSpeech() {
        mIat = SpeechRecognizer.createRecognizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                Log.e("test", "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(context, "语音功能初始化失败，错误码：" + code,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");

    }


    private void initImageLoader() {
        File cacheDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), getPackageName() + "_cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        int maxMemory = (int) Runtime.getRuntime().maxMemory();// 获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;// 设置图片内存缓存占用八分之一
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).memoryCacheSize(memoryCacheSize)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY).build();
        com.nostra13.universalimageloader.utils.L.disableLogging();
        ImageLoader.getInstance().init(config);
    }


    private void findView() {
        videoview = (MyVideo) findViewById(ApiUtils.getResIdID(R.id.videoview,"videoview"));//  R.id.videoview);// //视频播放控件

        ll_show = (RelativeLayout) findViewById(ApiUtils.getResIdID(R.id.ll_show,"ll_show"));//R.id.ll_show);//  //答题的界面

        ll_say = (RelativeLayout) findViewById(ApiUtils.getResIdID(R.id.ll_say,"ll_say"));//R.id.ll_say);// //语音答题界面
        skip_yuyin = (ImageView) findViewById(ApiUtils.getResIdID(R.id.skip_yuyin,"skip_yuyin"));//R.id.skip_yuyin);//
        ll_text = (RelativeLayout) findViewById(ApiUtils.getResIdID(R.id.ll_text,"ll_text"));//R.id.ll_text);// //文字或者图片答题的界面
        yuyin = (ImageView) findViewById(ApiUtils.getResIdID(R.id.yuyin,"yuyin"));// R.id.yuyin);// //语音图标
        //苹果界面
        rl_video_disney_apple = (RelativeLayout) findViewById(ApiUtils.getResIdID(R.id.rl_video_disney_apple,"rl_video_disney_apple"));//R.id.rl_video_disney_apple);//
        //苹果图片
        video_disney_apple_img = (ImageView) findViewById(ApiUtils.getResIdID(R.id.video_disney_apple_img,"video_disney_apple_img"));//R.id.video_disney_apple_img);//
        ll_show_bg = (ImageView) findViewById(ApiUtils.getResIdID(R.id.ll_show_bg,"ll_show_bg"));//R.id.ll_show_bg);// //答题界面的背景
        tv_tpName = (TextView) findViewById(ApiUtils.getResIdID(R.id.tv_tpName,"tv_tpName"));//R.id.tv_tpName);// //题目
        tv_tpName_cn = (TextView) findViewById(ApiUtils.getResIdID(R.id.tv_tpName_cn,"tv_tpName_cn"));//R.id.tv_tpName_cn);//    //题目里面的中文
        choose_gridView = (GridView) findViewById(ApiUtils.getResIdID(R.id.choose_gridView,"choose_gridView"));//R.id.choose_gridView); // //图片答案的选项
        choose_listview = (ListView) findViewById(ApiUtils.getResIdID(R.id.choose_listview,"choose_listview"));//R.id.choose_listview); //  //文字答案的选项
        error = (ImageView) findViewById(ApiUtils.getResIdID(R.id.error,"error"));//R.id.error);// //选择错误时显示的图片
        error_gif = (GifImageView) findViewById(ApiUtils.getResIdID(R.id.error_gif,"error_gif"));//R.id.error_gif);//   //选错时候的gif图片
        right = (ImageView) findViewById(ApiUtils.getResIdID(R.id.right,"right"));//R.id.right); //    //选择正确时显示的图片
        right_gif = (GifImageView) findViewById(ApiUtils.getResIdID(R.id.right_gif,"right_gif"));//R.id.right_gif);// //选对时候的gif图片
    }

    static int i = 0;

    private Runnable xunfeiAnswerError = new Runnable() {
        @Override
        public void run() {
            if (topics.get(current_question_num - 1).getIsRight() == 0) {
                saveAnswer(0, getScoreSonId(Integer.parseInt(voiceTopic.getTopicId())), null);
            }
            topics.get(current_question_num - 1).setIsRight(1);
            playMp(false);
            switchRightOrError(1);
            if (isEnd) {
                mHandler.removeCallbacks(runnable);
                if (isOnce && getIntent().getBooleanExtra("isDisney", false)) {
                    if (videoview.getDuration() * 0.8 < reallyPlayTime) {
                        isOnce = false;
                        rl_video_disney_apple.setVisibility(View.VISIBLE);
                        ll_show.setVisibility(GONE);
                        videoview.setCanShowControllerAndLoading(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Net net = new Net();
                                net.addApple("1");
                            }
                        }).start();
                    }
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rl_video_disney_apple.setVisibility(GONE);
                                if (afterIndex < afterClass.size()) {
                                    getQuestionAfterClass();
                                } else {
                                    MyToast.show(context, "正在获取评分数据...");
                                    getScoreAndFlow();
                                }
                            }
                        });

                    }
                }, 2000);
            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AnimationUtils.startTranslateAnimation(ll_show, 0, 0, 0, 2, 250);
                                videoview.start();
                                ll_show.setVisibility(GONE);
                                videoview.setCanShowControllerAndLoading(true);
                            }
                        });
                    }
                }, 2000);
            }
        }
    };

    private int images_index = 0;

    public void startVoiceAnimation() {
        mHandler.removeCallbacks(voiceRunnable);
        mHandler.postDelayed(voiceRunnable, 200);
    }

    Runnable voiceRunnable = new Runnable() {
        int[] images = new int[]{
                ApiUtils.getResDrawableID(R.drawable.yuyin,"yuyin"),//R.drawable.yuyin,//
                ApiUtils.getResDrawableID(R.drawable.yuyin_1,"yuyin_1"),//R.drawable.yuyin_1,//
                ApiUtils.getResDrawableID(R.drawable.yuyin_2,"yuyin_2"),//R.drawable.yuyin_2,//
                ApiUtils.getResDrawableID(R.drawable.yuyin_3,"yuyin_3"),// R.drawable.yuyin_3,//
                ApiUtils.getResDrawableID(R.drawable.yuyin_4,"yuyin_4"),//R.drawable.yuyin_4,//
                ApiUtils.getResDrawableID(R.drawable.yuyin_5,"yuyin_5"),//R.drawable.yuyin_5,//
                ApiUtils.getResDrawableID(R.drawable.yuyin_6,"yuyin_6")//R.drawable.yuyin_6//
        };

        @Override
        public void run() {
            if (ll_say.getVisibility() == GONE) {
                images_index = 0;
                mHandler.removeCallbacks(this);
            } else {
                MyBitmapUtils.display(yuyin, images[images_index]);
                images_index++;
                if (images_index > 6) {
                    images_index = 1;
                }
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 200);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:// 音量增大
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        currentVolume + 1, 1);

                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:// 音量减小
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        currentVolume - 1, 1);

                return true;
            default:
                videoview.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    int px,py;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                px=(int) event.getX();
                py=(int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(px>500){
                    //调整亮度
                    if(event.getY()-py>50){
                        setLightness(-1);
                        py=(int) event.getY();
                    }else if(event.getY()-py<-50){
                        setLightness(1);
                        py=(int) event.getY();
                    }
                }else{
                    //调整音量
                    if(event.getY()-py>50){
                        setAudio(-1);
                        py=(int) event.getY();
                    }else if(event.getY()-py<-50){
                        setAudio(1);
                        py=(int) event.getY();
                    }

                }
                return true;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.onTouchEvent(event);
    }
    public void setLightness(float lightness) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        // 屏幕的亮度,最大是255
        layoutParams.screenBrightness = layoutParams.screenBrightness
                + (lightness/15f);
        if (layoutParams.screenBrightness > 1) {
            layoutParams.screenBrightness = 1;
        } else if (layoutParams.screenBrightness < 0) {
            layoutParams.screenBrightness = 0f;
        }
        MyToast.show(context, "当前亮度:"+(int)(layoutParams.screenBrightness*100)
                + "%");
        getWindow().setAttributes(layoutParams);
    }

    // 加减音量
    int k;

    public void setAudio(int volume) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 当前音量
        k = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 最大音量
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        k = k + volume;
        if (k >= 0 && k <= max) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, k,
                    AudioManager.FLAG_PLAY_SOUND);
        } else {
            return;
        }
        MyToast.show(context, "当前音量:" + (int)((1.0f * k / max) * 100) + "%");
        // audioManager.adjustVolume(i+volume,AudioManager.FLAG_PLAY_SOUND);
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            // TODO Auto-generated method stub
//            MyToast.show("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub
//            MyToast.show("停止说话");
        }

        @Override
        public void onError(SpeechError arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(context,
                    "发生错误:" + arg0.getErrorDescription(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onResult(RecognizerResult results, boolean arg1) {
            // TODO Auto-generated method stub
            final String text = JsonParser.parseIatResult(results
                    .getResultString());
            if (text.length() == 1) {
                return;
            }
            /*if (System.currentTimeMillis() - xunfeiTime >= voiceTopic.getPlayStat() * 1000) {
                return;
            }*/
            if (ll_say.getVisibility() == GONE) {
                return;
            }
//            MyToast.show(i++ +"识别出来:"+text+ "         "+(!topics.get(current_question_num-1).isPlayed()));
            if (voiceTopic.getTopicName().toUpperCase().equals(text.toUpperCase())) {
                mIat.stopListening();
                switchRightOrError(2);
                MyToast.show(context, "回答正确,你真棒");
                if (getIntent().getBooleanExtra("isYunhailuo", false) && topics.get(current_question_num - 1).getIsRight() == 0) {
                    hailuo_score.setVisibility(View.VISIBLE);
                    answer_right_num++;
                    final int current_question_score = (int) (Math.round(100f * answer_right_num / topics.size()) - yunhailuo_score_sum_num);
                    hailuo_question_score_add.setText("+" + current_question_score);

                    AnimationUtils.hailuoAnimation(hailuo_score, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            hailuo_score.setVisibility(GONE);
                            yunhailuo_score_sum_num += (int) current_question_score;
                            if (yunhailuo_score_sum_num > 100) {
                                yunhailuo_score_sum_num = 100;
                            }
                            setStarAndYunhailuo_score_sum_margin();
                            yunhailuo_score_sum.setText("" + (int) yunhailuo_score_sum_num);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                if (topics.get(current_question_num - 1).getIsRight() == 0) {
                    saveAnswer(1, getScoreSonId(Integer.parseInt(voiceTopic.getTopicId())), null);
                }
                topics.get(current_question_num - 1).setIsRight(2);
                playMp(true);
                if (isEnd) {
                    mHandler.removeCallbacks(runnable);
                /*判断课间问答是否回答完   没回答完先弹出课间问答

                if (inClass.size() > 0 && !inClass.get(inClass.size() - 1).isPlayed()) {
                    for (int i = 0; i < inClass.size(); i++) {
                        if (!inClass.get(i).isPlayed()) {
                            inClass.get(i).setPlayed(true);
                            final int a = i + 1;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    voiceTopic = inClass.get(a);
                                    setTitle(inClass.get(a).getTopicName(), a);
                                    getOptions(inClass.get(a).getTopicId());
                                }
                            }, 2000);
                            return;
                        }
                    }
                }*/
                    if (isOnce && getIntent().getBooleanExtra("isDisney", false)) {
                        if (videoview.getDuration() * 0.8 < reallyPlayTime) {
                            isOnce = false;
                            rl_video_disney_apple.setVisibility(View.VISIBLE);
                            ll_show.setVisibility(GONE);
                            videoview.setCanShowControllerAndLoading(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Net net = new Net();
                                    net.addApple("1");
                                }
                            }).start();
                        }
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rl_video_disney_apple.setVisibility(GONE);
                                    if (afterIndex < afterClass.size()) {
                                        getQuestionAfterClass();
                                    } else {
                                        MyToast.show(context, "正在获取评分数据...");
                                        getScoreAndFlow();
                                    }
                                }
                            });

                        }
                    }, 2000);
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimationUtils.startTranslateAnimation(ll_show, 0, 0, 0, 2, 250);
                                    videoview.start();
                                    ll_show.setVisibility(GONE);
                                    videoview.setCanShowControllerAndLoading(true);
                                }
                            });
                        }
                    }, 2000);
                }
            } else {
                if (topics.get(current_question_num - 1).getIsRight() == 0) {
                    saveAnswer(0, getScoreSonId(Integer.parseInt(voiceTopic.getTopicId())), null);
                }
                topics.get(current_question_num - 1).setIsRight(1);
                playMp(false);
                switchRightOrError(1);
            }
            mHandler.removeCallbacks(xunfeiAnswerError);
            xunfeiTime = 0;


        }

        @Override
        public void onVolumeChanged(int arg0, byte[] arg1) {
            // TODO Auto-generated method stub

        }
    };

    long historyTime;

    //初始化界面
    private void initView() {
        Uri uri = Uri.parse(getIntent().getStringExtra("videoPath"));
        L.e("视频路径:" + getIntent().getStringExtra("videoPath"));
        MediaController mc = new MediaController(this);
        videoview.setVideoURI(uri);
        historyTime = videoHistoryUtils.queryHistory(UserInfo.getUserId(context), getIntent().getStringExtra("videoId"));
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoview.seekTo((int) historyTime);
            }
        });
        MyBitmapUtils.display(video_disney_apple_img, "drawable://" + ApiUtils.getResDrawableID(R.drawable.video_apple,"video_apple"));//R.drawable.video_apple);//
        MyBitmapUtils.display(right, "drawable://" + ApiUtils.getResDrawableID(R.drawable.right,"right"));//R.drawable.right);//
        MyBitmapUtils.display(error, "drawable://" + ApiUtils.getResDrawableID(R.drawable.error,"error"));//R.drawable.error);//
        MyBitmapUtils.display(yuyin, "drawable://" + ApiUtils.getResDrawableID(R.drawable.yuyin,"yuyin"));//R.drawable.yuyin);//
        error_gif.setImageResource(ApiUtils.getResDrawableID(R.drawable.error_gif,"error_gif"));//R.drawable.error_gif);//
        right_gif.setImageResource(ApiUtils.getResDrawableID(R.drawable.right_gif,"right_gif"));//R.drawable.right_gif);//
    }

    //初始化数据
    private void initData() {
        RequestParams params = new RequestParams(Constants.BASE_URL + Constants.GET_TOPICS);
        params.addBodyParameter("videoId", "" + getIntent().getStringExtra("videoId"));
        L.e("videoIdaaa:" + getIntent().getStringExtra("videoId"));
        MyHttpUtils.request(params, mHandler, 1);
        soMp = MediaPlayer.create(this, ApiUtils.getResRawID(R.raw.sogreat,"sogreat"));//R.raw.sogreat);//
        coMp = MediaPlayer.create(this, ApiUtils.getResRawID(R.raw.comeon,"comeon"));//R.raw.comeon);//
    }

    private void getCollectStatus() {
        RequestParams params = new RequestParams(Constants.BASE_URL + Constants.GET_VIDEO_COLLECT_STATUS);
        params.addBodyParameter("userId", UserInfo.getUserId(context));
        params.addBodyParameter("videoId", getIntent().getStringExtra("videoId"));
        MyHttpUtils.request(params, mHandler, 8);
    }
    //播放答对答错语音
    private void playMp(boolean isRight) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (soMp.isPlaying()) {
            soMp.stop();
        }
        if (coMp.isPlaying()) {
            coMp.stop();
        }
        if (isRight) {
            soMp.release();
            soMp = MediaPlayer.create(this,ApiUtils.getResRawID(R.raw.sogreat,"sogreat"));//R.raw.sogreat);//
            soMp.start();
        } else {
            coMp.release();
            coMp = MediaPlayer.create(this,ApiUtils.getResRawID(R.raw.comeon,"comeon"));//R.raw.comeon);//
            coMp.start();
        }

    }

    private void setListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (options.get(position).getIsRight() == 1) {
                    switchRightOrError(2);
                    MyToast.show(context, "回答正确,你真棒");
                    if (getIntent().getBooleanExtra("isYunhailuo", false) && topics.get(current_question_num - 1).getIsRight() == 0) {
                        hailuo_score.setVisibility(View.VISIBLE);
                        answer_right_num++;
                        final int current_question_score = (int) (Math.round(100f * answer_right_num / topics.size()) - yunhailuo_score_sum_num);
                        hailuo_question_score_add.setText("+" + current_question_score);
                        hailuo_score.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                hailuo_score.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                L.e("yunhailuo_score_sum:" + yunhailuo_score_sum.getLeft() + "        hailuo_score:" + hailuo_score.getLeft());
                            }
                        });
                        hailuo_score.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                L.e("width" + hailuo_score.getWidth());
                                hailuo_score.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                AnimationUtils.hailuoAnimation(hailuo_score, new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        hailuo_score.setVisibility(GONE);
                                        yunhailuo_score_sum_num += current_question_score;
                                        if (yunhailuo_score_sum_num > 100) {
                                            yunhailuo_score_sum_num = 100;
                                        }
                                        setStarAndYunhailuo_score_sum_margin();
                                        yunhailuo_score_sum.setText("" + (int) yunhailuo_score_sum_num);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                            }
                        });


                    }
                    if (topics.get(current_question_num - 1).getIsRight() == 0) {
                        saveAnswer(1, getScoreSonId(Integer.parseInt(voiceTopic.getTopicId())), "" + options.get(position).getOptionId());
                    }
                    
                    topics.get(current_question_num - 1).setIsRight(2);
                    playMp(true);
                    choose_listview.setEnabled(false);
                    choose_gridView.setEnabled(false);
                    if (isEnd) {
                        mHandler.removeCallbacks(runnable);
                        /*if (inClass.size() > 0 && !inClass.get(inClass.size() - 1).isPlayed()) {
                            for (int i = 0; i < inClass.size(); i++) {
                                if (!inClass.get(i).isPlayed()) {
                                    inClass.get(i).setPlayed(true);
                                    final int a = i;
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            voiceTopic = inClass.get(a);
                                            setTitle(inClass.get(a).getTopicName(), a + 1);
                                            L.e("获取课间练习题");
                                            getOptions(inClass.get(a).getTopicId());
                                        }
                                    }, 2000);
                                    return;
                                }
                            }
                        }*/
                        if (isOnce && getIntent().getBooleanExtra("isDisney", false)) {
                            if (videoview.getDuration() * 0.8 < reallyPlayTime) {
                                isOnce = false;
                                rl_video_disney_apple.setVisibility(View.VISIBLE);
                                ll_show.setVisibility(GONE);
                                videoview.setCanShowControllerAndLoading(true);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Net net = new Net();
                                        net.addApple("1");
                                    }
                                }).start();
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rl_video_disney_apple.setVisibility(GONE);
                                if (afterIndex < afterClass.size()) {
                                    getQuestionAfterClass();
                                } else {
                                    L.e("弹出评分表");
                                    MyToast.show(context, "正在获取评分数据...");
                                    getScoreAndFlow();
                                }
                            }
                        }, 2000);
                    } else {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AnimationUtils.startTranslateAnimation(ll_show, 0, 0, 0, 2, 250);
                                        videoview.start();
                                        ll_show.setVisibility(GONE);
                                        videoview.setCanShowControllerAndLoading(true);
                                    }
                                });
                            }
                        }, 2000);
                    }
                } else {
                    if (topics.get(current_question_num - 1).getIsRight() == 0) {
                        saveAnswer(0, getScoreSonId(options.get(position).getTopicId()), "" + options.get(position).getOptionId());
                    }
                    playMp(false);
                    switchRightOrError(1);
                    MyToast.show(context, "回答错误,请再次选择");
                    topics.get(current_question_num - 1).setIsRight(1);
                }
            }
        };
        choose_gridView.setOnItemClickListener(listener);
        choose_listview.setOnItemClickListener(listener);
        choose_gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view == null) {
                    if (mHandler != null) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                parent.getOnItemSelectedListener().onItemSelected(parent, parent.getChildAt(position), position, id);
                            }
                        }, 10);
                    }

                } else {
                    for (int i = 0; i < parent.getCount(); i++) {
                        View childAt = parent.getChildAt(i);
                        if (childAt != null) {
                            ImageView smile = (ImageView) childAt.findViewById(ApiUtils.getResIdID(R.id.smile,"smile"));//R.id.smile);//
                            ImageView image = (ImageView) childAt.findViewById(ApiUtils.getResIdID(R.id.image,"image"));//R.id.image);//
                            if (position == i) {
                                smile.setVisibility(View.VISIBLE);
                                AnimationUtils.chooseGrid(image);
                            } else {
                                smile.setVisibility(GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        videoview.setCollectionClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams(Constants.BASE_URL + Constants.COLLECTION_OR_CANCEL);
                params.addBodyParameter("userId", UserInfo.getUserId(context));
                params.addBodyParameter("videoId", "" + getIntent().getStringExtra("videoId"));
                MyHttpUtils.request(params, mHandler, 7);
            }
        });
    }


    //切换回答状态   0 未答题    1 回答错误  2 回答正确
    private void switchRightOrError(int isTrue) {
        if (isTrue == 1) {
            error.setVisibility(View.VISIBLE);
            error_gif.setVisibility(View.VISIBLE);
            right.setVisibility(GONE);
            right_gif.setVisibility(GONE);
        } else if (isTrue == 2) {
            error.setVisibility(GONE);
            error_gif.setVisibility(GONE);
            right.setVisibility(View.VISIBLE);
            right_gif.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(GONE);
            error_gif.setVisibility(GONE);
            right.setVisibility(GONE);
            right_gif.setVisibility(GONE);
        }
    }

    //获取课后练习题
    public void getQuestionAfterClass() {
        rl_video_disney_apple.setVisibility(GONE);
        TopicBean topicBean = afterClass.get(afterIndex);
        voiceTopic = topicBean;
        afterIndex++;
        setTitle(topicBean.getTopicName(), inClass.size() + afterIndex);
        if (choose_gridView.getAdapter() != null) {
            choose_gridView.requestFocus();
        }
        topicBean.setPlayed(true);
        getOptions(topicBean.getTopicId());
    }


    //分割英文中文
    private void setTitle(String topicName, int a) {
        String[] s = topicName.split("@");
        tv_tpName.setText(a + "." + s[0]);
        current_question_num = a;
        if (getIntent().getBooleanExtra("isYunhailuo", false)) {
            current_question.setText("" + a);
        }
        if (s.length > 1) {
            tv_tpName_cn.setText(s[1]);
        } else {
            tv_tpName_cn.setText("");
        }
    }

    Runnable noUserPause = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(this);
            MyToast.show(getApplicationContext(), "网络不给力!");
        }
    };


    private void setStarAndYunhailuo_score_sum_margin() {
        if (yunhailuo_score_sum_num / 100f == 1) {
            MyBitmapUtils.display(star3,ApiUtils.getResDrawableID(R.drawable.hailuo_star,"hailuo_star"));//R.drawable.hailuo_star);//
        } else if (yunhailuo_score_sum_num / 100f >= 0.66f) {
            MyBitmapUtils.display(star2,ApiUtils.getResDrawableID(R.drawable.hailuo_star,"hailuo_star"));//R.drawable.hailuo_star);//
        } else if (yunhailuo_score_sum_num / 100f >= 0.33f) {
            MyBitmapUtils.display(star1,ApiUtils.getResDrawableID(R.drawable.hailuo_star,"hailuo_star"));//R.drawable.hailuo_star);//
        } else {
            MyBitmapUtils.display(star1,ApiUtils.getResDrawableID(R.drawable.hailuo_star_no,"hailuo_star_no"));// R.drawable.hailuo_star_no);//
            MyBitmapUtils.display(star2,ApiUtils.getResDrawableID(R.drawable.hailuo_star_no,"hailuo_star_no"));// R.drawable.hailuo_star_no);//
            MyBitmapUtils.display(star3,ApiUtils.getResDrawableID(R.drawable.hailuo_star_no,"hailuo_star_no"));// R.drawable.hailuo_star_no);//
        }
    }


    @Override
    protected void onStop() {
        if (addToHistoryBack != null && reallyPlayTime > 1000 * 60 * 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Net net = new Net();
                    try {
                        net.updateHistory(addToHistoryBack.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isCreated = false;
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        //保存播放记录
        if (isEnd) {
            videoHistoryUtils.deleteHistory(UserInfo.getUserId(context), getIntent().getStringExtra("videoId"));
        } else {
            if (currentTime > 30 * 1000) {
                videoHistoryUtils.insertHistory(UserInfo.getUserId(context), getIntent().getStringExtra("videoId"), currentTime);
            }
        }
        if (videoview != null) {
//            videoview.stopPlayback();
            videoview.stopPlayback();
            videoview = null;
        }
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacks(noUserPause);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
//        recycleBitmap(goto_center,goto_homepage,video_disney_apple_img,right,error,video_loading);
        super.onDestroy();
    }
}

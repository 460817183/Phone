package com.example.goviewer;

/**
 * Created by xiao on 2016/10/31.
 * 存放所有常量
 */

public class Constants {
    public static final String SP_TAG="config";
    public static final String BASE_URL="http://115.28.83.211:8082/goviewer966/";
    public static final String BASE_URL2="http://139.129.129.247:8088/goviewer966/";
    public static final int HTTP_ERRORCODE=10086;
    public static final int TRAJECTORY=10087;
    public static final String NET_ERROR="网络请求出错，请检查网络";
    /**
     * 调用自己的支付接口
     * */
    public static final String CUSTOM_PAY="fore/homePage/qrcodeTv.html";

    /*
    * 获取小米订单信息
    *
    * */
    public static final String XIAOMI_PAY="fore/homePage/getXiaomiOrderInfo.html";

    /*
    * 判断支付是否成功
    * */
    public static final String CHECK_PAY_SUCCESS="edu/fore/InterAction/updateXiaomiOrder.html";


    /**
     * 获取课堂问答得分
     * */
    public static final String QUESTION_SCORE_DATA="edu/fore/InterAction/getAllVideoScore.html";

    /**
     * 获取奖励数据
     * */
    public static final String REWARD="fore/homePage/getById.html";
    /**
     * 获取当前视频下的话题
     * */
    public static final String GET_TOPICS="edu/fore/InterAction/getQuestionByVideoId.html";
    /**
     * 获取当前话题下的选项
     * */
    public static final String GET_OPTIONS="edu/fore/InterAction/getOptionsByTopicId.html";

    /*
    * 判断语音是否正确
    * */
    public static final String CHECK_VOICE_ISRIGHT="edu/fore/InterAction/pdVoice.html";

    /*
    * 获取评分子表与话题映射关系
    * */
    public static final String EDU_SCORE_ADD="fore/member/eduScoreAdd.html";

    /**
     * 更新评分子表
     * */
    public static final String UPDATE_SON="fore/member/updateSon.html";

    /**
     * 添加到播放历史记录
     * */
    public static final String ADD_TO_HISTORY="edu/fore/InterAction/addVoidMark.html";

    /**
     * 获取评分表
     * */
    public static final String GET_SCORE_AND_FLOWER="fore/member/scoreAndFlow.html";
    /**
     * 获取水平测试题目
     * */
    public static final String GET_TEST_QUESTION="https://tx.ikidstv.com/api/v1/quiz/quizzes/get_one_quiz_for_level_test";

    /*
    获取年级
    *
    * */
    public static final String GET_GRADES="fore/homePage/getCategoryType.html";

    /*
    * 获取年级下的视频
    * */
    public static final String GET_VIDEO_BY_GRADE="fore/homePage/getMessByTypeId.html";

    /*
    * 判断视频是否可以观看
    * */
    public static final String CHECK_VIDEO="fore/member/checkPro_1.html";

    /*
    * 获取收藏的视频
    * */
    public static final String GET_COLLECTIONED="edu/fore/InterAction/getVoidCollect.html";

    /*
    * 收藏或取消收藏
    * */
    public static final String COLLECTION_OR_CANCEL="edu/fore/InterAction/addVoidCollect.html";

    /*
    * 已购买的视频
    * */
    public static final String GET_BOUGHT_VIDEO="edu/fore/InterAction//getYiGouMaiByUserId.html";

    /*
    * 判断视频是否收藏
    * */
    public static final String GET_VIDEO_COLLECT_STATUS="edu/fore/InterAction/getVoidCollectStatus.html";


    /*
    * 获取本类视频信息
    * */
    public static final String GET_CATEGORY_BY_TYPEID="edu/fore/InterAction/getCategoryByTypeId.html";

    /*
    * 注册发送验证码
    * */
    public static final String GET_REGISTER_CODE="fore/homePage/getPhoneCode.html";
    /*
    * 判断手机号是否已注册
    * */
    public static final String IS_USER_REGISTER="fore/member/isUserRegister.html";

    /*
    * 用户轨迹记录
    * */
    public static final String SAVE_USER_HISTORY="fore/homePage/saveUserHistory.html";

    /*
    * 登录
    * */
    public static final String LOGIN="goviewer966/login/logcheck.html";

    /*
    * 注册
    * */
    public static final String REGISTER="fore/member/register.html";

    /*
    * 快速注册
    * */
    public static final String QUICK_REGISTER="edu/fore/InterAction/getUserNm.html";
    /*
    * 绑定手机
    * */
    public static final String BIND_PHONE="edu/fore/InterAction/boundTelNum.html";

    /*
    * 获取赠送套餐
    *
    * */
    public static final String  GET_COMBO="fore/homePage/getCombo.html";


    /*
    * 上次学到
    *
    * */
    public static final String  GET_SCXD="edu/fore/InterAction/getScgk.html";

    /*
    * 本次应学
    * */
    public static final String  GET_BCYX="edu/fore/InterAction/getBcgk.html";

    /*
    * 获取课程分类
    * */
    public static final String GET_CATEGORY="fore/homePage/getCategoryType.html";
}

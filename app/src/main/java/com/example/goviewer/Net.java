package com.example.goviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class Net {

    public static String urlTest;

    private static Net net=new Net();;

    public static Net getInstance(){
       return net;
    }
    /*
     * 获取用户详情
     */
    public String getUserInfo() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getUserdetails.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 奖励详情
     */
    public String getReward() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getById.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 自己的支付接口
     */
    public static String payWeixinAndZhifubao(String typeId) {
        return javaHttpGet("http://139.129.129.247:8088/goviewer966/fore/homePage/qrcodeTv.html?userId=" + UserInfo.getUserId() + "&typeId=" + typeId);
    }

    /*
     * 修改用户信息
     */
    public String updateUserInfo(String newAccount, String oldPwd, String newPwd) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/updateUser.html?oldUserName=" + UserInfo.getUserAccount() + "&oldPassword=" + oldPwd + "&userName=" + newAccount + "&password=" + newPwd);
    }

    /*
     * 添加苹果
     */
    public String addApple(String num) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/addApple.html?userId=" + UserInfo.getUserId() + "&apple=" + num);
    }

    /*
     * 视频结束
     */
    public String updateHistory(String history) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/updateVoidMark.html?id=" + history);
    }

    /*
     * 软件更新
     */
    public String updateApk(String version) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getBatMessage.html?batJb=" + version);
    }

    /*
     * 支付23333
     */
    public String payErr(String orderno) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/updateXiaomiOrderFalse.html?orderno=" + orderno);
    }

    /*
     * 支付成功通知
     */
    public String paySucc(String orderno) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/updateXiaomiOrder.html?orderno=" + orderno);
    }



    /*
   * 获取getXiaomiOrderInfo
   */
    public String getXiaomiOrderInfo(String typeId,int month) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getXiaomiOrderInfo.html?typeId=" + typeId + "&userId=" + UserInfo.getUserId()+"&month="+month);
    }
    /*
  * 获取getXiaomiOrderInfo
  * 单集购买
  */
    public String getXiaomiOrderInfo(String videoId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getXiaomiOrderInfo.html?videoId=" + videoId + "&userId=" + UserInfo.getUserId());
    }

    /*
     * 获取榜单
     */
    public String getRanking() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getPhbMess.html"+(UserInfo.getUserId().equals("-1")?"":("?userId="+UserInfo.getUserId())));
    }

    /*
     * 获取上次应学
     */
    public String getHLearn() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getScgk.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 获取本次应学
     */
    public String getLearn() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getBcgk.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 获取历史记录
     */
    public String getHistory() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getVoidHistory.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 添加历史记录
     */
    public String addHistory(String videoId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/addVoidMark.html?userId=" + UserInfo.getUserId() + "&videoId=" + videoId);
    }

    /*
     * 获取得分
     */
    public String getScore() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getAllVideoScore.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 获取报表
     */
    public String getReport(String type) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getAllVideoTestScore.html?userId=" + UserInfo.getUserId() + "&type=" + type);
    }

    /*
     * 获取收藏列表
     */
    public String getCollect() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getVoidCollect.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 收藏
     */
    public String collect(String videoId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/addVoidCollect.html?userId=" + UserInfo.getUserId() + "&videoId=" + videoId);
    }

    /*
     * 获取已购买列表
     */
    public String getPurchased() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getTypeIdByUserId.html?userId=" + UserInfo.getUserId());
    }

    /*
     * 获取评分
     */
    public String scoreAndFlow(String scoreId, String videoId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/scoreAndFlow.html?userId=" + UserInfo.getUserId() + "&type=" + 0 + "&scoreId=" + scoreId + "&videoId=" + videoId);
    }

    /*
     * 跟新评分子表
     */
    public String updateSon(String stat, String id) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/updateSon.html?userId=" + UserInfo.getUserId() + "&stat=" + stat + "&id=" + id);
    }

    /*
     * 评分奖励
     */
    public String eduScoreAdd(String videoId, String typeId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/eduScoreAdd.html?videoId=" + videoId + "&userId=" + UserInfo.getUserId() + "&typeId=" + typeId);
    }

    /*
     * 使用卡密
     */
    public String useCamilo(String camiCode, String typeId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/pdCami.html?camiCode=" + camiCode + "&userId=" + UserInfo.getUserId() + "&typeId=" + typeId);
    }

    /*
     * 判断用户是否可以观看  旧版本
     */
    /*public String judgeWatch(String typeId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/checkPro.html?typeId=" + typeId + "&userId=" + UserInfo.getUserId());
    }*/

    /*
     * 判断用户是否可以观看  新版本
     */
    public String judgeWatch(String videoId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/checkPro_1.html?videoId=" + videoId + "&userId=" + UserInfo.getUserId());
    }

    /*
     * 登陆
     */
    public String login(String userNm, String pwd) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/goviewer966/login/logcheck.html?userNm=" + userNm + "&pwd=" + pwd);
    }

    /*
     * 注册
     */
    public String register(String userNm, String pwd,String fld1) {
        UserInfo.setUserFastLogin("0");
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/member/register.html?userNm=" + userNm + "&pwd=" + pwd+"&fld1="+fld1);
    }

    /*
     * 获取视频
     */
    public String getVideoList(String type) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getMessByTypeId.html?typeId=" + type + "&userId=" + UserInfo.getUserId());
    }

    /*
    * 获取视频信息
    */
    public String getVideoAllDetails(String type) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getAllVideoByTypeId.html?categoryId=" + type + "&userId=" + UserInfo.getUserId());
    }

    /*
     * 获取年纪
     */
    public String getNJ(String type) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getCategoryType.html?dictype=3&parentid=" + type);
    }

    /*
     * 获取课程
     */
    public String getKC(String type) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getCategoryType.html?dictype=2&parentid=" + type);
    }

    /*
     * 获取首页图片
     */
    public String getMainImg() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getCategoryType.html?dictype=1");
    }

    /*
     * 获取视频 
     */
    public String getVideo() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getVideo.html");
    }

    /*
     * 获取视频下话题 --------------------------------------
     */
    public String getTopic(String videoId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getQuestionByVideoId.html?videoId=" + videoId);
    }
    /*
     * 判断是否绑定手机号
     */
    public String checktelNum() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/checktelNum.html?userId=" +UserInfo.getUserId());
    }

    /*
     * 获取语音输入是否正确
     */
    public String getVoice(String content) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/pdVoice.html?content=" + content + "&userId=" + UserInfo.getUserId());
    }

    /*
     * 获取话题下的选项
     */
    public String getOption(String optionId) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/edu/fore/InterAction/getOptionsByTopicId.html?topicId=" + optionId);
    }

    //获取课程信息
    public String getCategory() {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getCategory.html");
    }

    //获取视频列表
    public String getVideo(String typeid) {
        return javaHttpGet("http://115.28.83.211:8082/goviewer966/fore/homePage/getMessByTypeId.html?typeId=" + typeid);
    }
    //获取视频列表
    public String getQuestion(String id,int answer,String session) {
        return javaHttpGet("https://tx.ikidstv.com/api/v1/quiz/quizzes/get_one_quiz_for_level_test?id=" +id+"&answer="+answer+"&session="+session);
    }

    /*
     * 从网络获取图片
     */
    protected InputStream getImageStream2(String path) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 从网络获取图片
     */
    protected Bitmap getImageStream(String path) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String javaHttpGet(String url) {
        Log.v("url：", url);
        urlTest = url;
        URL pathUrl;
        String resultData;
        try {
            //创建一个URL对象
            pathUrl = new URL(url);
            HttpURLConnection urlConnect = (HttpURLConnection) pathUrl.openConnection();  //打开一个HttpURLConnection连接
            urlConnect.setConnectTimeout(10000);  // 设置连接超时时间
            urlConnect.connect();
            //输入流读取字节，再将它们转换成字符.得到读取的内容
            InputStreamReader in = new InputStreamReader(urlConnect.getInputStream());
            //BufferedReader:接受Reader对象作为参数，并对其添加字符缓冲器，使用readline()方法可以读取一行
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = "";
            resultData = "";
            while ((inputLine = buffer.readLine()) != null) {
                //利用循环来读取数据
                resultData += inputLine;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "MalformedURLException";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "IOException";
        }
        return resultData;
    }

    public List<Map<String, Object>> jsonToList(String json) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = (JSONObject) jsonArray.opt(i);
                Iterator keys = jo.keys();
                Map<String, Object> maps = new HashMap<String, Object>();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    maps.put(key.toString(), jo.get(key).toString());
                }
                list.add(maps);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                map.put(key.toString(), jsonObject.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}

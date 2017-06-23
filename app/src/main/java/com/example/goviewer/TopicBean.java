package com.example.goviewer;

/**
 * Created by xiao on 2016/11/8.
 */

public class TopicBean {
    private int playTime;
    private int playStat;
    private String topicId;
    private String stat;
    private String topicName;
    private String paramType;
    private String voice;
    private String bgimg;
    private boolean isPlayed;
    private int isRight;
    private int isSuspend;
    public synchronized boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public int getPlayTime() {
        return playTime;
    }
    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getPlayStat() {
        return playStat;
    }

    public void setPlayStat(int playStat) {
        this.playStat = playStat;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }


    public int getIsRight() {
        return isRight;
    }

    public void setIsRight(int isRight) {
        this.isRight = isRight;
    }

    public int getIsSuspend() {
        return isSuspend;
    }

    public void setIsSuspend(int isSuspend) {
        this.isSuspend = isSuspend;
    }
}

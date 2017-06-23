package com.example.goviewer;


import java.util.List;

/**
 * Created by xiao on 2016/11/9.
 */
//评分表
public class ScoreAndFlow {
    private int scores; //本次得分
    private int pmNum;  //本次排名
    private List<Pm> pm;//排名列表
    private String stat;//是否获得小红花
    private int honorStat;
    private AchievementBean.Honor honor;
    public class Pm{
        private String userNm;
        private int score;
        private int pm;

        public String getUserNm() {
            return userNm;
        }

        public void setUserNm(String userNm) {
            this.userNm = userNm;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getPm() {
            return pm;
        }

        public void setPm(int pm) {
            this.pm = pm;
        }
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getPmNum() {
        return pmNum;
    }

    public void setPmNum(int pmNum) {
        this.pmNum = pmNum;
    }

    public List<Pm> getPm() {
        return pm;
    }

    public void setPm(List<Pm> pm) {
        this.pm = pm;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public int getHonorStat() {
        return honorStat;
    }

    public void setHonorStat(int honorStat) {
        this.honorStat = honorStat;
    }

    public AchievementBean.Honor getHonor() {
        return honor;
    }

    public void setHonor(AchievementBean.Honor honor) {
        this.honor = honor;
    }
}

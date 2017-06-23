package com.example.goviewer;

import java.util.List;

/**
 * Created by Shinelon on 2017/4/7.
 */

public class AchievementBean {
    private List<Honor> honor;
    private List<Honor> collageList;
    private String score;
    private Grade grade;
    public class Honor{
        private String name;
        private String desc;
        private int stat;
        private String id;
        private String img;
        private String type;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStat() {
            return stat;
        }

        public void setStat(int stat) {
            this.stat = stat;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public class Grade{
        private String name;
        private int stat;
        private int id;
        private List<Honor> universityList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStat() {
            return stat;
        }

        public void setStat(int stat) {
            this.stat = stat;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Honor> getUniversityList() {
            return universityList;
        }

        public void setUniversityList(List<Honor> universityList) {
            this.universityList = universityList;
        }
    }


    public List<Honor> getHonor() {
        return honor;
    }

    public void setHonor(List<Honor> honor) {
        this.honor = honor;
    }

    public List<Honor> getCollageList() {
        return collageList;
    }

    public void setCollageList(List<Honor> collageList) {
        this.collageList = collageList;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}

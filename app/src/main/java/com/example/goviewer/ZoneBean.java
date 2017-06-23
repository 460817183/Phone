package com.example.goviewer;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by xqf on 2017/5/24.
 */

public class ZoneBean {
    private String check;
    private List<Zone> province;
    private List<JSONObject> city;
    private List<JSONObject> zone;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public List<Zone> getProvince() {
        return province;
    }

    public void setProvince(List<Zone> province) {
        this.province = province;
    }

    public List<JSONObject> getCity() {
        return city;
    }

    public void setCity(List<JSONObject> city) {
        this.city = city;
    }

    public List<JSONObject> getZone() {
        return zone;
    }

    public void setZone(List<JSONObject> zone) {
        this.zone = zone;
    }
}

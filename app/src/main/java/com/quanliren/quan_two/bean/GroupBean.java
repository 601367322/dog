package com.quanliren.quan_two.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupBean implements Serializable {
    private String id;
    private String crowdcode;
    private String crowdname;
    private String avatar;
    private String summary;
    private String nownum;
    private String maxnum;
    private String userid;
    private String hostavatar;
    private int crowdrole;

    public int getCrowdrole() {
        return crowdrole;
    }

    public void setCrowdrole(int crowdrole) {
        this.crowdrole = crowdrole;
    }

    private String AdminBeansStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCrowdcode() {
        return crowdcode;
    }

    public void setCrowdcode(String crowdcode) {
        this.crowdcode = crowdcode;
    }

    public String getCrowdname() {
        return crowdname;
    }

    public void setCrowdname(String crowdname) {
        this.crowdname = crowdname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNownum() {
        return nownum;
    }

    public void setNownum(String nownum) {
        this.nownum = nownum;
    }

    public String getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHostavatar() {
        return hostavatar;
    }

    public void setHostavatar(String hostavatar) {
        this.hostavatar = hostavatar;
    }

    public String getAdminBeansStr() {
        if (admins == null || admins.size() == 0) {
            return "";
        }
        return new Gson().toJson(admins);
    }

    public void setAdminBeansStr(String adminBeansStr) {
        AdminBeansStr = adminBeansStr;
        admins = new Gson().fromJson(adminBeansStr, new TypeToken<ArrayList<AdminBean>>() {
        }.getType());
    }

    public List<AdminBean> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminBean> admins) {
        this.admins = admins;
    }

    private List<AdminBean> admins;

    public class AdminBean implements Serializable {
        public String userid;
        public String imgpath;
    }
}

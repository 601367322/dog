package com.quanliren.quan_two.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DongTaiBean implements Serializable {
    private String dyid;
    private String ctime;
    private String content;
    private String userid;
    private String nickname;
    private String age;
    private String sex;
    private String avatar;
    private String cnum;
    private String longitude;
    private String distance;
    private String area;
    private int isvip;
    private int fontColor;
    private String type;
    private String latitude;
    public int zambia;
    public String zambiastate;
    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setType(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
    public int getIsvip() {
        return isvip;
    }

    public void setIsvip(int isvip) {
        this.isvip = isvip;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCnum() {
        if (cnum == null || cnum.equals("")) {
            return "0";
        }
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public String getDyid() {
        return dyid;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        if (sex.equals("")) {
            return "0";
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String imgs;

    public String getImgs() {
        return new Gson().toJson(imglist);
    }

    public void setImgs(String imgs) {
        this.imglist = new Gson().fromJson(imgs,
                new TypeToken<List<ImageBean>>() {
                }.getType());
        this.imgs = imgs;
    }

    private List<ImageBean> imglist;

    public List<ImageBean> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImageBean> imglist) {
        this.imglist = imglist;
    }

    private List<DongTaiReplyBean> commlist;

    public List<DongTaiReplyBean> getCommlist() {
        if (commlist == null) {
            return new ArrayList<DongTaiReplyBean>();
        }
        return commlist;
    }

    public void setCommlist(List<DongTaiReplyBean> commlist) {
        this.commStr = new Gson().toJson(commlist);
        this.commlist = commlist;
    }

    private String commStr;

    public String getCommStr() {
        return new Gson().toJson(commlist);
    }

    public void setCommStr(String commStr) {
        this.commlist = new Gson().fromJson(commStr,
                new TypeToken<List<DongTaiReplyBean>>() {
                }.getType());
        this.commStr = commStr;
    }

}

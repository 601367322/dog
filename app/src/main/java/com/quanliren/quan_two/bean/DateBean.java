package com.quanliren.quan_two.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DateBean implements Serializable {

    private int dtid;
    private int dtype = 1;

    private String aim;
    private String objsex;
    private String address;
    private String whopay;

    private int ctype;
    private int coin;

    private String dtime;
    private String remark;
    private String busurl;
    private int peoplenum = 1;

    public String getBusurl() {
        return busurl;
    }

    public void setBusurl(String busurl) {
        this.busurl = busurl;
    }

    public int getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(int peoplenum) {
        this.peoplenum = peoplenum;
    }

    private int applynum;
    private int cnum;
    private String pubtime;
    private String userid;
    private int isapply;
    private int dtstate;

    public int getDtstate() {
        return dtstate;
    }

    public void setDtstate(int dtstate) {
        this.dtstate = dtstate;
    }

    public int getIsapply() {
        return isapply;
    }

    public void setIsapply(int isapply) {
        this.isapply = isapply;
    }

    private int iscollect;

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    private String usernumber;
    private String nickname;
    private String age;
    private int sex;
    private ArrayList<DongTaiReplyBean> commlist;
    private int isvip;
    private String birthday;
    private String avatar;
    private String cuserId;
    private String distance;
    private double longitude;
    private double latitude;

    public DateBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DateBean(int dtid, int dtype, String aim, String objsex,
                    String address, String whopay, int ctype, int coin, String dtime,
                    String remark, int applynum, int cnum, String pubtime,
                    String userid, String usernumber, String nickname, int age,
                    int sex, int isvip, String birthday, String avatar,String cuserId,
                    String distance, String longitude, String latitude) {
        super();
        this.dtid = dtid;
        this.dtype = dtype;
        this.aim = aim;
        this.objsex = objsex;
        this.address = address;
        this.whopay = whopay;
        this.ctype = ctype;
        this.coin = coin;
        this.dtime = dtime;
        this.remark = remark;
        this.applynum = applynum;
        this.cnum = cnum;
        this.pubtime = pubtime;
        this.userid = userid;
        this.usernumber = usernumber;
        this.nickname = nickname;
        this.sex = sex;
        this.isvip = isvip;
        this.birthday = birthday;
        this.avatar = avatar;
        this.cuserId=cuserId;
        this.distance = distance;
    }

    public ArrayList<DongTaiReplyBean> getCommlist() {
        if (commlist == null) {
            return new ArrayList<DongTaiReplyBean>();
        }
        return commlist;
    }

    public void setCommlist(ArrayList<DongTaiReplyBean> commlist) {
        this.commlist = commlist;
    }

    public int getDtid() {
        return dtid;
    }

    public void setDtid(int dtid) {
        this.dtid = dtid;
    }

    public int getDtype() {
        return dtype;
    }

    public void setDtype(int dtype) {
        this.dtype = dtype;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getObjsex() {
        return objsex;
    }

    public void setObjsex(String objsex) {
        this.objsex = objsex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWhopay() {
        return whopay;
    }

    public void setWhopay(String whopay) {
        this.whopay = whopay;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getApplynum() {
        return applynum;
    }

    public void setApplynum(int applynum) {
        this.applynum = applynum;
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsvip() {
        return isvip;
    }

    public void setIsvip(int isvip) {
        this.isvip = isvip;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setCuserId(String cuserId) {
        this.cuserId = cuserId;
    }
    public String getCuserId() {
        return cuserId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

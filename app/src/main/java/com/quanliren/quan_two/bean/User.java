package com.quanliren.quan_two.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String usernumber;
    private String activetime;
    private String visittime;
    private String income;
    private String emotion;
    private String appearance;
    private String bgimg;
    private String constell;
    private int userstate=0;
    private int coin;
    private String avatar;
    private String mobile;
    private String pwd;
    private String sex;
    private int uvid;
    private String nickname;
    private String signature;
    private String qq;
    private String job;
    private int isvip;
    private String height;
    private String weight;
    private String nature;
    private String hometown;
    private String birthday;
    private String age;
    private int isblacklist;
    private String education;
    private String avtwidth;
    private String avtheight;
    private String viptime;
    private String vipday;
    private String attenstatus;
    private String distance;
    private String levelname;
    private String dyid;
    private String dycontent;
    private String dyimgurl;
    private String dytime;
    private String cityname;
    private String levelid;
    private String otherName;
    private double longitude;
    private double latitude;
    private int applystate;
    private String message;
    private int isAtten;
    private String create_time;
    private boolean timeOut = false;
    private String mstate;
    private String xlstate;
    private String wxstate;
    private String qqstate;


    public String getXlstate() {
        return xlstate;
    }

    public void setXlstate(String xlstate) {
        this.xlstate = xlstate;
    }

    public String getWxstate() {
        return wxstate;
    }

    public void setWxstate(String wxstate) {
        this.wxstate = wxstate;
    }

    public String getQqstate() {
        return qqstate;
    }

    public void setQqstate(String qqstate) {
        this.qqstate = qqstate;
    }

    public String getMstate() {
        return mstate;
    }

    public void setMstate(String mstate) {
        this.mstate = mstate;
    }

    public int getIsAtten() {
        return isAtten;
    }

    public void setIsAtten(int isAtten) {
        this.isAtten = isAtten;
    }

    private int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public int getApplystate() {
        return applystate;
    }

    public void setApplystate(int applystate) {
        this.applystate = applystate;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getViptime() {
        return viptime;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public int getUvid() {
        return uvid;
    }

    public void setUvid(int uvid) {
        this.uvid = uvid;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getActivetime() {
        return activetime;
    }

    public void setActivetime(String activetime) {
        this.activetime = activetime;
    }

    public String getVisittime() {
        return visittime;
    }

    public void setVisittime(String visittime) {
        this.visittime = visittime;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public String getConstell() {
        return constell;
    }

    public void setConstell(String constell) {
        this.constell = constell;
    }


    public int getUserstate() {
        return userstate;
    }

    public void setUserstate(int  userstate) {
        this.userstate = userstate;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public String getVipday() {
        return vipday;
    }

    public void setVipday(String vipday) {
        this.vipday = vipday;
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


    private String token;
    private int userrole;
    private String userid;
    private String ctime;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getIsblacklist() {
        return isblacklist;
    }

    public void setIsblacklist(int isblacklist) {
        this.isblacklist = isblacklist;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getUserrole() {
        return userrole;
    }

    public void setUserrole(int userrole) {
        this.userrole = userrole;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getLevelid() {
        if (levelid.equals("")) {
            return "0";
        }
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }


    private String imgs;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public int getAvtwidth() {
        if (avtwidth == null || avtwidth.equals("")) {
            return 0;
        }
        return Integer.valueOf(avtwidth);
    }

    public void setAvtwidth(String avtwidth) {
        this.avtwidth = avtwidth;
    }

    public int getAvtheight() {
        if (avtheight == null || avtheight.equals("")) {
            return 0;
        }
        return Integer.valueOf(avtheight);
    }

    public void setAvtheight(String avtheight) {
        this.avtheight = avtheight;
    }

    public String getImgs() {
        return new Gson().toJson(imglist);
    }

    public void setImgs(String imgs) {
        this.imglist = new Gson().fromJson(imgs,
                new TypeToken<List<ImageBean>>() {
                }.getType());
        this.imgs = imgs;
    }

    private ArrayList<ImageBean> imglist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }


    public ArrayList<ImageBean> getImglist() {
        return imglist;
    }

    public void setImglist(ArrayList<ImageBean> imglist) {
        this.imglist = imglist;
        this.imgs = new Gson().toJson(imglist);
    }

    public User(String id, String avatar, String userName, String passWord,
                int sex, String nickname, String signature, String qq, String job,
                String hobby, int isvip, String height, String weight,
                String nature, String hometown, String birthday, String education,
                String constellation, int isOnline,
                ArrayList<ImageBean> imglist,String mstate,String xlstate,String wxstate,String qqstate) {
        super();
        this.id = id;
        this.avatar = avatar;
        this.nickname = nickname;
        this.signature = signature;
        this.qq = qq;
        this.job = job;
        this.height = height;
        this.weight = weight;
        this.nature = nature;
        this.hometown = hometown;
        this.birthday = birthday;
        this.education = education;
        this.imglist = imglist;
        this.mstate=mstate;
        this.wxstate=wxstate;
        this.xlstate=xlstate;
        this.qqstate=qqstate;

    }


    public int getIsvip() {
        return isvip;
    }

    public void setIsvip(int isvip) {
        this.isvip = isvip;
    }


    public String getAttenstatus() {
        return attenstatus;
    }

    public void setAttenstatus(String attenstatus) {
        this.attenstatus = attenstatus;
    }


    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public User(String id, String avatar, String nickname) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.id = id;
    }

    public User(MessageListBean bean) {
        this.avatar = bean.getAvatar();
        this.nickname = bean.getNickname();
        this.id = bean.getSenduid();
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public String getDyid() {
        return dyid;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
    }

    public String getDycontent() {
        return dycontent;
    }

    public void setDycontent(String dycontent) {
        this.dycontent = dycontent;
    }

    public String getDyimgurl() {
        return dyimgurl;
    }

    public void setDyimgurl(String dyimgurl) {
        this.dyimgurl = dyimgurl;
    }

    public String getDytime() {
        return dytime;
    }

    public void setDytime(String dytime) {
        this.dytime = dytime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }
}

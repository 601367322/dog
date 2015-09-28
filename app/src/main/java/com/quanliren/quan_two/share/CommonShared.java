package com.quanliren.quan_two.share;

import android.content.Context;

public class CommonShared {
    private SharedDataUtil sp;
    private SharedDataUtil.SharedDataEditor editor;
    private final String LOCATION = "location";// 定位的城市
    private final String AREA = "area";//区域
    private final String CHOSELOCATION = "choselocation";// 选择的城市
    private final String LOCATIONID = "locationid";// 城市id
    private final String CHOSELOCATIONID = "choselocationid";// 城市id
    private final String MESSAGEREMIND = "messageremind";// 消息提醒
    private final String FASTSTARTICON = "faststarticon";// 创建快捷方式
    public static final int OPEN = 1;
    public static final int CLOSE = 2;
    public final String VERSIONCODE = "versionCode";
    public final String VERSIONNAME = "versionName";
    public final String CHANNEL = "channel";
    public final String LAT = "lat";
    public final String LNG = "lng";
    public final String ISLOGIN = "isLogin";// 是否登陆
    public final String ISFIRSTSTART = "isfirststart";// 是否第一次打开软件
    public final String ISWHATSNEW = "iswhatsnew";// 是否whatnew跳转
    public final String ISFIRSTSEND = "isfirstsend";
    public final String DEVICEID = "deviceid";

    public final String NEWMSG_OPEN = "newmsgopen";
    public final String VIDEO_OPEN = "videoopen";
    public final String ZHENDONG_OPEN = "zhendongopen";


    public final String DCNT = "dcnt";
    public final String ACNT = "acnt";
    public final String PCNT = "pcnt";
    public final String VCNT = "vcnt";
    public final String CCNT = "ccnt";
    public final String PHIZCNT = "phizcnt";
    public final String MYPHIZCNT = "myphizcnt";
    public final String LOGINTIME = "logintime";
    public final String ISFIRSTCHAT = "isfirstchat";//用户第一次使用该手机打开聊天界面


    public CommonShared(Context context) {
        sp = SharedDataUtil.getInstance(context);
        editor = sp.getSharedDataEditor();
    }

    public void setLoginTime(String id,String loginTime) {
        editor.putString(LOGINTIME+id, loginTime);
        editor.commit();
    }

    public void setIsFirstChat(String isfirstchat) {
        editor.putString(ISFIRSTCHAT, isfirstchat);
        editor.commit();
    }

    public String getIsFirstChat() {
        return sp.getString(ISFIRSTCHAT, "");
    }

    public String getLoginTime(String id) {
        return sp.getString(LOGINTIME+id, "2015-8-4");
    }

    public String getDcnt() {
        return sp.getString(DCNT, "0");
    }

    public void setDcnt(String str) {
        editor.putString(DCNT, str);
        editor.commit();
    }
    public String getPhizcnt() {
        return sp.getString(PHIZCNT, "1");
    }
    public String getMyPhizcnt() {
        return sp.getString(MYPHIZCNT, "1");
    }

    public void setPhizcnt(String str) {
        editor.putString(PHIZCNT, str);
        editor.commit();
    }
    public void setMyPhizcnt(String str) {
        editor.putString(MYPHIZCNT, str);
        editor.commit();
    }
    public String getAcnt() {
        return sp.getString(ACNT, "0");
    }

    public void setAcnt(String str) {
        editor.putString(ACNT, str);
        editor.commit();
    }
    public String getPcnt() {
        return sp.getString(PCNT, "0");
    }

    public void setPcnt(String str) {
        editor.putString(PCNT, str);
        editor.commit();
    }
    public String getVcnt() {
        return sp.getString(VCNT, "0");
    }

    public void setVcnt(String str) {
        editor.putString(VCNT, str);
        editor.commit();
    }
    public String getCcnt() {
        return sp.getString(CCNT, "0");
    }

    public void setCcnt(String str) {
        editor.putString(CCNT, str);
        editor.commit();
    }



    public void setLocation(String str) {
        editor.putString(LOCATION, str);
        editor.commit();
    }

    public String getLocation() {
        return sp.getString(LOCATION, "北京");
    }

    public void setArea(String str) {
        editor.putString(AREA, str);
        editor.commit();
    }

    public String getArea() {
        return getLocation() + sp.getString(AREA, getLocation() + "");
    }

    public void setChoseLocation(String str) {
        editor.putString(CHOSELOCATION, str);
        editor.commit();
    }

    public String getChoseLocation() {
        return sp.getString(CHOSELOCATION, getLocation());
    }

    public void setDeviceId(String str) {
        editor.putString(DEVICEID, str);
        editor.commit();
    }

    public String getDeviceId() {
        return sp.getString(DEVICEID, "");
    }

    public void setIsFirstStart(String str) {
        editor.putString(ISFIRSTSTART + getVersionCode(), str);
        editor.commit();
    }



    public String getIsFirstStart() {
        return sp.getString(ISFIRSTSTART + getVersionCode(), "");
    }


    public void setIsFirstSend(String str) {
        editor.putString(ISFIRSTSEND + getVersionCode(), str);
        editor.commit();
    }

    public String getIsFirstSend() {
        return sp.getString(ISFIRSTSEND + getVersionCode(), "");
    }

    public void setLocationID(int str) {
        editor.putInt(LOCATIONID, str);
        editor.commit();
    }

    public String getLocationID() {
        return String.valueOf(sp.getInt(LOCATIONID, 1001));
    }

    public void setChoseLocationID(int str) {
        editor.putInt(CHOSELOCATIONID, str);
        editor.commit();
    }

    public String getChoseLocationID() {
        return String.valueOf(sp.getInt(CHOSELOCATIONID, Integer.valueOf(getLocationID())));
    }

    public void setMessageRemind(int str) {
        editor.putInt(MESSAGEREMIND, str);
        editor.commit();
    }

    public int getMessageRemind() {
        return sp.getInt(MESSAGEREMIND, OPEN);
    }

    public void setFastStartIcon(int str) {
        editor.putInt(FASTSTARTICON, str);
        editor.commit();
    }

    public int getFastStartIcon() {
        return sp.getInt(FASTSTARTICON, CLOSE);
    }

    public String getVersionName() {
        return sp.getString(VERSIONNAME, "1.0");
    }

    public void setVersionName(String str) {
        editor.putString(VERSIONNAME, str);
        editor.commit();
    }

    public String getLat() {
        return sp.getString(LAT, "");
    }

    public void setLat(String str) {
        editor.putString(LAT, str);
        editor.commit();
    }

    public String getLng() {
        return sp.getString(LNG, "");
    }

    public void setLng(String str) {
        editor.putString(LNG, str);
        editor.commit();
    }

    public String getChannel() {
        return sp.getString(CHANNEL, "QUANONE");
    }

    public void setChannel(String str) {
        editor.putString(CHANNEL, str);
        editor.commit();
    }

    public int getVersionCode() {
        return sp.getInt(VERSIONCODE, 1);
    }

    public void setVersionCode(int str) {
        editor.putInt(VERSIONCODE, str);
        editor.commit();
    }

    public void setIsLogin(boolean b) {
        editor.putBoolean(ISLOGIN, b);
        editor.commit();
    }

    public boolean getIsLogin() {
        return sp.getBoolean(ISLOGIN, false);
    }

    public boolean hasBind() {
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public void setBind(boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public int getMSGOPEN() {
        return sp.getInt(NEWMSG_OPEN, 1);
    }

    public void setMSGOPEN(int str) {
        editor.putInt(NEWMSG_OPEN, str);
        editor.commit();
    }

    public int getVIDEOOPEN() {
        return sp.getInt(VIDEO_OPEN, 1);
    }

    public void setVIDEOOPEN(int str) {
        editor.putInt(VIDEO_OPEN, str);
        editor.commit();
    }

    public int getZHENOPEN() {
        return sp.getInt(ZHENDONG_OPEN, 1);
    }

    public void setZHENOPEN(int str) {
        editor.putInt(ZHENDONG_OPEN, str);
        editor.commit();
    }
}

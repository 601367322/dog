package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class DogFood implements Serializable {
    String coin;
    String loginState;
    String shareState;
    String dynamicState;
    String datSate;
    String pairSate;

    String phoneBuildSate;
    String wxBuildSate;
    String qqBuildSate;
    String xlBuildSate;

    public void setXlBuildSate(String xlBuildSate) {
        this.xlBuildSate = xlBuildSate;
    }

    public DogFood(String coin, String loginState, String shareState, String dynamicState, String datSate, String pairSate, String phoneBuildSate, String wxBuildSate, String qqBuildSate, String xlBuildSate) {
        this.coin = coin;
        this.loginState = loginState;
        this.shareState = shareState;
        this.dynamicState = dynamicState;
        this.datSate = datSate;
        this.pairSate = pairSate;
        this.phoneBuildSate = phoneBuildSate;
        this.wxBuildSate = wxBuildSate;
        this.qqBuildSate = qqBuildSate;
        this.xlBuildSate = xlBuildSate;
    }

    public String getXlBuildSate() {
        return xlBuildSate;
    }

    public void setWxBuildSate(String wxBuildSate) {
        this.wxBuildSate = wxBuildSate;
    }

    public String getWxBuildSate() {
        return wxBuildSate;
    }

    public void setQqBuildSate(String qqBuildSate) {
        this.qqBuildSate = qqBuildSate;
    }

    public String getQqBuildSate() {
        return qqBuildSate;
    }

    public void setPhoneBuildSate(String phoneBuildSate) {
        this.phoneBuildSate = phoneBuildSate;
    }

    public String getPhoneBuildSate() {
        return phoneBuildSate;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getShareState() {
        return shareState;
    }

    public void setShareState(String shareState) {
        this.shareState = shareState;
    }

    public String getDynamicState() {
        return dynamicState;
    }

    public void setDynamicState(String dynamicState) {
        this.dynamicState = dynamicState;
    }

    public String getDatSate() {
        return datSate;
    }

    public void setDatSate(String datSate) {
        this.datSate = datSate;
    }

    public String getPairSate() {
        return pairSate;
    }

    public void setPairSate(String pairSate) {
        this.pairSate = pairSate;
    }

    public DogFood(String coin,String loginState,String shareState,String dynamicState,String datSate,String pairSate){
        this.coin=coin;
        this.loginState=loginState;
        this.shareState=shareState;
        this.dynamicState=dynamicState;
        this.dynamicState=dynamicState;
        this.datSate=datSate;
        this.pairSate=pairSate;
    }
    public DogFood() {
        super();
        // TODO Auto-generated constructor stub
    }
}

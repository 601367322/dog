package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class ContactsC implements Serializable{
    String milelistName;
    String nickname;
    String mobile;
    String title;
    String hideTitle;
    int relevant_state;
    String id;
    String avatar;

    public void setMilelistName(String milelistName) {
        this.milelistName = milelistName;
    }

    public String getMilelistName() {
        return milelistName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;

    }

    public int getRelevant_state() {
        return relevant_state;
    }

    public void setRelevant_state(int relevant_state) {
        this.relevant_state = relevant_state;
    }

    public String getHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(String hideTitle) {
        this.hideTitle = hideTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ContactsC(){

    }
    public ContactsC(String milelistName, String mobile){
        super();
        this.milelistName=milelistName;
        this.mobile=mobile;
    }
    public ContactsC(String milelistName, String nickname, String mobile, int relevant_state, String id,String avatar){
        super();
        this.milelistName=milelistName;
        this.nickname=nickname;
        this.mobile=mobile;
        this.avatar=avatar;
        this.relevant_state=relevant_state;
        this.id=id;
    }
}

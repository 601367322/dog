package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class DongTaiReplyMeBean implements Serializable {

    private String id;
    private String content;
    private String dyid;

    public String getDyid() {
        return dyid;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
    }

    private String ctime;
    private String userid;
    private String nickname;
    private String avatar;
    private String replyuid;
    private String replyuname;
    private String stype;
    private String scontent;

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public void setScontent(String scontent) {
        this.scontent = scontent;
    }

    public String getScontent() {
        return scontent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReplyuid() {
        return replyuid;
    }

    public void setReplyuid(String replyuid) {
        this.replyuid = replyuid;
    }

    public String getReplyuname() {
        return replyuname;
    }

    public void setReplyuname(String replyuname) {
        this.replyuname = replyuname;
    }

    public DongTaiReplyMeBean(String id, String content, String ctime,
                              String userid, String nickname, String avatar, String replyuid,
                              String replyuname,String stype,String scontent) {
        super();
        this.id = id;
        this.content = content;
        this.ctime = ctime;
        this.userid = userid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.replyuid = replyuid;
        this.replyuname = replyuname;
        this.stype=stype;
        this.scontent=scontent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DongTaiReplyMeBean() {
        super();
        // TODO Auto-generated constructor stub
    }

}

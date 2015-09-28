package com.quanliren.quan_two.bean;

public class MessageListBean {
    private String id;
    private int msgtype;
    private String content;
    private String createTime;
    private String crowdid;
    private String crowdname;
    private String applytype;
    private String applystatus;
    private String senduid;
    private String nickname;
    private String sex;
    private String age;
    private String avatar;
    private String birthday;
    private String cnt;
    private int loading; //0 完成 1 发送中 -1发送失败

    public int getLoading() {
        return loading;
    }

    public void setLoading(int loading) {
        this.loading = loading;
    }

    public MessageListBean(String id, String content) {
        this.senduid = id;
        this.content = content;
        this.loading = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCrowdid() {
        return crowdid;
    }

    public void setCrowdid(String crowdid) {
        this.crowdid = crowdid;
    }

    public String getCrowdname() {
        return crowdname;
    }

    public void setCrowdname(String crowdname) {
        this.crowdname = crowdname;
    }

    public String getApplytype() {
        return applytype;
    }

    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    public String getApplystatus() {
        return applystatus;
    }

    public void setApplystatus(String applystatus) {
        this.applystatus = applystatus;
    }

    public String getSenduid() {
        return senduid;
    }

    public void setSenduid(String senduid) {
        this.senduid = senduid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

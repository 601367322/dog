package com.quanliren.quan_two.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Shen on 2015/9/10.
 */
@DatabaseTable(tableName = "ZhenXinHuaTable")
public class ZhenXinHuaTable implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(unique = true)
    public String msgId;
    @DatabaseField
    public String sendUid;
    @DatabaseField
    public String receiverUid;
    @DatabaseField
    public String sendContent;
    @DatabaseField
    public String receiverContent;
    @DatabaseField
    public long time;
    @DatabaseField
    public String userId;
    @DatabaseField
    public int msgType;
    @DatabaseField
    public int state;

    public ZhenXinHuaTable() {
        super();
    }

    public ZhenXinHuaTable(String msgId, String sendUid, String receiverUid, String sendContent, String receiverContent, long time, String userId, int msgType) {
        this.msgId = msgId;
        this.sendUid = sendUid;
        this.receiverUid = receiverUid;
        this.sendContent = sendContent;
        this.receiverContent = receiverContent;
        this.time = time;
        this.userId = userId;
        this.msgType = msgType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSendUid() {
        return sendUid;
    }

    public void setSendUid(String sendUid) {
        this.sendUid = sendUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getReceiverContent() {
        return receiverContent;
    }

    public void setReceiverContent(String receiverContent) {
        this.receiverContent = receiverContent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

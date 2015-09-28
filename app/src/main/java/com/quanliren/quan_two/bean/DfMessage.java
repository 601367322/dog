package com.quanliren.quan_two.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonJsonBean;
import com.quanliren.quan_two.service.SocketManage;
import com.quanliren.quan_two.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "DfMessage")
public class DfMessage implements Serializable {
    public static final String TABLENAME = "DfMessage";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(index = true)
    private String msgid;
    @DatabaseField(index = true)
    private String userid;
    @DatabaseField(index = true)
    private String receiverUid;
    @DatabaseField(index = true)
    private String sendUid;
    @DatabaseField
    private String content;
    @DatabaseField(index = true)
    private int isRead = 0;// 是否已读
    @DatabaseField(index = true)
    private String ctime;// 信息发送时间
    private boolean showTime = false;// 是否显示信息
    @DatabaseField(index = true)
    private int msgtype = 0;// 0、文字 1、图片 2、语音
    @DatabaseField(index = true)
    private int download = 0;// 0 未下载 1已下载
    @DatabaseField
    private int timel = 0;// 语音长度
    @DatabaseField
    private String userlogo;
    @DatabaseField
    private String nickname;
    @DatabaseField
    private String z_msgid;
    @DatabaseField
    private int play_state = 0;//大冒险动画

    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    public static final int VOICE = 2;
    public static final int FACE = 5;
    public static final int ZHENXINHUA = 6;
    public static final int DAMAOXIAN = 7;
    public static final int VIDEO = 8;
    public static final int DAMAOXIAN_RESULT = 9;
    public static final int PIAOLIUPING = 10;//留言板漂流
    public static final int DAMAOXIAN_RESULT_ = 100;//大冒险 结果

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    private boolean playing;

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getZ_msgid() {
        return z_msgid;
    }

    public void setZ_msgid(String z_msgid) {
        this.z_msgid = z_msgid;
    }

    public String getUserlogo() {
        return userlogo;
    }

    public void setUserlogo(String userlogo) {
        this.userlogo = userlogo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public int getPlay_state() {
        return play_state;
    }

    public void setPlay_state(int play_state) {
        this.play_state = play_state;
    }

    public void setMsgtype(Integer msgtype) {
        this.msgtype = msgtype;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public DfMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

    //普通消息
    public static JSONObject getMessage(User user, String content, User friend,
                                        int msgtype, int timel) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("content", content);
            msg.put("ctime", Util.fmtDateTime.format(new Date()));
            msg.put("receiverUid", friend.getId());
            msg.put("userid", user.getId());
            msg.put("timel", timel);
            msg.put("userlogo", user.getAvatar());
            msg.put("nickname", user.getNickname());
            msg.put("sendUid", user.getId());
            msg.put("msgtype", msgtype);
            msg.put(SocketManage.MESSAGE_ID, String.valueOf(new Date().getTime()));
            return msg;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getMessage(User user, String content, User friend,
                                        int msgtype, String z_msgid) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("content", content);
            msg.put("ctime", Util.fmtDateTime.format(new Date()));
            msg.put("receiverUid", friend.getId());
            msg.put("userid", user.getId());
            msg.put("z_msgid", z_msgid);
            msg.put("userlogo", user.getAvatar());
            msg.put("nickname", user.getNickname());
            msg.put("sendUid", user.getId());
            msg.put("msgtype", msgtype);
            msg.put(SocketManage.MESSAGE_ID, String.valueOf(new Date().getTime()));
            return msg;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public DfMessage(int id, String msgid, String userid, String receiverUid,
                     String sendUid, String content, int isRead, String ctime,
                     boolean showTime, int msgtype, int download, int timel,
                     String userlogo, String nickname) {
        super();
        this.id = id;
        this.msgid = msgid;
        this.userid = userid;
        this.receiverUid = receiverUid;
        this.sendUid = sendUid;
        this.content = content;
        this.isRead = isRead;
        this.ctime = ctime;
        this.showTime = showTime;
        this.msgtype = msgtype;
        this.download = download;
        this.timel = timel;
        this.userlogo = userlogo;
        this.nickname = nickname;
    }

    public VideoBean getVideoBean() {
        return new Gson().fromJson(content, new TypeToken<VideoBean>() {
        }.getType());
    }

    public class VideoBean implements Serializable {
        public String path;
        public String thumb;
    }

    public PiaoLiuBean getPiaoLiuBean(){
        return new Gson().fromJson(content,new TypeToken<PiaoLiuBean>(){}.getType());
    }

    public class PiaoLiuBean implements Serializable{
        public String text;
        public String rtid;
    }

    public int getTimel() {
        return timel;
    }

    public void setTimel(int timel) {
        this.timel = timel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSendUid() {
        return sendUid;
    }

    public void setSendUid(String sendUid) {
        this.sendUid = sendUid;
    }

    public String getContent() {
        return content;
    }

    public EmoticonJsonBean getGifContent() {
        return new Gson().fromJson(content, new TypeToken<EmoticonJsonBean>() {
        }.getType());
    }

    public OtherHelperMessage getOtherHelperContent() {
        return new Gson().fromJson(content, new TypeToken<OtherHelperMessage>() {
        }.getType());
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class OtherHelperMessage implements Serializable {

        /**
         * 通知类型 0:评论/回复(留言板) 1:参与报名 (偷偷约) 2:选定和她约(偷偷约) 3:评论/回复(偷偷约) 4:放弃报名了 5：约会过期 6:发布成功和删除传递者 7牵手成功 8:真心话/私密真心话 9:动态推送消息
         * 10：大冒险
         */
        public static final int INFO_TYPE_COMMENT = 0;
        public static final int INFO_TYPE_APPLY = 1;
        public static final int INFO_TYPE_CHECKED = 2;
        public static final int INFO_TYPE_DATING_REPLY = 3;
        public static final int INFO_TYPE_QUIT_APPLY = 4;
        public static final int INFO_TYPE_PAST_DUE = 5;
        public static final int INFO_TYPE_THREAD_SUCCESS = 6;
        public static final int INFO_TYPE_PAIRING_SUCCESS = 7;
        public static final int INFO_TYPE_TRUE = 8;
        public static final int INFO_TYPE_DYNAMIC = 9;
        public static final int INFO_TYPE_ADVENTURE = 10;

        private int infoType;
        private String dyid;
        private String dcid;
        private String dtid;
        private int dtype;
        private String text;
        private int rtid;
        private String nickname;

        public OtherHelperMessage(int infoType, String dyid, String dcid, String dtid, int dtype, String text, int rtid, String nickname) {
            this.infoType = infoType;
            this.dyid = dyid;
            this.dcid = dcid;
            this.dtid = dtid;
            this.dtype = dtype;
            this.text = text;
            this.rtid = rtid;
            this.nickname = nickname;
        }

        public OtherHelperMessage() {
            super();
        }

        public int getInfoType() {
            return infoType;
        }

        public void setInfoType(int infoType) {
            this.infoType = infoType;
        }

        public String getDyid() {
            return dyid;
        }

        public void setDyid(String dyid) {
            this.dyid = dyid;
        }

        public String getDcid() {
            return dcid;
        }

        public void setDcid(String dcid) {
            this.dcid = dcid;
        }

        public String getDtid() {
            return dtid;
        }

        public void setDtid(String dtid) {
            this.dtid = dtid;
        }

        public int getDtype() {
            return dtype;
        }

        public void setDtype(int dtype) {
            this.dtype = dtype;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getRtid() {
            return rtid;
        }

        public void setRtid(int rtid) {
            this.rtid = rtid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}

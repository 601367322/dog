package com.quanliren.quan_two.service;

public class SocketManage {


    public static final Integer WORDPORT = 30003;// 文字端口
    public static final Integer IDELTIMEOUT = 60;// 连接超时时间1分钟

    public static final String ORDER = "cmd";// 指令

    public static final String ORDER_CONNECT = "order_connect";// 登陆连接指令
    public static final String ORDER_SENDMESSAGE = "order_sendmessage";// 发送消息指令
    public static final String ORDER_SAVEMESSAGE = "order_savemessage";// 存储消息
    public static final String ORDER_SENDED = "order_sended";// 已送达
    public static final String ORDER_READED = "order_readed";// 已读
    public static final String ORDER_OUTLINE = "order_outline";// 强迫下线
    public static final String ORDER_EXCHANGE = "order_exch_inform";//兑换消息
    public static final String SESSION = "session";// Session KEY
    public static final String USER = "user";// 用户对象KEY
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";// 用户名
    public static final String PASSWORD = "password";// 密码
    public static final String TOKEN = "token";// 用户令牌
    public static final String MESSAGE_ID = "msgid";// 消息唯一ID
    public static final String MESSAGE = "message";// 消息json
    public static final String DEVICE_ID = "deviceId";// 设备ID
    public static final String DEVICE_TYPE = "deviceType";// 设备类型 android or ios
    public static final String SEND_USER_ID = "sendUid";// 发送者ID
    public static final String RECEIVER_USER_ID = "receiverUid";// 接收者ID
    public static final String ORDER_SENDERROR="order_senderror";//发送失败
    public static final int ERROR_TYPE_MORE=1;//超过陌生人聊天限制数
    public static final int ERROR_TYPE_BLACK=2;//在对方黑名单中
    public static final String TYPE="type";//失败类型

    public static final int D_destroy = 0;//损坏
    public static final int D_downloading = 1;//正在下载
    public static final int D_downloaded = 2;//已下载
    public static final int D_nodownload = 3;//未下载
    public static final int D_fail = 4;//损坏

}

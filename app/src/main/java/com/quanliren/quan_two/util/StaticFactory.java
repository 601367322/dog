package com.quanliren.quan_two.util;

import android.os.Environment;

public class StaticFactory {
    public static final String DBName = "quan_one.db";                                                //数据库名称
    public static final Integer DBVersion = 1;                                                                //数据库版本
    public static final String Url = "";                                                                            //服务器地址
    public static final String SocketIP = "";                                                                    //Socket连接地址
    public static final String SDCardPath = Environment.getExternalStorageDirectory().getPath();//SD卡地址
    public static final String APKCardPath = Environment.getExternalStorageDirectory().getPath() + "/quan/";//SD卡地址
    public static final String APKCardPathChat = APKCardPath + "chat/";//
    public static final String APKCardPathChatImg = APKCardPath + "chat/img/";
    public static final String APKCardPathChatVoice = APKCardPath + "chat/voice/";//
    public static final String APKCardPathChatVideo = APKCardPath + "chat/video/";//
    public static final String APKCardPathEmoticon = APKCardPath + "emoticon/";//
    public static final String APKCardPathDownload = APKCardPath + "download/";//
    public static final String APKCardPathCrash = APKCardPath + "crash/";//

    public static final String _160x160 = "_160x160.jpg";
    public static final String _320x320 = "_320x320.jpg";
    public static final String _240x1000 = "_240x1000.jpg";
    public static final String _600x600 = "_600x600.jpg";
    public static final String _640x640 = "_640x640.jpg";
    public static final String _300x180 = "_300x180.jpg";
    public static final String _960x720 = "_720x960.jpg";

    public static final String AD_CACHE = "ad_cache";
    public static final String PB_CACHE = "pb_cache";
    public static final String CACHE_KEY = "cache_key";
}

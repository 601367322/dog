package com.quanliren.quan_two.util;

public class BroadcastUtil {
    public static final String packagestr = "com.quanliren.quan_two.";
    public static final String UPDATE_USER_INFO = packagestr + "update_user_info";

    public static final String ACTION_KEEPALIVE = packagestr + "service.keep_alive";
    public static final String ACTION_RECONNECT = packagestr + "service.reconnect";
    public static final String ACTION_CONNECT = packagestr + "service.connect";

    public static final String ACTION_CHECKCONNECT = packagestr + "service.checkconnect";
    public static final int CHECKCONNECT = 15 * 1000;
    public static final String ACTION_OUTLINE = packagestr + "service.outline";

    public static final String EXIT = packagestr + "exit";

    public static final String DOWNLOADEMOTICON = packagestr + "service.downloademoticon";
}

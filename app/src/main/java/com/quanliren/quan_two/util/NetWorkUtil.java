package com.quanliren.quan_two.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetWorkUtil {
    private ConnectivityManager connectivityManager;

    public NetWorkUtil() {
        super();
    }

    public NetWorkUtil(Context context) {
        super();
        this.connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public NetWorkUtil(ConnectivityManager connectivityManager,
                       TelephonyManager telephonyManager) {
        super();
        this.connectivityManager = connectivityManager;
    }

    public NetWorkUtil(Context context,
                       ConnectivityManager connectivityManager,
                       TelephonyManager telephonyManager) {
        super();
        this.connectivityManager = connectivityManager;
    }

    /**
     * 查看网络是否可用
     */
    // check network state
    public boolean hasInternet() {
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 查看网络能否连接服务器
     */
    // check network state
    public boolean canInternet() {
        String url = com.quanliren.quan_two.util.URL.URL;
        try {
            URL requestURL = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) requestURL
                    .openConnection();
            urlConnection.setConnectTimeout(8000);
            urlConnection.connect();
            urlConnection.disconnect();
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    public static final int NO_NETWORK = -1;
    public static final int WIFI = 1;
    public static final int WAP = 2;
    public static final int GPRS = 3;

    /**
     * 判断联网状态及联网方式
     *
     * @param context 当前应用上下文
     * @return NO_NETWORK 无可用网路; WIFI 通过wifi方式联网; GRPS 通过GPRS方式联网
     */
    public static int theWayOfNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conn.getActiveNetworkInfo() == null || (!conn.getActiveNetworkInfo().isAvailable())) {
            return NO_NETWORK;
        }
        if (conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return WIFI;
        }
        if (conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) {
            String proxyHost = android.net.Proxy.getDefaultHost();
            if (proxyHost != null && !proxyHost.equals("")) {
                // WAP方式
                return WAP;
            }
            return GPRS;
        }
        return NO_NETWORK;
    }
}

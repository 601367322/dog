package com.quanliren.quan_two.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.service.QuanPushService;
import com.quanliren.quan_two.service.SocketManage;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.NetWorkUtil;
import com.quanliren.quan_two.util.Util;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    public void onReceive(Context context, Intent intent) {
//		Log.d(TAG, intent.getAction());
        NetWorkUtil netWorkUtil = new NetWorkUtil(context);
        AppClass ac = (AppClass) context.getApplicationContext();
        DBHelper helper = OpenHelperManager.getHelper(context, DBHelper.class);
        if (netWorkUtil.hasInternet() && BroadcastUtil.ACTION_CHECKCONNECT.equals(intent.getAction())) {
            try {
                if (helper.getUser() != null) {
                    if (!Util.isServiceRunning(context,
                            QuanPushService.class.getName())) {
                        ac.startServices();
                    } else if (ac.remoteService == null) {
                        ac.bindServices();
                    } else if (!ac.isConnectSocket()) {
                        Intent i = new Intent(context,QuanPushService.class);
                        i.setAction(BroadcastUtil.ACTION_RECONNECT);
                        context.startService(i);
                    }
                    return;
                }else{
                    ac.stopServices();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Util.setAlarmTime(context, System.currentTimeMillis()
                                + BroadcastUtil.CHECKCONNECT,
                        BroadcastUtil.ACTION_CHECKCONNECT,
                        BroadcastUtil.CHECKCONNECT);
            }
        } else if (BroadcastUtil.ACTION_OUTLINE.equals(intent.getAction())) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(0);
            ac.dispose();
        } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (!netWorkUtil.hasInternet()) {
                ac.hasNet = false;
//				Log.d(TAG, String.valueOf(ac.hasNet));
            } else {
                if (!ac.hasNet) {
                    ac.hasNet = true;
                    Intent i = new Intent(BroadcastUtil.ACTION_CHECKCONNECT);
                    context.sendBroadcast(i);
//					Log.d(TAG, String.valueOf(ac.hasNet));
                }
            }
        }
    }

}
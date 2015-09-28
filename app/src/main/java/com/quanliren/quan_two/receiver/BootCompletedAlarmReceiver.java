package com.quanliren.quan_two.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.Util;

public class BootCompletedAlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "BootCompletedAlarmReceiver";

    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, intent.getAction());
        DBHelper helper = OpenHelperManager.getHelper(context, DBHelper.class);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (helper.getUser() != null) {
                Util.setAlarmTime(context, System.currentTimeMillis(), BroadcastUtil.ACTION_CHECKCONNECT, 60 * 1000);
            }
        }
    }
}
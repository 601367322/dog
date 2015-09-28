package com.quanliren.quan_two.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import com.quanliren.quan_two.activity.reg.RegGetCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 判断是系统短信；
        if (intent.getAction()
                .equals("android.provider.Telephony.SMS_RECEIVED")) {
            // 不再往下传播；
//			this.abortBroadcast();
            StringBuffer sb = new StringBuffer();

            String sender = null;
            String content = null;

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // 通过pdus获得接收到的所有短信消息，获取短信内容；
                Object[] pdus = (Object[]) bundle.get("pdus");
                // 构建短信对象数组；
                SmsMessage[] mges = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    // 获取单条短信内容，以pdu格式存,并生成短信对象；
                    mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage mge : mges) {
                    sb.append("短信来自：" + mge.getDisplayOriginatingAddress()
                            + "\n");
                    sb.append("短信内容：" + mge.getMessageBody());

                    sender = mge.getDisplayOriginatingAddress();// 获取短信的发送者
                    content = mge.getMessageBody();// 获取短信的内容
                    if (content.indexOf("偷") > -1 && content.indexOf("嗒") > -1) {
                        Pattern p = Pattern.compile("\\d{6}");
                        Matcher m = p.matcher(content);
                        String code = "";
                        while (m.find()) {
                            code = m.group();
                            break;
                        }

                        if (!code.equals("")) {
                            Intent i = new Intent(RegGetCode.GETCODE);
                            i.putExtra("code", code);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                        }
                    }
                }
            }
        }
    }
}
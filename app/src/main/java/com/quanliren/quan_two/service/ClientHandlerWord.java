package com.quanliren.quan_two.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.quanliren.quan_two.activity.Noti;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.activity.user.ChatActivity_;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.ChatListBean;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.db.MessageDao;
import com.quanliren.quan_two.db.ZhenXinHuaDao;
import com.quanliren.quan_two.fragment.message.MyLeaveMessageFragment;
import com.quanliren.quan_two.service.QuanPushService.ConnectionThread;
import com.quanliren.quan_two.util.BitmapCache;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClientHandlerWord {
    public static final String TAG = "ClientHandlerWord";

    AppClass ac;
    Context c;
    User user;
    int num = 0;

    DBHelper helper = null;
    MessageDao messageDao;
    ZhenXinHuaDao zhenXinHuaDao;

    public ClientHandlerWord(Context context) {
        ac = (AppClass) context.getApplicationContext();
        this.c = context;

        helper = OpenHelperManager.getHelper(context, DBHelper.class);
        messageDao = MessageDao.getInstance(context);
        zhenXinHuaDao = ZhenXinHuaDao.getInstance(context);
    }

    public void sessionConnected(ConnectionThread session) {
        try {
            JSONObject jo = new JSONObject();
            jo.put(SocketManage.ORDER, SocketManage.ORDER_CONNECT);
            jo.put(SocketManage.TOKEN, helper.getUser().getToken());
            jo.put(SocketManage.DEVICE_TYPE, "0");
            jo.put(SocketManage.DEVICE_ID, ac.cs.getDeviceId());
            session.write(jo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageReceived(ConnectionThread session, Object message)
            throws Exception {
        LogUtil.d(message.toString());
        user = helper.getUserInfo();
        JSONObject jo = new JSONObject(message.toString());
        String order = jo.getString(SocketManage.ORDER);
        if (order.equals(SocketManage.ORDER_SENDMESSAGE)) {
            getMessage(session, jo);
        } else if (order.equals(SocketManage.ORDER_SENDED)) {
            sended(jo);
        } else if (order.equals(SocketManage.ORDER_OUTLINE)) {
            Intent i = new Intent(BroadcastUtil.ACTION_OUTLINE);
            c.sendBroadcast(i);
            NotificationManagerCompat notificationManager = NotificationManagerCompat
                    .from(c);
            notificationManager.cancelAll();
        } else if (order.equals(SocketManage.ORDER_SENDERROR)) {
            sendederror(jo);
        }
    }


    public void notify(String nick, String title, String content, Intent intent) {
        int notificationId = (nick + title).hashCode();

        PendingIntent viewPendingIntent =

                PendingIntent.getActivity(c, num++, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                c)
                .setTicker(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_taskbar_icon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(
                        BitmapCache.getInstance().getBitmap(R.mipmap.ic_launcher, c))
                .setContentTitle(title).setOnlyAlertOnce(true)
                .setContentText(content).setContentIntent(viewPendingIntent);

        if (ac.cs.getZHENOPEN() == 1 && ac.cs.getVIDEOOPEN() == 1) {
            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        } else if (ac.cs.getZHENOPEN() == 1) {
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        } else if (ac.cs.getVIDEOOPEN() == 1) {
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(c);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public void sended(JSONObject jo) {
        try {
            String msgid = jo.getString(SocketManage.MESSAGE_ID);
            List<DfMessage> list = helper.getDao(DfMessage.class).queryForEq(
                    "msgid", msgid);
            if (list.size() > 0) {
                DfMessage m = list.get(0);
                m.setDownload(SocketManage.D_downloaded);
                helper.getDao(DfMessage.class).update(m);
                Intent i = new Intent(ChatActivity.CHANGESEND);
                i.putExtra("bean", m);
                c.sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendederror(JSONObject jo) {
        try {
            String msgid = jo.getString(SocketManage.MESSAGE_ID);
            List<DfMessage> list = helper.getDao(DfMessage.class).queryForEq(
                    "msgid", msgid);
            if (list.size() > 0) {
                DfMessage m = list.get(0);
                m.setDownload(SocketManage.D_destroy);
                helper.getDao(DfMessage.class).update(m);
                Intent i = new Intent(ChatActivity.CHANGESEND);
                i.putExtra("bean", m);
                i.putExtra("type", jo.getInt(SocketManage.TYPE));
                c.sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void getMessage(ConnectionThread session, JSONObject jo) {
        final DfMessage defMessage = new Gson().fromJson(
                jo.opt(SocketManage.MESSAGE).toString(), DfMessage.class);

        try {
            if (jo.opt(SocketManage.MESSAGE_ID) != null) {
                JSONObject jos = new JSONObject();
                jos.put(SocketManage.ORDER, SocketManage.ORDER_SENDED);
                jos.put(SocketManage.MESSAGE_ID,
                        jo.opt(SocketManage.MESSAGE_ID));
                session.write(jos.toString());
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        //如果用户屏蔽了信息
        if (ac.cs.getMSGOPEN() != 1) {
            return;
        }

        //如果为文件消息，将消息的下载状态改为为下载
        switch (defMessage.getMsgtype()) {
            case DfMessage.VIDEO:
                DfMessage.VideoBean vbean = defMessage.getVideoBean();
                defMessage.setContent(vbean.path + "," + vbean.thumb);
            case DfMessage.IMAGE:
            case DfMessage.VOICE:
            case DfMessage.DAMAOXIAN_RESULT:
                defMessage.setDownload(SocketManage.D_nodownload);
                break;
            default:
                defMessage.setDownload(SocketManage.D_downloaded);
                break;
        }

        //将消息的主人ID
        defMessage.setUserid(user.getId());

        ChatListBean cb = new ChatListBean(defMessage);
        try {
            //大冒险
            if (defMessage.getMsgtype() == DfMessage.DAMAOXIAN_RESULT) {
                defMessage.setPlay_state(1);
            }
            //存储消息
            messageDao.saveMessage(defMessage, cb);

            //真心话
            if (!TextUtils.isEmpty(defMessage.getZ_msgid())) {
                ZhenXinHuaTable zt = zhenXinHuaDao.getZhenByMsgId(defMessage.getUserid(), defMessage.getZ_msgid());
                zhenXinHuaDao.setContent(zt, defMessage.getSendUid(), defMessage.getContent());
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Intent broad = new Intent(ChatActivity.ADDMSG);
        broad.putExtra("bean", defMessage);
        c.sendBroadcast(broad);

        broad = new Intent(MyLeaveMessageFragment.ADDMSG);
        broad.putExtra("bean", cb);
        c.sendBroadcast(broad);

        String content = null;
        switch (defMessage.getMsgtype()) {
            case DfMessage.TEXT:
                if ("99".equals(defMessage.getSendUid())) {
                    DfMessage.OtherHelperMessage msg = defMessage.getOtherHelperContent();
                    switch (msg.getInfoType()) {
                        case DfMessage.OtherHelperMessage.INFO_TYPE_COMMENT://评论留言
                            content = (msg.getNickname() + c.getString(R.string.info_type_0));
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_APPLY://参与报名 (偷偷约)
                            content = (msg.getNickname() + c.getString(R.string.info_type_1));
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_CHECKED://选定和她约(偷偷约)
                            content = (msg.getNickname() + c.getString(R.string.info_type_2));
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_DATING_REPLY:
                            content = (msg.getNickname() + c.getString(R.string.info_type_3));
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_QUIT_APPLY:
                            content = (msg.getNickname() + c.getString(R.string.info_type_4));
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_PAST_DUE:
                            content = c.getString(R.string.info_type_5);
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_THREAD_SUCCESS:
                            content = (msg.getNickname() + c.getString(R.string.info_type_6));
                            break;
                        case DfMessage.OtherHelperMessage.INFO_TYPE_PAIRING_SUCCESS:
                            content = (msg.getNickname() + c.getString(R.string.info_type_7));
                            break;
                    }
                } else {
                    content = defMessage.getContent();
                }
                break;
            case DfMessage.IMAGE:
            case DfMessage.VOICE:
            case DfMessage.ZHENXINHUA:
            case DfMessage.DAMAOXIAN:
            case DfMessage.DAMAOXIAN_RESULT:
            case DfMessage.VIDEO:
                content = cb.getContent();
                break;
            case DfMessage.FACE:
                content = defMessage.getGifContent().flagName;
                break;
            case DfMessage.PIAOLIUPING:
                content = defMessage.getPiaoLiuBean().text;
                break;
        }

        Intent intent = new Intent(c, Noti.class);
        intent.putExtra("activity", ChatActivity_.class);
        User friend = new User();
        friend.setId(defMessage.getSendUid());
        friend.setNickname(defMessage.getNickname());
        friend.setAvatar(defMessage.getUserlogo());
        intent.putExtra("friend", friend);

        notify(defMessage.getSendUid(), defMessage.getNickname(), content,
                intent);
    }

}

package com.quanliren.quan_two.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.Noti;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.seting.EmoticonListActivity;
import com.quanliren.quan_two.activity.seting.EmoticonListActivity_;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonImageBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.BitmapCache;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.FileUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.ZipUtil;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.OrmLiteDao;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.http.Header;

@EService
public class DownLoadEmoticonService extends Service {

    @App
    AppClass ac;

    AsyncHttpClient httpClient;
    NotificationManagerCompat notificationManager = null;

    List<EmoticonZip> list = new ArrayList<EmoticonZip>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(new PersistentCookieStore(this));
        httpClient.setThreadPool(Executors.newFixedThreadPool(2));
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equals(BroadcastUtil.DOWNLOADEMOTICON)) {
                EmoticonZip bean = (EmoticonZip) intent
                        .getSerializableExtra("bean");
                list.add(bean);
                RequestParams rp = getAjaxParams();
                rp.put("id", bean.getId() + "");
                httpClient.post(URL.DOWNLOAD_EMOTICON_FIRST,
                        rp,
                        new downloadRunnable(bean));
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public RequestParams getAjaxParams() {
        LoginUser user = OpenHelperManager.getHelper(this, DBHelper.class)
                .getUser();
        RequestParams ap = new RequestParams();
        ap.put("versionName", ac.cs.getVersionName());
        ap.put("versionCode", String.valueOf(ac.cs.getVersionCode()));
        ap.put("channel", ac.cs.getChannel());
        ap.put("dtype", "0");
        ap.put("deviceid", ac.cs.getDeviceId());
        if (user != null) {
            ap.put("token", user.getToken());
        }
        return ap;
    }

    @OrmLiteDao(helper = DBHelper.class, model = EmoticonZip.class)
    Dao<EmoticonZip, Integer> emoticonDao;

    class downloadRunnable extends JsonHttpResponseHandler {
        EmoticonZip bean;

        public downloadRunnable(EmoticonZip bean) {
            this.bean = bean;
        }

        @Override
        public void onStart() {
            Intent i = new Intent(
                    EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS);
            i.putExtra("bean", bean);
            i.putExtra("state", 0);
            sendBroadcast(i);
        }
        @Override
        public void onSuccess(JSONObject response) {
            try {
                int status = response.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        final EmoticonZip ebean = new Gson().fromJson(
                                response.getString(URL.RESPONSE),
                                new TypeToken<EmoticonZip>() {
                                }.getType());
                        if (ebean != null) {
                            File dFile = new File(StaticFactory.APKCardPathDownload);
                            if (!dFile.exists()) {
                                dFile.mkdirs();
                            }
                            ac.finalHttp
                                    .get(ebean.getDownUrl(),
                                            new FileAsyncHttpResponseHandler(
                                                    new File(
                                                            StaticFactory.APKCardPathDownload
                                                                    + ebean.getDownUrl()
                                                                    .substring(
                                                                            ebean.getDownUrl()
                                                                                    .lastIndexOf(
                                                                                            "/")))) {
                                                @Override
                                                public void onSuccess(
                                                        int statusCode,
                                                        Header[] headers, File file) {
                                                    try {
                                                        manageFile(ebean, file);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(
                                                        int statusCode,
                                                        Header[] headers,
                                                        Throwable throwable,
                                                        File file) {
                                                    Intent i = new Intent(
                                                            EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS);
                                                    i.putExtra("bean", bean);
                                                    i.putExtra("state", -1);
                                                    sendBroadcast(i);
                                                    list.remove(bean);
                                                    if (list.size() == 0) {
                                                        stopSelf();
                                                    }
                                                }

                                                long time = 0;


                                                public void onProgress(
                                                        long bytesWritten,
                                                        long totalSize) {
                                                    if (bytesWritten > 0
                                                            && bytesWritten < totalSize) {
                                                        if (System
                                                                .currentTimeMillis()
                                                                - time > 1000) {
                                                            notifys(bean.getName(),

                                                                    bytesWritten, totalSize);
                                                            time = System
                                                                    .currentTimeMillis();
                                                            Intent i = new Intent(
                                                                    EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS);
                                                            i.putExtra("bean", bean);
                                                            i.putExtra("state", 1);
                                                            i.putExtra("progress",
                                                                    (int)bytesWritten);
                                                            i.putExtra("total",
                                                                    (int)totalSize);
                                                            sendBroadcast(i);
                                                        }
                                                    } else if (bytesWritten == totalSize) {
                                                        notifys(bean.getName(), 0,
                                                                0);
                                                    }
                                                }

                                                ;
                                            });
                        }
                        break;
                    default:
                        Intent i = new Intent(
                                EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS);
                        i.putExtra("bean", bean);
                        i.putExtra("state", -1);
                        sendBroadcast(i);
                        list.remove(bean);
                        if (list.size() == 0) {
                            stopSelf();
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure() {
            Intent i = new Intent(
                    EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS);
            i.putExtra("bean", bean);
            i.putExtra("state", -1);
            sendBroadcast(i);
            list.remove(bean);
            if (list.size() == 0) {
                stopSelf();
            }
        }
    }

    Map<Integer, NotificationCompat.Builder> notiMap = new HashMap<Integer, NotificationCompat.Builder>();

    public void notifys(String title, long progress, long max) {

        Intent intent = new Intent(this, Noti.class);
        intent.putExtra("activity", EmoticonListActivity_.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 1,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = null;
        if (notiMap.get(title.hashCode()) == null) {
            notificationBuilder = new NotificationCompat.Builder(this);
            notiMap.put(title.hashCode(), notificationBuilder);
        } else {
            notificationBuilder = notiMap.get(title.hashCode());
            if (progress == max) {
                notificationBuilder
                        .setAutoCancel(true)
                        .setProgress(0, 0, false)
                        .setLargeIcon(
                                BitmapCache.getInstance().getBitmap(
                                        R.mipmap.ic_launcher, this))
                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        .setContentTitle("表情下载")
                        .setContentText(title + "已下载完成").setContentInfo("点击查看")
                        .setTicker(title + "已下载完成")
                        .setContentIntent(viewPendingIntent);
            } else {
                notificationBuilder
                        .setAutoCancel(true)
                        .setProgress((int)max, (int)progress, false)
                        .setLargeIcon(
                                BitmapCache.getInstance().getBitmap(
                                        R.mipmap.ic_launcher, this))
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setContentTitle("表情下载").setContentText("正在下载" + title)
                        .setTicker("正在下载" + title)
                        .setContentIntent(viewPendingIntent);
            }
        }
        notificationManager.notify(title.hashCode(),
                notificationBuilder.build());
    }

    @Background
    public void manageFile(EmoticonZip bean, File dfile) {
        try {
            File files = null;

            files = new File(ZipUtil.unzip(dfile.getPath(),
                    StaticFactory.APKCardPathEmoticon));

            File icon = FileUtil.getFile(files, bean.getIconfile());
            if (icon != null) {
                File icon_rename = null;
                icon.renameTo(icon_rename = new File(icon.getParent() + "/"
                        + (files.getName() + icon.getName()).hashCode()));
                bean.setIconfile(icon_rename.getPath());
            }

            for (EmoticonImageBean ebi : bean.getImglist()) {
                File temp = FileUtil.getFile(files, ebi.getGiffile());
                File file2_rename = null;
                if (temp != null) {
                    temp.renameTo(file2_rename = new File(temp.getParentFile().getParent() + "/" + (files.getName() + temp.getName()).hashCode()));
                    ebi.setGiffile(file2_rename.getPath());
                }
                File temp1 = FileUtil.getFile(files, ebi.getPngfile());
                if (temp1 != null) {
                    temp1.renameTo(file2_rename = new File(temp1.getParentFile().getParent() + "/" + (files.getName() + temp1.getName()).hashCode()));
                    ebi.setPngfile(file2_rename.getPath());
                }
            }
            bean.setUserId(ac
                    .getLoginUserId());
            emoticonDao.createOrUpdate(bean);

            Intent i = new Intent(
                    EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS);
            i.putExtra("bean", bean);
            i.putExtra("state", 2);
            sendBroadcast(i);
            list.remove(bean);
            if (list.size() == 0) {
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.quanliren.quan_two.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.table.TableUtils;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.quanliren.quan_two.activity.BuildConfig;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.service.IQuanPushService;
import com.quanliren.quan_two.service.QuanPushService;
import com.quanliren.quan_two.share.CommonShared;
import com.quanliren.quan_two.util.CrashHandler;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyHttpClient;

import org.androidannotations.annotations.EApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.pinguo.edit.sdk.PGEditImageLoader;
import us.pinguo.edit.sdk.base.PGEditSDK;

@EApplication
public class AppClass extends Application {

    public CommonShared cs = null;
    public MyHttpClient finalHttp;
    public SyncHttpClient asyncHttpClient;
    public boolean hasNet = true;
    public static List<String> mEmoticons = new ArrayList<String>();
    public static List<String> mEmoticons2 = new ArrayList<String>();
    public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
    public static Map<String, Integer> mEmoticons2Id = new HashMap<String, Integer>();

    public AppClass() {
    }

    public static final DisplayImageOptions options_defalut = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.image_group_qzl)
            .showImageForEmptyUri(R.drawable.image_group_qzl)
                    // .displayer(new FadeInBitmapDisplayer(200))
            .showImageOnFail(R.drawable.image_group_load_f).cacheInMemory(true)
            .cacheOnDisk(true).build();

    public static final DisplayImageOptions options_chat = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_chat_def_pic)
            .showImageForEmptyUri(R.drawable.ic_chat_def_emote_failure)
            .showImageOnFail(R.drawable.ic_chat_def_emote_failure)
            .cacheInMemory(true).cacheOnDisk(true).build();

    public static final DisplayImageOptions options_userlogo = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.defalut_logo)
            .showImageForEmptyUri(R.drawable.defalut_logo)
                    // .displayer(new FadeInBitmapDisplayer(200))
            .showImageOnFail(R.drawable.defalut_logo).cacheInMemory(true)
            .cacheOnDisk(true).build();
    public static final DisplayImageOptions options_dongtai = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.dongtai_content_default)
            .showImageForEmptyUri(R.drawable.dongtai_content_default)
                    // .displayer(new FadeInBitmapDisplayer(200))
            .showImageOnFail(R.drawable.dongtai_content_default).cacheInMemory(true)
            .cacheOnDisk(true).build();
    public static final DisplayImageOptions goods_default = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.goods_down_fail)
            .showImageForEmptyUri(R.drawable.goods_down_fail)
                    // .displayer(new FadeInBitmapDisplayer(200))
            .showImageOnFail(R.drawable.goods_down_fail).cacheInMemory(true)
            .cacheOnDisk(true).build();

    public static final DisplayImageOptions options_no_default = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true).build();

    private static Context context = null;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        if (!BuildConfig.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
        cs = new CommonShared(getApplicationContext());
        cs.setVersionName(Util.getAppVersionName(this));
        cs.setVersionCode(Util.getAppVersionCode(this));
        cs.setChannel(Util.getChannel(this));

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            cs.setDeviceId(tm.getDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(
                options_defalut).build();
        L.writeLogs(false);
        L.writeDebugLogs(false);
        ImageLoader.getInstance().init(config);

        initEmoticon();
        initEmoticon2();
        finalHttp = new MyHttpClient();
        finalHttp.setCookieStore(new PersistentCookieStore(this));

        asyncHttpClient = new SyncHttpClient();
        asyncHttpClient.setCookieStore(new PersistentCookieStore(this));

        //360
        PGEditImageLoader.initImageLoader(this);
        PGEditSDK.instance().initSDK(this);
    }

    public IQuanPushService remoteService = null;
    public CounterServiceConnection conn = null;

    public class CounterServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IQuanPushService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
        }
    }

    public boolean isConnectSocket() {
        try {
            if (remoteService != null && remoteService.getServerSocket()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendMessage(String str) {
        try {
            if (remoteService != null) {
                remoteService.sendMessage(str);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startServices() {
        stopServices();
        Intent i = new Intent(this, QuanPushService.class);
        i.setAction(
                com.quanliren.quan_two.util.BroadcastUtil.ACTION_CONNECT);
        startService(i);
        bindServices();
    }

    public void bindServices() {
        Intent i = new Intent(this, QuanPushService.class);
        if (conn == null)
            conn = new CounterServiceConnection();
        bindService(i, conn, Context.BIND_AUTO_CREATE);
    }

    public void stopServices() {
        try {
            Intent i = new Intent(this, QuanPushService.class);
            if (conn != null) {
                unbindService(conn);
                conn = null;
            }
            stopService(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        DBHelper helper = null;
        try {
            helper = OpenHelperManager.getHelper(getApplicationContext(),
                    DBHelper.class);
            TableUtils
                    .clearTable(helper.getConnectionSource(), LoginUser.class);
            if (remoteService != null) {
                remoteService.closeAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopServices();
    }

    public String getLoginUserId() {
        DBHelper helper = null;
        try {
            helper = OpenHelperManager.getHelper(getApplicationContext(),
                    DBHelper.class);
            LoginUser user = helper.getUser();
            if (user != null) {
                return user.getId();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    void initEmoticon() {
        PListXMLParser parser = new PListXMLParser();
        PListXMLHandler handler = new PListXMLHandler();
        parser.setHandler(handler);

        try {
            parser.parse(getAssets().open("emoticon.plist"));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
        Dict root = (Dict) actualPList.getRootElement();

        Map<String, PListObject> map = root.getConfigMap();

        for (String key : map.keySet()) {
            PListObject o = map.get(key);
            mEmoticons
                    .add(((com.longevitysoft.android.xml.plist.domain.String) o)
                            .getValue());
            mEmoticonsId.put(
                    ((com.longevitysoft.android.xml.plist.domain.String) o)
                            .getValue(),
                    getResources().getIdentifier(key, "drawable",
                            getPackageName()));
        }
    }

    void initEmoticon2() {
        PListXMLParser parser = new PListXMLParser();
        PListXMLHandler handler = new PListXMLHandler();
        parser.setHandler(handler);

        try {
            parser.parse(getAssets().open("emoticon2.plist"));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
        Dict root = (Dict) actualPList.getRootElement();

        Map<String, PListObject> map = root.getConfigMap();

        for (String key : map.keySet()) {
            PListObject o = map.get(key);
            mEmoticons2
                    .add(((com.longevitysoft.android.xml.plist.domain.String) o)
                            .getValue());
            mEmoticons2Id.put(
                    ((com.longevitysoft.android.xml.plist.domain.String) o)
                            .getValue(),
                    getResources().getIdentifier(key, "drawable",
                            getPackageName()));
        }
    }

}

package com.quanliren.quan_two.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.user.LoginActivity_;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.share.CommonShared;
import com.quanliren.quan_two.util.BitmapCache;
import com.quanliren.quan_two.util.StaticFactory;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.http.Header;

@EActivity(R.layout.whatsnew_viewpager)
public class Whatsnew extends BaseActivity {

    ArrayList<View> views = new ArrayList<View>();

    @ViewById
    ViewPager whatsnew_viewpager;
    @ViewById
    ImageView page0;
    @ViewById
    ImageView page1;
    @ViewById
    ImageView page2;
    @ViewById
    ImageView page3;
    @ViewById
    ImageView enter_btn;
    @ViewById
    TextView text;
    @ViewById
    TextView con;
    @ViewById
    View pages;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSwipeBackEnable(false);

    }

    public void download(final JSONArray jo, final int index){
        try {
            ac.finalHttp.get(jo.getString(index), new FileAsyncHttpResponseHandler(new File(StaticFactory.SDCardPath+"/daomu/"+index+".jpg")) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    download(jo,index+1);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init() {
        super.init();

        createShorcut(R.mipmap.ic_launcher);

        doSomeThing();
    }

    @UiThread(delay = 1500)
    public void doSomeThing() {
        String isFirstStart = ac.cs.getIsFirstStart();
        if ("".equals(isFirstStart)) {
            whatsnew_viewpager
                    .setOnPageChangeListener(new MyOnPageChangeListener());
            try{
                views = new ArrayList<View>();
                LayoutInflater mLi = LayoutInflater.from(Whatsnew.this);
                View view1 = mLi.inflate(R.layout.whats1, null);
                view1.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.welcome_1, Whatsnew.this)));
                views.add(view1);
                View view2 = mLi.inflate(R.layout.whats1, null);
                view2.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.welcome_2, Whatsnew.this)));
                views.add(view2);
                View view3 = mLi.inflate(R.layout.whats1, null);
                view3.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.welcome_3, Whatsnew.this)));
                view3.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        startbutton();
                        return false;
                    }
                });
                views.add(view3);
            }catch (Exception e) {
                e.printStackTrace();
            }
            PagerAdapter mPagerAdapter = new PagerAdapter() {

                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                public int getCount() {
                    return views.size();
                }

                public void destroyItem(View container, int position, Object object) {
                    ((ViewPager) container).removeView(views.get(position));
                }

                public Object instantiateItem(View container, int position) {
                    ((ViewPager) container).addView(views.get(position));
                    return views.get(position);
                }
            };
            whatsnew_viewpager.setAdapter(mPagerAdapter);
        } else {
            LoginUser user=getHelper().getUser();
            if(user==null){
                Intent intent = new Intent(Whatsnew.this, LoginActivity_.class);
                startActivity(intent);
                ac.cs.setIsFirstStart("1");
                finish();
            }else{
                Intent intent = new Intent(Whatsnew.this, PropertiesActivity_.class);
                startActivity(intent);
                ac.cs.setIsFirstStart("1");
                finish();
            }
        }
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

        public void onPageSelected(int arg0) {
            if (arg0 == 3) {
                enter_btn.setVisibility(View.VISIBLE);
            } else {
                enter_btn.setVisibility(View.GONE);
            }

        }

        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    @Override
    protected boolean loginStatus() {
        return false;
    }

    //    @Click(R.id.enter_btn)
    public void startbutton() {
            Intent intent = new Intent(Whatsnew.this, LoginActivity_.class);
            startActivity(intent);
            ac.cs.setIsFirstStart("1");
            finish();

    }

    private void createShorcut(int id) {
        if (ac.cs.getFastStartIcon() == CommonShared.OPEN) {
            return;
        } else {
            ac.cs.setFastStartIcon(CommonShared.OPEN);
        }
        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                getApplicationContext(), id);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口

        // 点击快捷方式的操作
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(this, Whatsnew_.class);

        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        sendBroadcast(shortcutintent);
    }

    @Override
    public boolean needAddParent() {
        return false;
    }

    public String getChannel(Context context) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith("META-INF/dog")) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            return ret.substring(split[0].length() + 1);
        } else {
            return "dog";
        }
    }
}

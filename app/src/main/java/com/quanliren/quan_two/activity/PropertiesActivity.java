package com.quanliren.quan_two.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.DongTaiListNavFragment_;
import com.quanliren.quan_two.activity.group.date.DatePublishActivity;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.CacheBean;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.bean.VersionBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.fragment.GroundFragment_;
import com.quanliren.quan_two.fragment.MessageFragment_;
import com.quanliren.quan_two.fragment.SetingMoreFragment_;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EActivity(R.layout.properties_test)
public class PropertiesActivity extends BaseActivity {

    public static final String PROPERTIESACTIVITY_NEWMESSAGE = "com.quanliren.quan_two.activity.PropertiesActivity.newmessage";

    List<Fragment> addedList = new ArrayList<Fragment>(); // 已添加的窗体
    List<Fragment> unaddList = new ArrayList<Fragment>(); // 所有将要添加的窗体
    @ViewById(R.id.nav_btn1)
    View btn1;
    @ViewById(R.id.nav_btn2)
    View btn2;
    @ViewById(R.id.nav_btn3)
    View btn3;
    @ViewById(R.id.nav_btn4)
    View btn4;
    @ViewById
    TextView messagecount;
    @ViewById
    View setcount;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        mTintManager.setStatusBarDarkMode(true, this);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                switch (i) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(PropertiesActivity.this, updateResponse);
                        break;
                }
            }
        });
    }

    @Override
    protected boolean translucentStatus() {
        return true;
    }

    List<View> list = new ArrayList<View>();

    @OrmLiteDao(helper = DBHelper.class, model = DfMessage.class)
    Dao<DfMessage, Integer> messageDao;
    @OrmLiteDao(helper = DBHelper.class, model = VersionBean.class)
    Dao<VersionBean, Integer> versionDao;

    @AfterViews
    void initView() {
        unaddList.add(DongTaiListNavFragment_.builder().build());
        unaddList.add(GroundFragment_.builder().build());
        unaddList.add(MessageFragment_.builder().build());
        unaddList.add(SetingMoreFragment_.builder().build());

        list.add(btn1);
        list.add(btn2);
        list.add(btn3);
        list.add(btn4);

        // 显示第一页
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, unaddList.get(0),
                        ((Object) unaddList.get(0)).getClass().getName())
                .commitAllowingStateLoss();

        addedList.add(unaddList.get(0));

        setSwipeBackEnable(false);

        btn1.setSelected(true);

        LoginUser lu = getHelper().getUser();

        if (lu != null) {
            Util.setAlarmTime(this, System.currentTimeMillis(),
                    BroadcastUtil.ACTION_CHECKCONNECT,
                    BroadcastUtil.CHECKCONNECT);
        }

        sendLog();

    }

    @Override
    public boolean needAddParent() {
        return false;
    }

    public static final String USERINFOCACHE = "userinfo";

    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        User temp = new Gson().fromJson(jo.getString(URL.RESPONSE),
                                User.class);
                        UserTable ut = new UserTable(temp);
                        userTableDao.delete(ut);
                        userTableDao.create(ut);

                        CacheBean cb = new CacheBean(USERINFOCACHE, "",
                                new Date().getTime());
                        cacheDao.delete(cb);
                        cacheDao.create(cb);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        AM.getActivityManager().popActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCount();
    }

    /**
     * 切换页面
     *
     * @param clazz 页面的Class
     */
    public void switchContent(Class clazz) {
        if (clazz == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment fg : addedList) {
            // if (!fg.isHidden()) {
            ft.hide(fg);
            // }
        }
        boolean showComplete = false;
        for (Fragment fg : addedList) {
            if (((Object) fg).getClass().getName().equals(clazz.getName())) {
                ft.show(fg);
                showComplete = true;
            }
        }
        if (!showComplete) {
            for (Fragment fg : unaddList) {
                if (((Object) fg).getClass().getName().equals(clazz.getName())) {
                    ft.add(R.id.content, fg, ((Object) fg).getClass().getName());
                    addedList.add(fg);
                }
            }
        }
        ft.commitAllowingStateLoss();
    }

    private long temptime;

    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - temptime > 3000) {
                Toast.makeText(this, "再按一次将退出程序", Toast.LENGTH_LONG).show();
                temptime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Click({R.id.nav_btn1, R.id.nav_btn2, R.id.nav_btn3, R.id.nav_btn4})
    public void setCurrentIndex(View view) {
        if (view.getId() == R.id.nav_btn3) {
            if (getHelper().getUser() == null) {
                startLogin();
                return;
            }
        }
        switchContent(((Object) unaddList.get(list.indexOf(view))).getClass());
        for (View v : list) {
            if (!v.equals(view))
                v.setSelected(false);
            else
                v.setSelected(true);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Receiver(actions = {ChatActivity.ADDMSG, PROPERTIESACTIVITY_NEWMESSAGE,
            BroadcastUtil.ACTION_OUTLINE, BroadcastUtil.EXIT})
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(ChatActivity.ADDMSG)) {
            setCount();
        } else if (action.equals(BroadcastUtil.ACTION_OUTLINE)) {

            try {
                TableUtils.clearTable(getHelper().getConnectionSource(),
                        LoginUser.class);
                AM.getActivityManager().popAllActivityExceptOne(
                        PropertiesActivity_.class);
                setCurrentIndex(btn4);
                removeMessageFragment();
                ac.stopServices();
                Util.canalAlarm(this, BroadcastUtil.ACTION_CHECKCONNECT);
                startLogin();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (action.equals(BroadcastUtil.EXIT)) {
            removeMessageFragment();
            setCount();
        } else if (action.equals(PROPERTIESACTIVITY_NEWMESSAGE)) {
            setCount();
        }
    }

    @OnActivityResult(1)
    void onFilterResult(int result) {
        if (result == 1) {
            setCurrentIndex(btn1);
            Intent intent = new Intent(DatePublishActivity.TAG);
            sendBroadcast(intent);
        }
    }

    public void removeMessageFragment() {
        try {
            addedList.remove(unaddList.get(2));
            getSupportFragmentManager().beginTransaction()
                    .remove(unaddList.get(2)).commitAllowingStateLoss();
            Fragment f = unaddList.get(2);
            f = new MessageFragment_();
            unaddList.set(2, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCount() {
        LoginUser user = getHelper().getUser();
        if (user == null) {
            messagecount.setVisibility(View.GONE);
            setcount.setVisibility(View.GONE);
            return;
        }
        try {

            QueryBuilder<DfMessage, Integer> qb = messageDao.queryBuilder();
            Where<DfMessage, Integer> where = qb.where();
            where.and(where.eq("userid", user.getId()),
                    where.eq("receiverUid", user.getId()),
                    where.eq("isRead", 0));

            long count = qb.countOf();

            if (count > 0) {
                messagecount.setVisibility(View.VISIBLE);
                if (count > 99) {
                    messagecount.setText("99+");
                } else {
                    messagecount.setText(count + "");
                }
            } else {
                messagecount.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface ITitle {
        public String getTitle();
    }

    void sendLog() {
        try {
            File file = new File(StaticFactory.APKCardPathCrash);
            if (file.exists()) {
                File[] list = file.listFiles();
                for (File file2 : list) {
                    final File temp = file2;
                    RequestParams rp = new RequestParams();
                    rp.put("file", temp);
                    ac.finalHttp.post(URL.SEND_LOG, rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                int status = response.getInt(URL.STATUS);
                                switch (status) {
                                    case 0:
                                        temp.delete();
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1200) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}

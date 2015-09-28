/*
package com.quanliren.quan_two.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.date.DatePublishActivity_;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.ExchangeRemindBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.VersionBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.fragment.MessageFragment_;
import com.quanliren.quan_two.fragment.NearPeopleActivityFrament_;
import com.quanliren.quan_two.fragment.SetingMoreFragment_;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.properties_test)
public class NearPeoplePropertiesActivity extends BaseActivity {

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
    @ViewById
    ImageView center_date;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.context=this;
        getSupportActionBar().hide();

    }
    List<View> list = new ArrayList<View>();

    @OrmLiteDao(helper = DBHelper.class, model = DfMessage.class)
    Dao<DfMessage, Integer> messageDao;
    @OrmLiteDao(helper = DBHelper.class, model = VersionBean.class)
    Dao<VersionBean, Integer> versionDao;
    @OrmLiteDao(helper = DBHelper.class, model = ExchangeRemindBean.class)
    Dao<ExchangeRemindBean, String> exchangeDao;
    @AfterViews
    void initView() {
        unaddList.add(new NearPeopleActivityFrament_());
        unaddList.add(new ShopVipDetailFrament_());
        unaddList.add(new MessageFragment_());
        unaddList.add(new SetingMoreFragment_());

        list.add(btn1);
        list.add(btn2);
        list.add(btn3);
        list.add(btn4);

        // 显示第一页

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, unaddList.get(0),
                        ((Object) unaddList.get(0)).getClass().getName())
                .commit();

        addedList.add(unaddList.get(0));

        setSwipeBackEnable(true);

        btn1.setSelected(true);
        LoginUser lu = getHelper().getUser();

        if (lu != null) {
            Util.setAlarmTime(this, System.currentTimeMillis(),
                    BroadcastUtil.ACTION_CHECKCONNECT,
                    BroadcastUtil.CHECKCONNECT);
        }
    }
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    @Click
    void center_date(){
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        if (isFastDoubleClick()) {
            return;
        }
        RequestParams pub=getAjaxParams();
        ac.finalHttp.post(URL.AFFIRM_PUB, pub, new JsonHttpResponseHandler() {

            public void onSuccess(JSONObject jo) {
                try {
                    int status = jo.getInt(URL.STATUS);
                    switch (status) {
                        case 0:
                            String ratify=jo.getJSONObject(URL.RESPONSE).getString("ratify");
                            if(Integer.valueOf(ratify)==0){
                                DatePublishActivity_.intent(context).start();
                            }else if(Integer.valueOf(ratify)==1){
                                showCustomToast("您有正在进行中的约会");
                                return;
                            }
                            break;
                        case 1:
                            jo.getJSONObject(URL.RESPONSE);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setCount();
    }

    */
/**
     * 切换页面
     *
     * @param clazz 页面的Class
     *//*

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
                try {
//                    requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    getSupportActionBar().setTitle(((ITitle) fg).getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showComplete = true;
            }
        }
        if (!showComplete) {
            for (Fragment fg : unaddList) {
                if (((Object) fg).getClass().getName().equals(clazz.getName())) {
                    ft.add(R.id.content, fg, ((Object) fg).getClass().getName());
                    addedList.add(fg);
                    try {
//                        getSupportActionBar()
//                                .setTitle(((ITitle) fg).getTitle());
                    } catch (Exception e) {
                    }
                }
            }
        }
        ft.commitAllowingStateLoss();
    }

    @Click({R.id.nav_btn2, R.id.nav_btn3, R.id.nav_btn4})
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
    @Click(R.id.nav_btn1)
    void clickBtn(){
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Receiver(actions = {ChatActivity.ADDMSG, PROPERTIESACTIVITY_NEWMESSAGE,BroadcastUtil.ACTION_OUTLINE, BroadcastUtil.EXIT})
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
                Util.canalAlarm(this, BroadcastUtil.ACTION_CHECKMESSAGE);
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

    public void removeMessageFragment() {
        try {
            addedList.remove(unaddList.get(2));
            getSupportFragmentManager().beginTransaction()
                    .remove(unaddList.get(2)).commit();
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

        try {
            QueryBuilder<ExchangeRemindBean, String> qb = exchangeDao
                    .queryBuilder();
            qb.where().eq("userId", user.getId());
            long count = qb.countOf();
            if (count > 0) {
                setcount.setVisibility(View.VISIBLE);
            } else {
                setcount.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
*/

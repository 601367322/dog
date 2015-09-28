package com.quanliren.quan_two.activity.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseUserActivity;
import com.quanliren.quan_two.activity.group.date.DateDetailActivity_;
import com.quanliren.quan_two.activity.image.ImageBeanList;
import com.quanliren.quan_two.activity.image.ImageBrowserActivity_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.custom.CustomDialogEditText;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@EActivity(R.layout.user_other_info)
//@OptionsMenu(R.menu.other_userinfo_menu)
public class UserOtherInfoActivity extends BaseUserActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    @Extra
    public String userId;
    @ViewById
    View guanzhu_btn;
    @ViewById
    TextView care_me_txt;
    @ViewById
    ImageView care_me_icon;
    @ViewById
    View leavemsg_btn;
    @ViewById
    TextView public_date;
    @ViewById
    View bottom_btn_ll;
    AtomicBoolean menuInit = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Click
    void public_date() {
        ac.finalHttp.post(URL.OTHERP_DATE, getAjaxParams("otherid", user.getId()), new MyJsonHttpResponseHandler(this, getString(R.string.loading), null) {
            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                if (jo.has(URL.RESPONSE)) {
                    if (jo.optJSONObject(URL.RESPONSE).has(URL.LIST)) {
                        List<DateBean> list = Util.jsonToList(jo.optJSONObject(URL.RESPONSE).optString(URL.LIST), DateBean.class);
                        if (list.size() > 0) {
                            DateBean bean = list.get(0);
                            DateDetailActivity_.intent(UserOtherInfoActivity.this).bean(bean).start();
                            return;
                        }
                    }
                }
                showCustomToast("对方暂时没有进行中的约会");
            }
        });

    }


    @OptionsItem(R.id.jubao_and_lahei)
    void jubao_and_lahei() {
        menuClick(2);
    }

    @OptionsItem(R.id.lahei)
    void lahei() {
        menuClick(0);
    }

    @OptionsItem(R.id.jubao)
    void jubao() {
        menuClick(1);
    }

    public void menuClick(int which) {
        if (user == null) {
            return;
        }
        LoginUser my = getHelper().getUser();
        if (my == null) {
            startLogin();
            return;
        }
        if (my.getId().equals(user.getId())) {
            showCustomToast("这是自己哟~");
            return;
        }
        if (user.getIsblacklist() == 0) {
            choice(which);
        } else {
            cancelBlack();
        }
    }

    @OptionsItem(R.id.cancle)
    void cancleBlack() {
        menuClick(3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getUsernumber()) && (user.getUsernumber().equals("80000") || user.getUsernumber().equals("85001"))) {
                return true;
            }
            if (user.getIsblacklist() == 0) {
                getMenuInflater().inflate(R.menu.other_userinfo_menu, menu);
            } else {
                getMenuInflater().inflate(R.menu.other_userinfo_menu_cancle, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @AfterViews
    protected void initView() {
        super.initView();
        bottom_btn_ll.setVisibility(View.GONE);
        bottom_btn_ll.setTranslationY(ImageUtil.dip2px(this, 50));
        swipe.setOnRefreshListener(this);
        initPull();
//        try {
//            UserTable ut = userTableDao.queryForId(userId);
//            if (ut != null) {
//                user = ut.getUser();
//                initViewByUser();
//            }
//        } catch (Exception e) {
//        }

        mTintManager.setStatusBarDarkMode(true, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRefresh() {
        RequestParams rp = getAjaxParams();
        rp.put("otherid", userId);
        rp.put("longitude", ac.cs.getLng());
        rp.put("latitude", ac.cs.getLat());
        ac.finalHttp.post(URL.GET_USER_INFO, rp, new JsonHttpResponseHandler() {
            @Override
            public void onFailure() {
                swipe.setRefreshing(false);
                showIntentErrorToast();
            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int status = response.getInt(URL.STATUS);
                    switch (status) {
                        case 0:
                            User temp = new Gson().fromJson(
                                    response.getString(URL.RESPONSE), User.class);
                            if (temp != null) {
                                user = temp;
                                UserTable dbUser = new UserTable(temp);
                                userTableDao.deleteById(dbUser.getId());
                                userTableDao.create(dbUser);
                                initViewByUser();
                                isblack();
                                careText();
                            }
                            break;
                        default:
                            showFailInfo(response);
                            break;
                    }
                } catch (Exception e) {

                } finally {
                    swipe.setRefreshing(false);
                    swipe.setEnabled(false);
                }
            }

            @Override
            public void onStart() {
            }
        });
    }

    @UiThread(delay = 200)
    void initPull() {
        swipe.setRefreshing(true);
    }

    @Click
    public void guanzhu_btn(View v) {
        if (user == null) {
            return;
        }
        LoginUser my = getHelper().getUser();
        if (my == null) {
            startLogin();
            return;
        }
        if (my.getId().equals(user.getId())) {
            showCustomToast("这是自己哟~");
            return;
        }
        String str = user.getAttenstatus().equals("0") ? "您确定要关注TA吗?"
                : "您确定要取消关注吗?";
        new android.support.v7.app.AlertDialog.Builder(UserOtherInfoActivity.this)
                .setTitle("提示")
                .setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams ap = getAjaxParams();
                        ap.put("otherid", userId);
                        String url = user.getAttenstatus().equals("0") ? URL.CONCERN
                                : URL.CANCLECONCERN;
                        ac.finalHttp.post(url, ap, concernCallBack);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).create().show();
    }

    JsonHttpResponseHandler concernCallBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog(1);
        }

        ;

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        ;

        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        user.setAttenstatus(user.getAttenstatus().equals("0") ? "1"
                                : "0");

                        UserTable dbUser = new UserTable(user);
                        userTableDao.deleteById(dbUser.getId());
                        userTableDao.create(dbUser);

                        if (user.getAttenstatus().equals("1")) {
                            showCustomToast("已添加关注");
                        } else {
                            showCustomToast("已取消关注");
                        }
                        careText();
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    public void careText() {
        if (user != null && user.getAttenstatus() != null) {
            if (user.getAttenstatus().equals("0")) {
                care_me_txt.setText("关注我吧");
                care_me_txt.setTextColor(getResources().getColor(R.color.nav_bar_commonn));
                care_me_icon.setImageResource(R.drawable.ic_user_other_care_normal);
            } else {
                care_me_txt.setText("已关注");
                care_me_icon.setImageResource(R.drawable.ic_user_other_care_pressed);
                care_me_txt.setTextColor(getResources().getColor(R.color.nav_press_txt));
            }
        }
    }

    public void isblack() {
        if (user == null) {
            return;
        }
        if (user.getIsblacklist() == 0) {
            bottom_btn_ll.animate().translationY(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            bottom_btn_ll.setVisibility(View.VISIBLE);
                        }
                    });

            getSupportActionBar().invalidateOptionsMenu();
        } else {
            bottom_btn_ll.animate()
                    .translationY(ImageUtil.dip2px(this, 50))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            bottom_btn_ll.clearAnimation();
                            bottom_btn_ll.setVisibility(View.GONE);
                        }
                    });

            getSupportActionBar().invalidateOptionsMenu();
        }
    }

    public void cancelBlack() {
        RequestParams ap = getAjaxParams();
        ap.put("otherid", user.getId());
        ac.finalHttp.post(URL.CANCLEBLACK, ap, new setLogoCallBack());
    }

    public void choice(int witch) {
        switch (witch) {
            case 0:
                new android.support.v7.app.AlertDialog.Builder(UserOtherInfoActivity.this)
                        .setTitle("提示")
                        .setMessage("您确定要拉黑该用户吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RequestParams ap = getAjaxParams();
                                        ap.put("otherid", user.getId());
                                        ac.finalHttp.post(URL.ADDTOBLACK, ap,
                                                addBlackCallBack);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).create().show();
                break;
            case 2:
                new android.support.v7.app.AlertDialog.Builder(UserOtherInfoActivity.this)
                        .setTitle("提示")
                        .setMessage("您确定要举报并拉黑该用户吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialogChoiceReason();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).create().show();
                break;
            case 1:
                new android.support.v7.app.AlertDialog.Builder(UserOtherInfoActivity.this)
                        .setTitle("提示")
                        .setMessage("您确定要举报该用户吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                RequestParams ap = getAjaxParams();
//                                ap.put("otherid", bean.getUserid());
//                                ap.put("ptype",1);
//                                ap.put("tid",bean.getDyid());
//                                ac.finalHttp.post(URL.JUBAO, ap,
//                                        addBlackCallBack);
                                        dialogChoiceReason2();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).create().show();
        }
    }

    JsonHttpResponseHandler juBaoCallBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog("正在发送请求");
        }

        ;

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        showCustomToast("举报成功");
//                        jubao.setTitle("已举报");
                        Intent i = new Intent(BlackListFragment.ADDEBLACKLIST);
                        i.putExtra("bean", user);
                        sendBroadcast(i);
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                customDismissDialog();
            }
        }

        ;

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        ;
    };

    public void dialogChoiceReason2() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(
                        new String[]{"骚扰信息", "个人资料不当", "盗用他人资料", "垃圾广告",
                                "色情相关"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("otherid", user.getId());
                                ap.put("type", which + "");
                                ap.put("ptype", 0 + "");
                                ap.put("tid", 0 + "");
                                ac.finalHttp.post(URL.JUBAO, ap,
                                        juBaoCallBack);
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void dialogChoiceReason() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(
                        new String[]{"骚扰信息", "个人资料不当", "盗用他人资料", "垃圾广告",
                                "色情相关"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("otherid", user.getId());
                                ap.put("type", which + "");
                                ac.finalHttp.post(URL.JUBAOANDBLACK, ap,
                                        addBlackCallBack);
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    JsonHttpResponseHandler addBlackCallBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog("正在发送请求");
        }

        ;

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        showCustomToast("操作成功");
                        user.setIsblacklist(1);
                        user.setAttenstatus("0");
                        UserTable ut = new UserTable(user);
                        userTableDao.deleteById(user.getId());
                        userTableDao.create(ut);
                        isblack();
                        careText();
                        Intent i = new Intent(BlackListFragment.ADDEBLACKLIST);
                        i.putExtra("bean", user);
                        sendBroadcast(i);
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                customDismissDialog();
            }
        }

        ;

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        ;
    };

    class setLogoCallBack extends JsonHttpResponseHandler {

        public void onStart() {
            customShowDialog("正在发送请求");
        }

        ;

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        ;

        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        showCustomToast("取消成功");
                        user.setIsblacklist(0);
                        UserTable ut = new UserTable(user);
                        userTableDao.deleteById(user.getId());
                        userTableDao.create(ut);
                        isblack();
                        Intent i = new Intent(BlackListFragment.CANCLEBLACKLIST);
                        i.putExtra("id", user.getId());
                        sendBroadcast(i);
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    }

    @Click
    void userlogo() {
        if (user != null) {
            List<ImageBean> str = new ArrayList<ImageBean>();
            str.add(new ImageBean(user.getAvatar()));
            ImageBrowserActivity_.intent(this).mPosition(0).ibl(new ImageBeanList(str))
                    .start();
        }
    }

    CustomDialogEditText mdialog;


    @Click
    void leavemsg_btn() {
        LoginUser user = getHelper().getUser();
        if (user == null) {
            startLogin();
            return;
        }
        if (user.getId().equals(this.user.getId())) {
            showCustomToast("这是自己哟~");
            return;
        }
        AM.getActivityManager().popActivity(ChatActivity_.class.getName());
        ChatActivity_.intent(this).friend(this.user).start();
    }

}

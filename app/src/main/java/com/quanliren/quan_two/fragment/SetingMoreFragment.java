package com.quanliren.quan_two.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.PropertiesActivity;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.bindnum.LookContactState_;
import com.quanliren.quan_two.activity.group.date.MyDateNavActivity_;
import com.quanliren.quan_two.activity.redline.MyRedLineListNavActivity_;
import com.quanliren.quan_two.activity.seting.DogCoinActivity_;
import com.quanliren.quan_two.activity.seting.SetingOtherActivity_;
import com.quanliren.quan_two.activity.seting.ShareToFriendActivity_;
import com.quanliren.quan_two.activity.seting.TestSetting_;
import com.quanliren.quan_two.activity.user.MyFavoriteNavActivity_;
import com.quanliren.quan_two.activity.user.MyPersonalDongTaiActivity_;
import com.quanliren.quan_two.activity.user.MyVisitActivity_;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.custom.CircleImageView;
import com.quanliren.quan_two.custom.StateTextView;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

@EFragment(R.layout.my)
public class SetingMoreFragment extends MenuFragmentBase {

    public static final String UPDATE_USERINFO = "com.quanliren.quan_two.fragment.SetingMoreFragment.UPDATE_USERINFO";


    @OrmLiteDao(helper = DBHelper.class, model = UserTable.class)
    public Dao<UserTable, String> userTableDao;
    @ViewById
    CircleImageView userlogo;
    @ViewById
    TextView nickname;
    @ViewById(R.id.dog_food_count)
    TextView foodCount;
    @ViewById
    TextView dogFoodCount;
    @ViewById
    TextView myVisitCount;
    @ViewById
    TextView vip;
    @ViewById
    View ip_setting;
    @ViewById
    ImageView wxin;
    @ViewById
    ImageView qq;
    @ViewById
    ImageView phone;
    @ViewById
    ImageView sina;

    @Override
    public void init() {
        super.init();
        try {
            ApplicationInfo appInfo = getActivity().getPackageManager()
                    .getApplicationInfo(getActivity().getPackageName(),
                            PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("TEST_SETTING");
            if (msg.equals("open")) {
                ip_setting.setVisibility(View.VISIBLE);
            } else {
                ip_setting.setVisibility(View.GONE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的狗粮
     */
    @Click
    public void dog_food() {
        DogCoinActivity_.intent(this).start();
    }

    /**
     * 我的红线
     */
    @Click
    public void red_line() {
        MyRedLineListNavActivity_.intent(this).start();
    }

    /**
     * 我的动态
     */
    @Click
    public void my_quan() {
        MyPersonalDongTaiActivity_.intent(this).start();
    }

    /**
     * 我的约会
     */
    @Click
    public void my_date() {
        MyDateNavActivity_.intent(this).start();
    }

    /**
     * 我的收藏
     */
    @Click
    public void my_favorite() {
        MyFavoriteNavActivity_.intent(this).start();
    }

    /**
     * 访客记录
     */
    @Click
    public void my_visit() {
        User user = getHelper().getUserInfo();
        if (user.getIsvip() == 0) {
            Util.goVip(getActivity());
            return;
        }
        MyVisitActivity_.intent(this).start();
    }

    /**
     * 约会状态
     */
    @Click
    public void my_date_state() {
        setState();
    }

    /**
     * 邀请好友
     */
    @Click
    public void invit_friend() {
        ShareToFriendActivity_.intent(this).start();
    }

    /**
     * 手机绑定
     */
    @Click
    public void bind_phone() {
        if (u.getXlstate().trim().charAt(0) == '0' && u.getWxstate().trim().charAt(0) == '0' && u.getQqstate().trim().charAt(0) == '0') {
            LookContactState_.intent(this).extra("isBind", 1).start();
        } else {
            LookContactState_.intent(this).start();
        }

    }

    /**
     * 微信绑定
     */
    @Click
    public void bind_wechat() {
        channel = "微信";
        if (u != null) {
            final String wxState = u.getWxstate();
            if(wxState!=null){
            if (wxState.charAt(0) == '0') {
                Util.showDialog(getActivity(), "你确定要绑定微信账号吗？", "绑定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UMWXHandler wxHandler = new UMWXHandler(getActivity(), Constants.APP_ID, Constants.APP_SECRET);
                        wxHandler.addToSocialSDK();
                        platform_login(SHARE_MEDIA.WEIXIN);
                    }
                });

            } else {

                    if (u.getXlstate().charAt(0) == '0' && u.getMstate().charAt(0) == '0' && u.getQqstate().charAt(0) == '0') {
                        return;
                    } else {
                        Util.showDialog(getActivity(), "你确定要解绑微信账号吗？", "解绑", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("channel", channel);
                                ap.put("otherName", wxState.substring(1));

                                ac.finalHttp.post(URL.CANCLE_BIND, ap, new MyJsonHttpResponseHandler(getActivity(), "正在请求") {
                                    @Override
                                    public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                        u.setWxstate("0");
                                        UserTable ut = new UserTable(u);
                                        getHelper().getUserTableDao().delete(ut);
                                        getHelper().getUserTableDao().create(ut);
                                        initSource(u);
                                    }
                                });
                            }
                        });
                    }
                }


            }
        }

    }

    /**
     * qq绑定
     */
    @Click
    public void bind_qq() {
        channel = "QQ";
        if (u != null) {
            final String qqState = u.getQqstate();
            if(qqState!=null){
                if (qqState.charAt(0) == '0') {
                    Util.showDialog(getActivity(), "你确定要绑定QQ账号吗？", "绑定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), "1101960968",
                                    "ScXUZWBPnQoKQMIG");
                            qqSsoHandler.addToSocialSDK();
                            platform_login(SHARE_MEDIA.QQ);
                        }
                    });

                } else {
                    if (u.getXlstate().trim().charAt(0) == '0' && u.getMstate().trim().charAt(0) == '0' && u.getWxstate().trim().charAt(0) == '0') {
                        return;
                    } else {
                        Util.showDialog(getActivity(), "你确定要解绑QQ账号吗？", "解绑", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("channel", channel);
                                ap.put("otherName", qqState.substring(1));
                                ac.finalHttp.post(URL.CANCLE_BIND, ap, new MyJsonHttpResponseHandler(getActivity(), "正在请求") {
                                    @Override
                                    public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                        u.setQqstate("0");
                                        UserTable ut = new UserTable(u);
                                        getHelper().getUserTableDao().delete(ut);
                                        getHelper().getUserTableDao().create(ut);
                                        initSource(u);
                                    }
                                });
                            }
                        });
                    }

                }
            }

        }


    }

    /**
     * 新浪绑定
     */
    @Click
    public void bind_sina() {
        channel = "新浪";
        if (u != null) {
            final String xlState = u.getXlstate();
            if (xlState!=null) {
                if (xlState.charAt(0) == '0') {
                    Util.showDialog(getActivity(), "你确定要绑定新浪微博账号吗？", "绑定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mController.getConfig().setSsoHandler(new SinaSsoHandler());
                            platform_login(SHARE_MEDIA.SINA);
                        }
                    });

                } else {
                    if (u.getWxstate().trim().charAt(0) == '0' && u.getMstate().trim().charAt(0) == '0' && u.getQqstate().trim().charAt(0) == '0') {
                        return;
                    } else {
                        Util.showDialog(getActivity(), "你确定要解绑新浪微博账号吗？", "解绑", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("channel", channel);
                                ap.put("otherName", xlState.substring(1));
                                ac.finalHttp.post(URL.CANCLE_BIND, ap, new MyJsonHttpResponseHandler(getActivity(), "正在请求") {
                                    @Override
                                    public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                        u.setXlstate("0");
                                        UserTable ut = new UserTable(u);
                                        getHelper().getUserTableDao().delete(ut);
                                        getHelper().getUserTableDao().create(ut);
                                        initSource(u);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        }


    }

    /**
     * 其他设置
     */
    @Click
    public void other_setting() {
        SetingOtherActivity_.intent(this).start();
    }

    @Click
    public void head() {
        LoginUser user = getHelper().getUser();
        if (user == null) {
            startLogin();
        } else {
            UserInfoActivity_.intent(getActivity()).start();
        }
    }

    ListView clistview;
    List<ChoiceBean> choicelist;
    ChoseAdapter cadapter;

    public void setState() {
        choicelist = new ArrayList<ChoiceBean>();
        final User user = getHelper().getUserInfo();
        for (int i = 0; i < 7; i++) {
            if (i == 0 || i == 1 || i == 2 || i == 5) {
                if (i == user.getUserstate()) {
                    choicelist.add(new ChoiceBean(i, true));
                } else {
                    choicelist.add(new ChoiceBean(i, false));
                }
            }

        }
        cadapter = new ChoseAdapter(getActivity());
        cadapter.setList(choicelist);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int index = 0;
                        for (ChoiceBean cb : choicelist) {
                            if (cb.select) {
                                index = cb.index;
                            }
                        }
                        if (index != user.getUserstate()) {
                            RequestParams rp = getAjaxParams();
                            rp.put("userstate", index + "");
                            ac.finalHttp.post(URL.SET_STATE, rp,
                                    new stateCallBack(getActivity(), index));
                        }
                    }
                })
                .setAdapter(cadapter,
                        null).create();
        clistview = dialog.getListView();
        dialog.setCanceledOnTouchOutside(true);
        clistview.setFocusable(false);
        clistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                for (ChoiceBean cacheBean : choicelist) {
                    cacheBean.setSelect(false);
                }
                choicelist.get(arg2).setSelect(true);
                cadapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    class ChoseAdapter extends BaseAdapter<ChoiceBean> {


        public ChoseAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseHolder getHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public int getConvertView(int position) {
            return R.layout.chose_state;
        }

        class ViewHolder extends BaseHolder {
            @Bind(R.id.textview)
            StateTextView tv;
            @Bind(R.id.rb)
            RadioButton rb;

            @Override
            public void bind(ChoiceBean bean, int position) {
                tv.setState(bean.index);
                if (bean.select) {
                    rb.setChecked(true);
                } else {
                    rb.setChecked(false);
                }
            }

            public ViewHolder(View view) {
                super(view);
            }
        }
    }

    class ChoiceBean {
        int index;
        boolean select;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public ChoiceBean(int index, boolean select) {
            super();
            this.index = index;
            this.select = select;
        }

        public ChoiceBean() {
            super();
            // TODO Auto-generated constructor stub
        }
    }

    public void statistic() {
        RequestParams rp = getAjaxParams();
        ac.finalHttp.post(URL.STATISTIC, rp, new MyJsonHttpResponseHandler(getActivity()) {

            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                JSONObject jj = jo.getJSONObject(URL.RESPONSE);
                ac.cs.setVcnt(jj.getString("vcnt"));
                myVisitCount.setText("(" + ac.cs.getVcnt() + ")");
            }

        });
    }

    @Receiver(actions = {UPDATE_USERINFO,
            PropertiesActivity.PROPERTIESACTIVITY_NEWMESSAGE})
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(UPDATE_USERINFO)) {
            ac.finalHttp.post(URL.GET_USER_INFO, getAjaxParams(), callBack);
        }
    }

    MyJsonHttpResponseHandler callBack = new MyJsonHttpResponseHandler(getActivity()) {

        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {
            User temp = new Gson().fromJson(jo.getString(URL.RESPONSE),
                    User.class);
            UserTable ut = new UserTable(temp);
            getHelper().getUserTableDao().delete(ut);
            getHelper().getUserTableDao().create(ut);
            initSource(temp);
        }
    };

    class BindBack extends MyJsonHttpResponseHandler {

        SHARE_MEDIA platform;

        public BindBack(Context context, SHARE_MEDIA platform) {
            super(context);
            this.platform=platform;
        }

        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {
            showCustomToast("绑定成功");
            String type="";
            User temp = getHelper().getUserInfo();
            if (platform == SHARE_MEDIA.SINA) {
                temp.setXlstate("1" + uid);
                type="新浪";
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                temp.setWxstate("1" + uid);
                type="微信";
            } else if (platform == SHARE_MEDIA.QQ) {
                temp.setQqstate("1" + uid);
                type="QQ";
            }
            String result=jo.getJSONObject(URL.RESPONSE).getString("result");
            if(result!=null&&"1".equals(result)){
                showCustomToast("首次绑定"+type+"号成功，狗粮+1");
            }
            UserTable ut = new UserTable(temp);
            getHelper().getUserTableDao().delete(ut);
            getHelper().getUserTableDao().create(ut);
            initSource(temp);
        }

    }

    User u;

    public void initSource(User user) {
        this.u = user;
        vip.setVisibility(View.GONE);
        if (user != null) {
            ImageLoader.getInstance().displayImage(
                    user.getAvatar() + StaticFactory._320x320, userlogo,
                    ac.options_userlogo);
            nickname.setText(user.getNickname());
            if (user.getIsvip() > 0) {
                vip.setVisibility(View.VISIBLE);
                vip.setText("会员：" + Util.daysBetween(user.getViptime())
                        + "天到期");
            }
            if (user.getMstate() != null && !"".equals(user.getMstate())) {
                if (user.getMstate().charAt(0) == '0') {
                    phone.setImageResource(R.drawable.ic_my_bind_contact_default);
                } else {
                    phone.setImageResource(R.drawable.ic_my_bind_phone);
                }
            }

            if (user.getWxstate() != null && !"".equals(user.getWxstate())) {
                if (user.getWxstate().charAt(0) == '0') {
                    wxin.setImageResource(R.drawable.ic_my_bind_wechat_default);
                } else {
                    wxin.setImageResource(R.drawable.ic_my_bind_wechat);
                }
            }

            if (user.getXlstate() != null && !"".equals(user.getXlstate())) {
                if (user.getXlstate().trim().charAt(0) == '0') {
                    sina.setImageResource(R.drawable.ic_my_bind_sina_default);
                } else {
                    sina.setImageResource(R.drawable.ic_my_bind_sina);
                }
            }

            if (user.getQqstate() != null && !"".equals(user.getQqstate())) {

                if (user.getQqstate().trim().charAt(0) == '0') {
                    qq.setImageResource(R.drawable.ic_my_bind_qq_default);
                } else {
                    qq.setImageResource(R.drawable.ic_my_bind_qq);
                }
            }
            foodCount.setText("(" + user.getCoin() + ")");
        } else {
            nickname.setText("请登录");
            userlogo.setImageResource(R.drawable.defalut_logo);
        }


    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /** 更新用户信息 **/
        try {
            ac.finalHttp.post(URL.GET_USER_IN, getAjaxParams(), callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = getHelper().getUserInfo();
        if (user != null) {
            initSource(user);
        }
        statistic();
    }

    class stateCallBack extends MyJsonHttpResponseHandler {

        int index = 0;

        public stateCallBack(Context context, int index) {
            super(context,getString(R.string.loading),null);
            this.index = index;
        }

        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {
            showCustomToast("设置成功");
            User user = getHelper().getUserInfo();
            user.setUserstate(index);
            UserTable ut = new UserTable(user);
            getHelper().getUserTableDao().delete(ut);
            getHelper().getUserTableDao().create(ut);
        }
    }

    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    /**
     * 授权。如果授权成功，则获取用户信息
     */
    String uid;

    String channel;

    private void platform_login(SHARE_MEDIA platform) {

        mController.doOauthVerify(getActivity(), platform, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    uid = value.getString("uid");
                    RequestParams ap = getAjaxParams();
                    ap.put("mobile", "");
                    ap.put("token", getHelper().getUser().getToken());
                    ap.put("otherName", uid);
                    ap.put("channel", channel);
                    ac.finalHttp.post(URL.OTHER_BIND, ap, new BindBack(getActivity(), platform));
                } else {
                    showCustomToast("授权失败");
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }

    @Click
    public void ip_setting() {
        try {
            ApplicationInfo appInfo = getActivity().getPackageManager()
                    .getApplicationInfo(getActivity().getPackageName(),
                            PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("TEST_SETTING");
            if (msg.equals("open")) {
                TestSetting_.intent(this).start();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}

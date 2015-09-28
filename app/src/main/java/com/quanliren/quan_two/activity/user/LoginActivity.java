package com.quanliren.quan_two.activity.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.PropertiesActivity_;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.location.GDLocation;
import com.quanliren.quan_two.activity.reg.ForgetPassWordActivity1_;
import com.quanliren.quan_two.activity.reg.RegFirst_;
import com.quanliren.quan_two.activity.seting.TestSetting_;
import com.quanliren.quan_two.adapter.ParentsAdapter;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.MoreLoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.custom.RoundImageProgressBar;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.share.CommonShared;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@EActivity(R.layout.login)
public class LoginActivity extends BaseActivity {

    @ViewById
    EditText username;
    @ViewById
    EditText password;
    @ViewById
    View username_ll;
    @ViewById
    TextView forgetpassword;
    @ViewById
    Button loginBtn;
    @ViewById
    TextView regBtn;
    @ViewById
    LinearLayout margin_ll;
    @ViewById
    View delete_username_btn;
    @ViewById
    View delete_password_btn;
    @ViewById
    View more_username_btn;
    @ViewById
    RoundImageProgressBar round_img1;
    @ViewById
    RoundImageProgressBar round_img2;
    @ViewById
    View userlogo_ll;
    @ViewById
    ImageView userlogo;
    @ViewById
    ScrollView scrollview;
    @ViewById
    View ip_setting;
    boolean isShow = false; // 更多用户名是否展开
    private PopupWindow pop;
    private PopupAdapter adapter;
    private ListView listView;
    private List<MoreLoginUser> names = new ArrayList<MoreLoginUser>();
    String str_username, str_password;
    GDLocation location;

    @OrmLiteDao(helper = DBHelper.class, model = MoreLoginUser.class)
    public Dao<MoreLoginUser, Integer> moreLoginUserDao;

    @Override
    public void init() {
        super.init();

        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
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

        setListener();
        location = new GDLocation(getApplicationContext(), null, true);
        setSwipeBackEnable(false);
    }

    @Override
    public boolean needAddParent() {
        return false;
    }

    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    @Click
    public void sina_login(View view) {
        channel = "新浪";
        customShowDialog(2);
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        platform_login(SHARE_MEDIA.SINA);
    }

    @Click
    public void qq_login(View view) {
        channel = "QQ";
        customShowDialog(2);
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this, Constants.appId, Constants.appKey);
        qqSsoHandler.addToSocialSDK();
        platform_login(SHARE_MEDIA.QQ);
    }

    @Click
    public void wxin_login(View view) {
        channel = "微信";
        customShowDialog(2);
        UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, Constants.APP_ID, Constants.APP_SECRET);
        wxHandler.addToSocialSDK();
        platform_login(SHARE_MEDIA.WEIXIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 授权。如果授权成功，则获取用户信息
     */
    private void platform_login(SHARE_MEDIA platform) {
        mController.doOauthVerify(LoginActivity.this, platform, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                customDismissDialog();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {

                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    uid = value.getString("uid");
                    getUserInfo(platform);
                } else {
                    showCustomToast("授权失败");
                    customDismissDialog();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                customDismissDialog();
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }

    /**
     * 获取授权平台的用户信息
     */
    String nickname;
    String avatar;
    int gender;
    String uid;
    String channel;

    private void getUserInfo(final SHARE_MEDIA platform) {
        mController.getPlatformInfo(LoginActivity.this, platform, new SocializeListeners.UMDataListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
//                platform_logout(platform);
                if (info != null) {
                    JSONObject jsonObj = new JSONObject(info);
                    try {
                        if (platform == SHARE_MEDIA.WEIXIN) {
                            avatar = jsonObj.getString("headimgurl");
                            nickname = jsonObj.getString("nickname");
                            if ("2".equals(jsonObj.getString("sex"))) {
                                gender = 0;
                            } else {
                                gender = 1;
                            }
                        } else {
                            avatar = jsonObj.getString("profile_image_url");
                            nickname = jsonObj.getString("screen_name");
                            if (jsonObj.getInt("gender") == 0 || "女".equals(jsonObj.getString("gender"))) {
                                gender = 0;
                            } else {
                                gender = 1;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CommonShared cs = new CommonShared(getApplicationContext());
                    RequestParams ap = getAjaxParams();
                    ap.put("mobile", "");
                    ap.put("nickname", nickname);
                    ap.put("otherName", uid);
                    ap.put("pwd", "");
                    ap.put("birthday", "");
                    ap.put("userstate", 1);

                    ap.put("avatar", avatar);
                    ap.put("sex", gender);
                    ap.put("dtype", 0);
                    ap.put("deviceid", ac.cs.getDeviceId());
                    ap.put("versionCode", ac.cs.getVersionCode());
                    ap.put("versionName", ac.cs.getVersionName());
                    ap.put("channel", channel);

                    ap.put("cityid", String.valueOf(cs.getLocationID()));
                    ap.put("longitude", ac.cs.getLng());
                    ap.put("latitude", ac.cs.getLat());
                    User lou = new User();
                    lou.setMobile("");
                    lou.setPwd("");
                    ac.finalHttp.post(URL.OTHER_LOGIN, ap, new CallBack(LoginActivity.this, lou));
                } else {
                    showCustomToast("获取用户信息失败");
                }
            }
        });
    }

    User user;

    class CallBack extends MyJsonHttpResponseHandler {
        User u;

        public CallBack(Context context, User u) {
            super(context);
            this.u = u;
        }

        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {
            customDismissDialog();
            user = new Gson().fromJson(jo.getString(URL.RESPONSE),
                    User.class);
            LoginUser lu = new LoginUser(user.getId(), user.getMobile(),
                    u.getPwd(), user.getToken());

            // 保存用户
            userTableDao.deleteById(user.getId());
            userTableDao.create(new UserTable(user));

            // 保存登陆用户
            TableUtils.clearTable(getConnectionSource(),
                    LoginUser.class);
            loginUserDao.create(lu);

            ac.startServices();
            Intent intent = new Intent(LoginActivity.this, PropertiesActivity_.class);
            startActivity(intent);
            finish();

        }
    }

    public void setListener() {
        username.addTextChangedListener(usernameTW);
        password.addTextChangedListener(passwordTW);
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

    @Click(R.id.regBtn)
    public void reg(View v) {
        RegFirst_.intent(this).start();
    }

    @Click
    public void delete_username_btn(View v) {
        username.setText("");
        password.setText("");
    }

    @Click
    public void delete_password_btn(View v) {
        password.setText("");
    }

    @Click
    public void more_username_btn(View v) {
        if (!isShow) {
            isShow = true;
            initUserNamePop();
        } else {
            isShow = false;
            initUserNamePop();
        }
    }

    /**
     * 用户名输入框的输入事件
     */
    TextWatcher usernameTW = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                delete_username_btn.setVisibility(View.VISIBLE);
            } else {
                delete_username_btn.setVisibility(View.GONE);
            }
            try {
                QueryBuilder<UserTable, String> qb = userTableDao
                        .queryBuilder();
                qb.where().eq("mobile", s.toString());
                List<UserTable> uts = userTableDao.query(qb.prepare());
                if (uts.size() > 0) {
                    UserTable ut = uts.get(0);
                    ImageLoader.getInstance().displayImage(
                            ut.getUser().getAvatar() + StaticFactory._320x320,
                            userlogo, ac.options_userlogo);

                } else {
                    userlogo.setImageResource(R.drawable.defalut_logo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };
    TextWatcher passwordTW = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                delete_password_btn.setVisibility(View.VISIBLE);
            } else {
                delete_password_btn.setVisibility(View.GONE);
            }
        }
    };

    class PopupAdapter extends ParentsAdapter {

        public PopupAdapter(Context c, List list) {
            super(c, list);
        }

        public View getView(final int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            final String name = ((MoreLoginUser) list.get(position))
                    .getUsername();
            final String pass = ((MoreLoginUser) list.get(position))
                    .getPassword();
            if (convertView == null) {
                convertView = View.inflate(c, R.layout.username_popup, null);
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.more_user);
                holder.iv = (ImageView) convertView
                        .findViewById(R.id.more_clear);
                holder.ll = (LinearLayout) convertView
                        .findViewById(R.id.more_user_ll);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(name);
            holder.ll.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    initUserNamePop();
                    username.setText(name);
                    password.setText(pass);
                }
            });

            holder.iv.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    names.remove(position);
                    try {
                        moreLoginUserDao.delete(moreLoginUserDao
                                .deleteBuilder().where().eq("username", name)
                                .prepareDelete());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    if (names.size() == 0) {
                        initUserNamePop();
                    }
                }
            });
            if (position == (list.size() - 1)) {
                holder.ll.setBackgroundResource(R.drawable.input_btm_btn);
            } else {
                holder.ll.setBackgroundResource(R.drawable.input_mid_btn);
            }
            return convertView;
        }

    }

    static class ViewHolder {
        TextView tv;
        ImageView iv;
        LinearLayout ll;
    }

    public void initUserNamePop() {
        try {
            if (pop == null) {
                if (adapter == null) {
                    names = moreLoginUserDao.query(moreLoginUserDao
                            .queryBuilder().orderBy("id", false).prepare());
                    adapter = new PopupAdapter(getApplicationContext(), names);
                    listView = new ListView(LoginActivity.this);
                    int width = username_ll.getWidth();
                    pop = new PopupWindow(listView, width,
                            LayoutParams.WRAP_CONTENT);
                    pop.setOutsideTouchable(true);
                    listView.setItemsCanFocus(false);
                    listView.setDivider(null);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(adapter);
                    pop.setFocusable(false);
                    pop.showAsDropDown(username_ll);
                    isShow = true;
                    more_username_btn.animate().setDuration(200).rotation(180)
                            .start();
                }
            } else if (pop.isShowing()) {
                pop.dismiss();
                isShow = false;
                more_username_btn.animate().setDuration(200).rotation(0).start();
            } else if (!pop.isShowing()) {
                names = moreLoginUserDao.query(moreLoginUserDao.queryBuilder()
                        .orderBy("id", false).prepare());
                adapter.setList(names);
                adapter.notifyDataSetChanged();
                pop.showAsDropDown(username_ll);
                isShow = true;
                more_username_btn.animate().setDuration(200).rotation(180)
                        .start();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShow && pop != null) {
                initUserNamePop();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (isShow && pop != null) {
            initUserNamePop();
            return;
        }
        super.onBackPressed();
    }

    ValueAnimator anim, anim1, anim2, anim3;

    @Click(R.id.loginBtn)
    public void login(View v) {

        str_username = username.getText().toString().trim();
        str_password = password.getText().toString().trim();

        if (!Util.isMobileNO(str_username)) {
            showCustomToast("请输入正确的用户名");
            return;
        } else if (!Util.isPassword(str_password)) {
            showCustomToast("请输入正确的密码");
            return;
        }

        closeInput();

        anim2 = ObjectAnimator.ofFloat(margin_ll, "alpha", 1, 0).setDuration(
                200);
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) userlogo_ll
                .getLayoutParams();
        int screenY = getResources().getDisplayMetrics().heightPixels / 2;
        screenY = (screenY - lp.topMargin);
        screenY -= userlogo_ll.getHeight() / 2;

        anim3 = ObjectAnimator.ofInt(lp.topMargin, screenY).setDuration(200);

        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.topMargin = Integer.valueOf(animation.getAnimatedValue().toString());
                userlogo_ll.setLayoutParams(lp);
            }
        });

        final AnimatorSet set = new AnimatorSet();
        set.playTogether(anim2, anim3);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                round_img1.setVisibility(View.VISIBLE);
                round_img2.setVisibility(View.VISIBLE);

                anim = ObjectAnimator.ofFloat(0f, 360f).setDuration(1000);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(ValueAnimator.INFINITE);
                anim.addUpdateListener(round_img1);
                anim.setRepeatMode(ValueAnimator.RESTART);

                anim1 = ObjectAnimator.ofFloat(360f, 0f).setDuration(1000);
                anim1.setInterpolator(new LinearInterpolator());
                anim1.setRepeatCount(ValueAnimator.INFINITE);
                anim1.addUpdateListener(round_img2);
                anim1.setRepeatMode(ValueAnimator.RESTART);
                anim.start();
                anim1.start();
                margin_ll.clearAnimation();
                margin_ll.setVisibility(View.GONE);

                RequestParams ap = getAjaxParams();
                ap.put("mobile", str_username);
                ap.put("pwd", str_password);
                ap.put("cityid", String.valueOf(ac.cs.getLocationID()));
                ap.put("longitude", ac.cs.getLng());
                ap.put("latitude", ac.cs.getLat());
                ap.put("area", ac.cs.getArea());
                ap.put("dtype", "0");
                ap.put("deviceid", ac.cs.getDeviceId());
                ap.put("otherName", "");
                ac.finalHttp.post(URL.LOGIN, ap, callBack);
            }
        });
        set.start();

    }

    @UiThread
    void stopAnimate() {
        round_img1.clearAnimation();
        round_img2.clearAnimation();
        round_img1.setVisibility(View.GONE);
        round_img2.setVisibility(View.GONE);
        if (anim2 != null)
            anim2.reverse();
        if (anim3 != null)
            anim3.reverse();
        if (anim != null)
            anim.cancel();
        if (anim1 != null)
            anim1.cancel();
    }

    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {
        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        // 登陆记录
                        moreLoginUserDao.delete(moreLoginUserDao.deleteBuilder()
                                .where().eq("username", str_username)
                                .prepareDelete());
                        // .deleteByWhere(MoreLoginUser.class, "username='"
                        // + str_username + "'");
                        moreLoginUserDao.create(new MoreLoginUser(str_username,
                                str_password));

                        User u = new Gson().fromJson(jo.getString(URL.RESPONSE),
                                User.class);
                        LoginUser lu = new LoginUser(u.getId(), str_username,
                                str_password, u.getToken());

                        // 保存用户
                        userTableDao.deleteById(u.getId());
                        userTableDao.create(new UserTable(u));

                        // 保存登陆用户
                        TableUtils.clearTable(getConnectionSource(),
                                LoginUser.class);
                        loginUserDao.create(lu);

                        ac.startServices();
                        Intent intent = new Intent(LoginActivity.this, PropertiesActivity_.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                                URL.INFO));
                        margin_ll.clearAnimation();
                        margin_ll.setVisibility(View.VISIBLE);
                        stopAnimate();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onStart() {

        }

        public void onFailure() {
            margin_ll.clearAnimation();
            margin_ll.setVisibility(View.VISIBLE);
            stopAnimate();
            showIntentErrorToast();
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        location.destory();
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    @Click(R.id.forgetpassword)
    public void findpassword(View v) {
        ForgetPassWordActivity1_.intent(this).start();
    }

    @Override
    protected boolean loginStatus() {
        return false;
    }
    @Click
    public void ip_setting() {
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
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

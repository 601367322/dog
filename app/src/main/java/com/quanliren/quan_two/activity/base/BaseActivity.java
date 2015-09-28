package com.quanliren.quan_two.activity.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.seting.DogCoinActivity;
import com.quanliren.quan_two.activity.user.LoginActivity_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.CacheBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.SystemBarTintManager;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@EActivity
public abstract class BaseActivity extends OrmLiteBaseActivity<DBHelper> {

    @App
    public AppClass ac;
    @OrmLiteDao(helper = DBHelper.class, model = UserTable.class)
    public Dao<UserTable, String> userTableDao;

    @OrmLiteDao(helper = DBHelper.class, model = LoginUser.class)
    public Dao<LoginUser, String> loginUserDao;
    @OrmLiteDao(helper = DBHelper.class, model = CacheBean.class)
    public Dao<CacheBean, String> cacheDao;
    @ViewById(R.id.left)
    public LinearLayout left;
    @ViewById(R.id.left_icon)
    public ImageView left_icon;
    @ViewById(R.id.left_title)
    public TextView left_title;

    @ViewById(R.id.center)
    public TextView center;
    @ViewById(R.id.actionbar)
    public View actionbar;
    @ViewById(R.id.right)
    public LinearLayout right;
    @ViewById(R.id.right_icon)
    public ImageView right_icon;
    @ViewById(R.id.right_title)
    public TextView right_title;
    @ViewById
    public ViewGroup parent;

    protected SystemBarTintManager mTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AM.getActivityManager().pushActivity(this);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        if (translucentStatus()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT);
//            }
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            mTintManager.setStatusBarTintResource(R.color.actionbar);
            mTintManager.setStatusBarDarkMode(true, this);
        }
    }

    protected boolean translucentStatus() {
        return true;
    }

    protected boolean loginStatus() {
        return true;
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @AfterViews
    public void init() {
    }

    ;

    public void setTitleTxt(String title) {
        if (this.center != null) {
            this.center.setText(title);
        }
    }

    public void setTitleTxt(int title) {
        if (this.center != null) {
            this.center.setText(title);
        }

    }

    public void setLeftTitleTxt(String title) {
        if (this.left_title != null)
            this.left_title.setText(title);
    }

    public void setLeftTitleTxt(int title) {
        if (this.left_title != null)
            this.left_title.setText(title);
    }

    public void setLeftIcon(int title) {
        if (this.left_icon != null) {
            this.left_icon.setVisibility(View.VISIBLE);
            this.left_icon.setImageResource(title);
        }

    }

    public void setRightTitleTxt(String title) {
        if (this.right_title != null)
            this.right_title.setText(title);
    }

    public void setRightTitleTxt(int title) {
        if (this.right_title != null)
            this.right_title.setText(title);
    }

    public void setRightIcon(int title) {
        if (this.right_icon != null) {
            right_icon.setVisibility(View.VISIBLE);
            this.right_icon.setImageResource(title);
        }
    }

    public void closeActivity(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeInput();
        AM.getActivityManager().popActivity(this);
    }

    @Click(R.id.left_icon)
    public void finishAcy() {
        scrollToFinishActivity();
    }

    @OptionsItem(android.R.id.home)
    public void finishActivity() {
        scrollToFinishActivity();
    }

    public void closeInput() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void showKeyBoard() {
        Util.showKeyBoard(this);
    }

    public void startLogin() {
        LoginActivity_.intent(this).start();
        finish();
    }

    public void showCustomToast(String str) {
        Util.toast(this, str);
    }

    public void showIntentErrorToast() {
        showCustomToast("网络连接失败");
    }

    public ProgressDialog progressDialog;

    public void customDismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void customShowDialog(String str) {
        progressDialog = Util.progress(this, str);
    }

    String[] str = new String[]{"", "正在获取数据", "正在登录", "正在提交", "请稍等"};

    public void customShowDialog(int i) {
        progressDialog = Util.progress(this, str[i]);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        }
        return super.onTouchEvent(event);
    }

    public RequestParams getAjaxParams() {
        return Util.getAjaxParams(this);
    }

    public RequestParams getAjaxParams(String str, String strs) {
        RequestParams ap = getAjaxParams();
        ap.put(str, strs);
        return ap;
    }

    public void showFailInfo(JSONObject jo) {
        try {
            int status = jo.getInt(URL.STATUS);
            switch (status) {
                case 1:
                    showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                            URL.INFO));
                    break;
                case 2:
                    showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                            URL.INFO));
                    loginOut();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void loginOut() {
        try {
            DBHelper.clearTable(this, LoginUser.class);
            AM.getActivityManager().popAllActivity();
            LoginActivity_.intent(this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (loginStatus()) {
            try {
                final LoginUser user=getHelper().getUser();
                if(user==null){
                    return;
                }
                if (Util.fmtDate.parse(ac.cs.getLoginTime(user.getId())).before(Util.fmtDate.parse(Util.fmtDate.format(new Date())))) {

                    ac.finalHttp.post(URL.EVERYDAY_LOGIN, getAjaxParams(), new MyJsonHttpResponseHandler(this) {
                        @Override
                        public void onSuccessRetCode(JSONObject jo) throws Throwable {
                            jo = new JSONObject(jo.getString(URL.RESPONSE));
                            ac.cs.setLoginTime(user.getId(),Util.fmtDate.format(new Date()));
                            Intent intent=new Intent(DogCoinActivity.TAG);
                            intent.putExtra("coin",jo.getString("coin"));
                            sendBroadcast(intent);
                            if ("1".equals(jo.getString("result"))) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        LayoutInflater inflater = LayoutInflater.from(BaseActivity.this);
                                        View view = inflater.inflate(R.layout.login_remind,null);
                                        Toast toast = new Toast(BaseActivity.this);
                                        toast.setGravity(Gravity.FILL, 0, 0);
                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        toast.setView(view);
                                        toast.show();
                                    }
                                }, 1000);

                            }
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 配置分享平台参数,并设置分享内容</br>
     */
    public void configPlatforms(UMSocialService mController) {

        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加QQ、QZone平台
//        addQQZonePlatform(activity);
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, Constants.appId, Constants.appKey);
        qqSsoHandler.setTargetUrl(Constants.shareUrl);
        qqSsoHandler.addToSocialSDK();
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, Constants.appId, Constants.appKey);
        qZoneSsoHandler.addToSocialSDK();
        // 添加微信、微信朋友圈平台
//        addWXPlatform(activity);
        UMWXHandler wxHandler = new UMWXHandler(this, Constants.APP_ID, Constants.APP_SECRET);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, Constants.APP_ID, Constants.APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //设置各平台分享内容
        setShareContent(mController);
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    public void setShareContent(UMSocialService mController) {
        //设置分享内容
        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.setShareContent(Constants.shareContent);
        UMImage urlImage = null;
        try {
            urlImage = new UMImage(this, BitmapFactory.decodeStream(getAssets().open("share_icon.jpg")));
            UMImage shareImage = new UMImage(this, BitmapFactory.decodeStream(getAssets().open("share.jpg")));
            mController.setShareImage(shareImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setTitle(Constants.wxShareTitle);
        circleMedia.setShareContent(Constants.shareContent);
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(Constants.shareUrl);
        mController.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        Util.shareContent(qzone, urlImage);
        mController.setShareMedia(qzone);

        QQShareContent qqContent = new QQShareContent();
        Util.shareContent(qqContent, urlImage);
        mController.setShareMedia(qqContent);

        //设置微信分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        Util.shareContent(weixinContent, urlImage);
        mController.setShareMedia(weixinContent);
    }

}

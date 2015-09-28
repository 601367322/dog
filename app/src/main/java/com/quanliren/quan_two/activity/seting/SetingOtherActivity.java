package com.quanliren.quan_two.activity.seting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.j256.ormlite.table.TableUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.PropertiesActivity;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.user.BlackListActivity_;
import com.quanliren.quan_two.activity.user.LoginActivity_;
import com.quanliren.quan_two.activity.user.ModifyPasswordActivity_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.umeng.fb.FeedbackAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.http.Header;

@EActivity(R.layout.seting)
public class SetingOtherActivity extends BaseActivity {
    Context context;
    @ViewById
    TextView source;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }

    @AfterViews
    void initView() {
        getFileSize();
        user = getHelper().getUserInfo();

    }

    @Receiver(actions = {PropertiesActivity.PROPERTIESACTIVITY_NEWMESSAGE})
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(PropertiesActivity.PROPERTIESACTIVITY_NEWMESSAGE)) {
        }
    }

    @Background
    void getFileSize() {
        BigDecimal bd = new BigDecimal(getFolderSize(ImageLoader.getInstance()
                .getDiskCache().getDirectory()));
        bd = bd.divide(new BigDecimal(1024 * 1024));
        notifyDate(Util.RoundOf(bd.toPlainString()) + "MB");
    }

    @UiThread
    void notifyDate(String str){
        source.setText(str);
    }
        public double getFolderSize(java.io.File file) {
        double size = 0;
        java.io.File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return (double) size;
    }

    public void loginout() {
        new AlertDialog.Builder(this)
                .setMessage("您确定要残忍的离开吗？")
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            ac.finalHttp.post(URL.LOGOUT, getAjaxParams(),
                                    new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode,
                                                              Header[] headers,
                                                              byte[] responseBody) {
                                        }

                                        @Override
                                        public void onFailure(int statusCode,
                                                              Header[] headers,
                                                              byte[] responseBody,
                                                              Throwable error) {
                                        }
                                    });
                            TableUtils.clearTable(getHelper()
                                    .getConnectionSource(), LoginUser.class);
                            showCustomToast("退出成功");
                            ac.stopServices();
                            Intent i = new Intent(BroadcastUtil.EXIT);
                            context.sendBroadcast(i);
                            Util.canalAlarm(context, BroadcastUtil.ACTION_CHECKCONNECT);
                            AM.getActivityManager().popAllActivity();
                            Intent intent = new Intent(SetingOtherActivity.this, LoginActivity_.class);
                            startActivity(intent);
                            finish();

//                            onResume();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        User user = getHelper().getUserInfo();
        getFileSize();
    }


    public void clearCache() {
        new AlertDialog.Builder(this).setMessage("您确定要清除缓存吗？")
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customShowDialog("正在清理");
                        clear();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    @Background
    public void clear() {
        ImageLoader.getInstance().clearDiskCache();
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    customDismissDialog();
                    showCustomToast("清理完成");
                    notifyDate("0.0MB");
                }
            });
        }
    }


    @Click
    void clear_ll() {
        clearCache();
    }

    @Click
    void notify_ll() {
        RemindMessageActivity_.intent(this).start();
    }

    @Click
    void black_ll() {
        if (user == null) {
            startLogin();
            return;
        }
        BlackListActivity_.intent(this).start();
    }

    @Click
    void pwd_ll() {
        if (user == null) {
            startLogin();
            return;
        }
        ModifyPasswordActivity_.intent(this).start();
    }

    @Click
    void helper_ll() {
        Intent help = new Intent(this, HtmlActivity_.class);
        help.putExtra("title", "用户帮助");
        help.putExtra("url", "file:///android_asset/function.html");
        startActivity(help);
    }

    @Click
    void feedback() {
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.closeAudioFeedback();
        agent.closeFeedbackPush();
        agent.startFeedbackActivity();
    }

    @Click
    void about_us() {
        AboutUsActivity_.intent(this).start();
    }

    @Click
    void exit() {
        if (user == null) {
            startLogin();
            return;
        }
        loginout();
    }

}

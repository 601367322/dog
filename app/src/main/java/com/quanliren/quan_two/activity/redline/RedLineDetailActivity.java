package com.quanliren.quan_two.activity.redline;

import android.content.Intent;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.sso.UMSsoHandler;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by Shen on 2015/7/1.
 */
@EActivity(R.layout.activity_redline_detail)
public class RedLineDetailActivity extends BaseActivity {

    @Extra
    RedLineBean bean;

    @Override
    public void init() {

        getSupportFragmentManager().beginTransaction().replace(R.id.content,RedLineDetailFragment_.builder().bean(bean).build()).commitAllowingStateLoss();
    }
    //SSO授权回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean titleCenter() {
        return false;
    }
}

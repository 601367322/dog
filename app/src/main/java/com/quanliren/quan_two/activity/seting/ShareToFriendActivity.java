package com.quanliren.quan_two.activity.seting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

@EActivity
public class ShareToFriendActivity extends BaseActivity {

    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constants.DESCRIPTOR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx);
        getSupportActionBar().setTitle("邀请好友");
        // 配置需要分享的相关平台,并设置分享内容
        configPlatforms(mController);
    }

    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
     */
    private void postShare(SHARE_MEDIA mPlatform) {
        mController.postShare(ShareToFriendActivity.this, mPlatform, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败";
                } else {
                    ac.finalHttp.post(URL.SHARE_RECORD, getAjaxParams(), new MyJsonHttpResponseHandler(ShareToFriendActivity.this) {
                        @Override
                        public void onSuccessRetCode(JSONObject jo) throws Throwable {

                        }

                        @Override
                        public void onFailure() {
                        }
                    });
                }
                showCustomToast(showText);
            }
        });
    }

    @Click(R.id.fx_sina_btn)
    public void shareToSina(View v) {
        postShare(SHARE_MEDIA.SINA);
    }

    @Click(R.id.fx_qq_btn)
    public void shareToQQ(View v) {
        postShare(SHARE_MEDIA.QQ);
    }

    @Click(R.id.fx_qoze_btn)
    public void shareToQZone(View v) {
        postShare(SHARE_MEDIA.QZONE);
    }

    @Click(R.id.fx_weixin_btn)
    public void shareToWeiXin(View v) {
        postShare(SHARE_MEDIA.WEIXIN);
    }

    @Click(R.id.fx_friend_btn)
    public void shareToFriendQ(View v) {
        postShare(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    // 短信分享
    @Click(R.id.fx_msg_btn)
    public void shareToMsg(View v) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO,
                smsToUri);
        mIntent.putExtra("sms_body", Constants.shareContent);
        startActivity(mIntent);
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
}

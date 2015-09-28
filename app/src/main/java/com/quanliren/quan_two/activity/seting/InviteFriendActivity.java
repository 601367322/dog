//package com.quanliren.quan_two.activity.seting;
//
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.res.AssetManager;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import com.quanliren.quan_two.activity.R;
//import com.quanliren.quan_two.activity.base.BaseActivity;
//import com.quanliren.quan_two.util.StaticFactory;
//import com.quanliren.quan_two.util.Util;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.BaseResponse;
//import com.sina.weibo.sdk.api.share.IWeiboHandler;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WeiboAuth;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.constant.WBConstants;
//import com.tencent.connect.auth.QQAuth;
//import com.tencent.connect.share.QQShare;
//import com.tencent.mm.sdk.openapi.GetMessageFromWX;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.SendMessageToWX;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.openapi.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.WXTextObject;
//import com.tencent.mm.sdk.openapi.WXWebpageObject;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.ViewById;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@EActivity
//public class InviteFriendActivity extends BaseActivity implements
//        IWeiboHandler.Response {
//
//    // 新浪
//    public static final String APP_KEY = "645553196";
//    public static final String REDIRECT_URL = "http://www.quanliren.com";
//    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
//            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//            + "follow_app_official_microblog," + "invitation_write";
//    IWeiboShareAPI mWeiboShareAPI;
//
//    public static final String APP_ID = "1101960968"; // qq
//    //	public static final String WEIXIN_ID = "wx488920a29404152c";// 微信
//    public static final String WEIXIN_ID = "wx18fe5cdc33071d57";// 微信
//    Tencent mTencent;
//    QQAuth mQQAuth;
//    QQShare mQQShare = null;
//    IWXAPI api;
//    int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
//    private int mExtarFlag = 0x00;
//    @ViewById
//    View fx_sina_btn;
//    @ViewById
//    View fx_qq_btn;
//    @ViewById
//    View fx_msg_btn;
//    @ViewById
//    View fx_weixin_btn;
//    @ViewById
//    View fx_friend_btn;
//
//    String title;
//
//    String img;
//    String icon;
//    String url = "http://t.cn/RhyxVBy";
//    //	http://www.bjqlr.com/c.php
//    String content = "\"单身汪\"是一款基于地理位置的移动社交App。可查看附近帅哥美女的约会状态-约吃饭；约看电影；找游伴；找临时情侣等。成为会员可发布约会信息，寻找兴趣相投的小伙伴。成功约会将有机会获得狗粮，可使用狗粮兑换精美礼品。加入单身汪，马上进行一次神秘又浪漫的约会吧。下载地址：" + url;
//    File f;
//    File ficon;
//    /**
//     * 微博 Web 授权类，提供登陆等功能
//     */
//
//    private WeiboAuth mWeiboAuth;
//
//    /**
//     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
//     */
//    private Oauth2AccessToken mAccessToken;
//
//    /**
//     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
//     */
//    private SsoHandler mSsoHandler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        title = getResources().getString(R.string.app_name) + "分享";
//        img = "share.jpg";
//        icon = "icon_share.png";
//        setContentView(R.layout.fx);
//        getSupportActionBar().setTitle("邀请好友");
//
//        // qq
//        mQQAuth = QQAuth.createInstance(APP_ID, this.getApplicationContext());
//        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
//        mQQShare = new QQShare(this, mQQAuth.getQQToken());
//
//        // weixin
//        api = WXAPIFactory.createWXAPI(this, WEIXIN_ID, true);
//        api.registerApp(WEIXIN_ID);
//
//        mWeiboAuth = new WeiboAuth(this, APP_KEY, REDIRECT_URL, SCOPE);
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, APP_KEY);
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
//        }
//
////		if (mWeiboShareAPI.checkEnvironment(true)) {
////			if (i.compareAndSet(false, true))
////				mWeiboShareAPI.registerApp();
////		}
//
//        f = new File(StaticFactory.APKCardPath);
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//        f = new File(f, img.hashCode() + "");
////		if (!f.exists()) {
//        try {
//            InputStream i = getAssets().open(img);
//            OutputStream os = new FileOutputStream(f);
//            Util.CopyStream(i, os);
//            os.close();
//            i.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        }
////		}
//        File f1 = new File(StaticFactory.APKCardPath);
//        ficon = new File(f1, icon.hashCode() + "");
////		if (!ficon.exists()) {
//        try {
//            InputStream i = getAssets().open(icon);
//            OutputStream os = new FileOutputStream(ficon);
//            Util.CopyStream(i, os);
//            os.close();
//            i.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        }
////		}
//    }
//
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        mWeiboShareAPI.handleWeiboResponse(intent, this);
//    }
//    @Override
//    public void onResponse(BaseResponse baseResp) {
//        switch (baseResp.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                showCustomToast("分享成功");
//                break;
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                showCustomToast("取消分享");
//                break;
//            case WBConstants.ErrorCode.ERR_FAIL:
//                showCustomToast("分享失败，请再试一次");
//                break;
//        }
//    }
//
//    /**
//     * QQ分享
//     *
//     * @param v
//     */
//    @Click(R.id.fx_qq_btn)
//    public void shareToQQ(View v) {
//        PackageInfo packageInfo;
//        try {
//            packageInfo = getPackageManager().getPackageInfo(
//                    "com.tencent.mobileqq", 0);
//        } catch (NameNotFoundException e) {
//            packageInfo = null;
//            e.printStackTrace();
//        }
//        if (packageInfo == null) {
//            Toast.makeText(this, "未安装QQ", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
//        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
//        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, ficon.getPath());
//        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,
//                content);
//        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getResources().getString(R.string.app_name));
//
//        doShareToQQ(bundle);
//    }
//
//    /**
//     * 用异步方式启动分享
//     *
//     * @param params
//     */
//    private void doShareToQQ(final Bundle params) {
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                mQQShare.shareToQQ(InviteFriendActivity.this, params,
//                        new IUiListener() {
//
//                            @Override
//                            public void onCancel() {
//                                if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
//                                    runOnUiThread(new Runnable() {
//                                        public void run() {
//                                            showCustomToast("取消分享");
//                                        }
//                                    });
//
//                                }
//                            }
//
//                            @Override
//                            public void onComplete(final Object response) {
//                                // TODO Auto-generated method stub
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        showCustomToast("分享成功");
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onError(final UiError e) {
//                                // TODO Auto-generated method stub
//
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        showCustomToast("分享失败");
//                                    }
//                                });
//                            }
//
//                        });
//            }
//        }).start();
//    }
//
//    @Click({R.id.fx_friend_btn, R.id.fx_weixin_btn})
//    public void shareToWeiXin(View v) {
//        if (!api.openWXApp()) {
//            Toast.makeText(this, "未安装微信", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = url;
//
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = title;
//        msg.description = content;
//        try {
//            Bitmap thumb = null;
//            AssetManager am = getResources().getAssets();
//            try {
//                InputStream is = am.open("icon_share.png");
//                thumb = BitmapFactory.decodeStream(is);
//                is.close();
////            Bitmap thumb = BitmapFactory.decodeResource(getResources(),
////                    R.drawable.icon_share);
////            msg.thumbData = bmpToByteArray(thumb, true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            msg.thumbData = bmpToByteArray(thumb, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        switch (v.getId()) {
//            case R.id.fx_weixin_btn:
//                req.scene = SendMessageToWX.Req.WXSceneSession;
//                break;
//            case R.id.fx_friend_btn:
//                req.scene = SendMessageToWX.Req.WXSceneTimeline;
//                break;
//        }
//        api.sendReq(req);
//
//
//    }
//    public static byte[] bmpToByteArray(final Bitmap bmp,
//                                        final boolean needRecycle) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        bmp.compress(CompressFormat.PNG, 100, output);
//        if (needRecycle) {
//            bmp.recycle();
//        }
//
//        byte[] result = output.toByteArray();
//        try {
//            output.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    private static final int THUMB_SIZE = 50;
//
//    public static String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis())
//                : type + System.currentTimeMillis();
//    }
//
//    AtomicBoolean i = new AtomicBoolean(false);
//
//    @Click(R.id.fx_sina_btn)
//    public void shareToSina(View v) {
//        if (mWeiboShareAPI.checkEnvironment(true)) {
//            if (i.compareAndSet(false, true))
//                mWeiboShareAPI.registerApp();
//            sendMultiMessage(true, false, false, false, false, false);
//        }
//    }
//
//    /**
//     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
//     * {@link com.sina.weibo.sdk.auth.sso.SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
//     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
//     * SharedPreferences 中。
//     */
//    /*class AuthListener implements WeiboAuthListener {
//		@Override
//		public void onComplete(Bundle values) {
//			// 从 Bundle 中解析 Token
//			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//			if (mAccessToken.isSessionValid()) {
//				// 保存 Token 到 SharedPreferences
//				AccessTokenKeeper.writeAccessToken(InviteFriendActivity.this,
//						mAccessToken);
//				sendMultiMessage(true, true, false, false, false, false);
//			} else {
//				// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
//				String code = values.getString("code");
//				String message = "";
//				if (!TextUtils.isEmpty(code)) {
//					message = message + "\nObtained the code: " + code;
//				}
//				Toast.makeText(InviteFriendActivity.this, message,
//						Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onCancel() {
//			Toast.makeText(InviteFriendActivity.this, "取消", Toast.LENGTH_LONG)
//					.show();
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			Toast.makeText(InviteFriendActivity.this,
//					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
//					.show();
//		}
//	}*/
//    private TextObject getTextObj() {
//        TextObject textObject = new TextObject();
//        textObject.text = content;
//        return textObject;
//    }
//
//    private void sendMultiMessage(boolean hasText, boolean hasImage,
//                                  boolean hasWebpage, boolean hasMusic, boolean hasVideo,
//                                  boolean hasVoice) {
//        // 1. 初始化微博的分享消息
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        if (hasText) {
//            weiboMessage.textObject = getTextObj();
////			weiboMessage.imageObject = getImageObj();
//        }
//        // 2. 初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = weiboMessage;
//        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.sendRequest(request);
//    }
//
//	/*private ImageObject getImageObj() {
//		ImageObject imageObject = new ImageObject();
//		imageObject.setImageObject(BitmapFactory.decodeFile(f.getSwift Path()));
//		return imageObject;
//	}*/
//
//    // 短信分享
//    @Click(R.id.fx_msg_btn)
//    public void shareToMsg(View v) {
//        Uri smsToUri = Uri.parse("smsto:");
//        Intent mIntent = new Intent(Intent.ACTION_SENDTO,
//                smsToUri);
//        mIntent.putExtra("sms_body", content);
//        startActivity(mIntent);
//    }
//
//}

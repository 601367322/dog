package com.quanliren.quan_two.picturecrop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.PhotoAlbumMainActivity_;
import com.quanliren.quan_two.activity.group.PublishDongTaiActivity;
import com.quanliren.quan_two.activity.location.GDLocation;
import com.quanliren.quan_two.activity.location.ILocationImpl;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.custom.SquareLayout;
import com.quanliren.quan_two.custom.emoji.EmoticonsTextView;
import com.quanliren.quan_two.picturecrop.view.ClipImageLayout;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;


/**
 * 切图页面
 *
 * @author JiangPing
 */
@EActivity(R.layout.activity_picture_crop)
@OptionsMenu(R.menu.filter_people_menu)
public class PictureCropActivity extends BaseActivity implements ILocationImpl, ViewTreeObserver.OnGlobalLayoutListener {

    private static final int CHOOSE_PICTURE_REQCODE = 0;// 相册选择照片
    private static final int TAKE_PICTURE_REQCODE = 1;// 拍照
    private Bitmap mBitmap;
    @ViewById(R.id.id_clipImageLayout)
    ClipImageLayout mClipImageLayout;
    @ViewById
    SquareLayout etv_rl;
    @ViewById
    EmoticonsTextView tv_content;
    GDLocation location;

    @Extra
    String mPicPathStr;
    @Extra
    String mContent;

    @Override
    public void init() {
        super.init();
        location = new GDLocation(this, this, false);
        getSupportActionBar().setTitle("发表");
        initView();
    }

    @Click
    void tv_content() {
        finish();
    }

    void initView() {
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(mContent);
        }
        //图片路径不为空
        if (mPicPathStr != null && !TextUtils.isEmpty(mPicPathStr)) {
            mBitmap = ImageLoader.getInstance().loadImageSync(Util.FILE + mPicPathStr);
        } else {
            upload();
        }
        //图片放入布局中去
        mClipImageLayout.setClipImage(mBitmap);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mClipImageLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mClipImageLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    boolean once = true;

    @Override
    public void onGlobalLayout() {
        if (once) {
            int top = (mClipImageLayout.getHeight() - mClipImageLayout.getWidth()) / 4;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.topMargin = top;
            etv_rl.setLayoutParams(lp);
            once=false;
        }
    }

    @OptionsItem(R.id.ok)
    public void ok() {
        saveCropPictureToSDCard();
    }

    @Click
    void ablm() {
        PhotoAlbumMainActivity_
                .intent(this)
                .maxnum(1)
                .startForResult(CHOOSE_PICTURE_REQCODE);
    }


    int font_index = 0;

    @Click
    void font_select() {
        if (mBitmap == null) {
            return;
        }
        if (font_index == 0) {
            font_index = 1;
            tv_content.setTextColor(getResources().getColor(R.color.black));
            tv_content.setShadowLayer(1, 1, 1, getResources().getColor(R.color.white));
        } else {
            font_index = 0;
            tv_content.setTextColor(getResources().getColor(R.color.white));
            tv_content.setShadowLayer(1, 1, 1, getResources().getColor(R.color.black));
        }
    }


    String imageUri;

    @Click
    void take_picture() {
        if (Util.existSDcard()) {
            Intent intent = new Intent(); // 调用照相机
            String messagepath = StaticFactory.APKCardPath;
            File fa = new File(messagepath);
            if (!fa.exists()) {
                fa.mkdirs();
            }
            imageUri = messagepath + new Date().getTime();// 图片路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(imageUri)));
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PICTURE_REQCODE);
        } else {
            showCustomToast("亲，请检查是否安装存储卡!");
        }
    }

    String cutedUri = "";

    private void saveCropPictureToSDCard() {
        if (mBitmap != null) {
            Bitmap bitmap = mClipImageLayout.clip();
            String messagepath = StaticFactory.APKCardPath;
            cutedUri = messagepath + new Date().getTime();// 图片路径
            File file = new File(messagepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                FileOutputStream fos = new FileOutputStream(cutedUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        ImageUtil.downsize(cutedUri, cutedUri, this);
        upload();

    }

    void upload() {
        if (!Util.isStrNotNull(mContent) && TextUtils.isEmpty(cutedUri)) {
            showCustomToast("请输入内容或添加图片");
            return;
        }
        customShowDialog("正在定位");
        location.startLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 相册选择图片完成回调
            case CHOOSE_PICTURE_REQCODE:
                if (data == null) {
                    return;
                }
                ArrayList<String> list = data.getStringArrayListExtra("images");
                if (list.size() > 0) {
                    imageUri = list.get(0);
                    File fi = new File(imageUri);
                    if (fi != null && fi.exists()) {
                        String messagepath = StaticFactory.APKCardPath;
                        File fa = new File(messagepath);
                        if (!fa.exists()) {
                            fa.mkdirs();
                        }
//                        String newCameraUserLogo = messagepath + new Date().getTime();// 图片路径
                        ImageUtil.downsize(imageUri, imageUri, this);
                        //图片路径不为空
                        if (!TextUtils.isEmpty(Util.FILE + imageUri)) {
                            mBitmap = ImageLoader.getInstance().loadImageSync(Util.FILE + imageUri);
                        }
                        //图片放入布局中去
                        mClipImageLayout.setClipImage(mBitmap);
                        Intent intent = new Intent(PublishDongTaiActivity.TAG);
                        intent.putExtra("selectimgUri", imageUri);
                        sendBroadcast(intent);
                    }
                }
                break;
            // 拍照返回
            case TAKE_PICTURE_REQCODE:
                try {
                    if (imageUri != null) {
                        File fi = new File(imageUri);
                        if (fi != null && fi.exists()) {
                            ImageUtil.downsize(imageUri, imageUri, this);
                            //图片路径不为空
                            if (!TextUtils.isEmpty(Util.FILE + imageUri)) {
                                mBitmap = ImageLoader.getInstance().loadImageSync(Util.FILE + imageUri);
                            }
                            //图片放入布局中去
                            mClipImageLayout.setClipImage(mBitmap);
                            Intent intent = new Intent(PublishDongTaiActivity.TAG);
                            intent.putExtra("selectimgUri", imageUri);
                            sendBroadcast(intent);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }


    }

    @Override
    public void onLocationSuccess() {
        customDismissDialog();
        LoginUser user = getHelper().getUser();

        if (user != null) {
            RequestParams ap = getAjaxParams();
            ap.put("content", mContent);
            ap.put("area", ac.cs.getArea());
            ap.put("cityid", ac.cs.getLocationID());
            ap.put("longitude", ac.cs.getLng());
            ap.put("latitude", ac.cs.getLat());
            ap.put("fontColor", font_index);
            ac.finalHttp.post(URL.PUBLISH_TXT, ap, callBack);
        }
    }

    @Override
    public void onLocationFail() {
        showCustomToast("定位失败");
    }

    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog("正在上传");
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
                        if (!TextUtils.isEmpty(cutedUri)) {
                            dyid = jo.getJSONObject(URL.RESPONSE).getString("dyid");
                            uploadImg();
                        } else {
                            Intent intent = new Intent(PublishDongTaiActivity.EXIT);
                            sendBroadcast(intent);
                            showCustomToast("发表成功");
                            setResult(RESULT_OK);
                            finish();
                        }
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
    private String dyid;

    public void uploadImg() {
        LoginUser user = getHelper().getUser();
        try {
            RequestParams ap = getAjaxParams();
            ap.put("file", new File(cutedUri));
            ap.put("userid", user.getId());
            ap.put("dyid", dyid);
            ap.put("position", "0");
            ac.finalHttp.post(URL.PUBLISH_IMG, ap, new imgcallBack());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    class imgcallBack extends JsonHttpResponseHandler {
        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        ;

        public void onStart() {
            customShowDialog("正在上传图片");
        }

        ;

        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        Intent intent = new Intent(PublishDongTaiActivity.EXIT);
                        sendBroadcast(intent);
                        showCustomToast("上传成功");
                        setResult(1);
                        finish();
                        break;
                    default:
                        showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                                URL.INFO));
                        RequestParams aap = getAjaxParams();
                        aap.put("dyid", dyid);
                        ac.finalHttp.post(URL.DEL_DONGTAI, aap, new JsonHttpResponseHandler());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    }
}

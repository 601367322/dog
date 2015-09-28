package com.quanliren.quan_two.activity.group;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.location.GDLocation;
import com.quanliren.quan_two.activity.location.ILocationImpl;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.custom.CustomRelativeLayout;
import com.quanliren.quan_two.custom.emoji.EmoteView;
import com.quanliren.quan_two.custom.emoji.EmoticonsEditText;
import com.quanliren.quan_two.picturecrop.PictureCropActivity_;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

@EActivity(R.layout.publish_dongtai)
@OptionsMenu(R.menu.reg_first_menu)
public class PublishDongTaiActivity extends BaseActivity implements ILocationImpl {
    public static final String TAG = "com.quanliren.quan_two.activity.group.PublishDongTaiActivity";
    public static final String EXIT = "com.quanliren.quan_two.activity.group.PublishDongTaiActivity.Exit";
    GDLocation location;
    @ViewById
    ImageView imageView;
    @ViewById
    EmoticonsEditText et_content;
    @ViewById
    CustomRelativeLayout rlayout;
    @ViewById
    View chat_layout_emote;
    @FragmentById(R.id.chat_eiv_inputview)
    EmoteView gridview;

    @Override
    public void init() {
        super.init();
        ImageLoader.getInstance().stop();
        getSupportActionBar().setTitle("发表");
        gridview.setEditText(et_content);
        location = new GDLocation(this, this, false);
        et_content.addTextChangedListener(etTW);
    }

    @Click
    void choose_picture() {
        PhotoAlbumMainActivity_
                .intent(this)
                .maxnum(1)
                .startForResult(CHOOSE_PICTURE_REQCODE);
    }
    String content;
    @OptionsItem(R.id.next)
    public void next() {
        content = et_content.getText().toString().trim();
        if (!TextUtils.isEmpty(selectImage)) {
            PictureCropActivity_.intent(this).mContent(content).mPicPathStr(selectImage).start();
        }else if(TextUtils.isEmpty(selectImage)){
            if(!TextUtils.isEmpty(content)){
                customShowDialog("正在定位");
                location.startLocation();
            }else{
                showCustomToast("请输入内容或添加图片");
                return;
            }
        }
    }
    @Override
    public void onLocationSuccess() {
        customDismissDialog();
        LoginUser user = getHelper().getUser();

        if (user != null) {
            RequestParams ap = getAjaxParams();
            ap.put("content", content);
            ap.put("area", ac.cs.getArea());
            ap.put("cityid", ac.cs.getLocationID());
            ap.put("longitude", ac.cs.getLng());
            ap.put("latitude", ac.cs.getLat());
            ap.put("fontColor", 0);
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
                        showCustomToast("发表成功");
                        setResult(RESULT_OK);
                        finish();
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

    @Receiver(actions = {TAG, EXIT})
    public void receiver(Intent i) {
        try {
            String action = i.getAction();
            if (action.equals(TAG)) {
                selectImage = i.getExtras().getString("selectimgUri");
                ImageLoader.getInstance().displayImage(Util.FILE + selectImage, imageView);
            } else if (action.equals(EXIT)) {
                setResult(RESULT_OK);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private static final int CHOOSE_PICTURE_REQCODE = 0;// 相册选择照片
    private static final int TAKE_PICTURE_REQCODE = 1;// 拍照
    String imageUri;

    String selectImage;

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
                        selectImage = imageUri;
//                        ImageLoader.getInstance().displayImage(Util.FILE + newCameraUserLogo, imageView);
                        ImageLoader.getInstance().displayImage(Util.FILE + imageUri, imageView);
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
                            selectImage = imageUri;
                            ImageLoader.getInstance().displayImage(Util.FILE + imageUri, imageView);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }


    }

    @Click
    void add_emoji_btn() {
        if (chat_layout_emote.getVisibility() == View.GONE) {
            chat_layout_emote.setVisibility(View.VISIBLE);
            Util.closeInput(this);
        }else{
            chat_layout_emote.setVisibility(View.GONE);
        }
    }

    public void onBackPressed() {
        if (chat_layout_emote.getVisibility() == View.VISIBLE) {
            et_content.requestFocus();
            chat_layout_emote.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
    TextWatcher etTW = new TextWatcher() {
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
            if (s.toString().length() >100) {
                showCustomToast("已超过规定字数");
            }
        }
    };
}

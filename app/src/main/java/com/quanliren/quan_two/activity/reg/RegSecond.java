package com.quanliren.quan_two.activity.reg;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.PropertiesActivity_;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.PhotoAlbumMainActivity_;
import com.quanliren.quan_two.activity.user.LoginActivity_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.MoreLoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.custom.CircleImageView;
import com.quanliren.quan_two.custom.RoundProgressBar;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.share.CommonShared;
import com.quanliren.quan_two.util.AndroidBug5497Workaround;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

@EActivity(R.layout.reg_second)
@OptionsMenu(R.menu.filter_people_menu)
public class RegSecond extends BaseActivity {

    @Extra
    String phone;
    @ViewById
    TextView age;
    @ViewById
    ImageView mHeadImg;
    @ViewById
    View scroll_ll;
    @ViewById
    EditText password;
    @ViewById
    EditText nickname;
    @ViewById
    RadioButton girl_boy, boy_girl;
    @ViewById
    RadioGroup sex_btn;
    @ViewById
    public ScrollView scrol;
    @ViewById
    CircleImageView upload_userlogo;
    @ViewById
    RoundProgressBar loadProgressBar;

    @OrmLiteDao(helper = DBHelper.class, model = MoreLoginUser.class)
    public Dao<MoreLoginUser, Integer> moreLoginUserDao;

    @Override
    public void init() {
        super.init();
        setSwipeBackEnable(false);
        if (Util.getSDK() >= Build.VERSION_CODES.KITKAT) {
            scrol.setPadding(0, Util.getStatusBarHeight(this), 0, 0);
        }
        AndroidBug5497Workaround.assistActivity(this);
        initView();
        setListener();
    }

    @Override
    public boolean needAddParent() {
        return false;
    }

    protected void initView() {
        mTintManager.setStatusBarDarkMode(true, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("输入信息(3/3)");
        setSupportActionBar(toolbar);
    }

    public void setListener() {
    }

    @OptionsItem(R.id.ok)
    public void rightClick() {
        String str_nickname = nickname.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        String str_age = age.getText().toString().trim();

        int sex = 8;
        int sexButtonId = sex_btn.getCheckedRadioButtonId();
        switch (sexButtonId) {
            case R.id.boy_girl:
                sex = 0;
                break;
            case R.id.girl_boy:
                sex = 1;
                break;
        }
        if (str_nickname.length() == 0) {
            showCustomToast("请输入昵称");
            nickname.requestFocus();
            return;
        } else if (str_age.length() == 0) {
            showCustomToast("请选择出生日期");
            age.requestFocus();
            return;
        } else if (sex == 8) {
            showCustomToast("请选择性别");
            return;
        } else if (str_password.length() > 16 || str_password.length() < 6) {
            showCustomToast("密码长度为6-16个字符");
            password.requestFocus();
            return;
        } else if (!str_password.matches("^[a-zA-Z0-9 -]+$")) {
            showCustomToast("密码中不能包含特殊字符");
            password.requestFocus();
            return;
        }
        if (fi == null) {
            showCustomToast("请上传头像");
            return;
        }

        CommonShared cs = new CommonShared(getApplicationContext());

        RequestParams ap = getAjaxParams();
        ap.put("mobile", phone);

        ap.put("nickname", str_nickname);
        ap.put("pwd", str_password);
        ap.put("birthday", str_age);
        ap.put("userstate", 1);
        ap.put("sex", sex);
        ap.put("cityid", String.valueOf(cs.getLocationID()));
        ap.put("longitude", ac.cs.getLng());
        ap.put("latitude", ac.cs.getLat());
        User lou = new User();
        lou.setMobile(phone);
        lou.setPwd(str_password);
        ac.finalHttp.post(URL.REG_THIRD, ap, new callBack(lou));
    }

    User user;

    class callBack extends JsonHttpResponseHandler {
        User u;

        public callBack(User u) {
            this.u = u;
        }

        public void onStart() {
            customShowDialog(1);
        }

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:

                        moreLoginUserDao.delete(moreLoginUserDao.deleteBuilder()
                                .where().eq("username", u.getMobile())
                                .prepareDelete());
                        moreLoginUserDao.create(new MoreLoginUser(u.getMobile(), u
                                .getPwd()));

                        user = new Gson().fromJson(jo.getString(URL.RESPONSE),
                                User.class);
                        LoginUser lu = new LoginUser(user.getId(), u.getMobile(),
                                u.getPwd(), user.getToken());

                        // 保存用户
                        userTableDao.deleteById(user.getId());

                        userTableDao.create(new UserTable(user));
                        // 保存登陆用户
                        TableUtils.clearTable(getConnectionSource(),
                                LoginUser.class);
                        loginUserDao.create(lu);
                        ac.startServices();
                        if (fi != null && !"".equals(fi.toString())) {
                            uploadUserLogo(fi);
                        }
                        break;
                    default:
                        showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                                URL.INFO));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onBackPressed() {
        dialogFinish();
    }

    @Override
    public void finishActivity() {
        dialogFinish();
    }

    public void dialogFinish() {
        new android.support.v7.app.AlertDialog.Builder(RegSecond.this).setTitle("提示")
                .setMessage("您确定要放弃本次注册吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        scrollToFinishActivity();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).create().show();
    }


    private static final int CAMERA_USERLOGO = 1;
    private static final int ALBUM_USERLOGO = 2;
    private static final int USERLOGO = 1;
    private int uploadImgType;
    String cameraUserLogo;

    @Click
    void upload_userlogo() {
        uploadImgType = USERLOGO;
        uploadImg();
    }

    public void uploadImg() {
        AlertDialog dialog = new AlertDialog.Builder(this).setItems(
                new String[]{"相册", "拍照"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 1:
                                if (Util.existSDcard()) {
                                    Intent intent = new Intent(); // 调用照相机
                                    String messagepath = StaticFactory.APKCardPath;
                                    File fa = new File(messagepath);
                                    if (!fa.exists()) {
                                        fa.mkdirs();
                                    }
                                    cameraUserLogo = messagepath
                                            + new Date().getTime();// 图片路径
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(cameraUserLogo)));
                                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, CAMERA_USERLOGO);
                                } else {
                                    showCustomToast("亲，请检查是否安装存储卡!");
                                }
                                break;
                            case 0:
                                if (Util.existSDcard()) {
                                    String messagepath = StaticFactory.APKCardPath;
                                    File fa = new File(messagepath);
                                    if (!fa.exists()) {
                                        fa.mkdirs();
                                    }
                                    switch (uploadImgType) {
                                        default:
                                            PhotoAlbumMainActivity_
                                                    .intent(RegSecond.this)
                                                    .maxnum(1)
                                                    .startForResult(ALBUM_USERLOGO);
                                            break;
                                    }
                                } else {
                                    showCustomToast("亲，请检查是否安装存储卡!");
                                }
                                break;
                        }
                    }
                }).create();
        switch (uploadImgType) {
            case USERLOGO:
                dialog.setTitle("上传头像");
            default:
                break;
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    File fi;
    @OnActivityResult(CAMERA_USERLOGO)
    void onCameraUserLogoResult(int result, Intent data) {
        try {
            if (cameraUserLogo != null) {
                fi = new File(cameraUserLogo);
                if (fi != null && fi.exists()) {
                    ImageUtil.downsize(cameraUserLogo, cameraUserLogo, this);
                    switch (uploadImgType) {
                        case USERLOGO:
                            ImageLoader.getInstance().displayImage(
                                    Util.FILE + cameraUserLogo, upload_userlogo);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void uploadUserLogo(final File file) {
        try {
            RequestParams ap = getAjaxParams();
            ap.put("file", file);
            ac.finalHttp.post(URL.UPLOAD_USER_LOGO, ap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            // int progress = (int) (((float) bytesWritten /
                            // (float) totalSize) * 100);
                            if (bytesWritten > 0 && bytesWritten < totalSize) {
                                loadProgressBar.setVisibility(View.VISIBLE);
                                loadProgressBar.setMax((int) totalSize);
                                loadProgressBar.setProgress((int) bytesWritten);
                            }
                        }

                        @Override
                        public void onSuccess(JSONObject response) {
                            showCustomToast("注册成功");
                            try {
                                ImageLoader.getInstance().displayImage(
                                        response.getJSONObject(URL.RESPONSE)
                                                .getString("imgurl")
                                                + StaticFactory._320x320,
                                        upload_userlogo, ac.options_no_default);
                                user.setAvatar(response.getJSONObject(
                                        URL.RESPONSE).getString("imgurl"));
                                UserTable ut = new UserTable(user);
                                userTableDao.delete(ut);
                                userTableDao.create(ut);

                                AM.getActivityManager().popActivity(
                                        LoginActivity_.class.getName());
                                Intent intent = new Intent(RegSecond.this, PropertiesActivity_.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } finally {
                                loadProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure() {
                            android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(
                                    RegSecond.this)
                                    .setTitle("提示")
                                    .setMessage("上传失败，是否重试？")
                                    .setNegativeButton(
                                            "取消",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {

                                                }
                                            })
                                    .setPositiveButton(
                                            "确定",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    uploadUserLogo(file);
                                                }
                                            }).create();
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        }
                    });
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnActivityResult(ALBUM_USERLOGO)
    void onAlbumUserLogoResult(int result, Intent data) {
        if (data == null) {
            return;
        }
        ArrayList<String> list = data.getStringArrayListExtra("images");
        if (list.size() > 0) {
            cameraUserLogo = list.get(0);
            fi = new File(cameraUserLogo);
            if (fi != null && fi.exists()) {
                ImageUtil.downsize(cameraUserLogo, cameraUserLogo, this);
                switch (uploadImgType) {

                    case USERLOGO:
                        ImageLoader.getInstance().displayImage(
                                Util.FILE + cameraUserLogo, upload_userlogo);
                        break;
                }
            }
        }
    }


}

package com.quanliren.quan_two.activity.bindnum;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.ContactsC;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@EActivity
@OptionsMenu(R.menu.bind_cancel_menu)
public class LookContactState extends BaseActivity {


    @ViewById
    View ll_bind;
    @ViewById
    View ll_upload;
    @ViewById
    View ll_look;
    @ViewById
    TextView phone_upload;
    @ViewById
    TextView phone_look;

    @OptionsMenuItem
    MenuItem next;
    String mobile;
    @Extra
    int isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_contact_state);
        getSupportActionBar().setTitle("绑定手机号");
        initView();
        /**得到手机通讯录联系人信息**/
        getPhoneContacts();
    }

    void initView() {
        if (next != null) {
            next.setVisible(false);
        }
        ll_look.setVisibility(View.GONE);
        ll_bind.setVisibility(View.GONE);
        ll_upload.setVisibility(View.GONE);
    }

    @OptionsItem
    void next() {
        new AlertDialog.Builder(LookContactState.this)
                .setTitle("提示")
                .setMessage("解绑后，单身汪将不能为你找到手机通讯录里的单身汪朋友，服务器上属于你的通讯录数据也将被删除。")
                .setPositiveButton("解绑",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("mobile", mobile);
                                if (mobile != null && !"".equals(mobile)) {
                                    ac.finalHttp.post(URL.CANCLE_BIND, ap, cancle_callBack);
                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface arg0, int arg1) {
                            }
                        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        next.setVisible(false);
        ac.finalHttp.post(URL.LOOK_STATE, getAjaxParams(), callBack);
        return super.onCreateOptionsMenu(menu);
    }

    @Click(R.id.look_contacts)
    public void lookcontacts(View view) {
        BindActivity_.intent(this).start();
    }

    @Click(R.id.bind_phone)
    public void bindphone(View view) {
        BindFirst_.intent(this).start();
    }

    @Click(R.id.upload_contacts)
    public void uploadcontacts(View view) {
        uploadContact();
    }

    MyJsonHttpResponseHandler cancle_callBack = new MyJsonHttpResponseHandler(LookContactState.this, "正在获取数据") {
        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {
            initView();
            ll_bind.setVisibility(View.VISIBLE);
        }

        ;
    };
    MyJsonHttpResponseHandler callBack = new MyJsonHttpResponseHandler(LookContactState.this, "正在获取数据") {
        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {

            JSONObject jb = new JSONObject(jo.getString(URL.RESPONSE));
            int result = jb.getInt("result");
            mobile = jb.getString("mobile");
            ll_look.setVisibility(View.GONE);
            ll_bind.setVisibility(View.GONE);
            ll_upload.setVisibility(View.GONE);
            next.setVisible(false);
            if (next != null) {
                next.setVisible(false);
            }


            if (result == 0) {
                ll_bind.setVisibility(View.VISIBLE);

            } else if (result == 1) {
                ll_upload.setVisibility(View.VISIBLE);
                phone_upload.setText("你的手机号:" + mobile);
                next.setVisible(true);

            } else if (result == 2) {
                ll_look.setVisibility(View.VISIBLE);
                phone_look.setText("你的手机号:" + mobile);
                next.setVisible(true);

            }

        }

        ;
    };

    public void uploadContact() {
        new AlertDialog.Builder(LookContactState.this)
                .setTitle("启用手机通讯录匹配")
                .setMessage("看看手机通讯录里谁在使用单身汪？")
                .setPositiveButton("是",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (json != null && !"".equals(json)) {
                                    ac.finalHttp.post(URL.SYNC_CONTACTS, getAjaxParams("allMileList", json), contact_callBack);
                                } else {
                                    showCustomToast("您的通讯录为空，不能上传哦");
                                }

                            }
                        })
                .setNegativeButton("否",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface arg0, int arg1) {

                            }
                        }).create().show();
    }

    MyJsonHttpResponseHandler contact_callBack = new MyJsonHttpResponseHandler(LookContactState.this, "数据同步中，请稍后...") {
        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {

            next.setVisible(true);
            ll_bind.setVisibility(View.GONE);
            ll_upload.setVisibility(View.GONE);
            ll_look.setVisibility(View.VISIBLE);
            phone_look.setText("你的手机号:" + mobile);
        }
        ;
    };

    @Receiver(actions = BindSecond.TAG)
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(BindSecond.TAG)) {
            if (!"".equals(i.getStringExtra("mobile")) && i.getStringExtra("mobile") != null) {
                mobile = i.getStringExtra("mobile");
                next.setVisible(true);
                ll_bind.setVisibility(View.GONE);
                ll_look.setVisibility(View.GONE);
                ll_upload.setVisibility(View.VISIBLE);
                phone_upload.setText("你的手机号:" + mobile);
            }
            uploadContact();
        }
    }

    String json;
    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    /**
     * 联系人信息
     **/
    private List<ContactsC> mContactsList = new ArrayList<ContactsC>();


    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = LookContactState.this.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        ContactsC cc;
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                phoneNumber = dealPhone(phoneNumber);
                if (Util.isMobileNO(phoneNumber)) {
                    cc = new ContactsC(contactName, phoneNumber);
                    mContactsList.add(cc);
                }
            }
            phoneCursor.close();
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);//取得相关系统服务
            if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                if (mContactsList != null&&mContactsList.size()>0) {
                    json = new Gson().toJson(mContactsList);
                }

            } else {
                getSIMContacts();
            }

        }
    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private void getSIMContacts() {
        ContentResolver resolver = LookContactState.this.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        ContactsC cc;
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                //Sim卡中没有联系人头像

                phoneNumber = dealPhone(phoneNumber);
                if (Util.isMobileNO(phoneNumber)) {
                    cc = new ContactsC(contactName, phoneNumber);
                    mContactsList.add(cc);
                }
            }
            if (mContactsList != null&&mContactsList.size()>0) {
                json = new Gson().toJson(mContactsList);
            }
            phoneCursor.close();
        }
    }

    String dealPhone(String phoneStr) {
        String phoneNumber = phoneStr.trim().replace(" ", "");
        phoneNumber = phoneNumber.replace("+86", "");
        if(phoneNumber.startsWith("86")){
            phoneNumber=phoneNumber.substring(2);
        }
        return phoneNumber;
    }

    ;
}

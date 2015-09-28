package com.quanliren.quan_two.activity.bindnum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity
@OptionsMenu(R.menu.reg_first_menu)
public class BindFirst extends BaseActivity {

    @ViewById
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_first);
        getSupportActionBar().setTitle("绑定手机号");
    }

    @OptionsItem
    void next() {
        String pstr = phone.getText().toString();
        if (pstr.length() == 11) {
            ac.finalHttp.post(URL.PHONE__BIND_CORRECT, getAjaxParams("mobile", pstr), callBack);
        } else {
            showCustomToast("请输入正确的手机号码！");
            return;
        }
    }

    MyJsonHttpResponseHandler callBack = new MyJsonHttpResponseHandler(this, "正在获取数据") {
        @Override
        public void onSuccessRetCode(JSONObject jo) throws Throwable {
            if (!jo.isNull(URL.RESPONSE)) {
                jo = jo.getJSONObject(URL.RESPONSE);
                BindSecond_.intent(BindFirst.this).phone(phone.getText().toString()).code(jo.getString("authcode")).result(Integer.parseInt(jo.getString("result").trim())).start();
            } else {
                BindSecond_.intent(BindFirst.this).phone(phone.getText().toString()).start();
            }

        }
    };

    @Receiver(actions = BindSecond.TAG)
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(BindSecond.TAG)) {
            finish();
        }
    }
}

package com.quanliren.quan_two.activity.reg;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.seting.*;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity
@OptionsMenu(R.menu.reg_first_menu)
public class RegFirst extends BaseActivity {

    @ViewById
    TextView agreement2;
    @ViewById
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_first);
        getSupportActionBar().setTitle("输入手机号(1/3)");
//		this.setTitleRightTxt(R.string.next);
        agreement2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线

        agreement2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ServiceInfoActivity_.intent(RegFirst.this).start();
            }
        });
    }

    @OptionsItem
    void next() {
        String pstr = phone.getText().toString();
        if (Util.isMobileNO(pstr)) {
            ac.finalHttp.post(URL.REG_FIRST, getAjaxParams("mobile", pstr), callBack);
        } else {
            showCustomToast("请输入正确的手机号码！");
            return;
        }
    }

    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog(1);
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
                        if (!jo.isNull(URL.RESPONSE)) {
                            jo = jo.getJSONObject(URL.RESPONSE);
                            RegGetCode_.intent(RegFirst.this).phone(phone.getText().toString()).code(jo.getString("authcode")).start();
                        } else {
                            RegGetCode_.intent(RegFirst.this).phone(phone.getText().toString()).start();
                        }
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
}

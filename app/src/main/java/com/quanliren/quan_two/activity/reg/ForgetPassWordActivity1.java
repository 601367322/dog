package com.quanliren.quan_two.activity.reg;

import android.os.Bundle;
import android.widget.EditText;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
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
public class ForgetPassWordActivity1 extends BaseActivity {

    @ViewById
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_first);
        getSupportActionBar().setTitle(R.string.findpassword);
    }

    @OptionsItem(R.id.next)
    void rightClick() {
        String pstr = Util.FilterEmoji(phone.getText().toString());
        if (Util.isMobileNO(pstr)) {
            ac.finalHttp.post(URL.FINDPASSWORD_FIRST,
                    getAjaxParams("mobile", pstr), callBack);
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
                            ForgetPassWordActivity2_
                                    .intent(ForgetPassWordActivity1.this)
                                    .code(jo.getJSONObject(URL.RESPONSE).getString("authcode"))
                                    .phone(phone.getText().toString()).start();
                        } else {
                            ForgetPassWordActivity2_
                                    .intent(ForgetPassWordActivity1.this)
                                    .phone(phone.getText().toString()).start();
                        }
                        finish();
                        break;
                    default:
                        if(jo.getJSONObject(URL.RESPONSE).getString(
                                URL.INFO).equals("验证码已发送到您的手机,如未收到请稍后重新获取!")){
                            ForgetPassWordActivity2_
                                    .intent(ForgetPassWordActivity1.this)
                                    .phone(phone.getText().toString()).start();
                            finish();
                        }
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

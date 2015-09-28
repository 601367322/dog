package com.quanliren.quan_two.activity.reg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.PropertiesActivity_;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity
@OptionsMenu(R.menu.filter_people_menu)
public class ForgetPassWordActivity2 extends BaseActivity {

    @ViewById
    TextView phone_txt;
    @ViewById
    EditText password;
    @ViewById
    EditText confirm_password;
    @ViewById(R.id.code)
    EditText code_txt;
    @ViewById
    Button ok;
    @Extra
    String phone;
    @Extra
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_second);
        getSupportActionBar().setTitle(R.string.findpassword);

        phone_txt.setText(phone);
//        code_txt.setText(code);

    }

    @Receiver(actions = RegGetCode.GETCODE)
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(RegGetCode.GETCODE)) {
            String code = i.getExtras().getString("code");
            code_txt.setText(code);
        }
    }

    @OptionsItem(R.id.ok)
    public void confirm() {
        String str_password = password.getText().toString().trim();
        String str_confirm_password = confirm_password.getText().toString()
                .trim();
        String str_code = code_txt.getText().toString().trim();

        if (str_code.trim().length() != 6) {
            showCustomToast("请输入正确的验证码！");
            return;
        } else if (str_password.length() > 16 || str_password.length() < 6) {
            showCustomToast("密码长度为6-16个字符");
            return;
        } else if (!str_password.matches("^[a-zA-Z0-9 -]+$")) {
            showCustomToast("密码中不能包含特殊字符");
            return;
        } else if (!str_confirm_password.equals(str_password)) {
            showCustomToast("确认密码与密码不同");
            return;
        }

        RequestParams ap = getAjaxParams();
        ap.put("mobile", phone);
        ap.put("authcode", str_code);
        ap.put("pwd", str_password);
        ap.put("repwd", str_confirm_password);

        User lou = new User();
        lou.setMobile(phone);
        lou.setPwd(str_password);
        ac.finalHttp.post(URL.FINDPASSWORD_SECOND, ap, new callBack(lou));
    }

    class callBack extends JsonHttpResponseHandler {
        User u;

        public callBack(User u) {
            this.u = u;
        }

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
                        showCustomToast("修改成功");
                        finish();
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

}

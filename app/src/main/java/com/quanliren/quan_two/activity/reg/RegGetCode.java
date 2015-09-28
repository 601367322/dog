package com.quanliren.quan_two.activity.reg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity
@OptionsMenu(R.menu.reg_first_menu)
public class RegGetCode extends BaseActivity {

    public static final String GETCODE = "com.quanliren.quan_two.activity.reg.RegGetCode";

    @Extra
    String phone;
    @Extra
    String code;
    @ViewById
    TextView phone_txt;
    @ViewById(R.id.code)
    EditText code_edit;
    @ViewById
    Button resend_btn;
    int allSec = 3 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_getcode);

        getSupportActionBar().setTitle("输入验证码(2/3)");

        setListener();

    }

    @Receiver(actions = GETCODE)
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(GETCODE)) {
            String code = i.getExtras().getString("code");
            code_edit.setText(code);
        }
    }

    @OptionsItem(R.id.next)
    public void rightClick() {
        String codes = code_edit.getText().toString();
        if (codes.trim().length() != 6) {
            showCustomToast("请输入正确的验证码！");
            return;
        }
        RequestParams ap = getAjaxParams();
        ap.put("mobile", phone);
        ap.put("authcode", codes);
        ac.finalHttp.post(URL.REG_SENDCODE, ap, sendCodeCallBack);
    }

    @Click(R.id.resend_btn)
    public void resend(View v) {
        ac.finalHttp.post(URL.REG_FIRST, new RequestParams("mobile", phone), callBack);
    }

    public void setListener() {
        phone_txt.setText(phone);
//        code_edit.setText(code);
        resend_btn.setText("收到短信大约需要" + allSec + "秒");
        handler.postDelayed(runable, 1000);
    }

    Runnable runable = new Runnable() {

        @Override
        public void run() {
            allSec--;
            resend_btn.setText("收到短信大约需要" + allSec + "秒");
            if (allSec > 0) {
                handler.postDelayed(runable, 1000);
            } else {
                resend_btn.setText("重新获取验证码");
                resend_btn.setEnabled(true);
            }
        }
    };

    Handler handler = new Handler();

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
            showCustomToast("重新发送验证码成功！");
            try {
                int status = jo.getInt(URL.STATUS);
                jo = jo.getJSONObject(URL.RESPONSE);
                switch (status) {
                    case 0:
                        code = jo.getString("authcode");
                        allSec = 180;
                        resend_btn.setText("收到短信大约需要" + allSec + "秒");
                        resend_btn.setEnabled(false);
                        handler.postDelayed(runable, 1000);
                        break;
                    case 1:
                        String str = jo.getString(URL.INFO);
                        showCustomToast(str);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    JsonHttpResponseHandler sendCodeCallBack = new JsonHttpResponseHandler() {
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
                        RegSecond_.intent(RegGetCode.this).phone(phone).start();
                        finish();
                        break;
                    case 1:
                        showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(URL.INFO));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    @Override
    public void finishActivity() {
        dialogFinish();
    }

    ;

    public void onBackPressed() {
        dialogFinish();
    }

    ;

    public void dialogFinish() {
        new android.support.v7.app.AlertDialog.Builder(RegGetCode.this)
                .setTitle("提示")
                .setMessage("您确定要放弃本次注册吗？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                scrollToFinishActivity();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface arg0, int arg1) {
                            }
                        }).create().show();
    }
}

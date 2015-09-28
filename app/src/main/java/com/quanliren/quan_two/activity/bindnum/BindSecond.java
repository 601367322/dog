package com.quanliren.quan_two.activity.bindnum;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity
public class BindSecond extends BaseActivity {

    public static final String GETCODE = "com.quanliren.quan_two.activity.bindnum.BindSecond";
    public static final String TAG = "com.quanliren.quan_two.activity.bindnum.bindcontact";

    @Extra
    String phone;
    @Extra
    int result;
    @Extra
    String code;
    @ViewById
    TextView phone_txt;
    @ViewById(R.id.code)
    EditText code_edit;
    @ViewById
    Button resend_btn;
    @ViewById
    EditText pwd;
    @ViewById
    EditText repwd;
    @ViewById
    View pwd_ll;
    int allSec = 3 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_getcode);

        getSupportActionBar().setTitle("绑定手机号");
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

    @Click(R.id.next)
    public void commitClick() {
        String str_pwd = pwd.getText().toString().trim();
        String str_repwd = repwd.getText().toString().trim();
        if(result==0){
            if (str_pwd.length() > 16 || str_pwd.length() < 6) {
                showCustomToast("密码长度为6-16个字符");
                pwd.requestFocus();
                return;
            } else if (!str_pwd.matches("^[a-zA-Z0-9 -]+$")) {
                showCustomToast("密码中不能包含特殊字符");
                pwd.requestFocus();
                return;
            }else if(!str_repwd.equals(str_pwd)){
                showCustomToast("确认密码与密码不同");
                pwd.requestFocus();
                return;
            }
        }

        String codes = code_edit.getText().toString();
        if (codes.trim().length() != 6) {
            showCustomToast("请输入正确的验证码！");
            return;
        }
        RequestParams ap = getAjaxParams();
        ap.put("mobile", phone);
        ap.put("authcode", codes);
        if(result==0){
            ap.put("pwd",str_pwd);
            ap.put("repwd",str_repwd);
        }
        ac.finalHttp.post(URL.VERTIFY_SENDCODE, ap, sendCodeCallBack);
    }

    @Click(R.id.resend_btn)
    public void resend(View v) {
//        ac.finalHttp.post(URL.PHONE__BIND_CORRECT, new RequestParams("mobile", phone), callBack);
    }

    public void setListener() {
        pwd_ll.setVisibility(View.GONE);
        phone_txt.setText("+86 " + phone);
        resend_btn.setText("收到短信大约需要" + allSec + "秒");
        if(result==1){
            pwd_ll.setVisibility(View.GONE);
        }else{
            pwd_ll.setVisibility(View.VISIBLE);
        }
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
                        Intent intent = new Intent(TAG);
                        intent.putExtra("mobile",phone);
                        sendBroadcast(intent);
                        String result=jo.getJSONObject(URL.RESPONSE).getString("result");
                        if(result!=null&&"1".equals(result)){
                            showCustomToast("首次绑定手机号成功，狗粮+1");
                        }
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
        new AlertDialog.Builder(BindSecond.this)
                .setTitle("提示")
                .setMessage("验证码短信可能略有迟缓，确定返回并重新开始？")
                .setPositiveButton("返回",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                scrollToFinishActivity();
                            }
                        })
                .setNegativeButton("等待",
                        new DialogInterface.OnClickListener() {

                            public void onClick(
                                    DialogInterface arg0, int arg1) {
                            }
                        }).create().show();
    }
}

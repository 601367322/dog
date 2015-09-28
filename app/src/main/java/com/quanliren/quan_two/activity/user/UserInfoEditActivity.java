package com.quanliren.quan_two.activity.user;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.a.net.simonvt.numberpicker.NumberPicker;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;

@EActivity(R.layout.user_info_edit)
@OptionsMenu(R.menu.edit_info_menu)
public class UserInfoEditActivity extends BaseActivity {
    @ViewById
    EditText nickname;
    @ViewById
    TextView face;
    @ViewById
    TextView age;
    @ViewById
    TextView love;
    @ViewById
    TextView job;
    @ViewById
    TextView money;
    @ViewById
    EditText signature;

    User user;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getHelper().getUserInfo();
    }

    ;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSupportActionBar().setTitle("编辑");
    }

    @AfterViews
    void initView() {
        nickname.setText(user.getNickname());
        age.setText(user.getBirthday());
        face.setText(user.getAppearance());
        job.setText(user.getJob());
        money.setText(user.getIncome());
        love.setText(user.getEmotion());
        signature.setText(user.getSignature());
    }

    @OptionsItem
    void save() {
//        String str_nickname = Util.FilterEmoji(nickname.getText().toString().trim());
        String str_nickname = nickname.getText().toString().trim();
        String str_age = age.getText().toString().trim();
        String str_face = face.getText().toString().trim();
        String str_love = love.getText().toString().trim();
        String str_work = job.getText().toString().trim();
        String str_money = money.getText().toString().trim();
//        String str_signature = Util.FilterEmoji(signature.getText().toString().trim());
        String str_signature = signature.getText().toString().trim();

        if (str_nickname.length() == 0) {
            showCustomToast("请输入昵称");
            return;
        } else if (str_age.length() == 0) {
            showCustomToast("请选择出生日期");
            return;
        }

        RequestParams ap = getAjaxParams();
        ap.put("nickname", str_nickname);
        ap.put("birthday", str_age);
        ap.put("appearance", str_face);
        ap.put("emotion", Arrays.asList(loves).indexOf(str_love));
        ap.put("signature", str_signature);
        ap.put("income", Arrays.asList(moneys).indexOf(str_money));
        ap.put("job", Arrays.asList(ol).indexOf(str_work));
        LogUtil.d("=============string",ap.toString());
        ac.finalHttp.post(URL.EDIT_USER_INFO, ap, editInfoCallBack);
    }


    JsonHttpResponseHandler editInfoCallBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog("正在更新信息");
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
                        showCustomToast("保存成功");
                        setResult(1);
                        finish();
                        break;
                    case -2:
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

        ;
    };

    String[] height = new String[72];
    String[] weight = new String[72];
    String[] boy = { "清新俊秀","气宇轩昂", "高大威猛", "文质彬彬", "血性精悍", "儒雅风趣"};
    String[] girl = {"活泼可爱", "温柔可人", "娴静端庄", "秀外慧中", "妩媚妖艳", "火爆性感"};
    String[] body=null;
    @Click
    void face() {
        for (int i = 0; i < 71; i++) {
            height[i] = 130 + i + "cm";
        }
        height[71] = 200 + "cm+";
        for (int i = 0; i < 71; i++) {
            weight[i] = 30 + i + "kg";
        }
        weight[71] = 100 + "kg+";
        View choseView = View.inflate(this, R.layout.chose_face, null);
        final NumberPicker npHeight = (NumberPicker) choseView
                .findViewById(R.id.height);
        npHeight.setMaxValue(height.length - 1);
        npHeight.setMinValue(0);
        npHeight.setFocusable(true);
        npHeight.setFocusableInTouchMode(true);
        npHeight.setDisplayedValues(height);
        npHeight.setValue(30);
        final NumberPicker npWeight = (NumberPicker) choseView
                .findViewById(R.id.weight);
        npWeight.setMaxValue(weight.length - 1);
        npWeight.setMinValue(0);
        npWeight.setFocusable(true);
        npWeight.setFocusableInTouchMode(true);
        npWeight.setDisplayedValues(weight);
        npWeight.setValue(15);
        final NumberPicker npBody = (NumberPicker) choseView
                .findViewById(R.id.body);
        if(user.getSex().equals("男")){
            body=boy;
        }else if(user.getSex().equals("女")){
            body=girl;

        }
        npBody.setMaxValue(body.length - 1);
        npBody.setMinValue(0);
        npBody.setFocusable(true);
        npBody.setFocusableInTouchMode(true);
        npBody.setDisplayedValues(body);
        if(!face.getText().equals("")){
            String face_str[]=face.getText().toString().split(" ");
            for(int i=0;i<height.length-1;i++){
                if (height[i].equals(face_str[0])){
                    npHeight.setValue(i);
                    break;
                }
            }
            for(int i=0;i<weight.length-1;i++){
                if (weight[i].equals(face_str[1])){
                    npWeight.setValue(i);
                    break;
                }
            }
            for(int i=0;i<body.length-1;i++){
                if (body[i].equals(face_str[2])||boy[i].equals(face_str[2])||girl[i].equals(face_str[2])){
                    npBody.setValue(i);
                    break;
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("身高、体重、类型").setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        face.setText(height[npHeight.getValue()] + " "
                                + weight[npWeight.getValue()] + " "
                                + body[npBody.getValue()]);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setView(choseView, 0, 0, 0, 0);
        dialog.show();
    }

    String[] moneys = {"4000元以下", "4001-6000元", "6001-10000元", "10001-15000元",
            "15001-20000元", "20001-50000元", "50000元以上"};

    @Click
    void money() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoEditActivity.this);
        builder.setItems(moneys, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int position) {
                money.setTag(position + "");
                money.setText(moneys[position]);
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    String[] loves = {"单身", "恋爱中", "已婚"};

    @Click
    void love() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoEditActivity.this);
        builder.setItems(loves, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int position) {
                love.setTag(position + "");
                love.setText(loves[position]);
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    String[] ol = {"计算机/互联网/通信", "生产/工艺/制造", "商业/服务业/个人体经营",
            "金融/银行／投资／保险", "文化／广告／传媒", "娱乐／艺术／表演", "医疗／护理／制药", "公务员／事业单位",
            "学生", "无"};

    @Click
    void job() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoEditActivity.this);
        builder.setItems(ol, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int position) {
                job.setTag(position + "");
                job.setText(ol[position]);
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
}

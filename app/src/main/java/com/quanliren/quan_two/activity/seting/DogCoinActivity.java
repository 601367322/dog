package com.quanliren.quan_two.activity.seting;

import android.content.Intent;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.shop.ShopVip_;
import com.quanliren.quan_two.bean.DogFood;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity(R.layout.dog_coin)
public class DogCoinActivity extends BaseActivity {
    public static final String TAG="com.quanliren.quan_two.activity.seting.DogCoinActivity";
    @ViewById
    TextView dogfood;
    @ViewById
    TextView share;
    @ViewById
    TextView dongtai;
    @ViewById
    TextView date;
    @ViewById
    TextView hand;

    @ViewById
    TextView tv_phone;
    @ViewById
    TextView tv_wxin;
    @ViewById
    TextView tv_qq;
    @ViewById
    TextView tv_sina;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSupportActionBar().setTitle("我的狗粮");
    }

    @AfterViews
    void initView() {
        ac.finalHttp.post(URL.DOG_FOOD_RECORD, getAjaxParams(), new MyJsonHttpResponseHandler(this) {
            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {

                if (!jo.isNull(URL.RESPONSE)) {
                    DogFood food = new Gson().fromJson(jo.getString(URL.RESPONSE), new TypeToken<DogFood>() {
                    }.getType());
                    initSource(food);
                }

            }
        });

    }

    void initSource(DogFood food) {
        if (food != null) {
            dogfood.setText(food.getCoin());
            if ("0".equals(food.getShareState())) {
                share.setText("未完成");
                share.setTextColor(getResources().getColor(R.color.time));
            } else {
                share.setText("+1");
                share.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(food.getDynamicState())) {
                dongtai.setText("未完成");
                dongtai.setTextColor(getResources().getColor(R.color.time));
            } else {
                dongtai.setText("+1");
                dongtai.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(food.getDatSate())) {
                date.setText("未完成");
                date.setTextColor(getResources().getColor(R.color.time));
            } else {
                date.setText("+2");
                date.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(food.getPairSate())) {
                hand.setText("未完成");
                hand.setTextColor(getResources().getColor(R.color.time));
            } else {
                hand.setText("+2");
                hand.setTextColor(getResources().getColor(R.color.red));
            }
            //绑定状态
            if ("0".equals(food.getPhoneBuildSate())) {
                tv_phone.setText("未完成");
                tv_phone.setTextColor(getResources().getColor(R.color.time));
            } else {
                tv_phone.setText("+1");
                tv_phone.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(food.getWxBuildSate())) {
                tv_wxin.setText("未完成");
                tv_wxin.setTextColor(getResources().getColor(R.color.time));
            } else {
                tv_wxin.setText("+1");
                tv_wxin.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(food.getQqBuildSate())) {
                tv_qq.setText("未完成");
                tv_qq.setTextColor(getResources().getColor(R.color.time));
            } else {
                tv_qq.setText("+1");
                tv_qq.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(food.getXlBuildSate())) {
                tv_sina.setText("未完成");
                tv_sina.setTextColor(getResources().getColor(R.color.time));
            } else {
                tv_sina.setText("+1");
                tv_sina.setTextColor(getResources().getColor(R.color.red));
            }

        }
    }
    @Receiver(actions =DogCoinActivity.TAG)
    public void receiver(Intent i) {
        String action = i.getAction();
        String coin=i.getStringExtra("coin");
        if (action.equals(DogCoinActivity.TAG)&&coin!=null&&!"".equals(coin)) {
           if(dogfood!=null){
               dogfood.setText(coin);
           }
        }
    }
    /**
     * 前往商城
     */
    @Click
    void rl_shop() {
        ShopVip_.intent(this).start();
    }

    /**
     * 分享成功
     */
    @Click
    void rl_share() {
        IntroduceActivity_.intent(this).extra("state", 0).start();
    }

    /**
     * 发表动态
     */
    @Click
    void rl_dongtai() {
        IntroduceActivity_.intent(this).extra("state", 1).start();
    }

    /**
     * 约会成功
     */
    @Click
    void rl_date() {
        IntroduceActivity_.intent(this).extra("state", 2).start();
    }

    /**
     * 牵手成功
     */
    @Click
    void rl_hand() {
        IntroduceActivity_.intent(this).extra("state", 3).start();
    }
    /**
     * 绑定规则
     */
    @Click
    void bind_rule() {
        IntroduceActivity_.intent(this).extra("state", 4).start();
    }
}

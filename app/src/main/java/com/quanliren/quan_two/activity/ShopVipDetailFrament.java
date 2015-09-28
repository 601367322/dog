/*
package com.quanliren.quan_two.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.dd.CircularProgressButton;
import com.alipay.Keys;
import com.alipay.Result;
import com.alipay.Rsa;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.seting.HtmlActivity_;
import com.quanliren.quan_two.activity.shop.VipCardActivity_;
import com.quanliren.quan_two.activity.shop.product.GoodsExchangeActivity_;
import com.quanliren.quan_two.adapter.ShopAdapter;
import com.quanliren.quan_two.adapter.ShopAdapter.IBuyListener;
import com.quanliren.quan_two.bean.OrderBean;
import com.quanliren.quan_two.bean.ShopBean;
import com.quanliren.quan_two.bean.ShopListBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.fragment.SetingMoreFragment;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.pull.PullToRefreshLayout;
import com.quanliren.quan_two.pull.lib.ActionBarPullToRefresh;
import com.quanliren.quan_two.pull.lib.listeners.OnRefreshListener;
import com.quanliren.quan_two.util.BitmapCache;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.vip_detail_last)
public class ShopVipDetailFrament extends MenuFragmentBase implements IBuyListener,OnRefreshListener {
    private static final int RQF_PAY = 1;
    private static final int RQF_LOGIN = 2;

    @ViewById
    TextView vip_des;
    @ViewById
    TextView coin_des;
//    @ViewById
//    ListView listview;
    @ViewById
    ListView listview_vip;
    @ViewById
    ListView listview_coin;
    @ViewById
    TextView exchange_goods;
    @ViewById
    ImageView exchange_detail_image;
    @ViewById
    ImageView head_img;
    @ViewById
    View vip_ll;
    @ViewById
    View coin_ll;
    @ViewById
    View exchange_lay;
//    ShopAdapter adapter;
    ShopAdapter adapter_vip;
    ShopAdapter adapter_coin;
//    List<ShopBean> list = new ArrayList<ShopBean>();
    List<ShopBean> list_vip = new ArrayList<ShopBean>();
    List<ShopBean> list_coin = new ArrayList<ShopBean>();

    @ViewById
    PullToRefreshLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Click
    void exchange_goods(){
        GoodsExchangeActivity_.intent(this).start();
    }
    @Click
    void exchange_detail_image(){
        GoodsExchangeActivity_.intent(this).start();
    }
    @AfterViews
    void initView() {
        head_img.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.vip_banner, getActivity())));
        vip_ll.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.vip_bg_bg, getActivity())));
        coin_ll.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.coin_bg_bg, getActivity())));
        exchange_lay.setBackgroundDrawable(new BitmapDrawable(BitmapCache.getInstance().getBitmap(R.drawable.exchange_bg_bg, getActivity())));
        setTitleTxt("商城");
        ActionBarPullToRefresh.from(getActivity()).setAutoStart(true).allChildrenArePullable().listener(this).setup(layout);

//        list.add(new ShopBean(-1, 0, "会员购买", "¥0", 0));
//        list.add(new ShopBean(0, R.drawable.shop_icon_month_vip1, "1个月会员", "¥12.00", 1));
//        list.add(new ShopBean(1, R.drawable.shop_icon_month_vip2, "3个月会员", "¥30.00", 2));
//        list.add(new ShopBean(2, R.drawable.shop_icon_year_vip, "12个月年费会员", "¥88.00", 3));
//        list.add(new ShopBean(-1, 0, "狗粮购买", "¥0", 0));
//        list.add(new ShopBean(3, R.drawable.shop_icon_5, "5个狗粮", "¥50.00", 1));
//        list.add(new ShopBean(4, R.drawable.shop_icon_10, "10个狗粮", "¥98.00", 2));
//        list.add(new ShopBean(5, R.drawable.shop_icon_20, "20个狗粮", "¥198.00", 2));
//        list.add(new ShopBean(6, R.drawable.shop_icon_50, "50个狗粮", "¥488.00", 2));
//        list.add(new ShopBean(7, R.drawable.shop_icon_100, "100个狗粮", "¥998.00", 3));


//        adapter = new ShopAdapter(getActivity(), list,this);
//        listview.setAdapter(adapter);
        adapter_vip = new ShopAdapter(getActivity(), list_vip,this);
        listview_vip.setAdapter(adapter_vip);
        adapter_coin = new ShopAdapter(getActivity(), list_coin,this);
        listview_coin.setAdapter(adapter_coin);


//        try {
//            Bitmap loadedImage = ((BitmapDrawable) banner.getDrawable()).getBitmap();
//            int swidth = getResources().getDisplayMetrics().widthPixels;
//            float widthScale = (float) swidth / (float) loadedImage.getWidth();
//            int height = (int) (widthScale * loadedImage.getHeight());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    swidth, height);
//            banner.setLayoutParams(lp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Click
    void vip_des() {
        HtmlActivity_.intent(this).title("会员介绍").url("").start();
    }

    @Click
    void coin_des() {
        HtmlActivity_.intent(this).title("狗粮介绍").url("").start();
    }


    public void startBao(final ShopBean sb) {
        RequestParams ap = getAjaxParams();
        ap.put("gnumber", sb.getId());
        ac.finalHttp.post(URL.GETALIPAY, ap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jo) {
                        try {
                            int status = jo.getInt(URL.STATUS);
                            switch (status) {
                                case 0:
                                    doSuccess();
                                    buy(jo.toString(), sb.getTitle());
                                    break;
                                default:
                                    doFail();
                                    showFailInfo(jo);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {
                        doFail();
                        showIntentErrorToast();
                    }
                });
    }

    @UiThread(delay = 500)
    void doSuccess() {
        if (mProgress != null) {
            mProgress.setProgress(0);
            mProgress = null;
        }
    }

    @UiThread(delay = 500)
    void doFail() {
        mProgress.setProgress(-1);
        doRstoref();
    }

    @UiThread(delay = 1500)
    void doRstoref() {
        mProgress.setProgress(0);
    }

    public void buy(String t, String name) {
        try {
            String info = getNewOrderInfo(t, name);
            String sign = Rsa.sign(info, Keys.PRIVATE);
            sign = URLEncoder.encode(sign);
            info += "&sign=\"" + sign + "\"&" + getSignType();

            final String orderInfo = info;
            new Thread() {
                public void run() {
                    AliPay alipay = new AliPay(getActivity(), mHandler);
                    // 设置为沙箱模式，不设置默认为线上环境
                    // alipay.setSandBox(true);

                    String result = alipay.pay(orderInfo);

                    Message msg = new Message();
                    msg.what = RQF_PAY;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }.start();

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity(), "remote_call_failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Result result = new Result((String) msg.obj);
            switch (msg.what) {
                case RQF_PAY:
                    if (result.getResultStatus().equals("9000")) {
                        showCustomToast("购买成功");
                        User user = getHelper().getUserInfo();
                        user.setIsvip(1);
                        UserTable ut = new UserTable(user);
//                        try {
//                            userTableDao.delete(ut);
//                            userTableDao.create(ut);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }

                        Intent i = new Intent(SetingMoreFragment.UPDATE_USERINFO);
                        getActivity().sendBroadcast(i);

                    } else if (result.getResultStatus().equals("6001")) {
                        // showCustomToast("取消购买");
                    } else {
                        showCustomToast("购买失败");
                    }
                    break;
                case RQF_LOGIN: {
                    Toast.makeText(getActivity(), result.getResult(),
                            Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
            }
        }

        ;
    };

    OrderBean ob = null;

    private String getNewOrderInfo(String t, String name) {
        String url = "";
        try {
            JSONObject jo = new JSONObject(t);
            ob = new Gson().fromJson(jo.getString(URL.RESPONSE), new TypeToken<OrderBean>() {
            }.getType());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
        sb.append(Keys.DEFAULT_PARTNER);
        sb.append("\"&out_trade_no=\"");

        sb.append(ob.getOrder_no());

        sb.append("\"&subject=\"");
        sb.append(name);
        sb.append("\"&body=\"");
        sb.append(name);
        sb.append("\"&total_fee=\"");
        sb.append(ob.getPrice());
        sb.append("\"&notify_url=\"");

        // 网址需要做URL编码
        sb.append(URLEncoder.encode(ob.getNotify_url()));

        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&_input_charset=\"UTF-8");
        sb.append("\"&return_url=\"");
        sb.append(URLEncoder.encode("http://m.alipay.com"));
        sb.append("\"&payment_type=\"1");
        sb.append("\"&seller_id=\"");
        sb.append(Keys.DEFAULT_SELLER);

        // 如果show_url值为空，可不传
        // sb.append("\"&show_url=\"");
        sb.append("\"&it_b_pay=\"1m");
        sb.append("\"");

        return new String(sb);
    }

    CircularProgressButton mProgress;

    @Override
    public void buyClick(final CircularProgressButton progress) {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        if ((this.mProgress != null && this.mProgress.getProgress() != 0)) {
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setItems(new String[]{"支付宝安全支付", "储蓄卡支付",
                "信用卡支付"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShopBean sb = (ShopBean) progress.getTag();
                switch (which) {
                    case 0:
                        mProgress = progress;
                        if (progress.getProgress() == 0) {
                            progress.setProgress(50);
                            startBao(sb);
                        }
                        break;
                    case 1:
                        VipCardActivity_.intent(ShopVipDetailFrament.this).sb(sb).channelType("DEBIT_EXPRESS").start();
                        break;
                    case 2:
                        VipCardActivity_.intent(ShopVipDetailFrament.this).sb(sb).channelType("OPTIMIZED_MOTO").start();
                        break;
                }
            }
        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onRefreshStarted(View view) {
        ac.finalHttp.post(URL.SHOPLIST,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int status=response.getInt(URL.STATUS);
                    switch (status){
                        case 0:
                            ShopListBean sb=new Gson().fromJson(response.getString(URL.RESPONSE),new TypeToken<ShopListBean>(){}.getType());
                            setSourse(sb);
                            break;
                        default:
                            showFailInfo(response);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    layout.setRefreshComplete();
                }
            }

            @Override
            public void onFailure() {
                layout.setRefreshComplete();
            }
        });
    }

    String[] vips=new String[]{"1","3","12"};

    Integer[] vip_icons=new Integer[]{R.drawable.shop_icon_month_vip1,R.drawable.shop_icon_month_vip2,R.drawable.shop_icon_year_vip};

    String[] conis=new String[]{"5","10","20","50","100","120"};
    Integer[] coin_icons=new Integer[]{R.drawable.shop_icon_5,R.drawable.shop_icon_10,R.drawable.shop_icon_20,R.drawable.shop_icon_50,R.drawable.shop_icon_100,R.drawable.shop_icon_100};

    @UiThread
    public void setSourse(ShopListBean sb){
//        list.clear();
//        list.add(new ShopBean(-1, 0, "会员购买", "¥0", 0));
//        for (int i = 0; i < sb.viplist.size(); i++) {
//            int viewType=1;
//            if(i>0&&i<sb.viplist.size()-1){
//                viewType=2;
//            }else if(i==sb.viplist.size()-1){
//                viewType=3;
//            }
//            list.add(new ShopBean(sb.viplist.get(i).getGnumber(), vip_icons[i], vips[i]+"个月会员", "¥"+sb.viplist.get(i).getPrice(), viewType));
//        }
        list_vip.clear();
//        list_vip.add(new ShopBean(-1, 0, "会员购买", "¥0", 0));
        for (int i = 0; i < sb.viplist.size(); i++) {
            int viewType=1;
            if(i>0&&i<sb.viplist.size()-1){
                viewType=2;
            }else if(i==sb.viplist.size()-1){
                viewType=3;
            }
            list_vip.add(new ShopBean(sb.viplist.get(i).getGnumber(), vip_icons[i], vips[i]+"个月会员", "¥"+sb.viplist.get(i).getPrice(), viewType));
        }
//        list.add(new ShopBean(-1, 0, "狗粮购买", "¥0", 0));
//        for (int i = 0; i < sb.coinlist.size(); i++) {
//            int viewType=1;
//            if(i>0&&i<sb.coinlist.size()-1){
//                viewType=2;
//            }else if(i==sb.coinlist.size()-1){
//                viewType=3;
//            }
//            list.add(new ShopBean(sb.coinlist.get(i).getGnumber(), coin_icons[i], conis[i]+"个狗粮", "¥"+sb.coinlist.get(i).getPrice(), viewType));
//        }
//        list_coin.add(new ShopBean(-1, 0, "狗粮购买", "¥0", 0));
        for (int i = 0; i < sb.coinlist.size(); i++) {
            int viewType=1;
            if(i>0&&i<sb.coinlist.size()-1){
                viewType=2;
            }else if(i==sb.coinlist.size()-1){
                viewType=3;
            }
            list_coin.add(new ShopBean(sb.coinlist.get(i).getGnumber(), coin_icons[i], conis[i]+"个狗粮", "¥"+sb.coinlist.get(i).getPrice(), viewType));
        }
//        adapter.setList(list);
//        adapter.notifyDataSetChanged();
        adapter_vip.setList(list_vip);
        adapter_vip.notifyDataSetChanged();
        adapter_coin.setList(list_coin);
        adapter_coin.notifyDataSetChanged();
    }
}
*/

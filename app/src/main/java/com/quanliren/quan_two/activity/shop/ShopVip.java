package com.quanliren.quan_two.activity.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alipay.Keys;
import com.alipay.PayResult;
import com.alipay.Rsa;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.wxapi.MD5;
import com.quanliren.quan_two.adapter.ShopAdapter;
import com.quanliren.quan_two.adapter.ShopAdapter.IBuyListener;
import com.quanliren.quan_two.bean.OrderBean;
import com.quanliren.quan_two.bean.ShopBean;
import com.quanliren.quan_two.bean.ShopListBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.fragment.SetingMoreFragment;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@OptionsMenu(R.menu.shop_menu)
@EActivity(R.layout.vip_detail)
public class ShopVip extends BaseActivity implements IBuyListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int RQF_PAY = 1;
    private static final int RQF_LOGIN = 2;
    @ViewById
    ListView listview;
    @ViewById
    ScrollView sv;
    ShopAdapter adapter;
    List<ShopBean> list = new ArrayList<ShopBean>();

    PayReq req = new PayReq();
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    StringBuffer sb = new StringBuffer();

    @ViewById
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initView() {
        sv.smoothScrollTo(0, 0);
        swipe.setOnRefreshListener(this);
        adapter = new ShopAdapter(this, list, this);
        listview.setAdapter(adapter);
        refresh();
    }

    @UiThread(delay = 200)
    public void refresh() {
        swipe.setRefreshing(true);
    }

    public void startBao(final ShopBean sb) {
        RequestParams ap = getAjaxParams();
        ap.put("gnumber", sb.getId());
        ac.finalHttp.post(URL.GETALIPAY, ap,
                new MyJsonHttpResponseHandler(this, "正在获取订单信息", null) {

                    @Override
                    public void onSuccessRetCode(JSONObject jo) throws Throwable {
                        buy(jo.toString(), sb.getTitle());
                    }
                });
    }

    public void startWXin(final ShopBean sb) {
        RequestParams ap = getAjaxParams();
        ap.put("gnumber", sb.getId());
        ac.finalHttp.post(URL.GETWXPAY, ap,
                new MyJsonHttpResponseHandler(this, "正在获取订单信息") {
                    @Override
                    public void onSuccessRetCode(JSONObject jo) throws Throwable {
                        wXinBuy(jo);
                    }
                });
    }

    private void wXinBuy(JSONObject jo) throws Throwable {
        String respense = jo.getString(URL.RESPONSE);
        JSONObject resp = new JSONObject(respense);
        req.appId = resp.getString("appid");
        req.partnerId = resp.getString("partnerid");
        req.prepayId = resp.getString("prepayid");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = resp.getString("noncestr");
        req.timeStamp = resp.getString("timestamp");
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(resp.getString("appkey"), signParams);
//        req.sign = resp.getString("sign");
        sendPayReq();
    }

    private void sendPayReq() {
        msgApi.registerApp(Constants.APP_ID_PAY);
        msgApi.sendReq(req);
    }

    /**
     * app 签名
     *
     * @param params
     * @return
     */
    private String genAppSign(String appkey, List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(appkey);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    public void buy(String t, String name) {
        try {
            String info = getNewOrderInfo(t, name);
            String sign = Rsa.sign(info, Keys.PRIVATE);
            try {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            info += "&sign=\"" + sign + "\"&" + getSignType();

            final String orderInfo = info;
            new Thread() {
                public void run() {
                    PayTask alipay = new PayTask(ShopVip.this);
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
            Toast.makeText(ShopVip.this, "remote_call_failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RQF_PAY: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showCustomToast("购买成功");
                        User user = getHelper().getUserInfo();
                        user.setIsvip(1);
                        UserTable ut = new UserTable(user);
                        try {
                            userTableDao.delete(ut);
                            userTableDao.create(ut);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(SetingMoreFragment.UPDATE_USERINFO);
                        sendBroadcast(i);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showCustomToast("支付结果确认中");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            showCustomToast("取消购买");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showCustomToast("购买失败");
                        }
                    }
                    break;
                }
                case RQF_LOGIN: {
                    showCustomToast(msg.obj.toString());
                    break;
                }
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

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + ob.getOrder_no() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + name + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + name + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + ob.getPrice() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + ob.getNotify_url()
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    Button mProgress;

    @Override
    public void buyClick(final Button progress) {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("支付方式").setItems(new String[]{"支付宝支付", "微信支付"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShopBean sb = (ShopBean) progress.getTag();
                mProgress = progress;
                switch (which) {
                    case 0:
                        startBao(sb);
                        break;
                    case 1:
                        startWXin(sb);
                        break;
                }
            }
        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    String[] vips = new String[]{"1", "3", "12"};
    Integer[] icons = new Integer[]{R.drawable.vip_icon, R.drawable.dog_icon};
    Integer[] vip_icons = new Integer[]{R.drawable.shop_point, R.drawable.shop_point, R.drawable.shop_point};

    String[] conis = new String[]{"6", "30", "108", "228", "358", "908"};
    Integer[] coin_icons = new Integer[]{R.drawable.shop_point, R.drawable.shop_point, R.drawable.shop_point, R.drawable.shop_point, R.drawable.shop_point, R.drawable.shop_point};

    @UiThread
    public void setSourse(ShopListBean sb) {
        list.clear();
        list.add(new ShopBean(-1, icons[0], "", "¥0", 1));
        for (int i = 0; i < sb.viplist.size(); i++) {
            int viewType = 1;
            if (i > 0 && i < sb.viplist.size() - 1) {
                viewType = 2;
            } else if (i == sb.viplist.size() - 1) {
                viewType = 3;
            }
            list.add(new ShopBean(sb.viplist.get(i).getGnumber(), vip_icons[i], vips[i] + "个月会员", "¥" + sb.viplist.get(i).getPrice(), viewType));
        }
        list.add(new ShopBean(-1, icons[1], "", "¥0", 1));
        for (int i = 0; i < sb.coinlist.size(); i++) {
            int viewType = 1;
            if (i > 0 && i < sb.coinlist.size() - 1) {
                viewType = 2;
            } else if (i == sb.coinlist.size() - 1) {
                viewType = 3;
            }
            list.add(new ShopBean(sb.coinlist.get(i).getGnumber(), coin_icons[i], conis[i] + "个狗粮", "¥" + sb.coinlist.get(i).getPrice(), viewType));
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        ac.finalHttp.post(URL.SHOPLIST, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int status = response.getInt(URL.STATUS);
                    switch (status) {
                        case 0:
                            ShopListBean sb = new Gson().fromJson(response.getString(URL.RESPONSE), new TypeToken<ShopListBean>() {
                            }.getType());
                            setSourse(sb);
                            break;
                        default:
                            showFailInfo(response);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    onFailure();
                }
            }

            @Override
            public void onFailure() {
                swipe.setRefreshing(false);
            }
        });
    }

    @OptionsItem
    public void vip_introduce() {
        VipIntroduceListActivity_.intent(this).start();
    }
}

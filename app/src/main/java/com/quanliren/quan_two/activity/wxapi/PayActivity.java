//
//package com.quanliren.quan_two.activity.wxapi;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.Xml;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.quanliren.quan_two.activity.R;
//import com.quanliren.quan_two.activity.base.BaseActivity;
//import com.quanliren.quan_two.util.Constants;
//import com.quanliren.quan_two.util.LogUtil;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.ViewById;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.xmlpull.v1.XmlPullParser;
//
//import java.io.StringReader;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//@EActivity
//public class PayActivity extends BaseActivity {
//    private static final String TAG = "com.quanliren.quan_two.activity.wxapi.PayActivity";
//
//    @ViewById
//    Button appay_btn;
//    PayReq req=new PayReq();
//    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//    TextView show;
//    Map<String,String> resultunifiedorder;
//    StringBuffer sb=new StringBuffer();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay);
//        show =(TextView)findViewById(R.id.editText_prepay_id);
//
//    }
//
//
//    @Click
//    public void appay_btn(View view){
//        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//        getPrepayId.execute();
//    }
//
//    /**
//     生成签名
//     */
//
//    private String genPackageSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(Constants.API_KEY);
//
//
//        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Log.e("orion", packageSign);
//        return packageSign;
//    }
//    private String genAppSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(Constants.API_KEY);
//
//        this.sb.append("sign str\n"+sb.toString()+"\n\n");
//        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Log.e("orion",appSign);
//        return appSign;
//    }
//    private String toXml(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<xml>");
//        for (int i = 0; i < params.size(); i++) {
//            sb.append("<"+params.get(i).getName()+">");
//
//
//            sb.append(params.get(i).getValue());
//            sb.append("</"+params.get(i).getName()+">");
//        }
//        sb.append("</xml>");
//
//        Log.e("orion",sb.toString());
//        return sb.toString();
//    }
//
//    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
//
//        private ProgressDialog dialog;
//
//
//        @Override
//        protected void onPreExecute() {
//            dialog = ProgressDialog.show(PayActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
//        }
//
//        @Override
//        protected void onPostExecute(Map<String,String> result) {
//            if (dialog != null) {
//                dialog.dismiss();
//            }
////			Log.e("===prepay_id",result.get("prepay_id"));
//            sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
//            show.setText(sb.toString());
//
//            resultunifiedorder=result;
//            genPayReq();
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//        @Override
//        protected Map<String,String>  doInBackground(Void... params) {
//
//            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
//            String entity = genProductArgs();
//
//            Log.e("orion",entity);
//
//            byte[] buf = Util.httpPost(url, entity);
//
//            String content = new String(buf);
//            Log.e("orion", content);
//            Map<String,String> xml=decodeXml(content);
//
//            return xml;
//        }
//    }
//
//
//
//    public Map<String,String> decodeXml(String content) {
//
//        try {
//            Map<String, String> xml = new HashMap<String, String>();
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setInput(new StringReader(content));
//            int event = parser.getEventType();
//            while (event != XmlPullParser.END_DOCUMENT) {
//
//                String nodeName=parser.getName();
//                switch (event) {
//                    case XmlPullParser.START_DOCUMENT:
//
//                        break;
//                    case XmlPullParser.START_TAG:
//
//                        if("xml".equals(nodeName)==false){
//                            //实例化student对象
//                            xml.put(nodeName,parser.nextText());
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        break;
//                }
//                event = parser.next();
//            }
//
//            return xml;
//        } catch (Exception e) {
//            Log.e("orion",e.toString());
//        }
//        return null;
//
//    }
//
//
//    private String genNonceStr() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }
//
//    private long genTimeStamp() {
//        return System.currentTimeMillis() / 1000;
//    }
//
//
//
//    private String genOutTradNo() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }
//
//
//    //
//    private String genProductArgs() {
//        StringBuffer xml = new StringBuffer();
//
//        try {
//            String	nonceStr = genNonceStr();
//
//
//            xml.append("</xml>");
//            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
//            packageParams.add(new BasicNameValuePair("body", "1个月会员"));
//            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
//            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
//            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
//            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
//            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));
//            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
//
//
//            String sign = genPackageSign(packageParams);
//            packageParams.add(new BasicNameValuePair("sign", sign));
//
//
//            String xmlstring =toXml(packageParams);
//            //改变拼接之后xml字符串格式
//            return new String(xmlstring.getBytes("UTF-8"), "ISO8859-1");
////			return xmlstring;
//
//        } catch (Exception e) {
//            LogUtil.d(TAG, "genProductArgs fail, ex = " + e.getMessage());
//            return null;
//        }
//
//
//    }
//    private void genPayReq() {
//
//        req.appId = Constants.APP_ID;
//        req.partnerId = Constants.MCH_ID;
//        req.prepayId = resultunifiedorder.get("prepay_id");
//        req.packageValue = "Sign=WXPay";
//        req.nonceStr = genNonceStr();
//        req.timeStamp = String.valueOf(genTimeStamp());
//
//
//        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//        signParams.add(new BasicNameValuePair("appid", req.appId));
//        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//        signParams.add(new BasicNameValuePair("package", req.packageValue));
//        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//
//        req.sign = genAppSign(signParams);
//
//        sb.append("sign\n"+req.sign+"\n\n");
//
//        show.setText(sb.toString());
//        sendPayReq();
//        Log.e("orion", signParams.toString());
//
//    }
//    private void sendPayReq() {
//
//
//        msgApi.registerApp(Constants.APP_ID);
//        msgApi.sendReq(req);
//    }
//}

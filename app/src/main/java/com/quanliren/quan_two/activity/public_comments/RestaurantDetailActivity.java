package com.quanliren.quan_two.activity.public_comments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.adapter.RestaurantListAdapter;
import com.quanliren.quan_two.bean.BusinessBean;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.restaurant_detail)
public class RestaurantDetailActivity extends BaseActivity {
    @ViewById
    WebView business_webView;
    @Extra
    String business_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("商户详情");
        customShowDialog("正在加载，请等待……");
    }
    @AfterViews
    void initView(){
        business_webView.loadUrl(business_url);
        business_webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器

                view.loadUrl(url);
                return true;
            }
           @Override
          public void onPageStarted(WebView view, String url, Bitmap favicon) {
               // TODO Auto-generated method stub
               LogUtil.d("===============onPageStarted");


                super.onPageStarted(view, url, favicon);

            }
            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                LogUtil.d("===============onPageFinished");

                super.onPageFinished(view, url);
                customDismissDialog();

            }
        });


        business_webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customDismissDialog();
    }
}



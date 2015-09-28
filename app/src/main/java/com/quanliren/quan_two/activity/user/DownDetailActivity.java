package com.quanliren.quan_two.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.restaurant_detail)
public class DownDetailActivity extends BaseActivity {
    @ViewById
    WebView business_webView;
    @Extra
    String business_url;
    MyWebViewDownLoadListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("详情");
    }
    @AfterViews
    void initView(){
        listener=new MyWebViewDownLoadListener();
        //支持javascript
        business_webView.getSettings().setJavaScriptEnabled(true);
        business_webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        business_webView.getSettings().setLoadWithOverviewMode(true);
        business_webView.setWebViewClient(new WebViewClient());
        business_webView.setDownloadListener(listener);
        business_webView.loadUrl(business_url);

    }

    class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

}



package com.quanliren.quan_two.activity.seting;

import android.os.Bundle;
import android.webkit.WebView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

@EActivity
public class FunctionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceinfo);

        getSupportActionBar().setTitle("会员介绍");
        WebView wView = (WebView) findViewById(R.id.webview);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.loadUrl("file:///android_asset/function.html");
    }
}

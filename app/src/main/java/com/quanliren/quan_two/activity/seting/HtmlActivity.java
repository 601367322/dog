package com.quanliren.quan_two.activity.seting;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.serviceinfo)
public class HtmlActivity extends BaseActivity {

    @Extra
    String url;
    @Extra
    String title;
    @ViewById
    WebView webview;
    @ViewById
    TextView text;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSupportActionBar().setTitle(title);
    }

    @AfterViews
    void initView() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(!"".equals(url)&&url!=null){
            webview.loadUrl(url);
            text.setVisibility(View.GONE);
        }else{
            webview.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            if("狗粮介绍".equals(title)){
                text.setText("1  狗粮不仅可以兑换心仪的礼物还可以兑换现金和年会员\n2  约会赠送狗粮越多 排名越靠前 还怕ta看不到吗？\n3  灵活应用狗粮 可赠送好友 可兑换礼物");
            }
        }

    }
}

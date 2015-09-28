package com.quanliren.quan_two.activity.seting;

import android.os.Bundle;
import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_introduce)
public class IntroduceActivity extends BaseActivity {

    @ViewById
    View share;
    @ViewById
    View dontai;
    @ViewById
    View date;
    @ViewById
    View redLine;
    @ViewById
    View bind_intro;
    @Extra
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
    }
    @AfterViews
    void initView(){

        share.setVisibility(View.GONE);
        dontai.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        redLine.setVisibility(View.GONE);
        bind_intro.setVisibility(View.GONE);

        if(state==0){
            share.setVisibility(View.VISIBLE);
        }else if(state==1){
            dontai.setVisibility(View.VISIBLE);
        }else if(state==2){
            date.setVisibility(View.VISIBLE);
        }else if(state==3){
            redLine.setVisibility(View.VISIBLE);
        }else if(state==4){
            bind_intro.setVisibility(View.VISIBLE);
        }
    }

}

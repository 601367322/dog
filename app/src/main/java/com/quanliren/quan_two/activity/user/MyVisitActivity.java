package com.quanliren.quan_two.activity.user;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_only_fragment)
public class  MyVisitActivity extends BaseActivity {

    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, MyVisitFragment_.builder().build()).commitAllowingStateLoss();
    }
}
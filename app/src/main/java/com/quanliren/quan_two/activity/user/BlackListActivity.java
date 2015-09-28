package com.quanliren.quan_two.activity.user;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_only_fragment)
public class BlackListActivity extends BaseActivity{

    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content,BlackListFragment_.builder().build()).commitAllowingStateLoss();
    }
}

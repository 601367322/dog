package com.quanliren.quan_two.activity.group.date;

import android.content.Intent;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.DateBean;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;

@EActivity(R.layout.activity_only_fragment)
public class DateApplyManageActivity extends BaseActivity {

    @Extra
    DateBean bean;

    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, DateApplyManageFragment_.builder().bean(bean).build()).commitAllowingStateLoss();
    }
}

package com.quanliren.quan_two.activity.group.date;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Shen on 2015/7/9.
 */
@EActivity(R.layout.activity_only_fragment)
public class MyDateNavActivity extends BaseActivity {

    @Override
    public void init() {
//        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, MyDateNavFragment_.builder().build()).commitAllowingStateLoss();
    }
}

package com.quanliren.quan_two.activity.user;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Shen on 2015/7/9.
 */
@EActivity(R.layout.activity_only_fragment)
public class MyFavoriteNavActivity extends BaseActivity {

    @Override
    public void init() {
//        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, MyFavoriteNavFragment_.builder().build()).commitAllowingStateLoss();
    }
}

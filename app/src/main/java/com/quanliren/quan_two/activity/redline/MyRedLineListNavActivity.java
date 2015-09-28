package com.quanliren.quan_two.activity.redline;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Shen on 2015/7/14.
 */
@EActivity(R.layout.activity_only_fragment)
public class MyRedLineListNavActivity extends BaseActivity {

    @Override
    public void init() {
        super.init();
//        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, RedLineNavFragment_.builder().showBackBtn(true).leftMode(RedLineFragment.RedLineMode.MY).rightMode(RedLineFragment.RedLineMode.JOIN).leftTabText(getString(R.string.my_publish)).rightTabText(getString(R.string.my_join)).build()).commitAllowingStateLoss();
    }
}

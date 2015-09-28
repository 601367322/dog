package com.quanliren.quan_two.activity.group.date;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Shen on 2015/7/20.
 */
@EActivity(R.layout.activity_only_fragment)
public class ConsumerRankingNavActivity extends BaseActivity {

    @Override
    public void init() {
        super.init();
//        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.content,ConsumerRankingNavFragment_.builder().build()).commitAllowingStateLoss();
    }
}

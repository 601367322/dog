package com.quanliren.quan_two.activity.group.date;

import android.text.TextUtils;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.bean.User;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by Shen on 2015/7/9.
 */
@EActivity(R.layout.activity_only_fragment)
public class OtherDateListActivity extends BaseActivity {

    @Extra
    public User bean;

    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, DateListFragment_.builder().autoStart(true).listType(NearPeopleActivity.NEARLISTTYPE.OTHER).otherId(bean.getId()).build()).commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(bean.getSex()) && bean.getSex().equals("0")) {
            getSupportActionBar().setTitle(R.string.other_date_girl);
        }
    }
}

package com.quanliren.quan_two.activity.redline;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.seting.HtmlActivity_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

/**
 * Created by Shen on 2015/7/13.
 */
@OptionsMenu(R.menu.red_line_interduce_menu)
@EActivity(R.layout.activity_only_fragment)
public class RedLinePublishUserListActivity extends BaseActivity {

    @Extra
    RedLineBean bean;

    @Override
    public void init() {
        super.init();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, RedLinePublishUserListFragment_.builder().bean(bean).build()).commitAllowingStateLoss();
    }

    @OptionsItem
    public void interduce(){
        HtmlActivity_.intent(this).title(getResources().getString(R.string.how_to_play)).url("file:///android_asset/red_line_intorduce.html").start();
    }
}

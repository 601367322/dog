package com.quanliren.quan_two.activity.user;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.group.quan.QuanAboutMeFragment_;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment_;
import com.quanliren.quan_two.fragment.base.ActionBarTabFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Shen on 2015/7/8.
 */
@EFragment(R.layout.fragment_my_dongtai_viewpager)
public class MyPersonalDongTaiNavFragment extends ActionBarTabFragment {

    @Override
    public String getLeftTabText() {
        return getString(R.string.my_publish);
    }

    @Override
    public String getRightTabText() {
        return getString(R.string.reply_me);
    }

    @Override
    public void initFragments() {
        views.add(QuanListFragment_.builder().listType(QuanListFragment.LISTTYPE.MY).build());
        views.add(QuanAboutMeFragment_.builder().build());
    }

    @Override
    public boolean showLeftBtn() {
        return true;
    }
}
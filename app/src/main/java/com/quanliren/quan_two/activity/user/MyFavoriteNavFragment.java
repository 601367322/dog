package com.quanliren.quan_two.activity.user;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.group.date.DateListFragment_;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.activity.redline.RedLineFragment;
import com.quanliren.quan_two.activity.redline.RedLineFragment_;
import com.quanliren.quan_two.fragment.base.ActionBarTabFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Shen on 2015/7/9.
 */
@EFragment(R.layout.fragment_my_dongtai_viewpager)
public class MyFavoriteNavFragment extends ActionBarTabFragment {

    @Override
    public String getLeftTabText() {
        return getString(R.string.favorite_redline);
    }

    @Override
    public String getRightTabText() {
        return getString(R.string.favorite_date);
    }

    @Override
    public void initFragments() {
        views.add(RedLineFragment_.builder().mode(RedLineFragment.RedLineMode.FAVORITE).build());
        views.add(DateListFragment_.builder().listType(NearPeopleActivity.NEARLISTTYPE.FAVORITE).build());
    }

    @Override
    public boolean showLeftBtn() {
        return true;
    }
}

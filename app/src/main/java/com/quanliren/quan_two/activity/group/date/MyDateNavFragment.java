package com.quanliren.quan_two.activity.group.date;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.fragment.base.ActionBarTabFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Shen on 2015/7/9.
 */
@EFragment(R.layout.fragment_my_dongtai_viewpager)
public class MyDateNavFragment extends ActionBarTabFragment {


    @Override
    public String getLeftTabText() {
        return getString(R.string.my_publish);
    }

    @Override
    public String getRightTabText() {
        return getString(R.string.my_join);
    }

    @Override
    public void initFragments() {
        views.add(DateListFragment_.builder().listType(NearPeopleActivity.NEARLISTTYPE.MY).build());
        views.add(DateListFragment_.builder().listType(NearPeopleActivity.NEARLISTTYPE.JOIN).build());
    }

    @Override
    public boolean showLeftBtn() {
        return true;
    }

}

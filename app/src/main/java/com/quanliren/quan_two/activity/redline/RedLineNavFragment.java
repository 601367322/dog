package com.quanliren.quan_two.activity.redline;

import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.fragment.base.ActionBarTabFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Shen on 2015/6/30.
 */
@EFragment(R.layout.fragment_redline_viewpager)
public class RedLineNavFragment extends ActionBarTabFragment {


    @FragmentArg
    public String leftTabText;
    @FragmentArg
    public String rightTabText;

    @ViewById
    public View publish;

    @FragmentArg
    public RedLineFragment.RedLineMode leftMode;
    @FragmentArg
    public RedLineFragment.RedLineMode rightMode;
    @FragmentArg
    public boolean showBackBtn = false;

    public void init() {
        super.init();
        if (leftMode == RedLineFragment.RedLineMode.HOT) {
            publish.setVisibility(View.VISIBLE);
//            setTransStatusBar();
        } else {
            publish.setVisibility(View.GONE);
        }
    }


    @Override
    public String getLeftTabText() {
        return leftTabText;
    }

    @Override
    public String getRightTabText() {
        return rightTabText;
    }

    @Override
    public void initFragments() {
        views.add(RedLineFragment_.builder().mode(leftMode).build());
        views.add(RedLineFragment_.builder().mode(rightMode).build());
    }

    @Click
    public void publish() {
        RedLinePublishUserListActivity_.intent(this).start();
    }

    @Override
    public boolean showLeftBtn() {
        return true;
    }
}

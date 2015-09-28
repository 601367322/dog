package com.quanliren.quan_two.activity.group;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment_;
import com.quanliren.quan_two.fragment.base.ActionBarTabFragment;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;

/**
 * Created by Shen on 2015/7/27.
 */
@EFragment(R.layout.fragment_redline_viewpager)
public class DongTaiListNavFragment extends ActionBarTabFragment {

    QuanListFragment all;
    public static final int QuanActivityPublishResponse = 1;

    @Override
    public String getLeftTabText() {
        return getString(R.string.near_dongtai);
    }

    @Override
    public String getRightTabText() {
        return getString(R.string.my_care_dongtai);
    }

    @Override
    public void initFragments() {
        views.add(all = QuanListFragment_.builder().listType(QuanListFragment.LISTTYPE.ALL).build());
        views.add(QuanListFragment_.builder().listType(QuanListFragment.LISTTYPE.MYCARE).build());
    }

    @Override
    public boolean showLeftBtn() {
        return false;
    }

    @Override
    public boolean showRightBtn() {
        return true;
    }

    @Override
    public String getRightBtnStr() {
        return getString(R.string.filter);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (position == 0) {
            right_btn.setVisibility(View.VISIBLE);
            setTransStatusBar();
        } else {
            right_btn.setVisibility(View.GONE);
        }
    }
    @Click
    public void right_btn(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.near_dongtai_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                switch (arg0.getItemId()) {
                    case R.id.only_boy:
                        all.only_boy();
                        break;
                    case R.id.only_girl:
                        all.only_girl();
                        break;
                    case R.id.all:
                        all.all();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Click
    public void publish() {
        if (Util.isFastDoubleClick()) {
            return;
        }
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        PublishDongTaiActivity_.intent(this).startForResult(QuanActivityPublishResponse);
    }

    @OnActivityResult(QuanActivityPublishResponse)
    void onPublishResult(int result, Intent data) {
        switch (result) {
            case Activity.RESULT_OK:
                if(viewpager.getCurrentItem()==0){
                    ((BaseListFragment)(views.get(viewpager.getCurrentItem()))).swipeRefresh();
                }
                break;
        }
    }
}

package com.quanliren.quan_two.fragment;

import android.widget.LinearLayout;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.group.DongTaiListNavActivity_;
import com.quanliren.quan_two.activity.group.date.ConsumerRankingNavActivity_;
import com.quanliren.quan_two.activity.group.date.DateListActivity_;
import com.quanliren.quan_two.activity.near.NearPeopleActivity_;
import com.quanliren.quan_two.activity.seting.EmoticonListActivity_;
import com.quanliren.quan_two.activity.shop.ShopVip_;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.ground)
public class GroundFragment extends MenuFragmentBase {

    @ViewById
    LinearLayout nearpeople;
    @ViewById
    LinearLayout leavemessage;
    @ViewById
    LinearLayout date;
    @ViewById
    LinearLayout ranking;
    @ViewById
    LinearLayout shop;
    @ViewById
    LinearLayout face;

    @Override
    public void init() {
        setTitleTxt(getString(R.string.wangwang));
        setTransStatusBar();
    }

    @Click
    public void nearpeople() {
        NearPeopleActivity_.intent(this).start();
    }

    @Click
    public void leavemessage() {
        DongTaiListNavActivity_.intent(this).start();
    }

    @Click
    public void date() {
        DateListActivity_.intent(this).start();
    }

    @Click
    public void ranking() {
        ConsumerRankingNavActivity_.intent(this).start();
    }

    @Click
    public void shop() {
//        ShopVipDetail_.intent(this).start();
        ShopVip_.intent(this).start();
    }
    @Click
    public void face() {
        EmoticonListActivity_.intent(this).start();
    }
}

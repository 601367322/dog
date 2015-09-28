package com.quanliren.quan_two.activity.group.quan;

import android.content.Intent;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.group.DongTaiDetailActivity_;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.QuanReplyAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DongTaiBean;
import com.quanliren.quan_two.bean.DongTaiReplyMeBean;
import com.quanliren.quan_two.fragment.base.BaseViewPagerListFragment;

import org.androidannotations.annotations.EFragment;

@EFragment
public class QuanAboutMeFragment extends BaseViewPagerListFragment<DongTaiReplyMeBean> implements QuanReplyAdapter.IQuanDetailReplyAdapter {

    @Override
    public int getConvertViewRes() {
        return R.layout.fragment_list;
    }

    @Override
    public BaseAdapter<DongTaiReplyMeBean> getAdapter() {
        QuanReplyAdapter adapter = new QuanReplyAdapter(getActivity());
        adapter.setListener(this);
        return adapter;
    }

    @Override
    public BaseApi getApi() {
        return new QuanAboutMeApi(getActivity());
    }

    @Override
    public Class<?> getClazz() {
        return DongTaiReplyMeBean.class;
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        return super.getCacheKey() + ac.getLoginUserId();
    }

    @Override
    public int getEmptyView() {
        return R.layout.quan_about_me_list_empty;
    }


    public void listview(int position) {
        DongTaiReplyMeBean bean = adapter.getItem(position);
        if (!"".equals(bean.getDyid()) && bean.getDyid() != null) {
            DongTaiBean dbean = new DongTaiBean();
            dbean.setDyid(bean.getDyid());
            DongTaiDetailActivity_.intent(this).bean(dbean).start();
        }
    }


    @Override
    public void logoCick(DongTaiReplyMeBean bean) {
        Intent i = new Intent(getActivity(), bean.getUserid()
                .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                : UserOtherInfoActivity_.class);
        i.putExtra("userId", bean.getUserid());
        startActivity(i);
    }
}
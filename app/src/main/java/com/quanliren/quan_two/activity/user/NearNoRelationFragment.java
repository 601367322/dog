package com.quanliren.quan_two.activity.user;

import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.adapter.NearNoRelationAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_list)
public class NearNoRelationFragment extends BaseListFragment<User> {
    String desc="附近的人";
    @Override
    public BaseAdapter<User> getAdapter() {
        return new NearNoRelationAdapter(getActivity());
    }

    @Override
    public BaseApi getApi() {
        return new NearNoRelationApi(getActivity());
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public boolean needLocation() {
            return true;
    }
    @Override
    public String getCacheKey() {
        return getClass().getName();
    }

    @Override
    public void initParams() {
            api.initParam(ac.cs.getLat(),ac.cs.getLng());
    }
    @Override
    public void initListView() {
        View view = View.inflate(getActivity(), R.layout.fx_fragment_content, null);
        listview.addHeaderView(view);
        getChildFragmentManager().beginTransaction().replace(R.id.content,ShareFragment_.builder().desc(desc).build()).commitAllowingStateLoss();
    }

    @Override
    public void listview(int position) {
        if (Util.isFastDoubleClick()) {
            return;
        }
        User user = adapter.getItem(position - 1);
        if (ac.getLoginUserId().equals(user.getId())) {
            UserInfoActivity_.intent(this).start();
        } else {
            UserOtherInfoActivity_.intent(this).userId(user.getId())
                    .start();
        }
    }

}
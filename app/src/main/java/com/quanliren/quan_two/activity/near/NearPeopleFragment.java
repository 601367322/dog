package com.quanliren.quan_two.activity.near;

import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.ad.HeadAdFragment;
import com.quanliren.quan_two.activity.ad.HeadAdFragment_;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.NearPeopleAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * Created by Shen on 2015/7/1.
 */
@EFragment(R.layout.fragment_list)
public class NearPeopleFragment extends BaseListFragment<User> {

    HeadAdFragment adFragment;

    @FragmentArg
    public double lat;
    @FragmentArg
    public double lng;
    @FragmentArg
    public NearPeopleActivity.NEARLISTTYPE listType = NearPeopleActivity.NEARLISTTYPE.NEAR;

    private static final String ADCacheKey = "Near_ADCacheKey";

    @Override
    public BaseAdapter getAdapter() {
        return new NearPeopleAdapter(getActivity());
    }

    @Override
    public BaseApi getApi() {
        return new NearPeopleApi(getActivity());
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
        if (listType == NearPeopleActivity.NEARLISTTYPE.NEAR) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getCacheKey() {
        return getClass().getName();
    }

    @Override
    public void initParams() {
        if (listType == NearPeopleActivity.NEARLISTTYPE.NEAR) {
            api.initParam(ac.cs.getLat(),ac.cs.getLng());
        } else {
            api.initParam(lat, lng);
        }
    }

    @Override
    public void httpPost() {
        super.httpPost();
        adFragment.getADBanner();
    }

    @Override
    public void initListView() {
        View view = View.inflate(getActivity(), R.layout.fragment_content, null);
        listview.addHeaderView(view);
        getChildFragmentManager().beginTransaction().replace(R.id.content, adFragment = HeadAdFragment_.builder().ADCacheKey(ADCacheKey).build()).commitAllowingStateLoss();
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

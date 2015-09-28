package com.quanliren.quan_two.activity.redline;

import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.user.ShareFragment_;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.fragment.message.MyCareListApi;
import com.quanliren.quan_two.fragment.message.MyCareListFragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.json.JSONObject;

/**
 * Created by Shen on 2015/7/13.
 */
@EFragment(R.layout.fragment_list)
public class RedLinePublishUserListFragment extends BaseListFragment<User> {

    @FragmentArg
    public RedLineBean bean;

    @Override
    public BaseAdapter<User> getAdapter() {
        return new RedLinePublishUserListAdapter(getActivity(), bean);
    }

    @Override
    public BaseApi getApi() {
        return new MyCareListApi(getActivity(), MyCareListFragment.CARETYPE.FRIEND);
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public void initParams() {
        super.initParams();
        api.initParam();
    }

    @Override
    public void setJsonData(JSONObject jo, boolean cache) {
        super.setJsonData(jo, cache);
        if (adapter.getCount() == 0) {
            User user = new User();
            user.setId("-1");
            adapter.add(0, user);
            adapter.notifyDataSetChanged();
        } else {
            if (!adapter.getItem(0).getId().equals("-2")) {
                User user = new User();
                user.setId("-2");
                adapter.add(0, user);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void initListView() {
        super.initListView();
//        listview.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.red_line_publish_user_list_head, listview, false));
        View view = View.inflate(getActivity(), R.layout.fx_fragment_content, null);
        listview.addHeaderView(view);
        getChildFragmentManager().beginTransaction().replace(R.id.content, ShareFragment_.builder().build()).commitAllowingStateLoss();
    }

    @Override
    public int getEmptyView() {
        return -1;
    }

    @Override
    public void listview(int position) {
        super.listview(position);
        User user = adapter.getItem(position-1);
        if (user != null) {
            if (user.getId().equals(ac.getLoginUserId())) {
                UserInfoActivity_.intent(getActivity()).start();
            } else {
                UserOtherInfoActivity_.intent(getActivity()).userId(user.getId()).start();
            }
        }
    }
}

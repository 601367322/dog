package com.quanliren.quan_two.fragment.message;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.NearPeopleAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseViewPagerListFragment;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemLongClick;
import org.json.JSONObject;

/**
 * Created by Shen on 2015/7/10.
 */
@EFragment
public class MyCareListFragment extends BaseViewPagerListFragment<User> {

    public enum CARETYPE {
        FRIEND, MYCARE, FANS
    }

    @FragmentArg
    public CARETYPE caretype = CARETYPE.FRIEND;

    @FragmentArg
    public int type;

    @Override
    public int getConvertViewRes() {
        return R.layout.fragment_list;
    }

    @Override
    public BaseAdapter<User> getAdapter() {
        NearPeopleAdapter adapter = new NearPeopleAdapter(getActivity());
        return adapter;
    }

    @Override
    public BaseApi getApi() {
        return new MyCareListApi(getActivity(), caretype);
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public int getEmptyView() {
        switch (caretype) {
            case FRIEND:
                return R.layout.my_care_list_empty1;
            case MYCARE:
                return R.layout.my_care_list_empty2;
            case FANS:
                return R.layout.my_care_list_empty3;
        }
        return super.getEmptyView();
    }

    @Override
    public void initParams() {
        api.initParam();
    }

    public void listview(int position) {
        User user = adapter.getItem(position);
        if (user != null) {
            Intent i = new Intent(getActivity(), user.getId().equals(getHelper().getUser().getId()) ? UserInfoActivity_.class : UserOtherInfoActivity_.class);
            i.putExtra("userId", user.getId());
            startActivity(i);
        }
    }

    @ItemLongClick(R.id.listview)
    public void onItemLongClick(final int position) {
        if (caretype == CARETYPE.FANS) {
            final User user = adapter.getItem(position);
            if (user != null) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setItems(new String[]{"移除粉丝"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams rp = getAjaxParams();
                        rp.put("otherid", user.getId());
                        ac.finalHttp.post(URL.DELETE_CARE, rp, new MyJsonHttpResponseHandler(getActivity(), getString(R.string.loading), null) {
                            @Override
                            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                adapter.remove(position);
                                adapter.notifyDataSetChanged();
                                if (adapter.getCount() == 0) {
                                    swipeRefresh();
                                }
                            }
                        });
                    }
                }).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }
}

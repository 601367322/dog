package com.quanliren.quan_two.activity.group.date;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.ad.HeadAdFragment;
import com.quanliren.quan_two.activity.ad.HeadAdFragment_;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.adapter.DateListAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.fragment.base.BaseViewPagerListFragment;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Shen on 2015/7/3.
 */
@EFragment
public class DateListFragment extends BaseViewPagerListFragment<DateBean> {

    private static final int DETAIL = 3;

    @Override
    public int getConvertViewRes() {
        return R.layout.fragment_list;
    }

    HeadAdFragment adFragment;

    private static final String ADCacheKey = "Date_ADCacheKey";

    @FragmentArg
    public NearPeopleActivity.NEARLISTTYPE listType = NearPeopleActivity.NEARLISTTYPE.NEAR;

    @FragmentArg
    public double lat;
    @FragmentArg
    public double lng;
    @FragmentArg
    public String otherId;
    @FragmentArg
    public boolean autoStart;

    @Override
    public void init() {
        if (autoStart) {
            refresh();
        }
    }

    @Override
    public BaseAdapter<DateBean> getAdapter() {
        DateListAdapter adapter = new DateListAdapter(getActivity());
        adapter.setListener(new DateListAdapter.IDateAdapterListener() {
            @Override
            public void click(DateBean bean) {
                DateDetailActivity_.intent(DateListFragment.this).bean(bean).startForResult(DETAIL);
            }

            @Override
            public void longClick(final DateBean bean) {
                switch (listType) {
                    case FAVORITE:
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setItems(new String[]{"取消收藏"}, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancleFavorite(bean);
                            }
                        }).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        break;
                }
            }
        });
        return adapter;
    }

    @Override
    public void httpPost() {
        super.httpPost();
        if (adFragment != null)
            adFragment.getADBanner();
    }

    @Override
    public void initListView() {
        switch (listType) {
            case NEAR:
            case THROUGH:
                View view = View.inflate(getActivity(), R.layout.fragment_content, null);
                listview.addHeaderView(view);
                getChildFragmentManager().beginTransaction().replace(R.id.content, adFragment = HeadAdFragment_.builder().ADCacheKey(ADCacheKey).build()).commitAllowingStateLoss();
                break;
        }
    }

    @Override
    public BaseApi getApi() {
        return new DateListApi(getActivity(), listType);
    }

    @Override
    public Class<?> getClazz() {
        return DateBean.class;
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
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        switch (listType) {
            case NEAR:
                return getClass().getName();
            case THROUGH:
                return getClass().getName() + "thorugh";
            case MY:
                return getClass().getName() + ac.getLoginUserId();
            case JOIN:
                return getClass().getName() + ac.getLoginUserId() + "join";
            case FAVORITE:
                return getClass().getName() + ac.getLoginUserId() + "favorite";
            case OTHER:
                return getClass().getName() + otherId;
        }
        return "";
    }

    @Override
    public void initParams() {
        switch (listType){
            case NEAR:
                api.initParam(ac.cs.getLat(), ac.cs.getLng());
                break;
            case THROUGH:
                api.initParam(lat, lng);
                break;
            case OTHER:
                api.initParam(otherId);
                break;
        }
    }

    @Override
    public int getEmptyView() {
        switch (listType) {
            case MY:
            case OTHER:
                return R.layout.my_date_list_empty;
            case JOIN:
                return R.layout.my_date_join_list_empty;
            case FAVORITE:
                return R.layout.my_date_favorite_list_empty;
        }
        return super.getEmptyView();
    }

    @OnActivityResult(DETAIL)
    void onDetailResult(int result, Intent data) {
        if(adapter == null){
            return;
        }
        if (result == 2) {
            DateBean bean = (DateBean) data
                    .getSerializableExtra("bean");
            List<DateBean> beans = adapter.getList();
            int position = -1;
            for (DateBean b : beans) {
                if (b.getDtid() == (bean.getDtid())) {
                    position = beans.indexOf(b);
                    break;
                }
            }
            if (position != -1){
                adapter.remove(position);
                adapter.notifyDataSetChanged();
                if (adapter.getCount() == 0) {
                    swipeRefresh();
                }
            }
        } else if (result == 4) {
            DateBean bean = (DateBean) data
                    .getSerializableExtra("bean");
            List<DateBean> beans = adapter.getList();
            int position = -1;
            for (DateBean b : beans) {
                if (b.getDtid() == (bean.getDtid())) {
                    position = beans.indexOf(b);
                    break;
                }
            }
            if (position != -1){
                adapter.remove(position);
                adapter.notifyDataSetChanged();
                if (adapter.getCount() == 0) {
                    swipeRefresh();
                }
            }
        } else if (result == 5) {
            DateBean bean = (DateBean) data
                    .getSerializableExtra("bean");
            List<DateBean> beans = adapter.getList();
            int position = -1;
            for (DateBean b : beans) {
                if (b.getDtid() == (bean.getDtid())) {
                    position = beans.indexOf(b);
                    break;
                }
            }
            if (position == -1) {
                adapter.add(0, bean);
                adapter.notifyDataSetChanged();
            }
        }
    }

    void cancleFavorite(final DateBean bean) {
        RequestParams rp = getAjaxParams();
        rp.put("dtid", bean.getDtid());
        rp.put("type", 1);
        ac.finalHttp.post(URL.DATE_COLLECT, rp, new MyJsonHttpResponseHandler(getActivity(),getString(R.string.loading),null) {

            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                showCustomToast("删除成功");
                Intent i = new Intent();
                i.putExtra("bean", bean);
                onDetailResult(2, i);
            }
        });
    }
}

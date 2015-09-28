package com.quanliren.quan_two.activity.group.quan;

import android.content.Intent;
import android.os.Bundle;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.group.DongTaiDetailActivity_;
import com.quanliren.quan_two.adapter.QuanListAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DongTaiBean;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.fragment.base.BaseViewPagerListFragment;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;

import java.util.List;

/**
 * Created by Shen on 2015/7/6.
 */
@EFragment
public class QuanListFragment extends BaseViewPagerListFragment<DongTaiBean> {

    public static final int QuanDetailResponse = 1;
    public static final int QuanDetailResult_delete = 2;

    RuntimeExceptionDao<FilterBean, Integer> filterDao;

    public enum LISTTYPE {
        ALL, MYCARE, MY, OTHER
    }

    @FragmentArg
    public LISTTYPE listType;
    @FragmentArg
    public String otherId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        filterDao = DBHelper.getDao_(getActivity(), FilterBean.class);
    }

    @Override
    public int getConvertViewRes() {
        return R.layout.fragment_list;
    }

    @Override
    public BaseAdapter<DongTaiBean> getAdapter() {
        QuanListAdapter adapter = new QuanListAdapter(getActivity());
        adapter.setListener(new QuanListAdapter.IQuanAdapter() {
            @Override
            public void detailClick(Object bean) {
                if (Util.isFastDoubleClick()) {
                    return;
                }
                DongTaiBean dt = (DongTaiBean) bean;
                if ("1".equals(dt.getType())) {
                } else {
                    DongTaiDetailActivity_.intent(QuanListFragment.this).bean((DongTaiBean) bean)
                            .startForResult(QuanDetailResponse);
                }
            }
        });
        return adapter;
    }

    @Override
    public BaseApi getApi() {
        return new QuanListApi(getActivity(), listType);
    }

    @Override
    public Class<?> getClazz() {
        return DongTaiBean.class;
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        switch (listType) {
            case MYCARE:
                return getClass().getName() + ac.getLoginUserId();
            case ALL:
                return getClass().getName();
            case MY:
                return getClass().getName() + ac.getLoginUserId() + "my";
            case OTHER:
                return getClass().getName() + otherId;
        }
        return getClass().getName();
    }

    @Override
    public void initParams() {
        switch (listType) {
            case OTHER:
                api.initParam(otherId);
                break;
            case ALL:
                api.initParam();
                break;
        }
    }

    @Override
    public int getEmptyView() {
        return R.layout.quan_list_mycare_empty;
    }

    @OnActivityResult(QuanDetailResponse)
    void onPublishResult(int result, Intent data) {
        if (result == QuanDetailResult_delete) {
            DongTaiBean bean = (DongTaiBean) data.getSerializableExtra("bean");
            if(adapter==null){
                return;
            }
            List<DongTaiBean> beans = adapter.getList();
            int position = -1;
            for (DongTaiBean b : beans) {
                if (b.getDyid().equals(bean.getDyid())) {
                    position = beans.indexOf(b);
                    break;
                }
            }
            if (position != -1) {
                adapter.remove(position);
                adapter.notifyDataSetChanged();
                if (adapter.getCount() == 0) {
                    swipeRefresh();
                }
            }
        }
    }

    public void only_girl() {
        updateFilter("0");
    }

    public void only_boy() {
        updateFilter("1");
    }

    public void all() {
        updateFilter(null);
    }

    public void updateFilter(String value) {

        List<FilterBean> filterList = filterDao.queryForEq(FilterBean.KEY, QuanListApi.QUAN_SEX);

        if (value == null) {
            if (filterList.size() > 0) {
                filterDao.delete(filterList);
            }
        } else {
            FilterBean fb = null;
            if (filterList.size() > 0) {
                fb = filterList.get(0);
            } else {
                fb = new FilterBean();
                fb.setKey(QuanListApi.QUAN_SEX);
            }
            fb.setValue(value);
            filterDao.createOrUpdate(fb);
        }

        setSubTitle();
        swipeRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        setSubTitle();
    }

    public void setSubTitle() {
        /*List<FilterBean> filterList = filterDao.queryForEq(FilterBean.KEY, QuanListApi.QUAN_SEX);
        if (filterList.size() > 0) {
            FilterBean fb = filterList.get(0);
            if (fb.getValue().equals("0")) {
                getSupportActionBar().setSubtitle("只看女生");
            } else {
                getSupportActionBar().setSubtitle("只看男生");
            }
        } else {
            getSupportActionBar().setSubtitle(null);
        }*/
    }

    @Override
    public boolean needLocation() {
        if(listType == LISTTYPE.ALL){
            return true;
        }
        return super.needLocation();
    }
}

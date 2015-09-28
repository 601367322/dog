package com.quanliren.quan_two.activity.group.quan;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.URL;

import java.util.List;

/**
 * Created by Shen on 2015/7/6.
 */
public class QuanListApi extends BaseApi {

    public static final String QUAN_SEX = "quan_sex";

    QuanListFragment.LISTTYPE listType;

    RuntimeExceptionDao<FilterBean, Integer> customFilterBeanDao;

    public QuanListApi(Context context, QuanListFragment.LISTTYPE listType) {
        super(context);
        this.listType = listType;

        if (context != null) {
            customFilterBeanDao = DBHelper.getDao_(context, FilterBean.class);
        }
    }

    @Override
    public String getUrl() {
        switch (listType) {
            case MYCARE:
                return URL.DONGTAI_FRIEND;
            case MY:
            case OTHER:
                return URL.PERSONALDONGTAI;
            default:
                return URL.DONGTAI;
        }
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        switch (listType) {
            case MYCARE:
            case MY:
                break;
            case OTHER:
                getParams().put("otherid", obj[0]);
                break;
            case ALL:
                List<FilterBean> listCB = customFilterBeanDao.queryForEq(FilterBean.KEY, QUAN_SEX);

                if (listCB != null && listCB.size() > 0) {
                    FilterBean bean = listCB.get(0);
                    getParams().put("sex", bean.getValue());
                }

                getParams().put("cityid",ac.cs.getLocationID());
                getParams().put("latitude", ac.cs.getLat());
                getParams().put("longitude", ac.cs.getLng());
                break;
        }
    }
}

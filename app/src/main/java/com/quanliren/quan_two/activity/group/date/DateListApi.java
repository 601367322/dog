package com.quanliren.quan_two.activity.group.date;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.URL;

import java.util.List;

/**
 * Created by Shen on 2015/7/3.
 */
public class DateListApi extends BaseApi {

    public static final String DATE_USEX = "DATE_USEX";
    public static final String DATE_SEX = "DATE_SEX";
    public static final String DATE_DTIME = "DATE_DTIME";
    public static final String DATE_WHOPAY = "DATE_WHOPAY";

    RuntimeExceptionDao<FilterBean, Integer> customFilterBeanDao;
    NearPeopleActivity.NEARLISTTYPE dateListType;

    public DateListApi(Context context, NearPeopleActivity.NEARLISTTYPE dateListType) {
        super(context);
        if (context != null) {
            customFilterBeanDao = DBHelper.getDao_(context, FilterBean.class);
        }
        this.dateListType = dateListType;
    }

    @Override
    public String getUrl() {
        switch (dateListType) {
            case NEAR:
            case THROUGH:
                return URL.DATE_LIST;
            case MY:
                return URL.MY_DATE_LIST;
            case JOIN:
                return URL.MY_APPLY_DATE_LIST;
            case FAVORITE:
                return URL.DATE_MY_FAVORITE;
            case OTHER:
                return URL.OTHERP_DATE;
        }
        return "";
    }

    @Override
    public void initParam(Object... obj) {
        try {
            switch (dateListType) {
                case NEAR:
                case THROUGH:
                    setParamsFilter(DATE_USEX,"usex");
                    setParamsFilter(DATE_SEX,"sex");
                    setParamsFilter(DATE_DTIME,"dtime");
                    setParamsFilter(DATE_WHOPAY,"whopay");
                    getParams().put("longitude", obj[1]);
                    getParams().put("latitude", obj[0]);
                    break;
                case MY:
                    break;
                case JOIN:
                    break;
                case FAVORITE:
                    break;
                case OTHER:
                    getParams().put("otherid", obj[0]);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParamsFilter(String key, String pkey) {
        List<FilterBean> list = customFilterBeanDao.queryForEq(FilterBean.KEY, key);

        if (list != null && list.size() > 0) {
            FilterBean bean = list.get(0);
            getParams().put(pkey, bean.getValue());
        }
    }
}

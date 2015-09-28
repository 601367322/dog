package com.quanliren.quan_two.activity.near;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.URL;

import java.util.List;

/**
 * Created by Shen on 2015/7/1.
 */
public class NearPeopleApi extends BaseApi {

    RuntimeExceptionDao<FilterBean, Integer> customFilterBeanDao;

    public static final String NEAR_PEOPLE_SEX = "near_people_sex";

    public NearPeopleApi(Context context) {
        super(context);
        if (context != null) {
            customFilterBeanDao = DBHelper.getDao_(context, FilterBean.class);
        }
    }

    @Override
    public String getUrl() {
        return URL.NearUserList;
    }

    @Override
    public void initParam(Object... obj) {
        try {
            List<FilterBean> listCB = customFilterBeanDao.queryForEq(FilterBean.KEY, NEAR_PEOPLE_SEX);

            if (listCB != null && listCB.size() > 0) {
                FilterBean bean = listCB.get(0);
                getParams().put("sex", bean.getValue());
            }

            getParams().put("longitude", obj[1]);
            getParams().put("latitude", obj[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

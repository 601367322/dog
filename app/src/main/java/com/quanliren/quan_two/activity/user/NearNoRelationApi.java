package com.quanliren.quan_two.activity.user;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;

import java.util.List;

/**
 * Created by Shen on 2015/7/8.
 */
public class NearNoRelationApi extends BaseApi {

    RuntimeExceptionDao<FilterBean, Integer> customFilterBeanDao;

    public static final String NEAR_PEOPLE = "near_people_no_relation";
    public NearNoRelationApi(Context context) {
        super(context);
        if (context != null) {
            customFilterBeanDao = DBHelper.getDao_(context, FilterBean.class);
        }
    }

    @Override
    public String getUrl() {
        return URL.NEAR_NO_RELATION;
    }
    @Override
    public void initParam(Object... obj) {
        try {
            List<FilterBean> listCB = customFilterBeanDao.queryForEq(FilterBean.KEY, NEAR_PEOPLE);

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

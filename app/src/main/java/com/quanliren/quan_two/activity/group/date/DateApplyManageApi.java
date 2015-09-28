package com.quanliren.quan_two.activity.group.date;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/9.
 */
public class DateApplyManageApi extends BaseApi {

    public DateApplyManageApi(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return URL.DATE_APPLY_MANAGE;
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        getParams().put("dtid", obj[0]);
    }
}

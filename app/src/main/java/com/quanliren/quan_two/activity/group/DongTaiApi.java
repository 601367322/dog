package com.quanliren.quan_two.activity.group;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/7.
 */
public class DongTaiApi extends BaseApi {

    @Override
    public String getUrl() {
        return URL.GETDONGTAI_DETAIL;
    }

    public DongTaiApi(Context context) {
        super(context);
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        getParams().put("dyid", obj[0]);
    }
}

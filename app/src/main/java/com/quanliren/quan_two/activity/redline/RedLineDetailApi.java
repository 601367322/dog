package com.quanliren.quan_two.activity.redline;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/1.
 */
public class RedLineDetailApi extends BaseApi {

    public RedLineDetailApi(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return URL.RED_LINE_DETAIL;
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        getParams().put("treadId", obj[0]);
    }
}

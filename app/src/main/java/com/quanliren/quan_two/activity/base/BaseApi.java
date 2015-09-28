package com.quanliren.quan_two.activity.base;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.util.Util;

import java.io.Serializable;

/**
 * Created by Shen on 2015/5/30.
 */
public abstract class BaseApi implements Serializable {

    public Context context;
    public AppClass ac;
    private RequestParams params;

    public int currentPage = 0;

    public BaseApi(Context context) {
        this.context = context;
        if (context != null) {
            ac = (AppClass) context.getApplicationContext();
            params = Util.getAjaxParams(context);
        }
    }

    public abstract String getUrl();

    public RequestParams getParams() {
        return params;
    }

    public void setPage(int currentPage) {
        params.put("p", currentPage);
        this.currentPage = currentPage;
    }

    public void initParam(Object... obj) {
    }

}

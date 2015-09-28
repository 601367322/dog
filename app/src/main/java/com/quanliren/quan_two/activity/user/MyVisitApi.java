package com.quanliren.quan_two.activity.user;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/8.
 */
public class MyVisitApi extends BaseApi {

    public MyVisitApi(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return URL.VISITLIST;
    }
}

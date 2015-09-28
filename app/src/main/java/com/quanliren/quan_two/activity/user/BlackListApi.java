package com.quanliren.quan_two.activity.user;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/9.
 */
public class BlackListApi extends BaseApi {

    public BlackListApi(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return URL.BLACKLIST;
    }
}

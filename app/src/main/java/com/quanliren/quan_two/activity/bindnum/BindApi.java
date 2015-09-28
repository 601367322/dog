package com.quanliren.quan_two.activity.bindnum;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.URL;

import java.util.List;

/**
 * Created by Shen on 2015/7/8.
 */
public class BindApi extends BaseApi {
    public BindApi(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return URL.LOOK_CONTACTS;
    }
}

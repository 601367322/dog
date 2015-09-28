package com.quanliren.quan_two.fragment.message;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/10.
 */
public class MyCareListApi extends BaseApi {

    MyCareListFragment.CARETYPE caretype;

    public MyCareListApi(Context context, MyCareListFragment.CARETYPE caretype) {
        super(context);
        this.caretype = caretype;
    }

    @Override
    public String getUrl() {
        return URL.CONCERNLIST;
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        int type = 1;
        switch (caretype) {
            case FRIEND:
                type = 1;
                break;
            case MYCARE:
                type = 2;
                break;
            case FANS:
                type = 3;
                break;
        }
        getParams().put("type", type);
    }
}

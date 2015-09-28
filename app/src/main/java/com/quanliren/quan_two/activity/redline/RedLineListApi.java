package com.quanliren.quan_two.activity.redline;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

import java.io.Serializable;

/**
 * Created by Shen on 2015/6/30.
 */
public class RedLineListApi extends BaseApi implements Serializable {

    RedLineFragment.RedLineMode redLineType;

    @Override
    public String getUrl() {
        return URL.RED_LINE_RANKING;
    }

    public RedLineListApi(Context context, RedLineFragment.RedLineMode redLineType) {
        super(context);
        this.redLineType = redLineType;
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        int type = 1;
        switch (redLineType) {
            case HOT:
                type = 1;
                break;
            case NEW:
                type = 2;
                break;
            case MY:
                type = 3;
                break;
            case JOIN:
                type = 4;
                break;
            case FAVORITE:
                type = 5;
                break;
        }
        getParams().put("type", type);
    }
}

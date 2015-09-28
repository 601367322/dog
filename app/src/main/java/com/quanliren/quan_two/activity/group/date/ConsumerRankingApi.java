package com.quanliren.quan_two.activity.group.date;

import android.content.Context;

import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.util.URL;

/**
 * Created by Shen on 2015/7/20.
 */
public class ConsumerRankingApi extends BaseApi {

    ConsumerRankingFragment.ConsumerRankingType rankingType;

    public ConsumerRankingApi(Context context, ConsumerRankingFragment.ConsumerRankingType rankingType) {
        super(context);
        this.rankingType = rankingType;
    }

    @Override
    public String getUrl() {
        return URL.CONSUMER_RANKING;
    }

    @Override
    public void initParam(Object... obj) {
        super.initParam(obj);
        int type = 0;
        switch (rankingType) {
            case MONTH:
                type = 0;
                break;
            case ALL:
                type = 1;
                break;
        }
        getParams().put("type", type);
    }
}

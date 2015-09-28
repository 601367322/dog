package com.quanliren.quan_two.activity.group.date;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.fragment.base.ActionBarTabFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Shen on 2015/7/20.
 */
@EFragment(R.layout.fragment_my_dongtai_viewpager)
public class ConsumerRankingNavFragment extends ActionBarTabFragment {

    @Override
    public String getLeftTabText() {
        return "本月消费";
    }

    @Override
    public String getRightTabText() {
        return "总消费";
    }

    @Override
    public void initFragments() {
        views.add(ConsumerRankingFragment_.builder().rankingType(ConsumerRankingFragment.ConsumerRankingType.MONTH).build());
        views.add(ConsumerRankingFragment_.builder().rankingType(ConsumerRankingFragment.ConsumerRankingType.ALL).build());
    }

    @Override
    public boolean showLeftBtn() {
        return true;
    }
}

package com.quanliren.quan_two.activity.near;

import android.os.Bundle;
import android.view.View;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.ThroughActivity;
import com.quanliren.quan_two.activity.group.ThroughActivity_;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Shen on 2015/7/1.
 */
@OptionsMenu(R.menu.near_people_menu)
@EActivity(R.layout.activity_near_fragment)
public class NearPeopleActivity extends BaseActivity {

    @ViewById
    public View ground_through;

    @OrmLiteDao(helper = DBHelper.class)
    RuntimeExceptionDao<FilterBean, Integer> filterDao;

    private NearPeopleFragment nearPeopleFragment;

    public enum NEARLISTTYPE {
        NEAR, THROUGH, MY, JOIN, FAVORITE, OTHER
    }

    @Extra
    public NEARLISTTYPE listType = NEARLISTTYPE.NEAR;

    @Extra
    public double lat;
    @Extra
    public double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        if (listType == NEARLISTTYPE.NEAR) {
            ground_through.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, nearPeopleFragment = NearPeopleFragment_.builder().build()).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, nearPeopleFragment = NearPeopleFragment_.builder().listType(listType).lat(lat).lng(lng).build()).commitAllowingStateLoss();
            ground_through.setVisibility(View.GONE);
        }
    }

    @Click
    void ground_through() {
        if (Util.isFastDoubleClick()) {
            return;
        }
        User user = getHelper().getUserInfo();
        if (user.getIsvip() == 0) {
            Util.goVip(this);
            return;
        }
        ThroughActivity_.intent(this).throughtype(ThroughActivity.THROUGHTYPE.PEOPLE).start();
    }

    @OptionsItem
    public void only_girl() {
        updateFilter("0");
    }

    @OptionsItem
    public void only_boy() {
        updateFilter("1");
    }

    @OptionsItem
    public void all() {
        updateFilter(null);
    }

    public void updateFilter(String value) {

        List<FilterBean> filterList = filterDao.queryForEq(FilterBean.KEY, NearPeopleApi.NEAR_PEOPLE_SEX);

        if (value == null) {
            if (filterList.size() > 0) {
                filterDao.delete(filterList);
            }
        } else {
            FilterBean fb = null;
            if (filterList.size() > 0) {
                fb = filterList.get(0);
            } else {
                fb = new FilterBean();
                fb.setKey(NearPeopleApi.NEAR_PEOPLE_SEX);
            }
            fb.setValue(value);
            filterDao.createOrUpdate(fb);
        }

        setSubTitle();
        nearPeopleFragment.swipeRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listType == NEARLISTTYPE.NEAR) {
            getSupportActionBar().setTitle(R.string.near_peoples);
        } else {
            getSupportActionBar().setTitle(R.string.vip_through);
        }
        setSubTitle();
    }

    public void setSubTitle() {
        List<FilterBean> filterList = filterDao.queryForEq(FilterBean.KEY, NearPeopleApi.NEAR_PEOPLE_SEX);
        if (filterList.size() > 0) {
            FilterBean fb = filterList.get(0);
            if (fb.getValue().equals("0")) {
                getSupportActionBar().setSubtitle("只看女生");
            } else {
                getSupportActionBar().setSubtitle("只看男生");
            }
        } else {
            getSupportActionBar().setSubtitle(null);
        }
    }
}

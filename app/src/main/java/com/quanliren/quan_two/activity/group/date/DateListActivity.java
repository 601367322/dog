package com.quanliren.quan_two.activity.group.date;

import android.view.View;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.ThroughActivity;
import com.quanliren.quan_two.activity.group.ThroughActivity_;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * Created by Shen on 2015/7/3.
 */
@OptionsMenu(R.menu.date_list_menu)
@EActivity(R.layout.activity_date_fragment)
public class DateListActivity extends BaseActivity {

    private static final int FILTER = 1;
    private static final int PUBLISH = 2;
    private static final int DETAIL = 3;
    @ViewById
    public View date_through;
    @ViewById
    public View date_publish;
    @Extra
    public NearPeopleActivity.NEARLISTTYPE listType = NearPeopleActivity.NEARLISTTYPE.NEAR;

    @Extra
    public double lat;
    @Extra
    public double lng;

    DateListFragment fragment;

    @Override
    public void init() {
        if (listType == NearPeopleActivity.NEARLISTTYPE.NEAR) {
            date_through.setVisibility(View.VISIBLE);
            date_publish.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment = DateListFragment_.builder().autoStart(true).listType(NearPeopleActivity.NEARLISTTYPE.NEAR).build()).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment = DateListFragment_.builder().autoStart(true).listType(listType).lat(lat).lng(lng).build()).commitAllowingStateLoss();
            date_through.setVisibility(View.GONE);
            date_publish.setVisibility(View.GONE);
        }
    }

    @Click
    public void date_publish() {
        User user = getHelper().getUserInfo();
        if (user == null) {
            startLogin();
            return;
        }
        RequestParams pub = getAjaxParams();
        ac.finalHttp.post(URL.AFFIRM_PUB, pub, new MyJsonHttpResponseHandler(this, "正在请求", null) {

            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                String ratify = jo.getJSONObject(URL.RESPONSE).getString("ratify");
                if (Integer.valueOf(ratify) == 0) {
                    DatePublishActivity_.intent(DateListActivity.this).startForResult(PUBLISH);
                } else if (Integer.valueOf(ratify) == 1) {
                    showCustomToast("您有正在进行中的约会");
                }
            }
        });
    }


    @Click
    public void date_through() {
        if (Util.isFastDoubleClick()) {
            return;
        }
        User user = getHelper().getUserInfo();
        if (user.getIsvip() == 0) {
            Util.goVip(this);
            return;
        }
        ThroughActivity_.intent(this).throughtype(ThroughActivity.THROUGHTYPE.DATE).start();
    }

    @OptionsItem
    public void filter() {
        DateFilterActivity_.intent(this).startForResult(FILTER);
    }

    @OnActivityResult(FILTER)
    void onFilterResult(int result) {
        if (result == RESULT_OK) {
            if (fragment != null) {
                fragment.swipeRefresh();
            }
        }
    }

    @OnActivityResult(PUBLISH)
    void onPublishResult(int result) {
        if (result == RESULT_OK) {
            if (fragment != null) {
                fragment.swipeRefresh();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listType == NearPeopleActivity.NEARLISTTYPE.NEAR) {
            getSupportActionBar().setTitle(R.string.tt_date);
        } else {
            getSupportActionBar().setTitle(R.string.vip_through);
        }
    }
}

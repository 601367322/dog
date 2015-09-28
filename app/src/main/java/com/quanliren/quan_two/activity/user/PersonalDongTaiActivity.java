package com.quanliren.quan_two.activity.user;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;

@EActivity(R.layout.activity_only_fragment)
public class PersonalDongTaiActivity extends BaseActivity {

    @Extra
    public String otherid;
    @Extra
    public QuanListFragment.LISTTYPE listtype = QuanListFragment.LISTTYPE.MY;

    QuanListFragment fragment;

    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment = QuanListFragment_.builder().otherId(otherid).listType(listtype).build()).commitAllowingStateLoss();

        refresh();
    }

    @UiThread
    public void refresh(){
        fragment.refresh();
    }
}

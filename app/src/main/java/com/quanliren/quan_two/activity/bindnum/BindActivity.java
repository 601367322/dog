package com.quanliren.quan_two.activity.bindnum;

import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.chose_contacts)
public class BindActivity extends BaseActivity{
    @ViewById(R.id.title_bar)
    TextView title_bar;
    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, BindFragment_.builder().build()).commitAllowingStateLoss();
    }
}

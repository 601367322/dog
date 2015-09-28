package com.quanliren.quan_two.activity.seting;

import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.remind_message)
public class RemindMessageActivity extends BaseActivity implements
        OnCheckedChangeListener {

    @ViewById
    SwitchCompat voice_cb;
    @ViewById
    SwitchCompat msg_cb;
    @ViewById
    SwitchCompat zhendong_cb;


    @Override
    public void init() {
        super.init();
        initCb();
        voice_cb.setOnCheckedChangeListener(this);
        msg_cb.setOnCheckedChangeListener(this);
        zhendong_cb.setOnCheckedChangeListener(this);
    }

    public void initCb() {
        int num = ac.cs.getMSGOPEN();
        if (num == 1) {
            msg_cb.setChecked(true);
            if (ac.cs.getVIDEOOPEN() == 1) {
                voice_cb.setChecked(true);
            } else {
                voice_cb.setChecked(false);
            }
            if (ac.cs.getZHENOPEN() == 1) {
                zhendong_cb.setChecked(true);
            } else {
                zhendong_cb.setChecked(false);
            }
        } else {
            msg_cb.setChecked(false);
            voice_cb.setChecked(false);
            zhendong_cb.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.voice_cb:
                ac.cs.setVIDEOOPEN(isChecked ? 1 : 0);
                if (isChecked)
                    ac.cs.setMSGOPEN(1);
                else {
//				if(!zhendong_cb.isChecked()){
//					ac.cs.setMSGOPEN(0);
//				}
                }
                break;
            case R.id.msg_cb:
                ac.cs.setMSGOPEN(isChecked ? 1 : 0);
                break;
            case R.id.zhendong_cb:
                ac.cs.setZHENOPEN(isChecked ? 1 : 0);
                if (isChecked)
                    ac.cs.setMSGOPEN(1);
                else {
//				if(!voice_cb.isChecked()){
//					ac.cs.setMSGOPEN(0);
//				}
                }
                break;
        }
        initCb();
    }
}

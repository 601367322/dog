package com.quanliren.quan_two.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.redline.RedLineBean;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class ZanLinearLayout extends LinearLayout {

    private RedLineBean bean;
    public TextView zan;

    public ZanLinearLayout(Context context) {
        super(context);
    }


    public ZanLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZanLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
        zan = (TextView) findViewById(R.id.zan);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean != null) {
                    RequestParams params = Util.getAjaxParams(getContext());
                    params.put("treadId", bean.id);
                    if (bean.zambiastate.equals("1")) {
                        params.put("type", 1);
                    } else {
                        params.put("type", 0);
                    }
                    ((AppClass) getContext().getApplicationContext()).finalHttp.post(URL.RED_ZAN, params, new MyJsonHttpResponseHandler(getContext(), getContext().getResources().getString(R.string.loading)) {
                        @Override
                        public void onSuccessRetCode(JSONObject jo) throws Throwable {
                            if (bean.zambiastate.equals("0")) {
                                bean.zambiastate = "1";
                                bean.zambia++;
                                setSelected(true);
                            } else {
                                bean.zambiastate = "0";
                                bean.zambia--;
                                setSelected(false);
                            }
                            zan.setText(bean.zambia + "");
                            EventBus.getDefault().post(bean);
                        }
                    });
                }
            }
        });
    }


    public RedLineBean getBean() {
        return bean;
    }

    public void setBean(RedLineBean bean) {
        this.bean = bean;
        zan.setText(bean.zambia + "");
        if (bean.zambiastate.equals("1")) {
            setSelected(true);
        } else {
            setSelected(false);
        }
    }
}

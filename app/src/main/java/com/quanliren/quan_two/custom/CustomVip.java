package com.quanliren.quan_two.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.quanliren.quan_two.activity.R;

public class CustomVip extends ImageView {

    public CustomVip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomVip(Context context) {
        super(context);
    }

    public CustomVip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setVip(int vip) {
        setVisibility(View.GONE);
        switch (vip) {
            case 0:
                break;
            case 1:
                setVisibility(View.VISIBLE);
                setImageResource(R.drawable.ic_vip_1);
                break;
            case 2:
                setVisibility(View.VISIBLE);
                setImageResource(R.drawable.ic_vip_2);
                break;
            case 3:
                setVisibility(View.VISIBLE);
                setImageResource(R.drawable.ic_vip_3);
                break;
        }
    }

}

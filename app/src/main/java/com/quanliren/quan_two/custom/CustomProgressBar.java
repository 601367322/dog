package com.quanliren.quan_two.custom;


import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;


public class CustomProgressBar extends Dialog {

    private TextView tv;
    private String str;
    private FrameLayout designCenterFrameLayout;
    private ImageView designCenterLinearLayout;

    public CustomProgressBar(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    protected CustomProgressBar(Context context, boolean cancelable,
                                OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public CustomProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setMessage(String str) {
        this.str = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.customprogress, null);
        tv = (TextView) view.findViewById(R.id.id_tv_loadingmsg);
        designCenterFrameLayout = (FrameLayout) view.findViewById(R.id.designCenterFrameLayout);
        designCenterLinearLayout = (ImageView) view.findViewById(R.id.designCenterLinearLayout);
        this.tv.setText(str);
        setContentView(view);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
//		listent.onAnimationEnd(null);
        designCenterLinearLayout.animate().setDuration(1000).setInterpolator(new LinearInterpolator()).setListener(listent).rotationYBy(360);
    }

    public void stopAnimation() {
        designCenterFrameLayout.clearAnimation();
        designCenterLinearLayout.clearAnimation();
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
        stopAnimation();
        listent = null;
    }

    public void setMsg(String txt) {
        tv.setText(txt);
    }

    Animator.AnimatorListener listent = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            designCenterLinearLayout.animate().setDuration(1000).setInterpolator(new LinearInterpolator()).setListener(listent).rotationYBy(360);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }
    };
}

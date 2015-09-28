package com.quanliren.quan_two.custom;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;


public class PopFactory extends PopupWindow {

	LinearLayout btn_ll;
	View parent;
	public boolean isShow;
	
	public PopFactory(Context context, String[] strs, OnClickListener click, View parent) {
		super(context);
		this.parent=parent;
		View v=null;
		this.setContentView(v= View.inflate(context, R.layout.menu_popup, null));
		btn_ll=(LinearLayout) v.findViewById(R.id.btn_ll);
		for(int i =0 ;i<strs.length;i++){
			LinearLayout.LayoutParams btnlp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ImageUtil.dip2px(context, 40));
			Button btn=new Button(context);
			btn.setGravity(Gravity.CENTER);
			btn.setText(strs[i]);
			btn.setTextColor(context.getResources().getColor(R.color.dialog_txt));
			btn.setLayoutParams(btnlp);
			if(i==0){
				if(i==strs.length-1){
					btn.setBackgroundResource(R.drawable.input_one_btn);
				}else{
                    btn.setTextColor(context.getResources().getColor(R.color.signature));
                    btn.setTextSize(17);
					btn.setBackgroundResource(R.drawable.input_top_btn);
				}
			}else if(i==strs.length-1){
				btn.setBackgroundResource(R.drawable.input_btm_btn);
			}else{
				btn.setBackgroundResource(R.drawable.input_mid_btn);
			}
			btn.setId(i);
			btn.setOnClickListener(click);
			btn.setTextSize(16);
			btn_ll.addView(btn);
		}
		this.setOutsideTouchable(true);
		this.setFocusable(false);
		this.setAnimationStyle(R.style.PopupAnimation);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(null);
        v.findViewById(R.id.exit).setOnClickListener(click);
	}
	
	public PopFactory(Context context, AttributeSet attrs){
		super(context, attrs);   
	}
	
	
	public boolean showMenu() {
		parent.animate().setDuration(200).alpha(1).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				parent.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		}).start();

		this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		this.update();
		isShow = true;
		return isShow;
	}

	public boolean closeMenu() {
		parent.animate().setDuration(200).alpha(0).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animation) {
				parent.clearAnimation();
				parent.setVisibility(View.GONE);
			}
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		}).start();
		this.dismiss();
		isShow = false;
		return isShow;
	}
	
	public void toogle(){
		if(isShow){
			closeMenu();
		}else{
			showMenu();
		}
	}
}

package com.quanliren.quan_two.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;

import org.androidannotations.api.SdkVersionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StateTextViewBg extends TextView {

    AtomicBoolean b = new AtomicBoolean(false);
    List<StateBean> list = new ArrayList<StateBean>();
    boolean large;
    Path path;
    int width = 0;
    ShapeDrawable mShapeDrawable;
    int height = 0;
    public StateTextViewBg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    public StateTextViewBg(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public StateTextViewBg(Context context) {
        super(context);
        init(context, null, 0);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {

        String str = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        width = ImageUtil.dip2px(context, Float.valueOf(str.substring(0, str.indexOf("."))));
        String str1 = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        height = ImageUtil.dip2px(context, Float.valueOf(str1.substring(0, str1.indexOf("."))));

        try {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.StateTextView, defStyleAttr, 0);

            large = a.getBoolean(R.styleable.StateTextView_large, false);
            a.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        list.add(new StateBean(R.drawable.ic_state_normal_large,
                R.drawable.ic_state_normal_large, R.string.state_normal,
                R.color.state_quite_color_large));
        list.add(new StateBean(R.drawable.ic_state_dinner,
                R.drawable.ic_state_dinner_large, R.string.state_dinner,
                R.color.state_dinner_color_large));
        list.add(new StateBean(R.drawable.ic_state_movie,
                R.drawable.ic_state_movie_large, R.string.state_movie,
                R.color.state_movie_color_large));
        list.add(new StateBean(R.drawable.ic_state_car,
                R.drawable.ic_state_car_large, R.string.state_car,
                R.color.state_car_color_large));
        list.add(new StateBean(R.drawable.ic_state_friend,
                R.drawable.ic_state_friend_large, R.string.state_friend,
                R.color.state_friend_color_large));
        list.add(new StateBean(R.drawable.ic_state_girl,
                R.drawable.ic_state_girl_large, R.string.state_girl,
                R.color.state_girl_color_large));

        setGravity(Gravity.CENTER_VERTICAL);

        setState(1);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setState(int state) {
        if (state >= list.size()) {
            return;
        }
        StateBean sb = list.get(state);
        setText(sb.String);
        Drawable icon = getResources().getDrawable(sb.img);
        icon.setBounds(0, 0, ImageUtil.dip2px(getContext(), 20),
                ImageUtil.dip2px(getContext(), 20));
        setTextColor(Color.WHITE);

        path = new Path();
        // 设置多边形
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width - ImageUtil.dip2px(getContext(), 10), height);
        path.lineTo(width, height / 2);
        path.lineTo(width - ImageUtil.dip2px(getContext(), 10), 0);
        // 使这些点封闭成多边形
        path.close();

        // PathShape后面两个参数分别是高度和宽度
        mShapeDrawable = new ShapeDrawable(
                new PathShape(path, width, height));

        // 得到画笔paint对象并设置其颜色
        mShapeDrawable.getPaint().setColor(Color.parseColor(getResources().getString(sb.color)));

        // 设置图像显示的区域
        mShapeDrawable.setBounds(0, 0, width, height);

        if (SdkVersionHelper.getSdkInt() >= 16) {
            setBackground(mShapeDrawable);
        } else {
            setBackgroundDrawable(mShapeDrawable);
        }

        setPadding(ImageUtil.dip2px(getContext(), 2), 0, 0, 0);

        setHeight(ImageUtil.dip2px(getContext(), 20));

        setGravity(Gravity.CENTER_VERTICAL);

        setTextSize(14);

        setCompoundDrawables(icon, null, null, null);
        setCompoundDrawablePadding(ImageUtil.dip2px(getContext(), 4));
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setState(int state,int color) {
        if (state >= list.size()) {
            return;
        }
        StateBean sb = list.get(state);
        setText(sb.String);
        Drawable icon = getResources().getDrawable(sb.img);
        icon.setBounds(0, 0, ImageUtil.dip2px(getContext(), 20),
                ImageUtil.dip2px(getContext(), 20));
        setTextColor(Color.WHITE);

        path = new Path();
        // 设置多边形
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width - ImageUtil.dip2px(getContext(), 10), height);
        path.lineTo(width, height / 2);
        path.lineTo(width - ImageUtil.dip2px(getContext(), 10), 0);
        // 使这些点封闭成多边形
        path.close();

        // PathShape后面两个参数分别是高度和宽度
        mShapeDrawable = new ShapeDrawable(
                new PathShape(path, width, height));

        // 得到画笔paint对象并设置其颜色
        mShapeDrawable.getPaint().setColor(Color.parseColor(getResources().getString(color)));

        // 设置图像显示的区域
        mShapeDrawable.setBounds(0, 0, width, height);

        if (SdkVersionHelper.getSdkInt() >= 16) {
            setBackground(mShapeDrawable);
        } else {
            setBackgroundDrawable(mShapeDrawable);
        }

        setPadding(ImageUtil.dip2px(getContext(), 2), 0, 0, 0);

        setHeight(ImageUtil.dip2px(getContext(), 20));

        setGravity(Gravity.CENTER_VERTICAL);

        setTextSize(14);

        setCompoundDrawables(icon, null, null, null);
        setCompoundDrawablePadding(ImageUtil.dip2px(getContext(), 4));
    }

    class StateBean {
        int img;
        int img_large;
        int String;
        int color;

        public StateBean() {
            super();
            // TODO Auto-generated constructor stub
        }

        public StateBean(int img, int img_large, int string, int color) {
            super();
            this.img = img;
            this.img_large = img_large;
            String = string;
            this.color = color;
        }

    }
}

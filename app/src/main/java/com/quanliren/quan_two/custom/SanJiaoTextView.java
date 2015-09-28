package com.quanliren.quan_two.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.quanliren.quan_two.util.ImageUtil;

import org.androidannotations.api.SdkVersionHelper;

public class SanJiaoTextView extends TextView {


    public SanJiaoTextView(Context context) {
        super(context);
    }

    int width = 0;
    int height = 0;

    public SanJiaoTextView(Context context, AttributeSet attr) {
        super(context, attr);

        String str = attr.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        width = ImageUtil.dip2px(context, Float.valueOf(str.substring(0, str.indexOf("."))));
        String str1 = attr.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        height = ImageUtil.dip2px(context, Float.valueOf(str1.substring(0, str1.indexOf("."))));

        setBackgroundColor(Color.parseColor("#007df8"));
    }

    ShapeDrawable mShapeDrawable;
    Path path;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制图像
//		mShapeDrawable.draw(canvas);
    }

    public int getWidths() {
        return width;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public void setBackgroundColor(int color) {
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
        mShapeDrawable.getPaint().setColor(color);

        // 设置图像显示的区域
        mShapeDrawable.setBounds(0, 0, width, height);

        if (SdkVersionHelper.getSdkInt() >= 16) {
            setBackground(mShapeDrawable);
        } else {
            setBackgroundDrawable(mShapeDrawable);
        }
    }
}

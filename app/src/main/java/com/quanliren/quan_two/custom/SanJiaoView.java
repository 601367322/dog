package com.quanliren.quan_two.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;

import com.quanliren.quan_two.util.ImageUtil;

public class SanJiaoView extends View {

    public SanJiaoView(Context context) {
        super(context);
    }

    int width = 0;

    public SanJiaoView(Context context, AttributeSet attr) {
        super(context, attr);
        String str = attr.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        width = ImageUtil.dip2px(context, Float.valueOf(str.substring(0, str.indexOf("."))));
        path = new Path();
        // 设置多边形
        path.moveTo(width / 2, 0);
        path.lineTo(0, width / 2);
        path.lineTo(width, width / 2);
        // 使这些点封闭成多边形
        path.close();

        // PathShape后面两个参数分别是高度和宽度
        mShapeDrawable = new ShapeDrawable(
                new PathShape(path, width, width / 2));

        // 得到画笔paint对象并设置其颜色
        mShapeDrawable.getPaint().setColor(Color.parseColor("#007df8"));

        // 设置图像显示的区域
        mShapeDrawable.setBounds(0, 0, width, width / 2);
    }

    ShapeDrawable mShapeDrawable;
    Path path;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制图像
        mShapeDrawable.draw(canvas);
    }

    public int getWidths() {
        return width;
    }
}

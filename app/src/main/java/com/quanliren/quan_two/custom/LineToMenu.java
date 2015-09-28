package com.quanliren.quan_two.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;


/**
 * Created by Administrator on 2014/9/20.
 */
public class LineToMenu extends View {

    Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
    int textSize = 14;

    int color = 0x00ffffff;

    String t = "没有找到？试试这个";

    Paint paintQ = new Paint();
    Path path = new Path();
    ;

    Paint san = new Paint();
    Path sanp = new Path();

    public LineToMenu(Context context) {
        super(context);

        init();

    }

    public LineToMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineToMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        color = getResources().getColor(R.color.actionbar);

        text.setColor(color);
        text.setTextSize(textSize = ImageUtil.dip2px(getContext(), 24));


        paintQ.setAntiAlias(true);
        paintQ.setStyle(Paint.Style.STROKE);
        paintQ.setStrokeWidth(5);
        paintQ.setColor(color);

        san.setAntiAlias(true);
        san.setStyle(Paint.Style.STROKE);
        san.setColor(color);
        san.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float textWidth = text.measureText(t);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间


        float width = (getWidth() - textWidth) / 2;
        float height = (getHeight() - textSize) / 2;

        canvas.drawText(t, width, height, text);
        if(visibCount<=10) {

            path.reset();
            path.moveTo(width + textWidth + 10, height);
            path.quadTo(getWidth() - 100, height / 1.3f, getWidth() - 50, 50);
            canvas.drawPath(path, paintQ);

            sanp.moveTo(getWidth() - 70, 50 + 10);
            sanp.lineTo(getWidth() - 50, 10 + 10);
            sanp.lineTo(getWidth() - 30, 50 + 10);
            canvas.drawPath(sanp, san);
        }
    }

    int visibCount = 0;

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.GONE) {
            visibCount++;
            if(visibCount>10){
                t="去其他地方看看吧~";
            }else if (visibCount > 1) {
                t = "还是没有，再试试~";
            } else if (visibCount > 0) {
                t = "火星来的？再试试~";
            }
        }
    }
}

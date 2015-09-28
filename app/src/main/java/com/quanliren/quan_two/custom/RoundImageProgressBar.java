package com.quanliren.quan_two.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 *
 * @author xiaanming
 */
public class RoundImageProgressBar extends ImageView implements
        ValueAnimator.AnimatorUpdateListener {

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_BORDER_RADIUS = 270;
    private static final int DEFAULT_BORDER_PROGRESS = 180;

    private final Paint mBorderPaint = new Paint();
    PaintFlagsDrawFilter mSetfil = new PaintFlagsDrawFilter(0,
            Paint.FILTER_BITMAP_FLAG);
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderRadius = DEFAULT_BORDER_RADIUS;
    private int mBorderProgress = DEFAULT_BORDER_PROGRESS;

    private boolean mSetupPending;

    RectF oval;

    public RoundImageProgressBar(Context context) {
        super(context);
    }

    public RoundImageProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageProgressBar(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(
                R.styleable.CircleImageView_border_widths, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_colors,
                DEFAULT_BORDER_COLOR);
        mBorderRadius = a.getInt(R.styleable.CircleImageView_border_radius,
                DEFAULT_BORDER_RADIUS);
        mBorderProgress = a.getInt(R.styleable.CircleImageView_border_progress,
                DEFAULT_BORDER_PROGRESS);
        a.recycle();

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    int progress;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (progress < mBorderProgress) {
            progress += 3;
        }
        if (progress >= mBorderProgress) {
            progress = mBorderProgress;
        }
        canvas.drawArc(oval, mBorderRadius + radius, progress, false,
                mBorderPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        oval = new RectF(ImageUtil.dip2px(getContext(), 2), ImageUtil.dip2px(
                getContext(), 2), getWidth()
                - ImageUtil.dip2px(getContext(), 2), getHeight()
                - ImageUtil.dip2px(getContext(), 2));
        setup();
    }

    private void setup() {

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        invalidate();
    }

    Float radius = 0f;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        radius = (Float) animation.getAnimatedValue();
        if (radius == 0) {
            progress = 0;
        }
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        // TODO Auto-generated method stub
        super.setVisibility(visibility);
        progress = 0;
    }
}

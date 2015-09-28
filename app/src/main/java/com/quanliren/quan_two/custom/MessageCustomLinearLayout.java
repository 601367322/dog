package com.quanliren.quan_two.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class MessageCustomLinearLayout extends LinearLayout {
    private int num = 1;

    public void setNum(int num) {
        this.num = num;
    }

    public void changeBtn(int num) {
        this.num = num;
        lpt = new LayoutParams(
                getResources().getDisplayMetrics().widthPixels
                        + ImageUtil.dip2px(getContext(), num * 56),
                LayoutParams.WRAP_CONTENT);
        setLayoutParams(lpt);
    }

    private LayoutParams lpt;
    private AtomicBoolean init = new AtomicBoolean(false);
    private IMessageCustomLinearLayout listener;

    public void setListener(IMessageCustomLinearLayout listener) {
        this.listener = listener;
    }

    public MessageCustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.MessageCustomLinearLayout);
            num = a.getInteger(R.styleable.MessageCustomLinearLayout_btn_num, 1);
            a.recycle();
        }
        lpt = new LayoutParams(
                getResources().getDisplayMetrics().widthPixels
                        + ImageUtil.dip2px(getContext(), num * 56),
                LayoutParams.WRAP_CONTENT);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (init.compareAndSet(false, true))
            setLayoutParams(lpt);
    }

    ;

    // 调用此方法滚动到目标位置
    public void smoothScrollTo(final int fx, final int fy) {
        ValueAnimator animator = ValueAnimator.ofInt(Math.abs(fx), fy)
                .setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (Integer) animation.getAnimatedValue();
                layout(-height, getTop(), getRight(), getBottom());
            }
        });
    }

    float x, y;
    int w;
    boolean isOpen;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (x == 0l)
                    x = event.getX();
                y = event.getY();
                this.setBackgroundColor(getResources().getColor(R.color.title_btn_press));
                break;
            case MotionEvent.ACTION_MOVE:
                w = (int) ((float) x - (float) event.getX());
                if (w < 0) {
                    w = 0;
                }
                int left = this.getLeft() - w;
                if (left < -ImageUtil.dip2px(getContext(), num * 56)) {
                    left = -ImageUtil.dip2px(getContext(), num * 56);
                }
                layout(left, getTop(), getRight(), getBottom());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setBackgroundColor(getResources().getColor(R.color.activity_bg));
                float nx = event.getX();
                float ny = event.getY();
                if (getLeft() > -ImageUtil.dip2px(getContext(), 10) && Math.abs(nx - x) < ImageUtil.dip2px(getContext(), 10) && Math.abs(ny - y) < ImageUtil.dip2px(getContext(), 10)) {
                    if (listener != null)
                        listener.startActivity(this);
                }
                if (getLeft() < -ImageUtil.dip2px(getContext(), 30) && !isOpen) {
                    w = ImageUtil.dip2px(getContext(), num * 56);
                    smoothScrollTo(getLeft(), w);
                    isOpen = true;
                    if (listener != null)
                        listener.change(this);
                } else {
                    x = 0;
                    smoothScrollTo(getLeft(), 0);
                    isOpen = false;
                    if (listener != null)
                        listener.change(null);
                }

                break;
        }
        return true;
    }

    public void open() {
        w = ImageUtil.dip2px(getContext(), num * 56);
        smoothScrollTo(getLeft(), w);
        isOpen = true;
        if (listener != null)
            listener.change(this);
    }

    public void close() {
        x = 0;
        smoothScrollTo(getLeft(), 0);
        isOpen = false;
    }
}
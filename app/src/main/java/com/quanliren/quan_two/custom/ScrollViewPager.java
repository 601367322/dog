package com.quanliren.quan_two.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

public class ScrollViewPager extends ViewPager {

    private boolean mIsEnable = true;
    private static final int MSG_SLIDE = 1;
    private static final long PERIOD = 5000L;
    private Timer timer = new Timer();

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SLIDE:
                    scrollToLeft();
//					LogUtil.d("task");
                default:
                    break;
            }
        }

        ;
    };

    private TimerTask task = new TimerTask() {

        public void run() {
            Message localMessage = Message.obtain();
            localMessage.what = MSG_SLIDE;
            handler.sendMessage(localMessage);
        }
    };

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAutoSlide();
    }

    ;

    public void scrollToLeft() {
        setCurrentItem(getCurrentItem() + 1);
        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);

    }

    public void cancelAutoSlide() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void startAutoSlide() {
        cancelAutoSlide();
        if (this.timer == null) {
            this.timer = new Timer();
            this.task = null;
            this.task = new TimerTask() {

                public void run() {
                    Message localMessage = Message.obtain();
                    localMessage.what = MSG_SLIDE;
                    handler.sendMessage(localMessage);
                }
            };
        }
        this.timer.schedule(this.task, PERIOD, PERIOD);
    }

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsEnable) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsEnable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public void setAdapter(PagerAdapter arg0) {
        super.setAdapter(arg0);
    }

    public void setAdapter(PagerAdapter arg0, int index) {
        super.setAdapter(arg0);
        setCurrentItem(index, false);
    }

    public void setEnableTouchScroll(boolean isEnable) {
        mIsEnable = isEnable;
    }

}

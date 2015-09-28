package com.quanliren.quan_two.custom.emoji;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.Util;

import java.util.concurrent.atomic.AtomicBoolean;

public class AutoHeightLayout extends ResizeLayout implements ResizeLayout.OnResizeListener {

    public static final int KEYBOARD_STATE_NONE = 100;
    public static final int KEYBOARD_STATE_FUNC = 102;
    public static final int KEYBOARD_STATE_BOTH = 103;

    protected Context mContext;
    protected int mAutoHeightLayoutId;
    protected int mAutoViewHeight;
    protected View mAutoHeightLayoutView;
    protected int mKeyboardState = KEYBOARD_STATE_NONE;
    protected AtomicBoolean emojiShow = new AtomicBoolean(false);

    public AutoHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mAutoViewHeight = Util.getDefKeyboardHeight(mContext);
        setOnResizeListener(this);
    }

    public void setAutoHeightLayoutView(View view) {
        mAutoHeightLayoutView = view;
    }

    public void setAutoViewHeight(final int height) {
        int heightDp = ImageUtil.px2dip(mContext, height);
        if (heightDp > 0 && heightDp != mAutoViewHeight) {
            mAutoViewHeight = heightDp;
            Util.setDefKeyboardHeight(mContext, mAutoViewHeight);
        }

        if (mAutoHeightLayoutView != null) {
            mAutoHeightLayoutView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAutoHeightLayoutView.getLayoutParams();
            params.height = height;
            mAutoHeightLayoutView.setLayoutParams(params);
        }
    }

    public void hideAutoView(){
        this.post(new Runnable() {
            @Override
            public void run() {
                Util.closeInput(mContext);
                setAutoViewHeight(0);
                if (mAutoHeightLayoutView != null) {
                    mAutoHeightLayoutView.setVisibility(View.GONE);
                }
            }
        });
        mKeyboardState = KEYBOARD_STATE_NONE ;
        emotionClose();
    }

    public void emotionOpen() {
        emojiShow.compareAndSet(false, true);
    }

    public void emotionClose() {
        emojiShow.compareAndSet(true, false);
    }

    public void showAutoView(){
        emotionOpen();
        if (mAutoHeightLayoutView != null) {
            mAutoHeightLayoutView.setVisibility(VISIBLE);
            setAutoViewHeight(ImageUtil.dip2px(mContext, mAutoViewHeight));
        }
        mKeyboardState = mKeyboardState == KEYBOARD_STATE_NONE ? KEYBOARD_STATE_FUNC : KEYBOARD_STATE_BOTH ;
    }

    @Override
    public void OnSoftPop(final int height) {
        if(height>0) {
            mKeyboardState = KEYBOARD_STATE_BOTH;
            post(new Runnable() {
                @Override
                public void run() {
                    setAutoViewHeight(height);
                }
            });
        }
    }

    @Override
    public void OnSoftClose(int height) {
        mKeyboardState = mKeyboardState == KEYBOARD_STATE_BOTH ? KEYBOARD_STATE_FUNC : KEYBOARD_STATE_NONE ;
    }

    @Override
    public void OnSoftChanegHeight(final int height) {
        post(new Runnable() {
            @Override
            public void run() {
                setAutoViewHeight(height);
            }
        });
    }
}
package com.quanliren.quan_two.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {

    private OnSizeChangedListener listener;

    public boolean isOpen = false;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    private View hideView;

    public View getHideView() {
        return hideView;
    }

    public void setHideView(View hideView) {
        if (!isOpen) {
            hideView.setVisibility(View.VISIBLE);
        } else {
            this.hideView = hideView;
        }
    }

    int keyBoardHeight = 0;

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*getViewTreeObserver().addOnGlobalLayoutListener(
	            new ViewTreeObserver.OnGlobalLayoutListener() {

	                @Override
	                public void onGlobalLayout() {
	                    // TODO Auto-generated method stub
	                    if (keyBoardHeight <= 100) {
	                        Rect r = new Rect();
	                        getWindowVisibleDisplayFrame(r);

	                        int screenHeight = getRootView()
	                                .getHeight();
	                        int heightDifference = screenHeight
	                                - (r.bottom - r.top);
	                        int resourceId = getResources()
	                                .getIdentifier("status_bar_height",
	                                        "dimen", "android");
	                        if (resourceId > 0) {
	                            heightDifference -= getResources()
	                                    .getDimensionPixelSize(resourceId);
	                        }
	                        if (heightDifference > 100) {
	                            keyBoardHeight = heightDifference;
	                        }

	                        LogUtil.d("Keyboard Size", "Size: " + heightDifference);
	                    }
	                    // boolean visible = heightDiff > screenHeight / 3;
	                }
	            });	*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    int _oldh = -1;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        if (listener != null && oldh != 0) {

            if (_oldh == -1) {
                _oldh = oldh;
            }
            if (h >= _oldh) {
                listener.close();
                isOpen = false;
                if (hideView != null) {
                    hideView.setVisibility(View.VISIBLE);
                    hideView = null;
                }
            } else if (h < _oldh) {
                listener.open(_oldh - h);
                isOpen = true;
            }
        }
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.listener = listener;
    }

    public interface OnSizeChangedListener {
        public void open(int height);

        public void close();
    }
}
package com.quanliren.quan_two.custom.emoji;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.GridView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonImageBean;

public class EmoteGridView extends GridView {

    public EmoteGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmoteGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmoteGridView(Context context) {
        super(context);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec + 50);
    }

    EmoticonListener eListener;

    public void setListener(EmoticonListener eListener) {
        this.eListener = eListener;
    }

    public interface EmoticonListener {
        public void onEmoticonClick(EmoticonImageBean bean);

        public void onEmoticonLongPress(EmoticonImageBean bean, int[] xy, int[] wh);

        public void onEmoticonLongPressCancle();
    }

    View selectedView;
    int selectIndex = -1;
    private int mLastMotionX, mLastMotionY;
    private boolean isReleased;
    private boolean canMove;
    private boolean isMove;
    private int mCounter;
    private Runnable mLongPressRunnable = new Runnable() {

        @Override
        public void run() {
            mCounter--;
            if (mCounter == 0 && !isReleased && !isMove) {
                doPress();
                canMove = true;
            }
        }
    };
    private static final int TOUCH_SLOP = 40;
    Handler handler = new Handler();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mCounter++;
                isReleased = false;
                isMove = false;
                selectIndex = -1;
                canMove = false;
                doSelectView(x, y);
                handler.postDelayed(mLongPressRunnable,
                        ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                        || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                    // 移动超过阈值，则表示移动了
                    isMove = true;
                    for (int i = 0; i < getChildCount(); i++) {
                        View view = getChildAt(i);
                        view.setSelected(false);
                    }
                }
                if (!canMove) {
                    break;
                }
                doSelectView(x, y);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isReleased = true;
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    view.setSelected(false);
                }
                eListener.onEmoticonLongPressCancle();
                if (!canMove && selectIndex != -1) {
                    EmoticonImageBean er = null;
                    if (selectedView != null) {
                        er = (EmoticonImageBean) selectedView.getTag();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP && !isMove)
                        eListener.onEmoticonClick(er);
                }
                selectedView = null;
                break;
        }
        return true;
    }

    public void doSelectView(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (x >= view.getLeft() && x <= view.getLeft() + view.getWidth()
                    && y >= view.getTop()
                    && y <= view.getTop() + view.getHeight()) {
                selectedView = view.findViewById(R.id.emote_item_iv_image);
                selectIndex = i;
                view.setSelected(true);
                if (canMove) {
                    doPress();
                }
            } else {
                view.setSelected(false);
            }
        }
    }

    public void doPress() {
        EmoticonImageBean er = null;
        int[] xy = new int[2];
        int[] wh = new int[2];
        if (selectedView != null) {
            er = (EmoticonImageBean) selectedView.getTag();
            selectedView.getLocationOnScreen(xy);
            wh[0] = selectedView.getWidth();
            wh[1] = selectedView.getHeight();
        }
        eListener.onEmoticonLongPress(er, xy, wh);
    }
}

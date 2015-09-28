package com.quanliren.quan_two.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifDrawable;

/**
 * An {@link android.widget.ImageView} which tries treating background and src as {@link pl.droidsonroids.gif.GifDrawable}
 *
 * @author koral--
 */
public class MyGifImageView extends ImageView {
    static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

    /**
     * A corresponding superclass constructor wrapper.
     *
     * @param context
     * @see android.widget.ImageView#ImageView(android.content.Context)
     */
    public MyGifImageView(Context context) {
        super(context);
    }

    /**
     * Like eqivalent from superclass but also try to interpret src and background
     * attributes as {@link pl.droidsonroids.gif.GifDrawable}.
     *
     * @param context
     * @param attrs
     * @see android.widget.ImageView#ImageView(android.content.Context, android.util.AttributeSet)
     */
    public MyGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler.post(refere);
    }

    /**
     * Like eqivalent from superclass but also try to interpret src and background
     * attributes as GIFs.
     *
     * @param context
     * @param attrs
     * @param defStyle
     * @see android.widget.ImageView#ImageView(android.content.Context, android.util.AttributeSet, int)
     */
    public MyGifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handler.post(refere);
    }

    int defaultTime = 150;

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable instanceof GifDrawable) {
            GifDrawable gif = (GifDrawable) drawable;
//            gif.setCallBack(callback);
            super.setImageDrawable(gif);
        } else {
            super.setImageDrawable(drawable);
        }
    }


    boolean b1 = true;

    Handler handler = new Handler(Looper.getMainLooper());

    Runnable refere = new Runnable() {

        @Override
        public void run() {
            invalidate();
            if (b1)
                handler.postDelayed(this, defaultTime);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        b1 = false;
    }

}

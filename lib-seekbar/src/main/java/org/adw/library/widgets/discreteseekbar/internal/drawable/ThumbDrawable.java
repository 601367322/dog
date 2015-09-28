/*
 * Copyright (c) Gustavo Claramunt (AnderWeb) 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.adw.library.widgets.discreteseekbar.internal.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * <h1>HACK</h1>
 * <p>
 * Special {@link StateDrawable} implementation
 * to draw the Thumb circle.
 * </p>
 * <p>
 * It's special because it will stop drawing once the state is pressed/focused BUT only after a small delay.
 * </p>
 * <p>
 * This special delay is meant to help avoiding frame glitches while the {@link org.adw.library.widgets.discreteseekbar.internal.Marker} is added to the Window
 * </p>
 *
 * @hide
 */
public class ThumbDrawable extends StateDrawable implements Animatable {
    //The current size for this drawable. Must be converted to real DPs
    public static final int DEFAULT_SIZE_DP = 24;
    public static final int DEFAULT_TEXT_SIZE_DP = 12;
    private final int mSize;

    public int getTextSize() {
        return textSize;
    }

    private int textSize;
    private boolean mOpen;
    private boolean mRunning;
    private String value;


    Paint mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ThumbDrawable(@NonNull ColorStateList tintStateList, int size) {
        super(tintStateList);
        mSize = size;

        mpaint.setAntiAlias(true);
        mpaint.setTextAlign(Paint.Align.CENTER);
        mpaint.setColor(Color.WHITE);
        mpaint.setTextSize(textSize);
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    @Override
    public void doDraw(Canvas canvas, Paint paint) {
        if (!mOpen) {
            Rect bounds = getBounds();
            float radius = (mSize / 2);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius, paint);

            drawText(canvas);
        }
    }

    public void drawText(Canvas canvas) {
        Rect bounds = new Rect();
        mpaint.getTextBounds(value, 0, value.length(), bounds);
        int width = bounds.width();
        int height = bounds.height();
        bounds = getBounds();
        canvas.drawText(value, bounds.centerX(), bounds.centerY() + height / 2, mpaint);
    }

    public void setTextSize(int value) {
        this.textSize = value;
        mpaint.setTextSize(textSize);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void animateToPressed() {
        scheduleSelf(opener, SystemClock.uptimeMillis() + 100);
        mRunning = true;
    }

    public void animateToNormal() {
        mOpen = false;
        mRunning = false;
        unscheduleSelf(opener);
        invalidateSelf();
    }

    private Runnable opener = new Runnable() {
        @Override
        public void run() {
            mOpen = true;
            invalidateSelf();
            mRunning = false;
        }
    };

    @Override
    public void start() {
        //NOOP
    }

    @Override
    public void stop() {
        animateToNormal();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }
}

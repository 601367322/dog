package com.quanliren.quan_two.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;

public class ThroughImageView {

    private Bitmap mBitmap;
    private Bitmap background;

    private Context context;

    public ThroughImageView(Context context) {
        this.context = context;
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.through_icon).copy(Bitmap.Config.ARGB_8888, true);
    }

    public synchronized Bitmap getmBitmap() {
        Bitmap temp = Bitmap.createBitmap(background);
        Canvas canvas = new Canvas(temp);
        canvas.drawBitmap(mBitmap, ImageUtil.dip2px(context, 2.5f), ImageUtil.dip2px(context, 3f), null);
        canvas.save();
        return temp;
    }

    public synchronized void setmBitmap(Bitmap srcBmp) {
        mBitmap = scaleCenterCrop(srcBmp, ImageUtil.dip2px(context, 50), ImageUtil.dip2px(context, 50));
        mBitmap = getRoundedCornerBitmap(mBitmap, ImageUtil.dip2px(context, 25));

    }


    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
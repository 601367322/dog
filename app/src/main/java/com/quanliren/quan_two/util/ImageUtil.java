package com.quanliren.quan_two.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static int[] downsize(String path, String toPath, Context context) {
        int angle = readPictureDegree(path);
        int[] wh = new int[2];
        if (angle != 0 && angle % 90 == 0) {
            try {
                File oldFile = new File(path);
                // decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(oldFile),
                        null, o);
                o.inJustDecodeBounds = false;
                // Find the correct scale value. It should be the power of 2.
                int maxWidth = 640;
                int maxHeight = 960;

                // decode with inSampleSize
                BitmapFactory.Options temp = new BitmapFactory.Options();
                temp.outWidth = o.outHeight;
                temp.outHeight = o.outWidth;
                o.inSampleSize = computeSampleSize(o, -1, maxHeight * maxWidth);
                Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(oldFile), null, o);
                if (angle != 0) {
                    bitmap1 = rotaingImageView(angle, bitmap1);
                }
                copy(bitmap1, toPath);

                wh[0] = bitmap1.getWidth();
                wh[1] = bitmap1.getHeight();
                bitmap1.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                File oldFile = new File(path);
                // decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(oldFile),
                        null, o);
                o.inJustDecodeBounds = false;
                // Find the correct scale value. It should be the power of 2.
                int maxWidth = 640;
                int maxHeight = 960;

                // decode with inSampleSize
                o.inSampleSize = computeSampleSize(o, -1, maxHeight * maxWidth);
                Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(oldFile), null, o);
                copy(bitmap1, toPath);

                wh[0] = bitmap1.getWidth();
                wh[1] = bitmap1.getHeight();
                bitmap1.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wh;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * ��תͼƬ
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static void copy(Bitmap bitmap, String newFile) {
        try {
            File f = new File(newFile);
            File parf = new File(f.getParent());
            if (!parf.exists()) {
                parf.mkdirs();
            }
            parf = null;
            FileOutputStream out = new FileOutputStream(f);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void copyPng(Bitmap bitmap, String newFile) {
        try {
            File f = new File(newFile);
            File parf = new File(f.getParent());
            if (!parf.exists()) {
                parf.mkdirs();
            }
            parf = null;
            FileOutputStream out = new FileOutputStream(f);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

package com.quanliren.quan_two.activity.whats;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.fragment.impl.LoaderImpl;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment
public class WahtsFragment1 extends MenuFragmentBase implements LoaderImpl {

    private static final String TAG = "WahtsFragment1";

    View view;
    @FragmentArg
    boolean have;
    @FragmentArg
    int res;
    @ViewById
    FrameLayout content;

    int screen_w, screen_h;
    int max_w = 640, max_h = 1136;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.whats1, null);
        } else {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }
        return view;
    }

    public void refresh() {
        if (getActivity() != null && init.compareAndSet(false, true)) {
            screen_h = getResources().getDisplayMetrics().heightPixels;
            screen_w = getResources().getDisplayMetrics().widthPixels;
            createTopYun(50, 150, R.drawable.yunduo1);
            createBg(100, 202, res);
            if (have) {
                createImageView(45, 326, R.drawable.welcome_1_icon_1);
                createImageView(57, 546, R.drawable.welcome_1_icon_3);
                createImageView(484, 267, R.drawable.welcome_1_icon_2);
                createImageView(484, 476, R.drawable.welcome_1_icon_4);
                createImageView(409, 646, R.drawable.welcome_1_icon_5);
            }
            createTopYun2(412, 785, R.drawable.yunduo2);
        }
    }

    List<Animator> list = new ArrayList<Animator>();
    int num = 0;

    public ImageView getPositionImageView(int i_x, int i_y, int res) {
        float x_scale = (float) i_x / (float) max_w;
        float x = x_scale * (float) screen_w;

        float y_scale = (float) i_y / (float) max_h;
        float y = y_scale * (float) screen_h;

        ImageView img = new ImageView(getActivity());
        img.setX( x);
        img.setScaleType(ScaleType.CENTER_CROP);
        img.setY(y);
        img.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        return img;
    }

    public ImageView createImageByCache(int i_x, int i_y, int res) {
        ImageView img = getPositionImageView(i_x, i_y, res);
        Bitmap loadedImage = decodeResource(getResources(), res);
        int i_w = loadedImage.getWidth();
        int i_h = loadedImage.getHeight();

        float w_scale = (float) i_w / (float) max_w;
        int n_i_w = (int) (w_scale * (float) screen_w);

        float n_w_scale = (float) n_i_w / (float) i_w;
        int n_i_h = (int) ((float) i_h * n_w_scale);

        img.setLayoutParams(new FrameLayout.LayoutParams(n_i_w, n_i_h));
        img.setImageBitmap(loadedImage);
        content.addView(img);
        return img;
    }

    public void createBg(int i_x, int i_y, int res) {
        ImageView img = createImageByCache(i_x, i_y, res);
        img.setAlpha( 0l);
        img.animate().alpha(1).setDuration(1000).start();
    }

    public void createTopYun(int i_x, int i_y, int res) {
        ImageView img = createImageByCache(i_x, i_y, res);
        float x = img.getX();
        img.setTranslationX( -img.getLayoutParams().width);
        img.animate().translationX(x).setDuration(1000)
                .start();
    }

    public void createTopYun2(int i_x, int i_y, int res) {
        ImageView img = createImageByCache(i_x, i_y, res);
        float x = img.getX();
        img.setTranslationX( screen_w + img.getLayoutParams().width);
        img.animate().translationX(x).setDuration(1000)
                .start();
    }

    public void createImageView(int i_x, int i_y, int res) {
        ImageView img = getPositionImageView(i_x, i_y, res);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(img, "scaleX", 0, 1)
                .setDuration(200), ObjectAnimator.ofFloat(img, "scaleY", 0, 1)
                .setDuration(200));
        list.add(set);

        Bitmap loadedImage = decodeResource(getResources(), res);
        img.setImageBitmap(loadedImage);
        int i_w = loadedImage.getWidth();
        int i_h = loadedImage.getHeight();

        float w_scale = (float) i_w / (float) max_w;
        int n_i_w = (int) (w_scale * (float) screen_w);

        float n_w_scale = (float) n_i_w / (float) i_w;
        int n_i_h = (int) ((float) i_h * n_w_scale);

        img.setLayoutParams(new FrameLayout.LayoutParams(
                n_i_w, n_i_h));
        img.setScaleX( 0);
        img.setScaleY( 0);
        img.setPivotX( n_i_w * 0.5f);
        img.setPivotY( n_i_h * 0.5f);

        content.addView(img);

        num++;
        if (num >= 5) {
            AnimatorSet setAll = new AnimatorSet();
            setAll.playSequentially(list);
            setAll.start();
        }
    }


    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
}

package com.quanliren.quan_two.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.base.BaseUserActivity;
import com.quanliren.quan_two.activity.user.UserInfoActivity;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.util.StaticFactory;

import java.util.List;

public class CustomPicWall extends LinearLayout {

    private List<ImageBean> imgList;

    public CustomPicWall(Context context) {
        super(context);
    }

    public CustomPicWall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImgList(List<ImageBean> imgList) {
        this.imgList = imgList;
        removeAllViews();
        this.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout ll1 = null;
        LinearLayout ll2 = null;
        LinearLayout ll3 = null;
        LinearLayout ll4 = null;
        LinearLayout ll5 = null;
        LinearLayout ll6 = null;
        switch (imgList.size()) {
            case 1:
            case 2:
                for (int i = 0; i < imgList.size(); i++) {
                    addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            case 3:
                addView(ll1 = createLayout(LinearLayout.VERTICAL));
                addView(ll2 = createLayout(LinearLayout.VERTICAL));

                for (int i = 0; i < imgList.size(); i++) {
                    if (i <= 0)
                        ll1.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else
                        ll2.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            case 4:
                addView(ll1 = createLayout(LinearLayout.VERTICAL));
                addView(ll2 = createLayout(LinearLayout.VERTICAL));

                for (int i = 0; i < imgList.size(); i++) {
                    if (i <= 1)
                        ll1.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else
                        ll2.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            case 5:
                addView(ll1 = createLayout(LinearLayout.VERTICAL));
                addView(ll2 = createLayout(LinearLayout.HORIZONTAL));
                ll2.addView(ll3 = createLayout(LinearLayout.VERTICAL));
                ll2.addView(ll4 = createLayout(LinearLayout.VERTICAL));

                for (int i = 0; i < imgList.size(); i++) {
                    if (i <= 0)
                        ll1.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else if (i <= 2)
                        ll3.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else
                        ll4.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            case 6:
                addView(ll1 = createLayout(LinearLayout.VERTICAL));
                addView(ll2 = createLayout(LinearLayout.HORIZONTAL));
                ll2.addView(ll3 = createLayout(LinearLayout.VERTICAL));
                ll2.addView(ll4 = createLayout(LinearLayout.VERTICAL));

                for (int i = 0; i < imgList.size(); i++) {
                    if (i <= 1)
                        ll1.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else if (i <= 3)
                        ll3.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else
                        ll4.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            case 7:
                addView(ll1 = createLayout(LinearLayout.VERTICAL));
                ll1.addView(ll3 = createLayout(LinearLayout.HORIZONTAL));
                addView(ll2 = createLayout(LinearLayout.HORIZONTAL));
                ll2.addView(ll5 = createLayout(LinearLayout.VERTICAL));
                ll2.addView(ll6 = createLayout(LinearLayout.VERTICAL));

                for (int i = 0; i < imgList.size(); i++) {
                    if (i <= 0)
                        ll1.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath), 0);
                    else if (i <= 2)
                        ll3.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else if (i <= 4)
                        ll5.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else
                        ll6.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            case 8:

                this.setOrientation(LinearLayout.VERTICAL);

                addView(ll1 = createLayout(LinearLayout.HORIZONTAL));
                addView(ll2 = createLayout(LinearLayout.HORIZONTAL));

                for (int i = 0; i < imgList.size(); i++) {
                    if (i <= 3)
                        ll1.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                    else
                        ll2.addView(createImageView(imgList.get(i).position, imgList.get(i).imgpath));
                }
                break;
            default:
                break;
        }
    }
    public final DisplayImageOptions options_defalut = new DisplayImageOptions.Builder().cacheInMemory(true)
            .cacheOnDisc(true).build();

    public ImageView createImageView(int i, String url) {
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        lp.weight = 1;
        ImageView image = new ImageView(getContext());
        image.setLayoutParams(lp);
        image.setScaleType(ScaleType.CENTER_CROP);
        image.setId(i);
        image.setTag(i);
        image.setOnClickListener(imgClick);
        image.setOnLongClickListener(longClick);
        if (imgList.size() < 3 || (imgList.size() < 4 && i == 0)) {
            ImageLoader.getInstance().displayImage(url + StaticFactory._600x600, image, options_defalut);
        } else {
            ImageLoader.getInstance().displayImage(url + StaticFactory._320x320, image, options_defalut);
        }
        return image;
    }

    public LinearLayout createLayout(int orientation) {
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        lp.weight = 1;
        LinearLayout ll = new LinearLayout(getContext());
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setOrientation(orientation);
        ll.setLayoutParams(lp);
        return ll;
    }


    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();
        int childHeightSize = (int) ((float) childWidthSize / 2);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    OnClickListener imgClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                if (getContext() instanceof BaseUserActivity) {
//						((BaseUserActivity)getContext()).viewPic((Integer)v.getTag());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OnLongClickListener longClick = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            try {
                if (getContext() instanceof UserInfoActivity) {
//						((UserInfoActivity)getContext()).picSeting((Integer)v.getTag());
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };
}

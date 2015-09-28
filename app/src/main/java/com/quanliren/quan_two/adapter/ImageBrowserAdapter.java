package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.a.uk.co.senab.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.custom.RoundProgressBar;
import com.quanliren.quan_two.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ImageBrowserAdapter extends PagerAdapter {

    private List<ImageBean> mPhotos = new ArrayList<ImageBean>();
    Context c;

    public ImageBrowserAdapter(List<ImageBean> photos, Context c) {
        if (photos != null) {
            mPhotos = photos;
        }
        this.c = c;
    }

    @Override
    public int getCount() {
        if (mPhotos.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(container.getContext(),
                R.layout.activity_image_item, null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoview);
        photoView.setOnClickListener(imgClick);
        final RoundProgressBar pb = (RoundProgressBar) view
                .findViewById(R.id.loadProgressBar);
        String imgPath = mPhotos.get(position % mPhotos.size()).imgpath;
        if (!imgPath.startsWith("http://")) {
            imgPath = Util.FILE + imgPath;
        }
        ImageLoader.getInstance().displayImage(
                imgPath, photoView, ((AppClass)(c.getApplicationContext())).options_userlogo
                , null, new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String imageUri, View view,
                                                 int current, int total) {
                        if (current == total) {
                            pb.setVisibility(View.GONE);
                        } else {
                            pb.setVisibility(View.VISIBLE);
                            pb.setMax(total);
                            pb.setProgress(current);
                        }
                    }
                });
        container.addView(view, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    OnClickListener imgClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ((BaseActivity) c).finish();
        }
    };
}

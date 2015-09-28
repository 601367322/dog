package com.quanliren.quan_two.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.ad.HeadAdFragment;
import com.quanliren.quan_two.bean.ADBannerBean;
import com.quanliren.quan_two.custom.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/7/2.
 */
public class PagerImageAdapter extends PagerAdapter {

    List<ADBannerBean> urllist = new ArrayList<>();

    HeadAdFragment.IProductGridListener listener = null;

    public PagerImageAdapter(List<ADBannerBean> list,
                             HeadAdFragment.IProductGridListener listener) {
        this.urllist = list;
        this.listener = listener;
    }

    public void setList(List<ADBannerBean> list) {
        this.urllist = list;
    }

    @Override
    public int getCount() {
        if (urllist.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return urllist.size();
    }

    int height = 68;

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private final DisplayImageOptions options_no_default = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true).build();

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        View view = View.inflate(container.getContext(),
                R.layout.emoticon_pager_image_item, null);
        final ImageView photoView = (ImageView) view.findViewById(R.id.img);
        final RoundProgressBar rp = (RoundProgressBar) view
                .findViewById(R.id.progressBar);
        ImageLoader.getInstance().displayImage(
                urllist.get(position % urllist.size()).getPath(),
                photoView, options_no_default,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri,
                                                  View view, Bitmap loadedImage) {
                        float scale = (float) container.getContext()
                                .getResources().getDisplayMetrics().widthPixels
                                / (float) loadedImage.getWidth();
                        height = (int) (loadedImage.getHeight() * scale);
                        ((ImageView) view)
                                .setLayoutParams(new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        height));
                        container
                                .setLayoutParams(new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        height));
                    }
                }, new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String imageUri,
                                                 View view, int current, int total) {
                        if (current == total) {
                            rp.setVisibility(View.GONE);
                        } else {
                            rp.setVisibility(View.VISIBLE);
                            rp.setMax(total);
                            rp.setProgress(current);
                        }
                    }
                });
        photoView.setTag(urllist.get(position % urllist.size()));
        photoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.imgClick(photoView);
            }
        });
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, height);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
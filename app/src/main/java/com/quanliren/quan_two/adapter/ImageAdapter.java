package com.quanliren.quan_two.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.ad.HeadAdFragment;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.custom.RoundProgressBar;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends PagerAdapter {

    List<ImageBean> urllist = new ArrayList<ImageBean>();

    HeadAdFragment.IProductGridListener listener = null;

    public ImageAdapter(List<ImageBean> list, HeadAdFragment.IProductGridListener listener) {
        this.urllist = list;
        this.listener = listener;
    }

    public void setList(List<ImageBean> list) {
        this.urllist = list;
    }

    @Override
    public int getCount() {
        return urllist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private final DisplayImageOptions options_no_default = new DisplayImageOptions.Builder().cacheInMemory(true)
            .cacheOnDisk(true).build();
    public static final DisplayImageOptions goods_default = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.goods_down_fail)
            .showImageForEmptyUri(R.drawable.goods_down_fail)
                    // .displayer(new FadeInBitmapDisplayer(200))
            .showImageOnFail(R.drawable.goods_down_fail).cacheInMemory(true)
            .cacheOnDisk(true).build();
    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.image_item, null);
        final ImageView photoView = (ImageView) view.findViewById(R.id.img);
        final RoundProgressBar rp = (RoundProgressBar) view.findViewById(R.id.progressBar);
        ImageLoader.getInstance().displayImage(urllist.get(position).imgpath + StaticFactory._300x180, photoView, goods_default, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                float h = ImageUtil.dip2px(container.getContext(), 240) * loadedImage.getWidth() / loadedImage.getHeight();
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams((int) h, ImageUtil.dip2px(container.getContext(), 240));
                rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                ((ImageView) view).setLayoutParams(rl);
            }
        }, new ImageLoadingProgressListener() {

            @Override
            public void onProgressUpdate(String imageUri, View view, int current,
                                         int total) {
                if (current == total) {
                    rp.setVisibility(View.GONE);
                } else {
                    rp.setVisibility(View.VISIBLE);
                    rp.setMax(total);
                    rp.setProgress(current);
                }
            }
        });
        photoView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.imgClick(photoView);
            }
        });
        container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}

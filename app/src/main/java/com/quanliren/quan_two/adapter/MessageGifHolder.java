package com.quanliren.quan_two.adapter;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean;
import com.quanliren.quan_two.custom.MyGifImageView;
import com.quanliren.quan_two.util.DrawableCache;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageGifHolder extends MessageBaseHolder {

    @Bind(R.id.gif)
    MyGifImageView gif;
    @Bind(R.id.gif_ll)
    View gif_ll;
    @Bind(R.id.gif_progress)
    View gif_progress;

    public MessageGifHolder(View view) {
        super(view);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean,position);
        gif_ll.setVisibility(View.VISIBLE);
        gif_ll.setTag(bean);
        gif_ll.setOnLongClickListener(long_click);
        gif_ll.setOnClickListener(null);
        EmoticonActivityListBean.EmoticonZip.EmoticonJsonBean eBean = bean.getGifContent();

        if (eBean.getGifUrl().startsWith("http://")) {
            ImageLoader.getInstance().loadImage(eBean.getGifUrl(),
                    new gifImageLoadListener(this));
        } else {
            ImageLoader.getInstance().loadImage(
                    Util.FILE + eBean.getGifFile(),
                    new gifImageLoadListener(this));
        }
    }

    class gifImageLoadListener extends SimpleImageLoadingListener {

        MessageGifHolder holder;

        public gifImageLoadListener(MessageGifHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            holder.gif_progress.setVisibility(View.VISIBLE);
            holder.gif.setImageDrawable(null);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            holder.gif_progress.setVisibility(View.GONE);
            DrawableCache.getInstance().displayDrawable(
                    holder.gif,
                    ImageLoader.getInstance().getDiskCache().get(imageUri)
                            .getPath());

        }

        @Override
        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            holder.gif_progress.setVisibility(View.GONE);
        }
    }
}

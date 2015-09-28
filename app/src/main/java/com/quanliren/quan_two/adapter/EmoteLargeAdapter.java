package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonImageBean;
import com.quanliren.quan_two.util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EmoteLargeAdapter extends ParentsAdapter {

    public EmoteLargeAdapter(Context context, List<EmoticonImageBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(c, R.layout.listitem_emote_large, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmoticonImageBean name = (EmoticonImageBean) getItem(position);
        if (name.getGiffile() != null && !name.getGiffile().equals("")) {
            ImageLoader.getInstance().displayImage(Util.FILE + name.getPngfile(), holder.mIvImage, ac.options_no_default);
        } else {
            ImageLoader.getInstance().displayImage(name.getPngUrl(), holder.mIvImage, ac.options_no_default);
        }
        holder.mIvImage.setTag(name);
        holder.name.setText(name.getNickname());
        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.emote_item_iv_image)
        ImageView mIvImage;
        @Bind(R.id.name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    int width, height;

    ImageLoadingListener listener = new SimpleImageLoadingListener() {
        public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
            if (width == 0 && height == 0) {
                width = loadedImage.getWidth();
                height = loadedImage.getHeight();
            }
            ((ImageView) view).setLayoutParams(new LinearLayout.LayoutParams(width, height));
            ((ImageView) view).setImageBitmap(loadedImage);
        }

        ;
    };
}

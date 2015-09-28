package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.PhotoAibum;
import com.quanliren.quan_two.bean.PhotoItem;
import com.quanliren.quan_two.custom.PhotoGridItem;
import com.quanliren.quan_two.util.Util;

import java.util.ArrayList;

public class PhotoAdappter extends BaseAdapter {
    private Context context;
    private PhotoAibum aibum;
    private ArrayList<PhotoItem> gl_arr;

    public PhotoAdappter(Context context, PhotoAibum aibum,
                         ArrayList<PhotoItem> gl_arr) {
        this.context = context;
        this.aibum = aibum;
        this.gl_arr = gl_arr;
    }

    @Override
    public int getCount() {
        if (gl_arr == null) {
            return aibum.getBitList().size();
        } else {
            return gl_arr.size();
        }

    }

    @Override
    public PhotoItem getItem(int position) {
        if (gl_arr == null) {
            return aibum.getBitList().get(position);
        } else {
            return gl_arr.get(position);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static final DisplayImageOptions options_defalut = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.image_group_qzl)
            .showImageForEmptyUri(R.drawable.image_group_qzl)
            .considerExifParams(true)
//	.displayer(new FadeInBitmapDisplayer(200))
            .showImageOnFail(R.drawable.image_group_load_f).cacheInMemory(true)
            .cacheOnDisc(false).build();


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoGridItem item;
        if (convertView == null) {
            item = new PhotoGridItem(context);
            item.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        } else {
            item = (PhotoGridItem) convertView;
        }
        // 通过ID 加载缩略图
        if (gl_arr == null) {
            // Bitmap bitmap
            // =BitmapCache.getInstance().getBitmaps(aibum.getBitList().get(position).getPhotoID(),
            // context);//
            // MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
            // aibum.getBitList().get(position).getPhotoID(),
            // Thumbnails.MICRO_KIND, null);
            // item.SetBitmap(bitmap);
//			BitmapCache.getInstance().getBitmaps(item.getmImageView(),
//					aibum.getBitList().get(position).getPhotoID(), context);
            ImageLoader.getInstance().displayImage(Util.FILE + aibum.getBitList().get(position).getPath(), item.getmImageView(), options_defalut);
            boolean flag = aibum.getBitList().get(position).isSelect();
            item.setChecked(flag);
        } else {
            Bitmap bitmap = Thumbnails.getThumbnail(context
                            .getContentResolver(), gl_arr.get(position).getPhotoID(),
                    Thumbnails.MICRO_KIND, null);
            item.SetBitmap(bitmap);
        }
        return item;
    }
}

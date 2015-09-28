package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.PhotoAibum;
import com.quanliren.quan_two.util.Util;

import java.util.List;

public class PhotoAibumAdapter extends BaseAdapter {
    private List<PhotoAibum> aibumList;
    private Context context;
    private ViewHolder holder;

    public PhotoAibumAdapter(List<PhotoAibum> list, Context context) {
        this.aibumList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return aibumList.size();
    }

    @Override
    public Object getItem(int position) {
        return aibumList.get(position);
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
        if (convertView == null) {
            convertView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.photoalbum_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.photoalbum_item_image);
            holder.tv = (TextView) convertView.findViewById(R.id.photoalbum_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /** 通过ID 获取缩略图*/
//		Bitmap bitmap = BitmapCache.getInstance().getBitmaps(aibumList.get(position).getBitmap(), context);//MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), aibumList.get(position).getBitmap(), Thumbnails.MICRO_KIND, null);
        ImageLoader.getInstance().displayImage(Util.FILE + aibumList.get(position).getPath(), holder.iv, options_defalut);
//		holder.iv.setImageBitmap(bitmap);
        holder.tv.setText(aibumList.get(position).getName() + " ( " + aibumList.get(position).getCount() + " )");
        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }

}

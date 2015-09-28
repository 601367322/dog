package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.image.*;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.util.StaticFactory;

import java.util.List;

public class QuanPicAdapter extends ParentsAdapter {

    int imgWidth;
    public QuanPicAdapter(Context c, List list, int imgwidth) {
        super(c, list);
        imgWidth = imgwidth;
    }

    public boolean ismy = false;

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = View.inflate(c, R.layout.quan_pic_item, null);

            ((ImageView) convertView).setLayoutParams(new AbsListView.LayoutParams(imgWidth, imgWidth));
        }
        ImageBean ib = (ImageBean) list.get(position);

        ImageLoader.getInstance().displayImage(ib.imgpath + StaticFactory._320x320, (ImageView) convertView);
        ((ImageView) convertView).setTag(position);
        ((ImageView) convertView).setOnClickListener(imgClick);
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }

    OnClickListener imgClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int positon = (Integer) v.getTag();

            ImageBrowserActivity_.intent(c).mPosition(positon).ibl(new ImageBeanList(list)).start();
        }
    };
}

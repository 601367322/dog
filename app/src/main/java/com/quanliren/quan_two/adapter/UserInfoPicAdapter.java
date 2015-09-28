package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseUserActivity;
import com.quanliren.quan_two.activity.user.UserInfoActivity;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import java.util.List;

public class UserInfoPicAdapter extends ParentsAdapter {

    OnImageClickListener listener;

    public UserInfoPicAdapter(Context c, List list, OnImageClickListener listener) {
        super(c, list);
        this.listener = listener;
    }

    public boolean ismy = false;

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(c, R.layout.user_info_pic_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.img);
            holder.iv.setLayoutParams(new RelativeLayout.LayoutParams((c.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(c, 20)) / 4, (c.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(c, 20)) / 4));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageBean ib = (ImageBean) list.get(position);
        if (ib.defaults) {
            holder.iv.setImageResource(R.drawable.default_userlogo);
            holder.iv.setOnClickListener(addImgClick);
            holder.iv.setOnLongClickListener(null);
        } else {
            if (ib.imgpath.startsWith(Util.FILE)) {
                ImageLoader.getInstance().displayImage(ib.imgpath, holder.iv);
            } else {
                ImageLoader.getInstance().displayImage(ib.imgpath + StaticFactory._320x320, holder.iv);
            }
            holder.iv.setTag(ib);
            holder.iv.setOnClickListener(imgClick);
            holder.iv.setOnLongClickListener(longClick);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }

    OnClickListener addImgClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                listener.addImg();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OnClickListener imgClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                if (c instanceof BaseUserActivity) {
                    listener.imgClick((ImageBean) v.getTag());
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
                if (c instanceof UserInfoActivity) {
                    listener.imgLongClick((ImageBean) v.getTag());
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    public interface OnImageClickListener {
        void addImg();

        void imgClick(ImageBean position);

        void imgLongClick(ImageBean position);
    }
}

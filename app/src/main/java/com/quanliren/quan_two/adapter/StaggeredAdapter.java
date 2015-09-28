package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.UserInfoActivity;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import java.util.List;

public class StaggeredAdapter extends ParentsAdapter {

    public StaggeredAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        User user = (User) list.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent
                    .getContext());
            convertView = layoutInflator.inflate(R.layout.infos_list, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.news_pic);
            holder.contentView = (TextView) convertView
                    .findViewById(R.id.news_title);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        int screenWidth = (int) ((c.getResources().getDisplayMetrics().widthPixels - ImageUtil
                .dip2px(c, 15)) / 2);
        float scaleWidth1 = ((float) screenWidth) / user.getAvtwidth();
        int height = (int) (user.getAvtheight() * scaleWidth1);
        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                (int) ((float) screenWidth), height));
//		RelativeLayout.LayoutParams txt=new RelativeLayout.LayoutParams(
//				(int) ((float) screenWidth),RelativeLayout.LayoutParams.WRAP_CONTENT);
//		txt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
//		holder.contentView.setLayoutParams(txt);
        if (Util.isStrNotNull(user.getSignature())) {
            holder.contentView.setVisibility(View.VISIBLE);
            holder.contentView.setText(user.getSignature().trim());
        } else {
            holder.contentView.setVisibility(View.GONE);
        }
        holder.imageView.setTag(user.getId());
        holder.imageView.setOnTouchListener(TouchDark);
        holder.imageView.setOnClickListener(click);
        try {
            ImageLoader.getInstance().displayImage(
                    user.getAvatar() + StaticFactory._240x1000,
                    holder.imageView);
        } catch (Exception e) {
        }
        return convertView;
    }

    /**
     * 图片点击事件
     */
    OnClickListener click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(c, v.getTag().toString().equals(getHelper().getUser().getId()) ? UserInfoActivity.class : UserOtherInfoActivity.class);
            i.putExtra("id", v.getTag().toString());
            c.startActivity(i);
        }
    };

    /**
     * 触摸变暗
     */
    OnTouchListener TouchDark = new OnTouchListener() {
        public final float[] BT_SELECTED = new float[]{1, 0, 0, 0, -50, 0, 1,
                0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0};
        public final float[] BT_NOT_SELECTED = new float[]{1, 0, 0, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((ImageView) v).getDrawable().setColorFilter(
                            new ColorMatrixColorFilter(BT_SELECTED));
                    ((ImageView) v).setImageDrawable(((ImageView) v).getDrawable());
                    return false;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    ((ImageView) v).getDrawable().setColorFilter(
                            new ColorMatrixColorFilter(BT_NOT_SELECTED));
                    ((ImageView) v).setImageDrawable(((ImageView) v).getDrawable());
                    return false;
            }
            return true;
        }
    };

    class ViewHolder {
        ImageView imageView;
        TextView contentView;
    }

}
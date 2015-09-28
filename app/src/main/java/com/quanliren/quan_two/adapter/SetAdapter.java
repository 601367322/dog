package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.SetBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetAdapter extends ParentsAdapter {

    public SetAdapter(Context c, List list) {
        super(c, list);
    }

    @Override
    public int getItemViewType(int position) {
        SetBean sb = (SetBean) list.get(position);
        return sb.isFirst;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {

            switch (getItemViewType(position)) {
                case 0:
                    convertView = View.inflate(c, R.layout.seting_item_top, null);
                    break;
                case 1:
                    convertView = View.inflate(c, R.layout.seting_item_mid, null);
                    break;
                default:
                    convertView = View.inflate(c, R.layout.seting_item_btm, null);
                    break;
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SetBean sb = (SetBean) list.get(position);
        holder.icon.setImageResource(sb.icon);
        holder.newimg.setVisibility(View.GONE);
        holder.text.setText(sb.title);
        if(sb.img==1){
            holder.newimg.setVisibility(View.VISIBLE);
        }else {
            holder.newimg.setVisibility(View.GONE);
        }

        if (sb.title.equals("清除缓存")) {
            holder.caret.setVisibility(View.GONE);
            holder.source.setVisibility(View.VISIBLE);
            holder.source.setText(sb.getSource());
        } else {
            try {
                holder.caret.setVisibility(View.VISIBLE);
                holder.source.setVisibility(View.GONE);
            } catch (Exception e) {
            }
        }
        if (holder.setcount != null) {
            if (sb.count > 0) {
                holder.setcount.setVisibility(View.VISIBLE);
            } else {
                holder.setcount.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.newimg)
        ImageView newimg;
        @Bind(R.id.text)
        TextView text;
        @Nullable
        @Bind(R.id.source)
        TextView source;
        @Nullable
        @Bind(R.id.caret)
        View caret;
        @Nullable
        @Bind(R.id.setcount)
        View setcount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}

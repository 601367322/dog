package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseArrayListAdapter;
import com.quanliren.quan_two.application.*;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonImageBean.EmoticonRes;

import java.util.List;

public class EmoteAdapter extends BaseArrayListAdapter {

    AppClass_ ac;

    public EmoteAdapter(Context context, List<String> datas) {
        super(context, datas);
        ac = (AppClass_) context.getApplicationContext();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_emote, null);
            holder = new ViewHolder();
            holder.mIvImage = (ImageView) convertView
                    .findViewById(R.id.emote_item_iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = (String) getItem(position);
        int id = AppClass.mEmoticonsId.get(name);
        holder.mIvImage.setImageResource(id);
//		ImageLoader.getInstance().displayImage("drawable://"+id, holder.mIvImage,ac.options_no_default);
        EmoticonRes er = new EmoticonRes();
        er.setNickname(name);
        er.setRes(id);
        holder.mIvImage.setTag(er);
        return convertView;
    }

    class ViewHolder {
        ImageView mIvImage;
    }
}

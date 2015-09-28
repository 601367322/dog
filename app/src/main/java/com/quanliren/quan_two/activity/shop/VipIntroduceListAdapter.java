package com.quanliren.quan_two.activity.shop;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;

import butterknife.Bind;

public class VipIntroduceListAdapter extends BaseAdapter<VipIntroduceBean> {

    public VipIntroduceListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.vip_introduce_list_item;
    }

    class ViewHolder extends BaseHolder {

        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.text)
        TextView text;

        @Override
        public void bind(VipIntroduceBean bean, int position) {
            img.setImageResource(bean.img);
            text.setText(bean.text);
        }

        public ViewHolder(View view) {
            super(view);
        }
    }
}

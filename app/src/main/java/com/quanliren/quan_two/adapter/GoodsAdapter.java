package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.GoodsBean;
import com.quanliren.quan_two.bean.ProductBean;
import com.quanliren.quan_two.bean.ProductListBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.StateTextView;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import java.util.List;

public class GoodsAdapter extends ParentsAdapter {

    public GoodsAdapter(Context c, List list) {
        super(c, list);
    }
    public void removeObj(int position) {
        list.remove(position);
    }
    public Object getItem(int position) {
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(c, R.layout.goods_list_item, null);
            holder.title_goods = (TextView) convertView
                    .findViewById(R.id.title_goods);
            holder.img_goods = (ImageView) convertView
                    .findViewById(R.id.img_goods);
            holder.coin_goods = (TextView) convertView
                    .findViewById(R.id.coin_goods);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductBean goods=(ProductBean)list.get(position);
            ImageLoader.getInstance().displayImage(
                    goods.getImgurl(), holder.img_goods,ac.goods_default);
            holder.title_goods.setText( goods.getTitle());
            holder.coin_goods.setText(Html
                    .fromHtml("狗粮:<font color=\"#eb100a\">"
                            + goods.getCoin() + "</font>"));

        return convertView;
    }

    class ViewHolder {
        ImageView img_goods;
        TextView title_goods, coin_goods;

    }

    @Override
    public void setList(List list) {
        super.setList(list);
    }
}

package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import android.view.View.OnClickListener;
import com.quanliren.quan_two.bean.BusinessBean;
import com.quanliren.quan_two.bean.DongTaiBean;
import com.quanliren.quan_two.util.Util;

import java.util.List;

public class RestaurantListAdapter extends ParentsAdapter {
    BusinessListener listener;
    public RestaurantListAdapter(Context c, List list,BusinessListener listener) {
        super(c, list);
        this.listener=listener;
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

           convertView = View.inflate(c, R.layout.restaurant_list_item, null);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.resta_image = (ImageView) convertView.findViewById(R.id.resta_image);
            holder.categories = (TextView) convertView.findViewById(R.id.categories);
            holder.avg_price = (TextView) convertView.findViewById(R.id.avg_price);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.eat_resta = (TextView) convertView.findViewById(R.id.eat_resta);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BusinessBean business = (BusinessBean) list.get(position);
        ImageLoader.getInstance().displayImage(business.getS_photo_url(), holder.resta_image,
                ac.options_defalut);
        holder.eat_resta.setTag(business);
        holder.eat_resta.setOnClickListener(returnResult);
        holder.name.setText(business.getName());
        holder.avg_price.setText("人均  ￥"+business.getAvg_price());

        StringBuffer sb=new StringBuffer();
        for(int i=0;i<business.getCategories().size();i++){
            sb.append(business.getCategories().get(i)+"  ");
        }
        holder.categories.setText(sb.toString());
        holder.address.setText(business.getAddress());
//        if(business.getDistance()!=0){
//            holder.distance.setText(business.getDistance()/1000.0+"km");
//        }else{
//            holder.distance.setText("");
//        }
        if(!"".equals(business.getLatitude())&&!"".equals(business.getLongitude())){
            holder.distance.setText(Util.getDistance(
                    Double.valueOf(ac.cs.getLng()),
                    Double.valueOf(ac.cs.getLat()), business.getLongitude(),
                    business.getLatitude())
                    + "km");
        }else{
            holder.distance.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        ImageView resta_image;
        TextView name, avg_price, categories, address,distance,eat_resta;
    }
    public interface BusinessListener {
        public void returnResult(Object bean);
    }
    OnClickListener returnResult = new OnClickListener() {

        @Override
        public void onClick(View v) {
            listener.returnResult((BusinessBean) v.getTag());
        }
    };
}

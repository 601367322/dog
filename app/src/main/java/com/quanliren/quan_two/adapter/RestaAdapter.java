package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.Area;
import com.quanliren.quan_two.fragment.ChosePositionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class RestaAdapter extends ParentsAdapter{
    public RestaAdapter(Context c, List list) {
        super(c,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflater= LayoutInflater.from(c).inflate(android.R.layout.simple_list_item_1,null);
        TextView tv=(TextView)inflater.findViewById(android.R.id.text1);
        tv.setTextSize(15);
        tv.setText(list.get(position).toString());
        return tv;
    }
}

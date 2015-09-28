package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.Area;
import com.quanliren.quan_two.fragment.ChosePositionFragment;

import java.util.ArrayList;
import java.util.List;

public class AreaAdapter extends ParentsAdapter{

	public AreaAdapter(Context c, List list) {
		super(c, list);
		charlist=new ArrayList<String>();
		for (char cs : ChosePositionFragment.firstChar) {
			charlist.add(String.valueOf(cs));
		}
	}
	private List<String> charlist;
	
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Area a=(Area) getList().get(position);
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView= View.inflate(c, R.layout.area_item, null);
			holder.title=(TextView) convertView.findViewById(R.id.title);
			holder.item=(TextView) convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		if(a.title!=null&&!a.title.equals("")){
			if(holder.title!=null){
				holder.title.setVisibility(View.VISIBLE);
			}
		}else{
			if(holder.title!=null){
				holder.title.setVisibility(View.GONE);
			}
		}
		holder.title.setText(a.title);
		holder.item.setText(a.name);
		return convertView;
	}
	
	static class ViewHolder{
		TextView title;
		TextView item;
	}
}

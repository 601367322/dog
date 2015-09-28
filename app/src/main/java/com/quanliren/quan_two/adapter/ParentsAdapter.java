package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.db.DBHelper;

import java.util.List;

public abstract class ParentsAdapter extends BaseAdapter {
    public List list;
    public Context c;
    public AppClass ac;

    public ParentsAdapter(Context c, List list) {
        this.c = c;
        this.ac = (AppClass) c.getApplicationContext();
        this.list = list;
    }

    public int getCount() {
        if (list != null)
            return list.size();
        else {
            return 0;
        }
    }

    public List getList() {
        return list;
    }

    public Object getItem(int position) {
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup arg2);

    public void addNewsItem(Object obj) {
        list.add(obj);
    }
    public void addFirstItem(Object obj) {
        list.add(0, obj);
    }

    public void addNewsItems(List obj) {
        list.addAll(obj);
    }

    public void setList(List list) {
        this.list = list;
    }

    public void clear() {
        if (list != null)
            list.clear();
    }

    public void removeObj(int position) {
        list.remove(position);
    }

    public void removeObj(Object obj) {
        list.remove(obj);
    }

    public void addToFirsts(List lists) {
        list.addAll(0, lists);
    }

    public void distory() {
        if (list != null) {
            list.clear();
        }
    }

    public DBHelper getHelper() {
        return OpenHelperManager.getHelper(c, DBHelper.class);
    }

}

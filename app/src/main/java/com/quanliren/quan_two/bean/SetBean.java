package com.quanliren.quan_two.bean;

import android.content.Intent;

public class SetBean {
    public int icon;
    public int img;
    public String title;
    public int isFirst;
    public Intent clazz;
    public String source;
    public boolean login;
    public int count;

    public String getSource() {
        if (source == null || source.equals("")) {
            return "0MB";
        }
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public SetBean() {
        super();
        // TODO Auto-generated constructor stub
    }

//    public SetBean(int icon, String title, int isFirst, Intent clazz, boolean login) {
//        super();
//        this.icon = icon;
//        this.title = title;
//        this.isFirst = isFirst;
//        this.clazz = clazz;
//        this.login = login;
//    }
    public SetBean(int icon, String title,int img, int isFirst, Intent clazz, boolean login) {
        super();
        this.icon = icon;
        this.title = title;
        this.img = img;
        this.isFirst = isFirst;
        this.clazz = clazz;
        this.login = login;
    }

}

package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class LeftMenuItem implements Serializable {
    public Integer icon; //默认位选中的图标
    public Integer focus_icon;    //选中的图标
    public String title;    //名称
    public boolean focus;    //是否已选中
    public Class clazz;    //切换的窗体

    /**
     * 构造函数
     *
     * @param icon       默认位选中的图标
     * @param focus_icon 选中的图标
     * @param title      名称
     * @param focus      是否已选中
     * @param clazz      切换的窗体
     */
    public LeftMenuItem(Integer icon, Integer focus_icon, String title,
                        boolean focus, Class clazz) {
        super();
        this.icon = icon;
        this.focus_icon = focus_icon;
        this.title = title;
        this.focus = focus;
        this.clazz = clazz;
    }
}

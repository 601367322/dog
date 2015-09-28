package com.quanliren.quan_two.bean;


import java.io.Serializable;

public class Area implements Serializable {
    public int id;
    public String name;
    public String hzpy;
    public boolean hot;
    public String title;
    public String hideTitle;

    public Area(int id, String name, String hzpy, boolean hot) {
        super();
        this.id = id;
        this.name = name;
        this.hzpy = hzpy;
        this.hot = hot;
        this.hideTitle = hzpy;
    }

    public Area() {
        super();
    }

    public Area(int id, String name, String hzpy, String title) {
        super();
        this.id = id;
        this.name = name;
        this.hzpy = hzpy;
        this.title = title;
    }

    public Area(int id, String name, String hzpy) {
        super();
        this.id = id;
        this.name = name;
        this.hzpy = hzpy;
    }
}

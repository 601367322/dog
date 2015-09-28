package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class AD implements Serializable {
    private int id;
    private String imgpath;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AD(int id, String imgpath, String url) {
        super();
        this.id = id;
        this.imgpath = imgpath;
        this.url = url;
    }

    public AD() {
        super();
        // TODO Auto-generated constructor stub
    }
}

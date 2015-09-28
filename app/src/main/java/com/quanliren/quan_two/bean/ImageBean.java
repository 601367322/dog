package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class ImageBean implements Serializable {
    public int position;
    public int imgid;
    public String imgpath;
    public boolean defaults;
    public int[] wh;

    public ImageBean(int position, String path, int[] wh) {
        super();
        this.position = position;
        this.imgpath = path;
        this.wh = wh;
    }

    public ImageBean(int position, String path) {
        super();
        this.position = position;
        this.imgpath = path;
    }

    public ImageBean() {
        super();
    }

    public ImageBean(boolean defaults) {
        super();
        this.defaults = defaults;
    }

    public ImageBean(String imgpath) {
        super();
        this.imgpath = imgpath;
    }

}

package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class ShopBean implements Serializable {

    private int id;
    private int img;
    private String title;
    private String price;
    private int viewType;
    private int progress;
    private int gnumber;

    public int getGnumber() {
        return gnumber;
    }

    public void setGnumber(int gnumber) {
        this.gnumber = gnumber;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ShopBean(int id, int img, String title, String price, int viewType) {
        super();
        this.id = id;
        this.img = img;
        this.title = title;
        this.price = price;
        this.viewType = viewType;
    }

    public ShopBean() {
        super();
        // TODO Auto-generated constructor stub
    }
}

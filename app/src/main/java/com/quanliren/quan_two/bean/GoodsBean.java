package com.quanliren.quan_two.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsBean implements Serializable {

    private int gid;
    private String coin;
    private String title;
    private String imgurl;
    public void setCoin(){
        this.coin=coin;
    }
    public String getCoin(){
        return coin;
    }


    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public GoodsBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

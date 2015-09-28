package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class OrderBean implements Serializable {

    private String notify_url;
    private String viptime;
    private String price;
    private int type;
    private String vipday;
    private int coin;
    private String order_no;

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVipday() {
        return vipday;
    }

    public void setVipday(String vipday) {
        this.vipday = vipday;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public OrderBean(String notify_url, String viptime, String price, int type,
                     String vipday, int coin, String order_no) {
        super();
        this.notify_url = notify_url;
        this.viptime = viptime;
        this.price = price;
        this.type = type;
        this.vipday = vipday;
        this.coin = coin;
        this.order_no = order_no;
    }

    public OrderBean() {
        super();
        // TODO Auto-generated constructor stub
    }


}

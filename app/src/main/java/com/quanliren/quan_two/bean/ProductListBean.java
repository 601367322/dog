package com.quanliren.quan_two.bean;

import java.io.Serializable;
import java.util.List;

public class ProductListBean implements Serializable {
    private String categroy;
    private List<ProductBean> itemlist;

    public String getCategroy() {
        return categroy;
    }

    public void setCategroy(String categroy) {
        this.categroy = categroy;
    }

    public List<ProductBean> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<ProductBean> itemlist) {
        this.itemlist = itemlist;
    }

}

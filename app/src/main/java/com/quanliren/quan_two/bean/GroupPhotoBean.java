package com.quanliren.quan_two.bean;

public class GroupPhotoBean {
    private int id;
    private String imgurl;
    private boolean selected;

    public GroupPhotoBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public GroupPhotoBean(int id, String imgurl, boolean selected) {
        super();
        this.id = id;
        this.imgurl = imgurl;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}

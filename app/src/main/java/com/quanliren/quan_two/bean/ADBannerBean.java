package com.quanliren.quan_two.bean;

import java.io.Serializable;

/**
 * Created by Shen on 2015/7/2.
 */
public class ADBannerBean implements Serializable {

    private String id;
    private String btype;
    private String path;
    private String url;
    private String pnum;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    public String getBtype() {
        return btype;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getPnum() {
        return pnum;
    }
}

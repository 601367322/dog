package com.quanliren.quan_two.bean;

import java.io.Serializable;
import java.util.List;

public class ProductBean implements Serializable {

    private int gid;
    private int gcoin;
    private String categroy;
    private String isVip;
    private String title;
    private String coin;
    private String imgurl;
    private String detail;
    private String remark;
    private String status;
    private String eaid;
    private String truename;
    private String mobile;
    private String email;
    private String address;
    private String exchremark;

    public int getGcoin() {
        return gcoin;
    }

    public void setGcoin(int gcoin) {
        this.gcoin = gcoin;
    }
    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExchremark() {
        return exchremark;
    }

    public void setExchremark(String exchremark) {
        this.exchremark = exchremark;
    }

    private String applytime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEaid() {
        return eaid;
    }

    public void setEaid(String eaid) {
        this.eaid = eaid;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    private List<ImageBean> imglist;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ImageBean> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImageBean> imglist) {
        this.imglist = imglist;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public ProductBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getCategroy() {
        return categroy;
    }

    public void setCategroy(String categroy) {
        this.categroy = categroy;
    }
    public String getIsVip(){
        return isVip;
    }
    public void setIsVip(String isVip){
        this.isVip=isVip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

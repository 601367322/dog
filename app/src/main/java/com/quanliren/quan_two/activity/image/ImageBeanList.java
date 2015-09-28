package com.quanliren.quan_two.activity.image;

import com.quanliren.quan_two.bean.ImageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shen on 2015/6/23.
 */
public class ImageBeanList implements Serializable{
    public List<ImageBean> mProfile;

    public ImageBeanList() {
        super();
    }

    public ImageBeanList(List<ImageBean> mProfile) {
        this.mProfile = mProfile;
    }
}

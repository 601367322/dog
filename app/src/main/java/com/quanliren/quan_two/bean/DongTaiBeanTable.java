package com.quanliren.quan_two.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "DongTaiBeanTable")
public class DongTaiBeanTable implements Serializable {
    @DatabaseField(id = true)
    private String dyid;

    public String getDyid() {
        return dyid;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
    }

    @DatabaseField
    private String content;
    private DongTaiBean bean;


    public DongTaiBeanTable(String dyid, String content, DongTaiBean bean) {
        super();
        this.dyid = dyid;
        this.content = content;
        this.bean = bean;
    }

    public DongTaiBean getBean() {
        if (bean != null) {
            return bean;
        } else {
            return new Gson().fromJson(content, new TypeToken<DongTaiBean>() {
            }.getType());
        }
    }

    public void setBean(DongTaiBean bean) {
        this.bean = bean;
        this.content = new Gson().toJson(bean);
    }

    public String getContent() {
        if (bean == null) {
            return content;
        }
        return new Gson().toJson(bean);
    }

    public void setContent(String content) {
        this.content = content;
        bean = new Gson().fromJson(content, new TypeToken<DongTaiBean>() {
        }.getType());
    }

    public DongTaiBeanTable() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DongTaiBeanTable(DongTaiBean bean) {
        super();
        setBean(bean);
        this.dyid = bean.getDyid();
    }
}

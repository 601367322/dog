package com.quanliren.quan_two.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "versionbean_table")
public class VersionBean implements Serializable {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String remark;
    @DatabaseField
    private String vname;
    @DatabaseField
    private String url;
    @DatabaseField
    private int isnewest;
    @DatabaseField
    private String size;
    @DatabaseField
    private String vcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VersionBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public VersionBean(String remark, String vname, String url, int isnewest,
                       long size, int vcode) {
        super();
        this.remark = remark;
        this.vname = vname;
        this.url = url;
        this.isnewest = isnewest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsnewest() {
        return isnewest;
    }

    public void setIsnewest(int isnewest) {
        this.isnewest = isnewest;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }
}

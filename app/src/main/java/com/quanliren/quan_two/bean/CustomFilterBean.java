package com.quanliren.quan_two.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "CustomFilterBean")
public class CustomFilterBean implements Serializable {
    @DatabaseField(id = true)
    public String key;
    @DatabaseField
    public int id;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomFilterBean(String key,
                            int id) {
        super();
        this.key = key;
        this.id = id;
    }


    public CustomFilterBean() {
        super();
        // TODO Auto-generated constructor stub
    }
}
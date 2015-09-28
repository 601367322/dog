package com.quanliren.quan_two.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shen on 2015/7/2.
 */
@DatabaseTable(tableName = "FilterBean")
public class FilterBean {

    public static final String KEY = "key";
    public static final String VALUE = "value";

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String key;
    @DatabaseField
    public String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FilterBean(int id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public FilterBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public FilterBean() {
        super();
    }
}

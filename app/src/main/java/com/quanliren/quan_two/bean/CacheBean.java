package com.quanliren.quan_two.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@DatabaseTable(tableName = "CacheBean")
public class CacheBean extends BaseBean {
    @DatabaseField(id = true)
    private String key;
    @DatabaseField
    private String value;
    @DatabaseField
    private long time;

    public CacheBean(String key, String value, long time) {
        super();
        this.key = key;
        this.value = value;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public CacheBean() {
        super();
        // TODO Auto-generated constructor stub
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

    public boolean isAfter() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Date old = new Date(time);
            Date now = new Date();
            String nowstr = format.format(now);
            String oldstr = format.format(old);
            return format.parse(nowstr).after(format.parse(oldstr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}

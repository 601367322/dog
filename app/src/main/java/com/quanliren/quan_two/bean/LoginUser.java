package com.quanliren.quan_two.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "LoginUserTable")
public class LoginUser {
    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String mobile;
    @DatabaseField
    private String pwd;
    @DatabaseField
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginUser(String id, String mobile, String pwd, String token) {
        super();
        this.id = id;
        this.mobile = mobile;
        this.pwd = pwd;
        this.token = token;
    }

    public LoginUser() {
        super();
        // TODO Auto-generated constructor stub
    }
}

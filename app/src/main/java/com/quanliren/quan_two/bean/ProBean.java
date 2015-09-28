package com.quanliren.quan_two.bean;

import java.io.Serializable;

public class ProBean implements Serializable {
    private String actid;
    private String title;
    private String acturl;
    private String code;
    private int codestatus;
    private String codeid;

    public String getCodeid() {
        return codeid;
    }

    public void setCodeid(String codeid) {
        this.codeid = codeid;
    }

    private int winstate;
    private String ctime;

    public ProBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ProBean(String actid, String title, String acturl, String code,
                   int codestatus, int winstate, String ctime) {
        super();
        this.actid = actid;
        this.title = title;
        this.acturl = acturl;
        this.code = code;
        this.codestatus = codestatus;
        this.winstate = winstate;
        this.ctime = ctime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodestatus() {
        return codestatus;
    }

    public void setCodestatus(int codestatus) {
        this.codestatus = codestatus;
    }

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActurl() {
        return acturl;
    }

    public void setActurl(String acturl) {
        this.acturl = acturl;
    }

    public int getWinstate() {
        return winstate;
    }

    public void setWinstate(int winstate) {
        this.winstate = winstate;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}

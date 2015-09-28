package com.quanliren.quan_two.activity.redline;

import com.quanliren.quan_two.bean.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shen on 2015/7/13.
 */
public class RedLineBean implements Serializable {

    public int id;
    public int transcount;
    public String message;
    public int zambia;
    public String zambiastate;
    public int pairingstate;
    public String createtime;
    public User user;
    public List<User> tranList;
    public User lastUser;
    public int collectionstate;
}

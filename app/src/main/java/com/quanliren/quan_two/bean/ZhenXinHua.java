package com.quanliren.quan_two.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/9/9.
 */
public class ZhenXinHua implements Serializable {

    public String txt;

    public List<Answer> answer = new ArrayList<>();

    public class Answer implements Serializable {

        public String answer;
        public String funId;
    }
}

package com.quanliren.quan_two.activity.user.perf;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface FilterPerfs {

    @DefaultInt(-1)
    int sex();

    @DefaultInt(-1)
    int time();

    @DefaultInt(0)
    int state();

    @DefaultInt(0)
    int xing();

    @DefaultInt(0)
    int age();

    @DefaultInt(0)
    int ol();
}

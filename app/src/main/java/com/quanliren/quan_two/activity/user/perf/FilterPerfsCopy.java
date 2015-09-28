package com.quanliren.quan_two.activity.user.perf;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface FilterPerfsCopy {

    @DefaultInt(2)
    int sex();

    @DefaultInt(0)
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

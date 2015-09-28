package com.quanliren.quan_two.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.User;

/**
 * Created by Shen on 2015/7/17.
 */
public class UserLogo extends CircleImageView {

    public UserLogo(Context context) {
        super(context);
    }

    public UserLogo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserLogo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private User user;

    public void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null && !TextUtils.isEmpty(user.getId())) {
                    if (user.getId().equals(((AppClass) getContext().getApplicationContext()).getLoginUserId())) {
                        UserInfoActivity_.intent(getContext()).start();
                    } else {
                        UserOtherInfoActivity_.intent(getContext()).userId(user.getId()).start();
                    }
                }
            }
        });
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

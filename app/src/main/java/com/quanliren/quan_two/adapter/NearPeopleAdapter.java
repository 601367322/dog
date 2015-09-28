package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.MyVisitActivity;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class NearPeopleAdapter extends BaseAdapter<User> {

    public NearPeopleAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.near_people_item;
    }

    public class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        ImageView userlogo;
        @Bind(R.id.signature)
        TextView signature;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nick_ll;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(User user, int position) {
            LogUtil.d("============visit",user.getAvatar());
            nick_ll.setUser(user);
            ImageLoader.getInstance().displayImage(
                    user.getAvatar() + StaticFactory._320x320, userlogo,
                    ac.options_userlogo);
            if (!TextUtils.isEmpty(user.getSignature())) {
                signature.setText(user.getSignature());
            } else {
                signature.setText(R.string.lazy);
            }
            if (user.getLatitude() != 0 && user.getLongitude() != 0
                    && !ac.cs.getLat().equals("") && !ac.cs.getLng().equals("")) {
                time.setText(Util.getDistance(
                        Double.valueOf(ac.cs.getLng()),
                        Double.valueOf(ac.cs.getLat()), user.getLongitude(),
                        user.getLatitude())
                        + "km");
            } else {
                time.setText("");
            }
            if (context instanceof MyVisitActivity) {
                time.setText(time.getText().toString() + " | " + Util.getTimeDateStr(user.getVisittime()));
            }

        }

    }

}

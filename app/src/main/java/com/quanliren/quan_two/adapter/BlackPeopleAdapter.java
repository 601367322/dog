package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class BlackPeopleAdapter extends BaseAdapter<User> {

    public BlackPeopleAdapter(Context context) {
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
        @Bind(R.id.nickname)
        TextView nickname;
        @Bind(R.id.signature)
        TextView signature;
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nick_ll;
        @Bind(R.id.time)
        TextView time;

        @Override
        public void bind(User user, int position) {
            nickname.setText(user.getNickname());
            ImageLoader.getInstance().displayImage(user.getAvatar() + StaticFactory._160x160, userlogo, ac.options_userlogo);
            if (!TextUtils.isEmpty(user.getSignature())) {
                signature.setText(user.getSignature());
            } else {
                signature.setText(R.string.lazy);
            }
            if (user.getCtime() != null)
                time.setText("于" + Util.getTimeDateStr(user.getCtime()) + "拉黑");
            else
                time.setText("");
            time.setCompoundDrawables(null, null, null, null);
            nick_ll.setUser(user);
        }

        public ViewHolder(View view) {
            super(view);
        }
    }
}

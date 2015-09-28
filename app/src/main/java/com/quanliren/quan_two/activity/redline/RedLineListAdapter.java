package com.quanliren.quan_two.activity.redline;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.custom.ZanLinearLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Shen on 2015/6/30.
 */
public class RedLineListAdapter extends BaseAdapter<RedLineBean> {

    public RedLineListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.red_line_list_item;
    }

    public class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nickLl;
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.dog_food)
        TextView dogFood;
        @Bind(R.id.gridview)
        GridView gridview;
        @Bind(R.id.usernumber)
        TextView usernumber;
        @Bind(R.id.zan_ll)
        ZanLinearLayout zan_ll;

        RedLineListGridViewAdapter adapter;


        public ViewHolder(View view) {
            super(view);
            adapter = new RedLineListGridViewAdapter(context);
            gridview.setAdapter(adapter);
        }

        @Override
        public void bind(RedLineBean bean, int position) {
            ImageLoader.getInstance().displayImage(
                    bean.user.getAvatar() + StaticFactory._320x320, userlogo,
                    ac.options_userlogo);
            userlogo.setUser(bean.user);
            nickLl.setUser(bean.user);
            if (TextUtils.isEmpty(bean.message)) {
                message.setText(R.string.lazy);
            } else {
                message.setText(bean.message);
            }
            time.setText(Util.getTimeDateStr(bean.createtime));
            dogFood.setText(Util.getDogFood(bean.user.getCoin()));
            usernumber.setText(bean.transcount + "");
            List<User> transList = new ArrayList<>();
            transList.addAll(bean.tranList);
            for (int i = 0; i < 10; i++) {
                transList.add(new User());
            }
            adapter.setList(transList);
            adapter.notifyDataSetChanged();
            zan_ll.setBean(bean);
        }
    }

}

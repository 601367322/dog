package com.quanliren.quan_two.activity.redline;

import android.content.Context;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.util.StaticFactory;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/13.
 */
public class RedLineListGridViewAdapter extends BaseAdapter<User> {

    public RedLineListGridViewAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.red_line_list_grid_item;
    }

    public class ViewHolder extends BaseHolder{

        @Bind(R.id.userlogo)
        UserLogo userlogo;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(User bean, int position) {
            ImageLoader.getInstance().displayImage(
                    bean.getAvatar() + StaticFactory._160x160, userlogo,
                    ac.options_userlogo);
            userlogo.setUser(bean);
        }
    }
}

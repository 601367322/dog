package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.CustomVip;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;

public class DateApplyManageAdapter extends BaseAdapter<User> {
    AgreeOnClickListener listener;
    public DateApplyManageAdapter(Context context,AgreeOnClickListener listener) {
        super(context);
        this.listener=listener;
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.date_people_item;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isFinish = false;

    public void setDtid(int dtid) {
        this.dtid = dtid;
    }

    public int dtid;

    @Override
    public void setList(List<User> list) {
        super.setList(list);
        for (Object user:list){
            if(((User)user).getApplystate()==1){
                this.isFinish=true;
                break;
            }
        }
    }

    class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        ImageView userlogo;
        @Bind(R.id.agree)
        ImageView agree;
        @Bind(R.id.nickLl)
        UserNickNameRelativeLayout nickLl;
        @Bind(R.id.signature)
        TextView signature;
        @Bind(R.id.time)
        TextView time;

        @Override
        public void bind(final User user, int position) {
            ImageLoader.getInstance().displayImage(
                    user.getAvatar() + StaticFactory._320x320, userlogo,
                    ac.options_userlogo);
            nickLl.setUser(user);
            if (!"".equals(user.getSignature()) && user.getSignature() != null) {
                signature.setText(user.getSignature());
            } else {
                signature.setText("这个人好懒，什么都没有留下");
            }

            if (user.getDistance() != null && !"".equals(user.getDistance())) {
                time.setText(Util.getDistance(user.getDistance()));
                time.setText(time.getText().toString() + " | "
                        + Util.getTimeDateStr(user.getActivetime()));
            } else if (user.getLatitude() != 0 && user.getLongitude() != 0
                    && !ac.cs.getLat().equals("")) {
                time.setText(Util.getDistance(
                        Double.valueOf(ac.cs.getLng()),
                        Double.valueOf(ac.cs.getLat()), user.getLongitude(),
                        user.getLatitude())
                        + "km");
                time.setText(time.getText().toString() + " | "
                        + Util.getTimeDateStr(user.getActivetime()));
            } else {
                time.setText("");
            }

            if (isFinish) {
                if (user.getApplystate() == 1) {
                    agree.setImageResource(R.drawable.date_selector);
                } else {
                    agree.setImageResource(R.drawable.undateable);
                }
                agree.setOnClickListener(null);
            } else {
                agree.setTag(user);
                agree.setOnClickListener(agreeClick);
                agree.setImageResource(R.drawable.dateable);
            }
        }

        public ViewHolder(View view) {
            super(view);
        }
    }

    OnClickListener agreeClick = new OnClickListener() {
        @Override
        public void onClick(final View agree) {
            listener.agreeOnClick((User)agree.getTag());

        }
    };
    public interface AgreeOnClickListener{
        void agreeOnClick(User user);
    }
}
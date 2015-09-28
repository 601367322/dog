package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DongTaiReplyMeBean;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class QuanReplyAdapter extends BaseAdapter<DongTaiReplyMeBean> {

    IQuanDetailReplyAdapter listener;

    public interface IQuanDetailReplyAdapter {
        void logoCick(DongTaiReplyMeBean bean);
    }

    public void setListener(IQuanDetailReplyAdapter listener) {
        this.listener = listener;
    }

    public QuanReplyAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.quan_reply_my;
    }

    public class ViewHolder extends BaseHolder {
        @Nullable
        @Bind(R.id.userlogo)
        ImageView userlogo;
        @Nullable
        @Bind(R.id.content_image)
        ImageView content_image;
        @Nullable
        @Bind(R.id.nickname)
        TextView username;
        @Nullable
        @Bind(R.id.time)
        TextView time;
        @Nullable
        @Bind(R.id.signature)
        TextView signature;
        @Nullable
        @Bind(R.id.content_text)
        TextView content_text;
        @Nullable
        @Bind(R.id.vip)
        View vip;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(DongTaiReplyMeBean bean, int position) {
            username.setText(bean.getNickname());
            userlogo.setTag(bean);
            userlogo.setOnClickListener(viewClick);
            time.setText(Util.getTimeDateStr(bean.getCtime()));
            if (bean.getReplyuid() != null && !bean.getReplyuid().equals("") && !bean.getReplyuid().equals("-1")) {
                signature.setText("回复  " + bean.getContent());
            } else {
                signature.setText("回复  " + bean.getContent());
            }
            ImageLoader.getInstance().displayImage(
                    bean.getAvatar() + StaticFactory._160x160, userlogo,
                    ac.options_userlogo);
            if (!"".equals(bean.getStype()) && bean.getStype() != null) {
                if ("0".equals(bean.getStype())) {
                    content_text.setVisibility(View.VISIBLE);
                    content_image.setVisibility(View.GONE);
                    content_text.setText(bean.getScontent());
                } else if ("1".equals(bean.getStype())) {
                    content_text.setVisibility(View.GONE);
                    content_image.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(
                            bean.getScontent() + StaticFactory._160x160, content_image,
                            ac.options_userlogo);
                }
            } else {
                content_text.setVisibility(View.GONE);
                content_image.setVisibility(View.GONE);
            }
        }
    }


    OnClickListener viewClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.userlogo:
                    listener.logoCick((DongTaiReplyMeBean) v.getTag());
                    break;
            }
        }
    };
}

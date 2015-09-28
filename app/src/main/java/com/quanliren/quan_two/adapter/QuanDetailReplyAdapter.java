package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DongTaiReplyBean;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class QuanDetailReplyAdapter extends BaseAdapter<DongTaiReplyBean> {

    public QuanDetailReplyAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.reply_item;
    }

    private IQuanDetailReplyAdapter listener;

    public IQuanDetailReplyAdapter getListener() {
        return listener;
    }

    public void setListener(IQuanDetailReplyAdapter listener) {
        this.listener = listener;
    }

    class ViewHolder extends BaseHolder {

        @Bind(R.id.nickname)
        TextView username;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.delete_content)
        TextView delete_content;
        @Bind(R.id.content_rl)
        View content_rl;
        @Bind(R.id.delete_ll)
        View delete_ll;
        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.reply_img)
        ImageView reply_img;

        @Override
        public void bind(DongTaiReplyBean bean, int position) {
            username.setText(bean.getNickname());
            content_rl.setTag(bean);
            content_rl.setOnClickListener(viewClick);
            delete_ll.setVisibility(View.GONE);
            if (bean.getUserid().equals(ac.getLoginUserId())) {
                delete_ll.setVisibility(View.VISIBLE);
                reply_img.setTag(bean);
                reply_img.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.contentClick((DongTaiReplyBean) v.getTag());
                    }
                });
                delete_content.setTag(bean);
                delete_content.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.deleteContentCick((DongTaiReplyBean) v.getTag());
                    }
                });
            }

            if (bean.getReplyuid() != null && !bean.getReplyuid().equals("") && !bean.getReplyuid().equals("-1")) {
                content.setText("回复 " + bean.getReplyuname() + " : " + bean.getContent());
            } else {
                content.setText(bean.getContent());
            }

            userlogo.setTag(bean);
            userlogo.setOnClickListener(viewClick);
            time.setText(Util.getTimeDateStr(bean.getCtime()));
            ImageLoader.getInstance().displayImage(
                    bean.getAvatar() + StaticFactory._160x160, userlogo, ac.options_userlogo);
        }

        public ViewHolder(View view) {
            super(view);
        }
    }

    public interface IQuanDetailReplyAdapter {
        void contentClick(DongTaiReplyBean bean);

        void logoCick(DongTaiReplyBean bean);

        void deleteContentCick(DongTaiReplyBean bean);

    }

    OnClickListener viewClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.userlogo:
                    listener.logoCick((DongTaiReplyBean) v.getTag());
                    break;
                case R.id.reply_icon:
                    listener.contentClick((DongTaiReplyBean) v.getTag());
                    break;
                case R.id.content_rl:
                    listener.contentClick((DongTaiReplyBean) v.getTag());
                    break;
                case R.id.de_ll:
                    listener.deleteContentCick((DongTaiReplyBean) v.getTag());
                    break;

            }
        }
    };
}

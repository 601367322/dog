package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DongTaiBean;
import com.quanliren.quan_two.custom.DongTaiZanLinearLayout;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class QuanListAdapter extends BaseAdapter<DongTaiBean> {

    IQuanAdapter listener;
    int imgWidth = 0;

    public QuanListAdapter(Context context) {
        super(context);

        imgWidth = (context.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(context, 88)) / 3;
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.quan_item;
    }

    public class ViewHolder extends BaseHolder {
        @Bind(R.id.userlogo)
        ImageView userlogo;
        @Bind(R.id.nickname)
        TextView username;
        @Bind(R.id.sex)
        TextView sex;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.reply_btn)
        TextView reply_btn;
        @Bind(R.id.signature)
        TextView signature;
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.content_rl)
        View content_rl;
        @Bind(R.id.click_ll)
        View click_ll;
        @Bind(R.id.vip)
        View vip;
        @Bind(R.id.show)
        View show;
        @Bind(R.id.img_content)
        ImageView img_content;
        @Bind(R.id.nickname_rl)
        UserNickNameRelativeLayout nick_ll;
        @Bind(R.id.zan_ll)
        DongTaiZanLinearLayout zan_ll;
        @Override
        public void bind(DongTaiBean db, int position) {
            if(db.getImglist() == null || db.getImglist().size() == 0){
                show.setVisibility(View.VISIBLE);
                img_content.setImageResource(R.drawable.dongtai_content_default);
            }else {
                show.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(db.getImglist().get(0).imgpath + StaticFactory._640x640, img_content, ac.options_dongtai, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        show.setVisibility(View.VISIBLE);
                    }
                });
            }
            ImageLoader.getInstance().displayImage(
                    db.getAvatar() + StaticFactory._160x160, userlogo, ac.options_userlogo);
            nick_ll.setUser(db.getNickname(), Integer.valueOf(db.getSex()), db.getAge(), db.getIsvip());
            time.setText(Util.getTimeDateStr(db.getCtime()));
            if (db.getContent().trim().length() > 0) {
                signature.setVisibility(View.VISIBLE);
                signature.setText(db.getContent());
            } else {
                signature.setVisibility(View.GONE);
            }
            if(db.getFontColor()==0){
                signature.setTextColor(ac.getResources().getColor(R.color.white));
                signature.setShadowLayer(1, 1, 1, ac.getResources().getColor(R.color.black));
            }else{
                signature.setTextColor(ac.getResources().getColor(R.color.black));
                signature.setShadowLayer(1, 1, 1, ac.getResources().getColor(R.color.white));
            }
            userlogo.setTag(position);
            userlogo.setOnClickListener(userlogoClick);

            nick_ll.setTag(position);
            nick_ll.setOnClickListener(userlogoClick);

            if (db.getLatitude() != null && db.getLongitude() != null && !"".equals(db.getLatitude()) && !"".equals(db.getLongitude())
                    && !ac.cs.getLat().equals("")) {
                location.setVisibility(View.VISIBLE);
                if (Integer.valueOf(db.getCnum()) <= 0) {
                    location.setText(Util.getDistance(
                            Double.valueOf(ac.cs.getLng()),
                            Double.valueOf(ac.cs.getLat()), Double.valueOf(db.getLongitude()),
                            Double.valueOf(db.getLatitude()))
                            + "km");
                } else {
                    location.setText(Util.getDistance(
                            Double.valueOf(ac.cs.getLng()),
                            Double.valueOf(ac.cs.getLat()), Double.valueOf(db.getLongitude()),
                            Double.valueOf(db.getLatitude()))
                            + "km");
                }

            } else {
                location.setText("");
                location.setVisibility(View.GONE);
            }
            click_ll.setTag(db);
            click_ll.setOnClickListener(detailClick);
            reply_btn.setText(db.getCnum() + " 条评论");
//            all.setTag(db);
//            all.setOnClickListener(detailClick);
            if (Integer.valueOf(db.getCnum()) <= 0) {
                reply_btn.setVisibility(View.GONE);
            } else {
                reply_btn.setVisibility(View.VISIBLE);
            }
//
//            if (reply_btn.getVisibility() == location.getVisibility() && location.getVisibility() == View.VISIBLE) {
//                line.setVisibility(View.VISIBLE);
//            }else{
//                line.setVisibility(View.GONE);
//            }
            zan_ll.setBean(db);
        }

        public ViewHolder(View view) {
            super(view);
        }
    }

    View.OnClickListener detailClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.detailClick(v.getTag());
            }
        }
    };


    View.OnClickListener userlogoClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DongTaiBean db = list.get(Integer.valueOf(v.getTag()
                    .toString()));
            Intent i = new Intent(context, db.getUserid()
                    .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                    : UserOtherInfoActivity_.class);
            i.putExtra("userId", db.getUserid());
            context.startActivity(i);
        }
    };

    public void setListener(IQuanAdapter listener) {
        this.listener = listener;
    }

    public interface IQuanAdapter {
        public void detailClick(Object bean);
    }

}

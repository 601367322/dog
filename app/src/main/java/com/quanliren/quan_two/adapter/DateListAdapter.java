package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class DateListAdapter extends BaseAdapter<DateBean> {

    IDateAdapterListener listener;

    public DateListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.date_item;
    }

    public class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nickLl;
        @Bind(R.id.location_tv)
        TextView location_tv;
        @Bind(R.id.dog_food)
        TextView coin;
        @Bind(R.id.place_tv)
        TextView place_tv;
        @Bind(R.id.pla)
        TextView pla;
        @Bind(R.id.sex_tv)
        TextView sex_tv;
        @Bind(R.id.money_tv)
        TextView money_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.bm_people_num)
        TextView bm_people_num;
        @Bind(R.id.reply_tv)
        TextView reply_tv;
        @Bind(R.id.date_head_text)
        TextView date_head_text;
        @Bind(R.id.top)
        View content_rl;
        @Bind(R.id.img_state)
        ImageView img_state;
        @Bind(R.id.date_selected)
        ImageView date_selected;
        @Bind(R.id.pub_time)
        TextView pub_time;

        public ViewHolder(View view) {
            super(view);
            date_selected.setVisibility(View.GONE);
        }

        @Override
        public void bind(DateBean bean, int position) {
            ImageLoader.getInstance().displayImage(
                    bean.getAvatar() + StaticFactory._160x160, userlogo, ac.options_userlogo);
            if(bean.getAge()==null||"".equals(bean.getAge())){
                nickLl.setUser(bean.getNickname(),bean.getSex(),"0",bean.getIsvip());
            }else{
                nickLl.setUser(bean.getNickname(),bean.getSex(),bean.getAge(),bean.getIsvip());
            }

            userlogo.setTag(position);
            userlogo.setOnClickListener(userlogo_click);
            if (ac.getLoginUserId() != null && !"".equals(ac.getLoginUserId()) && ac.getLoginUserId().equals(bean.getCuserId())) {
                date_selected.setVisibility(View.VISIBLE);
            } else {
                date_selected.setVisibility(View.GONE);
            }
            pub_time.setText(Util.getTimeDateStr(bean.getPubtime()));
            switch (bean.getDtype()) {
                case 1:
                    img_state.setImageResource(R.drawable.date_eat);
                    date_head_text.setText("约人吃饭");
                    pla.setText("餐厅：");
                    break;
                case 2:
                    img_state.setImageResource(R.drawable.date_movie);
                    date_head_text.setText("约看电影");
                    pla.setText("影院：");
                    break;
                case 5:
                    img_state.setImageResource(R.drawable.date_love);
                    switch (Integer.valueOf(bean.getAim())) {
                        case 0:
                            date_head_text.setText("临时情侣(安慰老妈)");
                            break;
                        case 1:
                            date_head_text.setText("临时情侣(狙击相亲)");
                            break;
                        case 2:
                            date_head_text.setText("临时情侣(充场面)");
                            break;
                        case 3:
                            date_head_text.setText("临时情侣(试着交往)");
                            break;
                    }
                    pla.setText("城市：");
                    break;
            }

            if (Integer.valueOf(bean.getCnum()) <= 0) {
                reply_tv.setText("0 条评论");
            } else {
                reply_tv.setText(bean.getCnum() + " 条评论");
            }
            if (bean.getApplynum() > 0) {
                bm_people_num.setText(bean.getApplynum() + " 人报名");
            } else {
                bm_people_num.setText("0 人报名");
            }

            if (bean.getDistance() != null && !"".equals(bean.getDistance())) {
                location_tv.setText(Util.getDistance(bean.getDistance()));
            } else if (bean.getLatitude() != 0 && bean.getLongitude() != 0
                    && !ac.cs.getLat().equals("")) {
                location_tv.setText(Util.getDistance(
                        Double.valueOf(ac.cs.getLng()),
                        Double.valueOf(ac.cs.getLat()), bean.getLongitude(),
                        bean.getLatitude())
                        + "km");
            }
            switch (bean.getCtype()) {
                case 0:
                    if (bean.getCoin() > 0) {
                        coin.setText(bean.getCoin() + "");
                    } else {
                        coin.setText("0");
                    }
                    break;
                case 2:
                    coin.setText(bean.getCoin() + "");
                    break;
                case 1:
                    coin.setText(bean.getCoin() + "");
                    break;
            }

            place_tv.setText(bean.getAddress());
            if ("1".equals(bean.getObjsex())) {
                sex_tv.setText("帅哥");
            } else if ("0".equals(bean.getObjsex())) {
                sex_tv.setText("美女");
            } else if ("2".equals(bean.getObjsex())) {
                sex_tv.setText("不限");
            }

            if ("0".equals(bean.getWhopay())) {
                money_tv.setText("AA制");
            } else if ("1".equals(bean.getWhopay())) {
                money_tv.setText("我消费");
            }
            time_tv.setText(bean.getDtime());
            content_rl.setTag(bean);
            content_rl.setOnClickListener(detail);
            content_rl.setOnLongClickListener(detailLongClick);
        }
    }


    View.OnClickListener userlogo_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DateBean db = list.get(Integer.valueOf(v.getTag()
                    .toString()));
            Intent i = new Intent(
                    context,
                    db.getUserid().equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                            : UserOtherInfoActivity_.class);
            i.putExtra("userId", db.getUserid());
            context.startActivity(i);
        }
    };
    View.OnClickListener detail = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.click((DateBean) v.getTag());
            }
        }
    };
    View.OnLongClickListener detailLongClick = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                listener.longClick((DateBean) v.getTag());
                return true;
            }
            return false;
        }
    };

    public IDateAdapterListener getListener() {
        return listener;
    }

    public void setListener(IDateAdapterListener listener) {
        this.listener = listener;
    }

    public interface IDateAdapterListener {
        public void click(DateBean bean);

        public void longClick(DateBean bean);
    }

}

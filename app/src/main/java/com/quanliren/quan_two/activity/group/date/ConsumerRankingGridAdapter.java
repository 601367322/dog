package com.quanliren.quan_two.activity.group.date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.AutoScrollImageSwitcher;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/21.
 */
public class ConsumerRankingGridAdapter extends BaseAdapter<User> {

    private List<User> all = new ArrayList<>();

    public ConsumerRankingGridAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.consumer_ranking_grid_item;
    }

    public List<User> getAll() {
        return all;
    }

    public void setAll(List<User> all) {
        this.all = all;
    }

    class ViewHolder extends BaseHolder {

        //        @Bind(R.id.userlogo)
//        ImageView userlogo;
        @Bind(R.id.nickname)
        TextView nickname;
        @Bind(R.id.dogfood)
        TextView dogfood;
        @Bind(R.id.vip)
        View vip;
        @Bind(R.id.imgSwitcher)
        AutoScrollImageSwitcher switcher;

        int secondWidth = 0;
        int thirdWidth = 0;

        public ViewHolder(View view) {
            super(view);

            secondWidth = (context.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(context, 4)) / 2;
            thirdWidth = (context.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(context, 8)) / 3;

        }

        @Override
        public void bind(User bean, int position) {
            ViewGroup.LayoutParams params = switcher.getLayoutParams();
            String imgSize = "";
            if (all.indexOf(bean) == 0) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = secondWidth;
                imgSize = StaticFactory._600x600;
            } else if (all.indexOf(bean) <= 2) {
                params.width = secondWidth;
                params.height = secondWidth;
                imgSize = StaticFactory._320x320;
            } else {
                params.width = thirdWidth;
                params.height = thirdWidth;
                imgSize = StaticFactory._320x320;
            }
            switcher.setLayoutParams(params);
            List<String> logos = new ArrayList<>();
            logos.add(bean.getAvatar() + imgSize);
            if (bean.getImglist() != null) {
                for (ImageBean ib : bean.getImglist()) {
                    logos.add(ib.imgpath + imgSize);
                }
            }
            switcher.setUserlogo(logos);
            nickname.setText(all.indexOf(bean) + 1 + "." + bean.getNickname());
            dogfood.setText("消耗：" + bean.getSum());
            if (bean.getIsvip() > 0) {
                vip.setVisibility(View.VISIBLE);
            } else {
                vip.setVisibility(View.GONE);
            }
        }

    }
}

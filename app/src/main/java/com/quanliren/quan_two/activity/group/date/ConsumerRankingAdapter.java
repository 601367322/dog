package com.quanliren.quan_two.activity.group.date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/20.
 */
public class ConsumerRankingAdapter extends BaseAdapter<User> {

    public ConsumerRankingAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.consumer_ranking_list_item_first;
    }

    class ViewHolder extends BaseHolder {

        @Bind(R.id.gridview)
        GridView gridview;

        ConsumerRankingGridAdapter adapter;

        int secondWidth = 0;
        int thirdWidth = 0;

        public ViewHolder(View view) {
            super(view);
            adapter = new ConsumerRankingGridAdapter(context);
            gridview.setAdapter(adapter);

            secondWidth = (context.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(context, 4)) / 2;
            thirdWidth = (context.getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(context, 8)) / 3;
        }

        @Override
        public void bind(User bean, int position) {
            List<User> users = new ArrayList<>();

            ViewGroup.LayoutParams params = gridview.getLayoutParams();
            if (position == 0) {
                users.add(bean);
                gridview.setNumColumns(1);
                params.height = secondWidth;
            } else if (position == 1) {
                gridview.setNumColumns(2);
                params.height = secondWidth;
                users.add(getItem(position));
                if (list.size() > position + 1) {
                    users.add(getItem(position + 1));
                }
            } else if (position == 2) {
                gridview.setNumColumns(3);
                params.height = thirdWidth;
                users.add(getItem(position + 1));
                if (list.size() > position + 2) {
                    users.add(getItem(position + 2));
                }
                if (list.size() > position + 3) {
                    users.add(getItem(position + 3));
                }
            } else {
                gridview.setNumColumns(3);
                params.height = thirdWidth;
                users.add(getItem(position * 3 - 3));
                if (list.size() > position * 3 - 2) {
                    users.add(getItem(position * 3 - 2));
                }
                if (list.size() > position * 3 - 1) {
                    users.add(getItem(position * 3 - 1));
                }
            }
            gridview.setTag(users);
            gridview.setOnItemClickListener(logoClick);
            gridview.setLayoutParams(params);
            adapter.setAll(list);
            adapter.setList(users);
        }
    }

    AdapterView.OnItemClickListener logoClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            List<User> users = (List<User>) adapterView.getTag();
            User user = users.get(i);
            if (ac.getLoginUserId().equals(user.getId())) {
                UserInfoActivity_.intent(context).start();
            } else {
                UserOtherInfoActivity_.intent(context).userId(user.getId()).start();
            }
        }
    };

    @Override
    public int getCount() {
        if (super.getCount() > 0) {
            if (super.getCount() == 1) {
                return 1;
            } else if (super.getCount() > 1 && super.getCount() <= 3) {
                return 2;
            } else {
                int sum = (super.getCount() - 3) / 3;
                if ((super.getCount() - 3) % 3 > 0) {
                    sum++;
                }
                return 2 + sum;
            }
        }
        return super.getCount();
    }
}

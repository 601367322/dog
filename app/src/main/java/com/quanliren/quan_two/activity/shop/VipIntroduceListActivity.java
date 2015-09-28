package com.quanliren.quan_two.activity.shop;

import android.widget.ListView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/8/4.
 */
@EActivity(R.layout.vip_introduce_list)
public class VipIntroduceListActivity extends BaseActivity {

    @ViewById
    public ListView listview;

    VipIntroduceListAdapter adapter;

    @Override
    public void init() {
        super.init();
        List<VipIntroduceBean> list = new ArrayList<>();
        list.add(new VipIntroduceBean("会员专属精美标识！", R.drawable.vip_introduce_1));
        list.add(new VipIntroduceBean("呆萌表情，萌化对方的心，更多表情属于你！", R.drawable.vip_introduce_2));
        list.add(new VipIntroduceBean("漫游城市，足不出户，让我们去另一个地方看看那里的美女帅哥和约会！", R.drawable.vip_introduce_3));
        list.add(new VipIntroduceBean("访客信息，想知道谁偷偷看了你吗？那就快加入会员吧！", R.drawable.vip_introduce_4));
        list.add(new VipIntroduceBean("发出更多的爱情传递，快速脱离单身狗队伍！", R.drawable.vip_introduce_5));
        list.add(new VipIntroduceBean("更多的筛选条件，更便捷地找到你想要的约会信息！", R.drawable.vip_introduce_6));
        adapter = new VipIntroduceListAdapter(this);
        adapter.setList(list);
        listview.setAdapter(adapter);
    }
}

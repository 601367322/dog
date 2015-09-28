package com.quanliren.quan_two.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.adapter.BlackPeopleAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.Receiver;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Shen on 2015/7/9.
 */
@EFragment(R.layout.fragment_list)
public class BlackListFragment extends BaseListFragment<User> {

    public static final String CANCLEBLACKLIST = "com.quanliren.quan_two.activity.user.BlackListActivity.CANCLEBLACKLIST";
    public static final String ADDEBLACKLIST = "com.quanliren.quan_two.activity.user.BlackListActivity.ADDBLACKLIST";

    @Override
    public BaseAdapter<User> getAdapter() {
        return new BlackPeopleAdapter(getActivity());
    }

    @Override
    public BaseApi getApi() {
        return new BlackListApi(getActivity());
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        return super.getCacheKey() + ac.getLoginUserId();
    }

    @Override
    public int getEmptyView() {
        return R.layout.my_black_list_empty;
    }

    @Receiver(actions = {CANCLEBLACKLIST, ADDEBLACKLIST})
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(CANCLEBLACKLIST)) {
            String id = i.getExtras().getString("id");
            List<User> user = adapter.getList();
            User temp = null;
            for (User user2 : user) {
                if (user2.getId().equals(id)) {
                    temp = user2;
                }
            }
            if (temp != null) {
                adapter.remove(temp);
                adapter.notifyDataSetChanged();
            }
        } else if (action.equals(ADDEBLACKLIST)) {
            User user = (User) i.getExtras().getSerializable("bean");
            user.setCtime(Util.fmtDateTime.format(new Date()));
            adapter.add(0, user);
            adapter.notifyDataSetChanged();
        }
    }

    public void listview(int position) {
        if (position <= adapter.getCount()) {
            User user = adapter.getItem(position);
            Intent i = new Intent(getActivity(), user.getId().equals(
                    ac.getLoginUserId()) ? UserInfoActivity_.class
                    : UserOtherInfoActivity_.class);
            i.putExtra("userId", user.getId());
            startActivity(i);
        }
    }

    @ItemLongClick(R.id.listview)
    void listviewlong(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setItems(new String[]{"取消黑名单"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                menuClick(position);
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void menuClick(final int position) {
        RequestParams ap = getAjaxParams();
        ap.put("otherid", ((User) adapter.getItem(position)).getId());
        ac.finalHttp.post(URL.CANCLEBLACK, ap, new MyJsonHttpResponseHandler(getActivity(), getResources().getString(R.string.loading), null) {
            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                adapter.remove(position);
                adapter.notifyDataSetChanged();
                if (adapter.getCount() == 0) {
                    swipeRefresh();
                }
                showCustomToast("删除成功");
            }
        });
    }
}

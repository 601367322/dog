package com.quanliren.quan_two.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.adapter.NearPeopleAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemLongClick;
import org.json.JSONObject;

@EFragment(R.layout.fragment_list)
public class MyVisitFragment extends BaseListFragment<User> {

    @Override
    public BaseAdapter<User> getAdapter() {
        return new NearPeopleAdapter(getActivity());
    }

    @Override
    public BaseApi getApi() {
        return new MyVisitApi(getActivity());
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
        return R.layout.my_visit_list_empty;
    }

    @ItemLongClick(R.id.listview)
    void listviewlong(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setItems(new String[]{"删除这条记录"},
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

    public void menuClick(int position) {
        RequestParams ap = getAjaxParams();
        ap.put("uvid", ((User) adapter.getItem(position)).getUvid());
        ac.finalHttp.post(URL.DELETE_VISITLIST, ap, new setLogoCallBack(position));
    }

    class setLogoCallBack extends JsonHttpResponseHandler {

        int position;

        public setLogoCallBack(int position) {
            this.position = position;
        }

        public void onStart() {
            customShowDialog("正在发送请求");
        }

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                        if (adapter.getCount() == 0) {
                            swipeRefresh();
                        }
                        showCustomToast("删除成功");
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void listview(int position) {
        super.listview(position);
        User user = adapter.getItem(position);
        Intent i = new Intent(getActivity(), user.getId().equals(
                ac.getLoginUserId()) ? UserInfoActivity_.class
                : UserOtherInfoActivity_.class);
        i.putExtra("userId", user.getId());
        startActivity(i);
    }
}
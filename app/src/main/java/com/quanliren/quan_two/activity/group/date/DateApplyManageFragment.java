package com.quanliren.quan_two.activity.group.date;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.DateApplyManageAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.json.JSONObject;

/**
 * Created by Shen on 2015/7/9.
 */
@EFragment(R.layout.fragment_list)
public class DateApplyManageFragment extends BaseListFragment<User> implements DateApplyManageAdapter.AgreeOnClickListener{
    public static final String TAG = "com.quanliren.quan_two.activity.group.date.DateApplyManageFragment";
    @FragmentArg
    DateBean bean;
    DateApplyManageAdapter adapter;
    @Override
    public BaseAdapter<User> getAdapter() {
        adapter = new DateApplyManageAdapter(getActivity(),this);
        if(bean!=null){
            adapter.setDtid(bean.getDtid());
            adapter.setIsFinish(bean.getDtstate() == 1 ? true : false);
        }
        return adapter;
    }

    @Override
    public BaseApi getApi() {
        return new DateApplyManageApi(getActivity());
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public void initParams() {
        api.initParam(bean.getDtid());
    }

    public void listview(int position) {
        User user =  adapter.getItem(position);
        if (ac.getLoginUserId().equals(user.getId())) {
            UserInfoActivity_.intent(this).start();
        } else {
            UserOtherInfoActivity_.intent(this).userId(user.getId()).start();
        }
    }
    @Override
    public void agreeOnClick( final User user) {
        new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("您确定要和" + user.getNickname() + "约会吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                RequestParams rp = getAjaxParams();
                rp.put("dtid", bean.getDtid());
                rp.put("otherid", user.getId());
                rp.put("nickname", getHelper().getUserInfo().getNickname());
                ac.finalHttp.post(URL.DATE_CHOSE_SOMEONE, rp, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt(URL.STATUS);
                            switch (status) {
                                case 0:
//                                    ((ImageView)agree).setImageResource(R.drawable.date_ta);
                                    user.setApplystate(1);
                                    adapter.setIsFinish(true);
                                    adapter.notifyDataSetChanged();
                                    break;
                                default:
                                    showFailInfo(response);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {
                        showIntentErrorToast();
                    }
                });
            }
        }).create().show();
    }
}

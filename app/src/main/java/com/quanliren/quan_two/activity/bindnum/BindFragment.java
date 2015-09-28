package com.quanliren.quan_two.activity.bindnum;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.BindAdapter;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.ContactsC;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_list)
public class BindFragment extends BaseListFragment<ContactsC> implements BindAdapter.OnBtnClickListener{
    @Override
    public BaseAdapter<ContactsC> getAdapter() {
        return new BindAdapter(getActivity(),this);
    }

    @Override
    public BaseApi getApi() {
        return new BindApi(getActivity());
    }

    @Override
    public Class<?> getClazz() {
        return ContactsC.class;
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        return getClass().getName();
    }

    @Override
    public void setJsonData(JSONObject jo, boolean cache) {
        super.setJsonData(jo, cache);
        adapter.setList(initList(adapter.getList()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void listview(int position) {
        ContactsC cc =  adapter.getItem(position);
        if(cc.getId()!=null&&!"".equals(cc.getId())){
            if (ac.getLoginUserId().equals(cc.getId())) {
                UserInfoActivity_.intent(this).start();
            } else {
                UserOtherInfoActivity_.intent(this).userId(cc.getId()).start();
            }
        }

    }

    public static final String[] firstChar = { "关注","邀请","已关注" };
    public List<ContactsC> initList(List<ContactsC> olist) {
        List<ContactsC> list=null;
        if (list != null) {
            list.clear();
        } else {
            list = new ArrayList<ContactsC>();
        }
        int i = 0;
        for (String s : firstChar) {
            i = 0;
            for (ContactsC cc : olist) {
                if(cc.getRelevant_state()==0&&s.equals("关注")){
                    if (i == 0) {
                        cc.setTitle(s);
                    }else{
                        cc.setTitle(null);
                    }
                    cc.setHideTitle(s);
                    list.add(cc);
                    i++;
                }else if(cc.getRelevant_state()==2&&s.equals("邀请")){
                    if (i == 0) {
                        cc.setTitle(s);
                    }else{
                        cc.setTitle(null);
                    }
                    cc.setHideTitle(s);
                    list.add(cc);
                    i++;
                }else if(cc.getRelevant_state()==1&&s.equals("已关注")){
                    if (i == 0) {
                        cc.setTitle(s);
                    }else{
                        cc.setTitle(null);
                    }
                    cc.setHideTitle(s);
                    list.add(cc);
                    i++;
                }
            }
        }
        return list;
    }

    ContactsC ctc;
    @Override
    public void opClick(final ContactsC cc) {
        if (cc == null) {
            return;
        }
        ctc=cc;
        LoginUser my = getHelper().getUser();
        if (my == null) {
            startLogin();
            return;
        }
        if (my.getId().equals(cc.getId())) {
            showCustomToast("这是自己哟~");
            return;
        }
        if(cc.getId()==null||"".equals(cc.getId())){
            Uri smsToUri = Uri.parse("smsto://"+cc.getMobile());
            Intent mIntent = new Intent(Intent.ACTION_SENDTO,smsToUri);
            mIntent.putExtra("sms_body", Constants.shareContent);
            startActivity(mIntent);
        }else if(!cc.getId().equals(getHelper().getUser().getId())){
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("您确定要关注TA吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            RequestParams ap = getAjaxParams();
                            ap.put("otherid", cc.getId());
                            ac.finalHttp.post(URL.CONCERN, ap, concernCallBack);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).create().show();
        }
    }
    JsonHttpResponseHandler concernCallBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog(1);
        }
        ;
        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }
        ;
        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        for(ContactsC cc:(List<ContactsC>)adapter.getList()){
                            if(cc.getMobile()==ctc.getMobile()){
                                cc.setRelevant_state(1);
                            }
                        }
                        adapter.setList(initList(adapter.getList()));
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };
}
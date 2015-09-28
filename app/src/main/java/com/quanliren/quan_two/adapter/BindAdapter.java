package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.bindnum.BindFragment;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.ContactsC;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class BindAdapter extends BaseAdapter<ContactsC> {
    OnBtnClickListener listener;
    private List<String> charlist;

    public BindAdapter(Context context, OnBtnClickListener listener) {
        super(context);
        this.listener = listener;
        charlist = new ArrayList<String>();
        for (String s : BindFragment.firstChar) {
            charlist.add(String.valueOf(s));
        }
    }
//
//    @Override
//    public void setList(List<ContactsC> list) {
//        super.setList(list);
//        initList(list);
//        notifyDataSetChanged();
//    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.contact_item;
    }

    public class ViewHolder extends BaseHolder {
        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.nickname)
        TextView nickname;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.opera)
        Button opera;
        @Bind(R.id.opera_false)
        TextView opera_false;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(final ContactsC cc, int position) {
            ImageLoader.getInstance().displayImage(cc.getAvatar(), userlogo, ac.options_userlogo);
            if (cc.getRelevant_state() == 0) {
                opera.setVisibility(View.VISIBLE);
                opera_false.setVisibility(View.GONE);
                opera.setText("关注");
            } else if (cc.getRelevant_state() == 1) {
                opera.setVisibility(View.GONE);
                opera_false.setVisibility(View.VISIBLE);
                opera.setText("已关注");
            } else if (cc.getRelevant_state() == 2) {
                opera.setVisibility(View.VISIBLE);
                opera_false.setVisibility(View.GONE);
                opera.setText("邀请");
            }
            if (cc.getTitle() != null && !"".equals(cc.getTitle())) {
                if (title != null) {
                    title.setVisibility(View.VISIBLE);
                }
            } else {
                if (title != null) {
                    title.setVisibility(View.GONE);
                }
            }
            title.setText(cc.getTitle());
            nickname.setVisibility(View.GONE);
            name.setText(cc.getMilelistName());
            if (cc.getNickname() != null && !"".equals(cc.getNickname())) {
                nickname.setVisibility(View.VISIBLE);
                nickname.setText(cc.getNickname());
            }
            opera.setTag(cc);
            opera.setOnClickListener(opClick);

        }
    }

    //    public static final String[] firstChar = { "关注","邀请","已关注" };
//    public List<ContactsC> initList(List<ContactsC> olist) {
//        List<ContactsC> list=null;
//        if (list != null) {
//            list.clear();
//        } else {
//            list = new ArrayList<ContactsC>();
//        }
//        int i = 0;
//        for (String s : firstChar) {
//            i = 0;
//            for (ContactsC cc : olist) {
//                if(cc.getRelevant_state()==0&&s.equals("关注")){
//                    if (i == 0) {
//                        cc.setTitle(s);
//                    }else{
//                        cc.setTitle(null);
//                    }
//                    cc.setHideTitle(s);
//                    list.add(cc);
//                    i++;
//                }else if(cc.getRelevant_state()==2&&s.equals("邀请")){
//                    if (i == 0) {
//                        cc.setTitle(s);
//                    }else{
//                        cc.setTitle(null);
//                    }
//                    cc.setHideTitle(s);
//                    list.add(cc);
//                    i++;
//                }else if(cc.getRelevant_state()==1&&s.equals("已关注")){
//                    if (i == 0) {
//                        cc.setTitle(s);
//                    }else{
//                        cc.setTitle(null);
//                    }
//                    cc.setHideTitle(s);
//                    list.add(cc);
//                    i++;
//                }
//            }
//        }
//        return list;
//    }
    View.OnClickListener opClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            listener.opClick((ContactsC) v.getTag());
        }
    };

    public interface OnBtnClickListener {
        void opClick(ContactsC cc);
    }
}

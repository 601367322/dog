package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.ZhenXinHua;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;
import com.quanliren.quan_two.custom.emoji.EmoticonsTextView;
import com.quanliren.quan_two.db.ZhenXinHuaDao;
import com.quanliren.quan_two.util.Util;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageZhenXinHuaHolder extends MessageBaseHolder {

    @Bind(R.id.chat_context_tv)
    EmoticonsTextView content;
    @Bind(R.id.chose_ll)
    LinearLayout chose_ll;
    @Bind(R.id.z_listview)
    ListView z_listview;
    ZhenXinHuaDao zhenXinHuaDao;
    ZhenXinHuaAdapter adapter;
    Gson gson = new Gson();

    Type type = new TypeToken<ZhenXinHua>() {
    }.getType();

    public MessageZhenXinHuaHolder(View view) {
        super(view);
        adapter = new ZhenXinHuaAdapter(context);
        z_listview.setAdapter(adapter);
        zhenXinHuaDao = ZhenXinHuaDao.getInstance(context);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean, position);
        content.setVisibility(View.VISIBLE);
        ZhenXinHua zhenXinHua = gson.fromJson(bean.getContent(), type);
        content.setText(zhenXinHua.txt);
        content.setTag(bean);
        content.setOnLongClickListener(long_click);
        content.setOnClickListener(null);
        adapter.setList(zhenXinHua.answer);
        adapter.notifyDataSetChanged();

        setListViewHeightBasedOnChildren(z_listview);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    class ZhenXinHuaAdapter extends BaseAdapter<ZhenXinHua.Answer> {

        public ZhenXinHuaAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseHolder getHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public int getConvertView(int position) {
            return R.layout.chat_zhenxinhua_chose_btn;
        }

        class ViewHolder extends BaseHolder {

            @Bind(R.id.chose_btn)
            Button choseBtn;

            public ViewHolder(View view) {
                super(view);
            }

            @Override
            public void bind(ZhenXinHua.Answer bean, int position) {
                choseBtn.setText(bean.answer);
                choseBtn.setTag(bean);
            }

            @OnClick(R.id.chose_btn)
            public void chose(View view) {
                ZhenXinHua.Answer answer = (ZhenXinHua.Answer) view.getTag();
                DfMessage dfMessage = (DfMessage) content.getTag();
                ZhenXinHuaTable zt = zhenXinHuaDao.getZhenByMsgId(user.getId(), dfMessage.getMsgid());
                if (zt != null) {
                    String content = null;
                    if (zt.getSendUid().equals(user.getId())) {//判断如果已经选过就不能再选
                        content = zt.getSendContent();
                    } else {
                        content = zt.getReceiverContent();
                    }
                    if (!TextUtils.isEmpty(content)) {
                        Util.toast(context, "您已经选过了");
                        return;
                    } else {
                        zhenXinHuaDao.setContent(zt,user.getId(),answer.answer);
                    }
                }
                Message msg = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("answer", answer.answer);
                b.putString("msgid", dfMessage.getMsgid());
                msg.setData(b);
                msg.what = ChatActivity.HANDLER_ZHENXINHUA;
                msg.sendToTarget();
            }
        }

    }
}

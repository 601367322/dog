package com.quanliren.quan_two.adapter;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.bean.DaMaoXian;
import com.quanliren.quan_two.bean.DfMessage;
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
public class MessageDaMaoXianHolder extends MessageBaseHolder {

    @Bind(R.id.chat_context_tv)
    EmoticonsTextView content;
    @Bind(R.id.chose_ll)
    LinearLayout chose_ll;
    @Bind(R.id.start_btn)
    ImageView start_btn;
    ZhenXinHuaDao zhenXinHuaDao;
    Gson gson = new Gson();

    Type type = new TypeToken<DaMaoXian>() {
    }.getType();

    public MessageDaMaoXianHolder(View view) {
        super(view);
        zhenXinHuaDao = ZhenXinHuaDao.getInstance(context);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean, position);
        content.setVisibility(View.VISIBLE);
        DaMaoXian zhenXinHua = gson.fromJson(bean.getContent(), type);
        content.setText(zhenXinHua.txt);
        content.setTag(bean);
        content.setOnLongClickListener(long_click);
        content.setOnClickListener(null);
        start_btn.setTag(zhenXinHua);
    }

    @OnClick(R.id.start_btn)
    public void startPlay(View view) {
        DaMaoXian answer = (DaMaoXian) view.getTag();
        DfMessage dfMessage = (DfMessage) content.getTag();
        ZhenXinHuaTable zt = zhenXinHuaDao.getZhenByMsgId(user.getId(), dfMessage.getMsgid());
        String result = "";
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
                if (zt.getSendUid().equals(user.getId())) {
                    result = answer.userResult;
                } else {
                    result = answer.otherResult;
                }
                zhenXinHuaDao.setContent(zt, user.getId(), result);
            }
        }
        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putSerializable("answer", answer);
        b.putString("result", result);
        b.putString("msgid", dfMessage.getMsgid());
        msg.setData(b);
        msg.what = ChatActivity.HANDLER_DAMAOXIAN;
        msg.sendToTarget();
    }

}

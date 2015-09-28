package com.quanliren.quan_two.adapter;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.ChatActivity;
import com.quanliren.quan_two.bean.DaMaoXian;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;
import com.quanliren.quan_two.db.MessageDao;
import com.quanliren.quan_two.db.ZhenXinHuaDao;
import com.quanliren.quan_two.util.Util;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageDaMaoXianResultHolder extends MessageBaseHolder {

    @Bind(R.id.damaoxian_result_img)
    ImageView damaoxian_result_img;
    Handler myHandler = new Handler();
    MessageDao messageDao;
    ZhenXinHuaDao zhenXinHuaDao;

    public MessageDaMaoXianResultHolder(View view) {
        super(view);
        messageDao = MessageDao.getInstance(context);
        zhenXinHuaDao = ZhenXinHuaDao.getInstance(context);
    }

    @Override
    public void bind(final DfMessage bean, int position) {
        super.bind(bean, position);
        final int result = Integer.valueOf(bean.getContent());
        if (bean.getPlay_state() == 1) {
            damaoxian_result_img.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.damaoxian_result_anim));
            AnimationDrawable animationDrawable = (AnimationDrawable) damaoxian_result_img
                    .getDrawable();
            animationDrawable.start();

            if (!bean.isPlaying()) {//确保直走一次
                bean.setPlaying(true);
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //更新消息动画状态
                        bean.setPlay_state(0);
                        messageDao.update(bean);
                        setResource(result);

                        //添加惩罚内容
                        ZhenXinHuaTable zt = zhenXinHuaDao.getZhenByMsgId(user.getId(), bean.getZ_msgid(),0);
                        if (zt != null && !TextUtils.isEmpty(zt.getSendContent()) && !TextUtils.isEmpty(zt.getReceiverContent())) {
                            zt.setState(1);
                            zhenXinHuaDao.update(zt);
                            DfMessage p_message = messageDao.getMessageByMsgId(bean.getZ_msgid());
                            DaMaoXian zhenXinHua = new Gson().fromJson(p_message.getContent(), new TypeToken<DaMaoXian>() {
                            }.getType());
                            final DfMessage message = new DfMessage();
                            message.setUserid(user.getId());
                            message.setMsgtype(DfMessage.DAMAOXIAN_RESULT_);
                            message.setContent(zhenXinHua.adventure);
                            message.setSendUid(p_message.getSendUid().equals(user.getId()) ? p_message.getReceiverUid() : p_message.getSendUid());
                            message.setReceiverUid(p_message.getReceiverUid().equals(user.getId()) ? p_message.getSendUid() : p_message.getReceiverUid());
                            message.setCtime(Util.fmtDateTime.format(new Date()));
                            message.setMsgid(String.valueOf(new Date().getTime()));
                            message.setZ_msgid(bean.getZ_msgid());
                            messageDao.saveMessage(message);
                            myHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ChatActivity.ADDMSG);
                                    intent.putExtra("bean",message);
                                    context.sendBroadcast(intent);
                                }
                            },3000);
                        }
                    }
                }, 3000);
            }
        } else if (bean.getPlay_state() == 0) {
            setResource(result);
        }
    }

    public void setResource(int result) {
        if (result == 0) {
            damaoxian_result_img.setImageResource(R.drawable.chat_damaoxian_result_0);
        } else if (result == 1) {
            damaoxian_result_img.setImageResource(R.drawable.chat_damaoxian_result_1);
        } else {
            damaoxian_result_img.setImageResource(R.drawable.chat_damaoxian_result_2);
        }
    }

}

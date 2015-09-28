package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ParentsAdapter {

    public static final int LEFT_TEXT = 0, LEFT_VOICE = 1, LEFT_IMG = 2, LEFT_FACE = 3, LEFT_HELPER = 4, LEFT_ZHENXINHUA = 10, LEFT_DAMAOXIAN = 11, LEFT_DAMAOXIAN_RESULT = 13, LEFT_VIDEO = 16,LEFT_PIAOLIU = 18;
    public static final int RIGHT_TEXT = 5, RIGHT_VOICE = 6, RIGHT_IMG = 7, RIGHT_FACE = 8, RIGHT_ZHENXINHUA = 9, RIGHT_DAMAOXIAN = 12, RIGHT_DAMAOXIAN_RESULT = 14, DAMAOXIAN_RESULT_ = 15, RIGHT_VIDEO = 17;

    private User friend;

    private Context context;

    private Handler handler;

    private User user;

    public MessageAdapter(Context c, List list) {
        super(c, list);
        this.context = c;

        user = OpenHelperManager.getHelper(context, DBHelper.class).getUserInfo();
    }

    @Override
    public int getViewTypeCount() {
        return 19;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        MessageBaseHolder holder = null;
        if (convertView == null) {
            holder = onCreateViewHolder(arg2, getItemViewType(position));
            convertView = holder.getView();
            convertView.setTag(holder);
        } else {
            holder = (MessageBaseHolder) convertView.getTag();
        }
        holder.bind(getItem(position), position);
        return holder.getView();
    }

    public MessageBaseHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        MessageBaseHolder holder = null;
        switch (type) {
            case LEFT_TEXT:
                holder = new MessageTextHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_text, viewGroup, false));
                break;
            case LEFT_VOICE:
                holder = new MessageVoiceHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_voice, viewGroup, false));
                break;
            case LEFT_IMG:
                holder = new MessageImgHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_img, viewGroup, false));
                break;
            case LEFT_FACE:
                holder = new MessageGifHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_gif, viewGroup, false));
                break;
            case LEFT_HELPER:
                holder = new MessageHelperHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_helper, viewGroup, false));
                break;
            case RIGHT_TEXT:
                holder = new MessageTextHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_text, viewGroup, false));
                break;
            case RIGHT_VOICE:
                holder = new MessageVoiceHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_voice, viewGroup, false));
                break;
            case RIGHT_IMG:
                holder = new MessageImgHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_img, viewGroup, false));
                break;
            case RIGHT_FACE:
                holder = new MessageGifHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_gif, viewGroup, false));
                break;
            case RIGHT_ZHENXINHUA:
                holder = new MessageZhenXinHuaHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_zhenxinhua, viewGroup, false));
                break;
            case LEFT_ZHENXINHUA:
                holder = new MessageZhenXinHuaHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_zhenxinhua, viewGroup, false));
                break;
            case LEFT_DAMAOXIAN:
                holder = new MessageDaMaoXianHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_damaoxian, viewGroup, false));
                break;
            case RIGHT_DAMAOXIAN:
                holder = new MessageDaMaoXianHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_damaoxian, viewGroup, false));
                break;
            case LEFT_DAMAOXIAN_RESULT:
                holder = new MessageDaMaoXianResultHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_damaoxian_result, viewGroup, false));
                break;
            case RIGHT_DAMAOXIAN_RESULT:
                holder = new MessageDaMaoXianResultHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_damaoxian_result, viewGroup, false));
                break;
            case DAMAOXIAN_RESULT_:
                holder = new MessageDaMaoXianResult_Holder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_damaoxian_result, viewGroup, false));
                break;
            case LEFT_VIDEO:
                holder = new MessageVideoHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_video, viewGroup, false));
                break;
            case RIGHT_VIDEO:
                holder = new MessageVideoHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_right_video, viewGroup, false));
                break;
            case LEFT_PIAOLIU:
                holder = new MessagePiaoLiuHolder(LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_left_text, viewGroup, false));
                break;
        }
        holder.setMsgType(type);
        holder.setList(list);
        holder.setHandler(handler);
        holder.setUser(user);
        holder.setFriend(friend);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        DfMessage entity = (DfMessage) getList().get(position);
        if (entity.getSendUid().equals(friend.getId())) {
            switch (entity.getMsgtype()) {
                case DfMessage.TEXT:
                    if ("99".equals(entity.getSendUid())) {
                        return LEFT_HELPER;
                    } else {
                        return LEFT_TEXT;
                    }
                case DfMessage.IMAGE:
                    return LEFT_IMG;
                case DfMessage.VOICE:
                    return LEFT_VOICE;
                case DfMessage.FACE:
                    return LEFT_FACE;
                case DfMessage.ZHENXINHUA:
                    return LEFT_ZHENXINHUA;
                case DfMessage.DAMAOXIAN:
                    return LEFT_DAMAOXIAN;
                case DfMessage.DAMAOXIAN_RESULT:
                    return LEFT_DAMAOXIAN_RESULT;
                case DfMessage.DAMAOXIAN_RESULT_:
                    return DAMAOXIAN_RESULT_;
                case DfMessage.VIDEO:
                    return LEFT_VIDEO;
                case DfMessage.PIAOLIUPING:
                    return LEFT_PIAOLIU;
            }
        } else {
            switch (entity.getMsgtype()) {
                case DfMessage.TEXT:
                    return RIGHT_TEXT;
                case DfMessage.IMAGE:
                    return RIGHT_IMG;
                case DfMessage.VOICE:
                    return RIGHT_VOICE;
                case DfMessage.FACE:
                    return RIGHT_FACE;
                case DfMessage.ZHENXINHUA:
                    return RIGHT_ZHENXINHUA;
                case DfMessage.DAMAOXIAN:
                    return RIGHT_DAMAOXIAN;
                case DfMessage.DAMAOXIAN_RESULT:
                    return RIGHT_DAMAOXIAN_RESULT;
                case DfMessage.DAMAOXIAN_RESULT_:
                    return DAMAOXIAN_RESULT_;
                case DfMessage.VIDEO:
                    return RIGHT_VIDEO;
            }
        }
        return super.getItemViewType(position);
    }

    public void remove(DfMessage msg) {
        getList().remove(msg);
        List<DfMessage> tempList = new ArrayList<>();
        for (int i = 0; i < getList().size(); i++) {
            DfMessage temp = (DfMessage) getList().get(i);
            if (!TextUtils.isEmpty(temp.getZ_msgid()) && temp.getZ_msgid().equals(msg.getMsgid())) {
                tempList.add(temp);
            }
        }
        for (int i = 0; i < tempList.size(); i++) {
            getList().remove(tempList.get(i));
        }
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public DfMessage getItem(int position) {
        return (DfMessage) list.get(position);
    }

    public void add(int position, DfMessage bean) {
        list.add(position, bean);
    }

    public void add(DfMessage bean) {
        list.add(bean);
    }

}

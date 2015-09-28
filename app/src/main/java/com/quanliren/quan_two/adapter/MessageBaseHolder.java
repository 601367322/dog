package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.service.SocketManage;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shen on 2015/7/22.
 */
public abstract class MessageBaseHolder extends RecyclerView.ViewHolder {
    @Nullable
    @Bind(R.id.chat_user_logo)
    ImageView user_logo;
    @Nullable
    @Bind(R.id.time)
    TextView time;

    @Nullable
    @Bind(R.id.progress)
    View progress;
    @Nullable
    @Bind(R.id.error_btn)
    View error_btn;
    @Nullable
    @Bind(R.id.error_msg)
    View error_msg;

    protected Context context;

    protected AppClass ac;

    protected int msgType;

    private List<DfMessage> list;

    public Handler handler;

    private User friend;

    public User user;

    public View view;

    public MessageBaseHolder(View view) {
        super(view);
        this.view = view;
        context = view.getContext();
        ac = (AppClass) context.getApplicationContext();
        ButterKnife.bind(this, view);
    }

    public void bind(DfMessage bean, int position) {
        try {
            if (user_logo != null) {
                if (bean.getSendUid().equals(user.getId())) {
                    ImageLoader.getInstance().displayImage(
                            user.getAvatar() + StaticFactory._160x160,
                            user_logo, ac.options_userlogo);
                } else {
                    ImageLoader.getInstance().displayImage(
                            friend.getAvatar() + StaticFactory._160x160,
                            user_logo, ac.options_userlogo);
                    user_logo.setOnClickListener(logo_click);
                }
            }

            if (error_msg != null)
                error_msg.setVisibility(View.GONE);

            if (error_btn != null && progress != null) {
                error_btn.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                if (bean.getDownload() == SocketManage.D_downloading) {
                    progress.setVisibility(View.VISIBLE);
                } else if (bean.getDownload() == SocketManage.D_destroy) {
                    error_btn.setVisibility(View.VISIBLE);
                    error_btn.setTag(bean);
                    error_btn.setOnClickListener(logo_click);
                } else if (bean.getDownload() == SocketManage.D_fail) {
                    error_btn.setVisibility(View.VISIBLE);
                    error_msg.setVisibility(View.VISIBLE);
                    error_btn.setTag(bean);
                    error_btn.setOnClickListener(logo_click);
                }
            }
            if (time != null) {
                if (bean.isShowTime()
                        || (position > 0 && Util.fmtDateTime.parse(bean.getCtime())
                        .getTime() - 60 * 1000 > Util.fmtDateTime.parse(
                        list.get(position - 1)
                                .getCtime()).getTime())) {
                    time.setVisibility(View.VISIBLE);
                    time.setText(Util.getChatTime(bean.getCtime()));
                } else {
                    time.setVisibility(View.GONE);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DfMessage msg = (DfMessage) v.getTag();
            Message ms = handler.obtainMessage();
            switch (msg.getMsgtype()) {
                case DfMessage.IMAGE:
                case DfMessage.VOICE:
                case DfMessage.VIDEO:
                    ms.what = msg.getMsgtype();
                    ms.obj = msg;
                    ms.sendToTarget();
                    break;
            }

        }
    };

    View.OnLongClickListener long_click = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            DfMessage msg = (DfMessage) v.getTag();
            Message ms = handler.obtainMessage();
            switch (v.getId()) {
                case R.id.chat_context_tv:
                    if (msg.getMsgtype() == DfMessage.TEXT || msg.getMsgtype() == DfMessage.PIAOLIUPING) {
                        ms.what = 3;
                    } else {
                        ms.what = 4;
                    }
                    ms.obj = msg;
                    ms.sendToTarget();
                    break;
                case R.id.helper:
                case R.id.img_ll:
                case R.id.voice_ll:
                case R.id.gif_ll:
                    ms.what = 4;
                    ms.obj = msg;
                    ms.sendToTarget();
                    break;
            }
            return true;
        }
    };

    View.OnClickListener logo_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chat_user_logo:
                    Intent i = new Intent(context, UserOtherInfoActivity_.class);
                    i.putExtra("userId", friend.getId());
                    context.startActivity(i);
                    break;
                case R.id.error_btn:
                    DfMessage msg = (DfMessage) v.getTag();
                    Message ms = handler.obtainMessage();
                    ms.what = 6;
                    ms.obj = msg;
                    ms.sendToTarget();
                    break;
                default:
                    break;
            }

        }
    };

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setList(List<DfMessage> list) {
        this.list = list;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}

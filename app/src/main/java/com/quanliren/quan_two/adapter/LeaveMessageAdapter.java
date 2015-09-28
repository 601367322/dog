package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.ChatListBean;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

public class LeaveMessageAdapter extends BaseAdapter<ChatListBean> {

    public LeaveMessageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.leave_message_item_normal;
    }

    class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.username)
        TextView username;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.signature)
        TextView signature;
        @Bind(R.id.messagecount)
        TextView messagecount;

        @Override
        public void bind(ChatListBean bean, int position) {
            ImageLoader.getInstance().displayImage(
                    bean.getUserlogo() + StaticFactory._160x160, userlogo, ac.options_userlogo);
            User user = new User();
            user.setId(bean.getFriendid());
            userlogo.setUser(user);

            time.setText(Util.getTimeDateStr(bean.getCtime()));
            if ("99".equals(bean.getFriendid())) {
                DfMessage.OtherHelperMessage msg = new Gson().fromJson(bean.getContent(), new TypeToken<DfMessage.OtherHelperMessage>() {
                }.getType());
                switch (msg.getInfoType()) {
                    case DfMessage.OtherHelperMessage.INFO_TYPE_COMMENT://评论留言
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_0));
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_APPLY://参与报名 (偷偷约)
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_1));
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_CHECKED://选定和她约(偷偷约)
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_2));
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_DATING_REPLY:
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_3));
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_QUIT_APPLY:
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_4));
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_PAST_DUE:
                        signature.setText(R.string.info_type_5);
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_THREAD_SUCCESS:
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_6));
                        break;
                    case DfMessage.OtherHelperMessage.INFO_TYPE_PAIRING_SUCCESS:
                        signature.setText(msg.getNickname() + context.getString(R.string.info_type_7));
                        break;

                }
            } else {
                signature.setText(bean.getContent());
            }
            username.setText(bean.getNickname());
            if (bean.getMsgCount() != 0) {
                messagecount.setVisibility(View.VISIBLE);
                if (bean.getMsgCount() > 99) {
                    messagecount.setText("99+");
                } else {
                    messagecount.setText(bean.getMsgCount() + "");
                }
            } else {
                messagecount.setVisibility(View.GONE);
            }
        }

        public ViewHolder(View view) {
            super(view);
        }
    }

}

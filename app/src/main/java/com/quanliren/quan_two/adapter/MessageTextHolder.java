package com.quanliren.quan_two.adapter;

import android.view.View;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.custom.emoji.EmoticonsTextView;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageTextHolder extends MessageBaseHolder {

    @Bind(R.id.chat_context_tv)
    EmoticonsTextView content;

    public MessageTextHolder(View view) {
        super(view);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean, position);
        if (bean.getMsgtype() == DfMessage.TEXT) {
            content.setVisibility(View.VISIBLE);
            content.setText(bean.getContent());
            content.setTag(bean);
            content.setOnLongClickListener(long_click);
            content.setOnClickListener(null);
        }else{
            content.setVisibility(View.GONE);
        }
    }
}

package com.quanliren.quan_two.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.util.ImageUtil;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageVoiceHolder extends MessageBaseHolder {

    @Bind(R.id.voice)
    ImageView voice;
    @Bind(R.id.timel)
    TextView timel;
    @Bind(R.id.voice_ll)
    View voice_ll;

    public MessageVoiceHolder(View view) {
        super(view);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean,position);
        voice_ll.setVisibility(View.VISIBLE);
        voice_ll.setTag(bean);
        voice_ll.setOnLongClickListener(long_click);
        voice_ll.setOnClickListener(click);
        timel.setText(bean.getTimel() + "''");
        int cha = bean.getTimel() - 5;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) voice_ll.getLayoutParams();
        if (cha > 0) {
            int sum = 60 + (cha * 3);
            if (sum > 150) {
                sum = 150;
            }
            lp.width = ImageUtil.dip2px(context, sum);
        } else {
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        voice_ll.setLayoutParams(lp);

        if (bean.isPlaying()) {
            if (msgType == MessageAdapter.LEFT_VOICE) {
                voice.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.chat_left_animation));
            } else {
                voice.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.chat_right_animation));
            }
            AnimationDrawable animationDrawable = (AnimationDrawable) voice
                    .getDrawable();
            animationDrawable.start();
        } else {
            if (msgType == MessageAdapter.LEFT_VOICE) {
                voice.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.chat_left_voice3));
            } else {
                voice.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.chat_right_voice3));
            }
        }
    }
}

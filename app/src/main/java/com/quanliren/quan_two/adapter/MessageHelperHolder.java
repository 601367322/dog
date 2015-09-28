package com.quanliren.quan_two.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageHelperHolder extends MessageBaseHolder {

    @Bind(R.id.helper)
    View helper;
    @Bind(R.id.send_nicker)
    TextView send_nicker;
    @Bind(R.id.date_ll)
    View date_ll;
    @Bind(R.id.send_type)
    TextView send_type;
    @Bind(R.id.title_type)
    TextView title_type;
    @Bind(R.id.send_comment)
    TextView send_comment;
    @Bind(R.id.image_type)
    ImageView image_type;
    @Bind(R.id.see_detail)
    TextView see_detail;


    public MessageHelperHolder(View view) {
        super(view);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean, position);
        helper.setTag(bean);
        helper.setOnLongClickListener(long_click);
        helper.setOnClickListener(null);
        final DfMessage.OtherHelperMessage msg = bean.getOtherHelperContent();
        send_nicker.setVisibility(View.VISIBLE);
        switch (msg.getInfoType()) {
            case 0:
                date_ll.setVisibility(View.GONE);
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_0));
                send_comment.setVisibility(View.VISIBLE);
                send_comment.setText(msg.getText());
                break;
            case 1:
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_1));
                        setDateUI(msg);
                break;
            case 2:
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_2));
                setDateUI(msg);
                break;
            case 3:
                date_ll.setVisibility(View.GONE);
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_3));
                send_comment.setVisibility(View.VISIBLE);
                send_comment.setText(msg.getText());
                break;
            case 4:
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_4));
                setDateUI(msg);
                break;
            case 5:
                send_nicker.setVisibility(View.GONE);
                send_type.setText(context.getString(R.string.info_type_5));
                setDateUI(msg);
                break;
            case 6:
                date_ll.setVisibility(View.GONE);
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_6));
                send_comment.setVisibility(View.VISIBLE);
                send_comment.setText(msg.getText());
                break;
            case 7:
                date_ll.setVisibility(View.GONE);
                send_nicker.setText(msg.getNickname());
                send_type.setText(context.getString(R.string.info_type_7));
                send_comment.setVisibility(View.GONE);
                break;
        }

        see_detail.setTag(bean);
        see_detail.setOnClickListener(detailClick);
    }

    public void setDateUI(DfMessage.OtherHelperMessage msg){
        date_ll.setVisibility(View.VISIBLE);
        send_comment.setVisibility(View.GONE);
        if (msg.getDtype() == 1) {
            image_type.setImageResource(R.drawable.ic_state_dinner_largest);
            title_type.setText("约人吃饭");
        } else if (msg.getDtype() == 2) {
            image_type.setImageResource(R.drawable.ic_state_movie_largest);
            title_type.setText("约看电影");
        } else if (msg.getDtype() == 5) {
            image_type.setImageResource(R.drawable.ic_state_girl_largest);
            title_type.setText("临时情侣");
        }
    }

    View.OnClickListener detailClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getHandler().obtainMessage(0, v.getTag()).sendToTarget();
        }
    };
}

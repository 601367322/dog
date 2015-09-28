package com.quanliren.quan_two.adapter;

import android.view.View;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageDaMaoXianResult_Holder extends MessageBaseHolder {

    @Bind(R.id.result)
    TextView result;


    public MessageDaMaoXianResult_Holder(View view) {
        super(view);
    }

    @Override
    public void bind(final DfMessage bean, int position) {
        result.setText(bean.getContent());
    }

}

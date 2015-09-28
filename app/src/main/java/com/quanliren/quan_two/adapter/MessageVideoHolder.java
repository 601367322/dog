package com.quanliren.quan_two.adapter;

import android.view.View;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageVideoHolder extends MessageBaseHolder {

    @Bind(R.id.img)
    PorterShapeImageView img;
    @Bind(R.id.img_ll)
    View img_ll;

    public MessageVideoHolder(View view) {
        super(view);
    }

    @Override
    public void bind(final DfMessage bean, int position) {
        super.bind(bean, position);
        img_ll.setTag(bean);
        img_ll.setOnLongClickListener(long_click);
        img_ll.setOnClickListener(click);

        String[] strs = bean.getContent().split(",");
        switch (msgType) {
            case MessageAdapter.RIGHT_VIDEO:
                ImageLoader.getInstance().displayImage(Util.FILE + strs[1], img, ac.options_defalut);//本地视频
                break;
            case MessageAdapter.LEFT_VIDEO:
                ImageLoader.getInstance().displayImage(strs[1], img, ac.options_defalut);//外部视频
                break;
        }

    }

}

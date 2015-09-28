package com.quanliren.quan_two.adapter;

import android.view.View;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/22.
 */
public class MessageImgHolder extends MessageBaseHolder {

    @Bind(R.id.img)
    PorterShapeImageView img;
    @Bind(R.id.img_ll)
    View img_ll;

    public MessageImgHolder(View view) {
        super(view);
    }

    @Override
    public void bind(DfMessage bean, int position) {
        super.bind(bean,position);
        img_ll.setTag(bean);
        img_ll.setOnLongClickListener(long_click);
        img_ll.setOnClickListener(click);
        if (bean.getContent().startsWith("http://")) {
            ImageLoader.getInstance().displayImage(
                    bean.getContent() + StaticFactory._160x160,
                    img, ac.options_chat);
        } else {
            ImageLoader.getInstance().displayImage(
                    Util.FILE + bean.getContent(), img,
                    ac.options_chat);
        }
    }
}

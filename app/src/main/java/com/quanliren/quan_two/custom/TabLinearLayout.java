package com.quanliren.quan_two.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class TabLinearLayout extends LinearLayout {

    OnTabClickListener listener;
    public List<LinearLayout> tabs = new ArrayList<>();
    public TabLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabLinearLayout(Context context) {
        super(context);
        init();
    }

    void init() {
//        setBackgroundResource(R.color.white);
        setBackgroundResource(R.color.white);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setDate(List<TabBean> list) {
        tabs.clear();
        removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final LinearLayout ll = createLinearLayout();
            ll.addView(createImageView(list.get(i).getImg()));
            ll.addView(createTextView(list.get(i).getText()));
            if (i == 0) {
                ll.setSelected(true);
            }
            final int j = i;
            ll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ll.setSelected(true);
                    if (listener != null) {
                        listener.onTabClick(j);
                    }
                }
            });
            tabs.add(ll);
            addView(ll);
        }
    }

    public void setCurrentIndex(int position) {
        for (LinearLayout lll : tabs) {
            lll.setSelected(false);
        }
        tabs.get(position).setSelected(true);
    }

    public LinearLayout createLinearLayout() {

        LinearLayout ll = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundResource(R.drawable.button_bg_selector_material);
        ll.setLayoutParams(lp);
        ll.setClickable(true);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        return ll;
    }

    public ImageView createImageView(int img) {
        ImageView iv = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(ImageUtil.dip2px(getContext(), 20), ImageUtil.dip2px(getContext(), 20));
        iv.setLayoutParams(lp);
        iv.setScaleType(ScaleType.CENTER_CROP);
        iv.setDuplicateParentStateEnabled(true);
        iv.setImageResource(img);
        return iv;
    }

    public TextView createTextView(String text) {
        TextView iv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ImageUtil.dip2px(getContext(), 25));
        lp.leftMargin = ImageUtil.dip2px(getContext(), 4);
        iv.setGravity(Gravity.CENTER_VERTICAL);
        iv.setLayoutParams(lp);
        iv.setText(text);
        iv.setDuplicateParentStateEnabled(true);
        iv.setTextColor(getResources().getColorStateList(R.color.nav_bar_text));
        iv.setTextSize(16);
        return iv;
    }

    public static class TabBean {
        int img;
        String text;

        public int getImg() {
            return img;
        }

        public void setImg(int img) {
            this.img = img;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public TabBean() {
            super();
            // TODO Auto-generated constructor stub
        }

        public TabBean(int img, String text) {
            super();
            this.img = img;
            this.text = text;
        }
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }

    public void setListener(OnTabClickListener listener) {
        this.listener = listener;
    }
}

package com.quanliren.quan_two.fragment.custom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.ImageUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.page_tab_goods)
public class PageTabGoods extends Fragment {

    ArrayList<String> titles = new ArrayList<String>();
    @ViewById
    LinearLayout tabs_ll;
    @ViewById
    View tab_icon;
    int tabWidth = 0;
    int width = 0;
    OnPageTitleClickListener listener;


    public void setListener(OnPageTitleClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        titles = getArguments().getStringArrayList("titles");
    }

    @AfterViews
    void initView() {
        for (int i = 0; i < titles.size(); i++) {
            TextView tv = null;
            tabs_ll.addView(tv = createTextView(titles.get(i)));
            final int num = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
//					listener=(OnPageTitleClickListener) getParentFragment();
                    listener.click(num);
                }
            });
        }

        int screenW = getResources().getDisplayMetrics().widthPixels;
        tabWidth = (int) ((float) screenW / titles.size());

        width = ImageUtil.dip2px(getActivity(), 110);
        tab_icon.setTranslationX( now = ((tabWidth - width) / 2));
    }

    public TextView createTextView(String str) {
        TextView tv = new TextView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
        lp.weight = 1;
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(lp);
        tv.setTextSize(18);
        tv.setTextColor(getResources().getColor(R.color.nav_bar_text));
        tv.setText(str);
        return tv;
    }

    float now = 0;

    public void setCurrendIndex(final int i) {
        if (getActivity() != null && tabs_ll != null) {
            for (int j = 0; j < titles.size(); j++) {
                if (i != j) {
                    ((TextView) (tabs_ll.getChildAt(j))).setTextColor(getResources().getColor(R.color.username_normal));
                } else {
                    ((TextView) (tabs_ll.getChildAt(j))).setTextColor(Color.BLACK);
                }
            }
        }
    }

    public interface OnPageTitleClickListener {
        void click(int index);
    }

    public void onPageScroll(int i, float bai, int x) {
        if (tab_icon != null)
            tab_icon.setTranslationX( tabWidth * i + now + tabWidth * bai);
    }

}

package com.quanliren.quan_two.fragment.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.a.me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import com.quanliren.quan_two.fragment.impl.LoaderImpl;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/6/30.
 */
@EFragment
public abstract class ActionBarTabFragment extends MenuFragmentBase implements ViewPager.OnPageChangeListener {

    @ViewById
    public ImageButton left_btn;
    @ViewById
    public Button right_btn;
    @ViewById
    public Button left_tab;
    @ViewById
    public Button right_tab;
    @ViewById
    public ViewPager viewpager;

    public List<Fragment> views = new ArrayList<Fragment>();

    public abstract String getLeftTabText();

    public abstract String getRightTabText();

    public void init() {
        left_tab.setText(getLeftTabText());
        right_tab.setText(getRightTabText());

        if (showLeftBtn()) {
            left_btn.setVisibility(View.VISIBLE);
        }
        if (showRightBtn()) {
            right_btn.setVisibility(View.VISIBLE);
            right_btn.setText(getRightBtnStr());
        }
        left_tab.setSelected(true);

        if (viewpager != null) {
            initFragments();
            viewpager.setAdapter(new mPagerAdapter(getChildFragmentManager()));
            viewpager.setOnPageChangeListener(this);
            viewpager.post(new Runnable() {
                @Override
                public void run() {
                    onPageSelected(0);
                }
            });
        }
    }

    public abstract void initFragments();


    List<Integer> imgs = new ArrayList<>();

    class mPagerAdapter extends FragmentPagerAdapter {

        public mPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return views.get(arg0);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            ((LoaderImpl) views.get(position)).refresh();
            super.setPrimaryItem(container, position, object);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                left_tab.setSelected(true);
                right_tab.setSelected(false);
                break;
            case 1:
                left_tab.setSelected(false);
                right_tab.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Click
    public void left_tab() {
        if (viewpager != null) {
            viewpager.setCurrentItem(0);
        }
    }

    @Click
    public void right_tab() {
        if (viewpager != null) {
            viewpager.setCurrentItem(1);
        }
    }

    @Click
    public void left_btn() {
        ((SwipeBackActivity) getActivity()).scrollToFinishActivity();
    }

    public boolean showLeftBtn() {
        return false;
    }

    public boolean showRightBtn() {
        return false;
    }

    public String getRightBtnStr() {
        return "";
    }
}

package com.quanliren.quan_two.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanliren.quan_two.fragment.impl.LoaderImpl;

/**
 * Created by Shen on 2015/7/6.
 */
public abstract class BaseViewPagerListFragment<T> extends BaseListFragment<T> implements
        LoaderImpl {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(getConvertViewRes(), null);
        }
        return mView;
    }

    public abstract int getConvertViewRes();

    @Override
    public void refresh() {
        if (getActivity() != null && init.compareAndSet(false, true)) {
            super.init();
        }
    }

    @Override
    public void init() {
    }
}

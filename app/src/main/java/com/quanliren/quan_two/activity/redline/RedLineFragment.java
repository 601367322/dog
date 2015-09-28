package com.quanliren.quan_two.activity.redline;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.fragment.base.BaseViewPagerListFragment;
import com.quanliren.quan_two.util.ImageUtil;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Shen on 2015/6/30.
 */
@EFragment
public class RedLineFragment extends BaseViewPagerListFragment<RedLineBean> {

    public enum RedLineMode {
        HOT, NEW, MY, JOIN, FAVORITE
    }

    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getConvertViewRes() {
        return R.layout.fragment_list;
    }

    @FragmentArg
    public RedLineMode mode;

    @Override
    public BaseAdapter getAdapter() {
        return new RedLineListAdapter(getActivity());
    }

    @Override
    public BaseApi getApi() {
        return new RedLineListApi(getActivity(), mode);
    }

    @Override
    public Class<?> getClazz() {
        return RedLineBean.class;
    }

    @Override
    public void initParams() {
        api.initParam();
    }

    @Override
    public void listview(int position) {
        RedLineDetailActivity_.intent(this).bean(adapter.getItem(position)).start();
    }

    @Override
    public void initListView() {
        super.initListView();
        listview.setHeaderDividersEnabled(true);
        listview.setFooterDividersEnabled(true);
        listview.setDividerHeight(ImageUtil.dip2px(getActivity(), 8));
    }

    @Override
    public void setJsonData(JSONObject jo, boolean cache) {
        super.setJsonData(jo, cache);
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        switch (mode) {
            case HOT:
                return super.getCacheKey() + "hot";
            case NEW:
                return super.getCacheKey() + "new";
            case JOIN:
                return super.getCacheKey() + ac.getLoginUserId() + "join";
            case MY:
                return super.getCacheKey() + ac.getLoginUserId();
            case FAVORITE:
                return super.getCacheKey() + ac.getLoginUserId() + "favorite";
        }
        return super.getCacheKey();
    }

    public void onEvent(RedLineBean redLineBean) {
        if (adapter != null) {
            List<RedLineBean> list = adapter.getList();
            for (RedLineBean bean : list) {
                if (bean.id == redLineBean.id) {
                    bean.zambiastate = redLineBean.zambiastate;
                    bean.zambia = redLineBean.zambia;
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

}

package com.quanliren.quan_two.fragment.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.location.GDLocation;
import com.quanliren.quan_two.activity.location.ILocationImpl;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.pull.XListView;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.ACache;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sbb on 2015/5/14.
 */
@EFragment(R.layout.fragment_list)
public abstract class BaseListFragment<T> extends MenuFragmentBase implements SwipeRefreshLayout.OnRefreshListener, XListView.IXListViewListener, ILocationImpl {

    @ViewById
    public XListView listview;
    @ViewById
    public SwipeRefreshLayout swipe;
    @ViewById
    public RelativeLayout parent;

    public BaseAdapter<T> adapter;

    public View emptyView;

    public int currentPage = 0;

    public GDLocation location;

    public BaseApi api;

    @Override
    public void init() {

        location = new GDLocation(getActivity(), this, false);

        if (getEmptyView() != -1) {
            emptyView = LayoutInflater.from(getActivity()).inflate(getEmptyView(), (ViewGroup) listview.getParent(), false);
            ((ViewGroup) listview.getParent().getParent()).addView(emptyView);
            emptyView.setVisibility(View.GONE);
        }

        listview.setPage(-1);

        listview.setXListViewListener(this);

        initListView();

        listview.setAdapter(adapter = getAdapter());

        swipe.setOnRefreshListener(this);

        if (needCache()) {
            ACache cache = ACache.get(getActivity());
            JSONObject jo = cache.getAsJSONObject(getCacheKey());
            if (jo != null) {
                setJsonData(jo, true);
            }
        }

        if (autoRefresh()) {
            swipeRefresh();
        }
    }

    public boolean autoRefresh() {
        return true;
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        listview.setSelection(0);
        onPullToRefresh();
    }

    @Override
    public void onLoadMore() {
        onPullToRefresh();
    }

    private void onPullToRefresh() {
        if (needLocation() && currentPage == 0) {
            location.startLocation();
        } else {
            httpPost();
        }
    }

    public void httpPost() {
        api = getApi();
        api.setPage(currentPage);
        initParams();
        ac.finalHttp.post(api.getUrl(), api.getParams(), new MyJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {

                if (needCache() && currentPage == 0) {
                    ACache.get(getActivity()).put(getCacheKey(), jo);
                }

                setJsonData(jo, false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onFinshRefreshUI();
            }

            @Override
            public void onFailRetCode(JSONObject jo) {
                super.onFailRetCode(jo);
                onFailCallBack();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                onFailCallBack();
            }
        });
    }

    public void setJsonData(JSONObject jo, boolean cache) {
        onSuccessCallBack(jo);

        List list = Util.jsonToList(jo.optJSONObject(URL.RESPONSE).optString(URL.LIST), getClazz());
        onSuccessRefreshUI(jo,list, cache);
    }

    @UiThread(delay = 200)
    public void swipeRefresh() {
        swipe.setRefreshing(true);
    }

    public int getEmptyView() {
        return R.layout.default_empty_view;
    }

    public abstract BaseAdapter<T> getAdapter();

    /**
     * 刷新list
     *
     * @param list
     * @param cache 如果是缓存数据，不改变页数
     */
    private void onSuccessRefreshUI(JSONObject jo,List list, boolean cache) {
        if (list != null) {
            if (currentPage == 0) {
                adapter.setList(list);
            } else {
                adapter.add(list);
            }

            if (!cache) {
                currentPage = Util.getPage(jo);
            }
        }
        if (!cache) {
            listview.setPage(currentPage);
            if(emptyView!=null) {
                if (adapter.getCount() <= 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void onFinshRefreshUI() {
        swipe.setRefreshing(false);
        listview.stop();
    }

    @ItemClick
    public void listview(int position) {

    }

    public abstract BaseApi getApi();

    /**
     * 请求成功后
     *
     * @param jo
     */
    public void onSuccessCallBack(JSONObject jo) {

    }

    /**
     * 请求失败后
     */
    public void onFailCallBack() {
    }

    /**
     * 在listview setAdapter前调用
     */
    public void initListView() {
    }

    public abstract Class<?> getClazz();

    /**
     * 是否有分页
     *
     * @return
     */
    public boolean pageEnable() {
        return true;
    }

    /**
     * 是否需要定位
     *
     * @return
     */
    public boolean needLocation() {
        return false;
    }

    @Override
    public void onLocationSuccess() {
        httpPost();
    }

    @Override
    public void onLocationFail() {
        Util.toast(getActivity(), getString(R.string.location_fail));
        onFinshRefreshUI();
    }

    /**
     * 是否需要缓存
     *
     * @return
     */
    public boolean needCache() {
        return false;
    }

    public String getCacheKey() {
        return getClass().getName();
    }

    public void initParams() {
    }
}

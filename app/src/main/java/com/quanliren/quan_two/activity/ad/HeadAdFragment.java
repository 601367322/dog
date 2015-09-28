package com.quanliren.quan_two.activity.ad;

import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.seting.HtmlActivity_;
import com.quanliren.quan_two.activity.user.DownDetailActivity_;
import com.quanliren.quan_two.adapter.PagerImageAdapter;
import com.quanliren.quan_two.bean.ADBannerBean;
import com.quanliren.quan_two.custom.ScrollViewPager;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.util.ACache;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/7/3.
 */
@EFragment(R.layout.fragment_head_ad)
public class HeadAdFragment extends MenuFragmentBase {

    @ViewById(R.id.viewpager)
    ScrollViewPager viewPager;

    PagerImageAdapter badapter;

    @FragmentArg
    public String ADCacheKey;

    @Override
    public void init() {
        viewPager.setOnPageChangeListener(null);
        viewPager.setAdapter(badapter = new PagerImageAdapter(new ArrayList<ADBannerBean>(), new IProductGridListener() {
            @Override
            public void imgClick(View view) {
                ADBannerBean banner = (ADBannerBean) view.getTag();
                LogUtil.d("============pnum", banner.getPnum());
                if ("0011".equals(banner.getPnum())) {
                    Intent help = new Intent(getActivity(), HtmlActivity_.class);
                    help.putExtra("title", "用户帮助");
                    help.putExtra("url", "file:///android_asset/function.html");
                    startActivity(help);
                }
                if ("1".equals(banner.getBtype())) {
                    DownDetailActivity_.intent(getActivity()).extra("business_url", banner.getUrl()).start();
                }
            }
        }));
        JSONObject json = ACache.get(getActivity()).getAsJSONObject(ADCacheKey);
        if (json != null) {
            setAdData(json);
        }
    }

    public void getADBanner() {
        ac.finalHttp.post(URL.ADBANNER_LIST, getAjaxParams(), new MyJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                ACache.get(getActivity()).put(ADCacheKey, jo);
                setAdData(jo);
            }
        });
    }

    public void setAdData(JSONObject jo) {
        try {
            String ll = jo.getJSONObject(URL.RESPONSE).getString(URL.LIST);
            List<ADBannerBean> listad = new Gson().fromJson(
                    ll,
                    new TypeToken<ArrayList<ADBannerBean>>() {
                    }.getType());
            initView(listad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initView(List<ADBannerBean> ads) {
        badapter.setList(ads);
        badapter.notifyDataSetChanged();
        viewPager.startAutoSlide();
    }

    public interface IProductGridListener {
        void imgClick(View view);
    }
}

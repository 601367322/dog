package com.quanliren.quan_two.activity.seting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.ad.HeadAdFragment;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.seting.emoticon.EmoticonDetailActivity_;
import com.quanliren.quan_two.activity.seting.emoticon.EmoticonHistoryListActivity_;
import com.quanliren.quan_two.adapter.EmoticonDownloadListAdapter;
import com.quanliren.quan_two.adapter.ShopAdapter.IBuyListener;
import com.quanliren.quan_two.bean.CacheBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EBanner;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip;
import com.quanliren.quan_two.custom.RoundProgressBar;
import com.quanliren.quan_two.custom.ScrollViewPager;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.service.DownLoadEmoticonService_;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.emoticonlist)
@OptionsMenu(R.menu.emoticon_menu)
public class EmoticonListActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, IBuyListener {

    public static final String TAG = "EmoticonListActivity";
    public static final String EMOTICONDOWNLOAD_PROGRESS = "com.quanliren.quan_two.activity.seting.EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS";
    public static final String DELETE_EMOTICONDOWNLOAD = "com.quanliren.quan_two.activity.seting.EmoticonListActivity.DELETE_EMOTICONDOWNLOAD";

    @ViewById
    SwipeRefreshLayout layout;
    @ViewById
    ListView listview;

    ScrollViewPager viewpager;

    EmoticonPagerImageAdapter badapter;

    EmoticonDownloadListAdapter adapter;

    @OrmLiteDao(helper = DBHelper.class, model = EmoticonZip.class)
    Dao<EmoticonZip, Integer> emoticonDao;

    @Receiver(actions = {DELETE_EMOTICONDOWNLOAD, EMOTICONDOWNLOAD_PROGRESS}, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    public void receiver(Intent i) {
        String action = i.getAction();
        LogUtil.d(action);
        if (action.equals(EMOTICONDOWNLOAD_PROGRESS)) {
            int state = i.getExtras().getInt("state");
            EmoticonZip bean = (EmoticonZip) i.getSerializableExtra("bean");

            List<EmoticonZip> list = adapter.getList();
            int position = -1;
            for (EmoticonZip emoticonZip : list) {
                if (emoticonZip.getId() == bean.getId()) {
                    position = list.indexOf(emoticonZip);
                }
            }
            if (position < 0) {
                return;
            }
            Button progress = (Button) listview
                    .getChildAt(position + 1).findViewById(R.id.buy);
            switch (state) {
                case 0:
                    showCustomToast("正在下载");
                    progress.setEnabled(false);
                    break;
                case 1:
                    progress.setEnabled(false);
                    break;
                case 2:
                    progress.setEnabled(true);
                    doSuccess(progress);
                    break;
                case -1:
                    progress.setEnabled(true);
                    showCustomToast("下载失败");
                    break;
            }

        } else if (action.equals(DELETE_EMOTICONDOWNLOAD)) {
            int id = i.getExtras().getInt("id");
            List<EmoticonZip> list = adapter.getList();
            for (EmoticonZip emoticonZip : list) {
                if (emoticonZip.getId() == id) {
                    emoticonZip.setHave(false);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @OptionsItem
    void manage() {
        EmoticonHistoryListActivity_.intent(this).start();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSupportActionBar().setTitle("表情下载");

        if(adapter!=null){
            checkHave(adapter.getList());
        }
    }

    @AfterViews
    void initView() {

        layout.setOnRefreshListener(this);

        if (Integer.valueOf(ac.cs.getPhizcnt()) > Integer.valueOf(ac.cs.getMyPhizcnt())) {
            ac.cs.setMyPhizcnt(ac.cs.getPhizcnt());
        }

        try {
            EmoticonActivityListBean list = null;
            CacheBean cb = cacheDao.queryForId(TAG);
            if (cb != null) {
                list = new Gson().fromJson(cb.getValue(),
                        new TypeToken<EmoticonActivityListBean>() {
                        }.getType());
            }
            initView(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        refresh();
    }

    @UiThread(delay = 200)
    public void refresh() {
        layout.setRefreshing(true);
    }

    @ItemClick
    void listview(int position) {
        if (position > 0)
            EmoticonDetailActivity_.intent(this)
                    .bean(((EmoticonZip) adapter.getItem(position - 1)))
                    .start();
    }

    @Override
    public void onRefresh() {
        ac.finalHttp.post(URL.EMOTICON_DOWNLOAD_LIST, getAjaxParams(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt(URL.STATUS);
                            switch (status) {
                                case 0:
                                    CacheBean cb = new CacheBean(TAG, response
                                            .getString(URL.RESPONSE), new Date()
                                            .getTime());
                                    cacheDao.delete(cb);
                                    cacheDao.create(cb);
                                    EmoticonActivityListBean list = new Gson().fromJson(
                                            response.getString(URL.RESPONSE),
                                            new TypeToken<EmoticonActivityListBean>() {
                                            }.getType());
                                    initView(list);
                                    break;
                                default:
                                    showFailInfo(response);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure() {
                        layout.setRefreshing(false);
                        showIntentErrorToast();
                    }
                });
    }

    void initView(EmoticonActivityListBean bean) {
        if (bean == null) {
            return;
        }
        if (viewpager == null) {
            View view = View.inflate(this, R.layout.emoticonlist_head, null);
            viewpager = (ScrollViewPager) view.findViewById(R.id.viewpager);
            listview.addHeaderView(view);
            viewpager.setOnPageChangeListener(null);
            viewpager.setAdapter(badapter = new EmoticonPagerImageAdapter(bean
                    .getBlist(), new HeadAdFragment.IProductGridListener() {

                @Override
                public void imgClick(View view) {
                    EBanner bean = (EBanner) view.getTag();
                    EmoticonZip zip = new EmoticonZip();
                    zip.setId(bean.getId());
                    EmoticonDetailActivity_.intent(EmoticonListActivity.this)
                            .bean(zip)
                            .start();
                }
            }));
            viewpager.startAutoSlide();
        } else {
            badapter.setList(bean.getBlist());
            badapter.notifyDataSetChanged();
        }

        if (bean.getPlist() != null) {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", ac.getLoginUserId());
                List<EmoticonZip> list = emoticonDao.queryForFieldValues(map);

                for (EmoticonZip ez : bean.getPlist()) {
                    for (EmoticonZip emoticonZip : list) {
                        if (ez.getId() == emoticonZip.getId()) {
                            ez.setHave(true);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (adapter == null) {
                adapter = new EmoticonDownloadListAdapter(this,
                        bean.getPlist(), this);
                OnScrollListener listener = new PauseOnScrollListener(
                        ImageLoader.getInstance(), false, true);
                listview.setOnScrollListener(listener);
                listview.setAdapter(adapter);
            } else {
                adapter.setList(bean.getPlist());
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void checkHave(List<EmoticonZip> plist) {
        if (plist != null) {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", ac.getLoginUserId());
                List<EmoticonZip> list = emoticonDao.queryForFieldValues(map);

                for (EmoticonZip ez : plist) {
                    ez.setHave(false);
                }

                for (EmoticonZip ez : plist) {
                    for (EmoticonZip emoticonZip : list) {
                        if (ez.getId() == emoticonZip.getId()) {
                            ez.setHave(true);
                        }
                    }
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class EmoticonPagerImageAdapter extends PagerAdapter {

        List<EBanner> urllist = new ArrayList<EBanner>();

        HeadAdFragment.IProductGridListener listener = null;

        public EmoticonPagerImageAdapter(List<EBanner> list,
                                         HeadAdFragment.IProductGridListener listener) {
            this.urllist = list;
            this.listener = listener;
        }

        public void setList(List<EBanner> list) {
            this.urllist = list;
        }

        @Override
        public int getCount() {
            if (urllist.size() > 1) {
                return Integer.MAX_VALUE;
            }
            return urllist.size();
        }

        int height = 200;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private final DisplayImageOptions options_no_default = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            View view = View.inflate(container.getContext(),
                    R.layout.emoticon_pager_image_item, null);
            final ImageView photoView = (ImageView) view.findViewById(R.id.img);
            final RoundProgressBar rp = (RoundProgressBar) view
                    .findViewById(R.id.progressBar);
            ImageLoader.getInstance().displayImage(
                    urllist.get(position % urllist.size()).getBannerUrl(),
                    photoView, options_no_default,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            float scale = (float) container.getContext()
                                    .getResources().getDisplayMetrics().widthPixels
                                    / (float) loadedImage.getWidth();
                            height = (int) (loadedImage.getHeight() * scale);
                            ((ImageView) view)
                                    .setLayoutParams(new RelativeLayout.LayoutParams(
                                            RelativeLayout.LayoutParams.MATCH_PARENT,
                                            height));
                            viewpager
                                    .setLayoutParams(new RelativeLayout.LayoutParams(
                                            RelativeLayout.LayoutParams.MATCH_PARENT,
                                            height));
                        }
                    }, new ImageLoadingProgressListener() {

                        @Override
                        public void onProgressUpdate(String imageUri,
                                                     View view, int current, int total) {
                            if (current == total) {
                                rp.setVisibility(View.GONE);
                            } else {
                                rp.setVisibility(View.VISIBLE);
                                rp.setMax(total);
                                rp.setProgress(current);
                            }
                        }
                    });
            photoView.setTag(urllist.get(position % urllist.size()));
            photoView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.imgClick(photoView);
                }
            });
            container.addView(view, LayoutParams.MATCH_PARENT, height);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void buyClick(final Button progress) {
        if (Util.isFastDoubleClick()) {
            return;
        }
        final EmoticonZip ez = (EmoticonZip) progress.getTag();
        User user = getHelper().getUserInfo();
        if (user == null) {
            return;
        }
        if (ez.getType() == 1 && user.getIsvip() == 0) {// 会员
            Util.goVip(this);
            return;
        }
        if (ez.getType() == 2 && ez.getIsBuy() == 0) {// 付费

            return;
        }
        boolean isExists = false;
        try {
            EmoticonZip ezb = emoticonDao.queryForId(ez.getId());
            if (ezb != null && ezb.getUserId().equals(ac.getLoginUserId())) {
                isExists = true;
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            if ((ez.getType() == 0 || ez.getIsBuy() == 1 || (ez.getType() == 1 && user
                    .getIsvip() > 0)) && !isExists) {
                Intent i = new Intent(this, DownLoadEmoticonService_.class);
                i.setAction(BroadcastUtil.DOWNLOADEMOTICON);
                i.putExtra("bean", ez);
                startService(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void doSuccess(Button mProgress) {
        EmoticonZip ez = (EmoticonZip) mProgress.getTag();
        ez.setHave(true);
        adapter.notifyDataSetChanged();
    }
}

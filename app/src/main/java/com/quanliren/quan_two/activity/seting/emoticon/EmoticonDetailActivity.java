package com.quanliren.quan_two.activity.seting.emoticon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.seting.EmoticonListActivity;
import com.quanliren.quan_two.adapter.EmoteLargeAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonImageBean;
import com.quanliren.quan_two.custom.CustomScrollView;
import com.quanliren.quan_two.custom.NumberProgressBar;
import com.quanliren.quan_two.custom.emoji.EmoteGridView;
import com.quanliren.quan_two.custom.emoji.EmoteGridView.EmoticonListener;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.pull.smoothprogressbar.SmoothProgressBar;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.service.DownLoadEmoticonService_;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.DrawableCache;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;

@EActivity(R.layout.emoticon_detail)
public class EmoticonDetailActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, EmoticonListener {

    @Extra
    EmoticonZip bean;
    @ViewById
    SwipeRefreshLayout layout;
    @ViewById
    EmoteGridView gridview;
    @ViewById
    ImageView banner;
    @ViewById
    TextView name;
    @ViewById
    TextView size;
    @ViewById
    TextView price;
    @ViewById
    Button buyBtn;
    @ViewById
    View child;
    @ViewById
    TextView remark;
    @ViewById
    CustomScrollView scrollview;
    @ViewById
    NumberProgressBar number_progress;
    @ViewById
    SmoothProgressBar smooth_progress;
    EmoteLargeAdapter adapter;

    @OrmLiteDao(helper = DBHelper.class, model = EmoticonZip.class)
    Dao<EmoticonZip, Integer> emoticonDao;

    @Receiver(actions = EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS,registerAt = Receiver.RegisterAt.OnResumeOnPause)
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS)) {
            int state = i.getExtras().getInt("state");
            EmoticonZip temp = (EmoticonZip) i.getSerializableExtra("bean");
            if (bean.getId() == temp.getId()) {
                switch (state) {
                    case 0:
                        smooth_progress.setVisibility(View.GONE);
                        number_progress.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        smooth_progress.setVisibility(View.GONE);
                        number_progress.setVisibility(View.VISIBLE);
                        int gress = i.getExtras().getInt("progress");
                        int total = i.getExtras().getInt("total");
                        int percent = (int) (((float) gress / (float) total) * 100);
                        if (percent > 0 && percent < 100) {
                            number_progress.setMax(total);
                            number_progress.setProgress(gress);
                        }
                        break;
                    case 2:
                        smooth_progress.setVisibility(View.GONE);
                        number_progress.setVisibility(View.GONE);
                        number_progress.setProgress(0);
                        buyBtn.setVisibility(View.VISIBLE);
                        buyBtn.setEnabled(false);
                        buyBtn.setText("已下载");
                        break;
                    case -1:
                        smooth_progress.setVisibility(View.GONE);
                        number_progress.setVisibility(View.GONE);
                        number_progress.setProgress(0);
                        buyBtn.setVisibility(View.VISIBLE);
                        buyBtn.setEnabled(true);
                        buyBtn.setText("下载");
                        break;
                }
            }
        }
    }

    @UiThread(delay = 200)
    public void refresh() {
        layout.setRefreshing(true);
    }

    @Click
    void buyBtn() {
        User user = getHelper().getUserInfo();
        if (user == null) {
            return;
        }
        if (bean.getType() == 1 && user.getIsvip() == 0) {// 会员
            Util.goVip(this);
            return;
        }
        if (bean.getType() == 2 && bean.getIsBuy() == 0) {// 付费

            return;
        }

        boolean isExists = false;
        try {
            EmoticonZip ezb = emoticonDao.queryForId(bean.getId());
            if (ezb != null && ezb.getUserId().equals(ac.getLoginUserId())) {
                isExists = true;
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            Intent i = new Intent(this, DownLoadEmoticonService_.class);
            i.setAction(BroadcastUtil.DOWNLOADEMOTICON);
            i.putExtra("bean", bean);
            startService(i);
            if ((bean.getType() == 0 || bean.getIsBuy() == 1 || (bean.getType() == 1 && user
                    .getIsvip() > 0)) && !isExists) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        smooth_progress.setInterpolator(new AccelerateInterpolator());
        smooth_progress.setIndeterminate(true);
        smooth_progress.setVisibility(View.VISIBLE);
        buyBtn.setVisibility(View.GONE);
    }

    @AfterViews
    void initView() {
        child.setVisibility(View.GONE);
        layout.setOnRefreshListener(this);
        try {
            EmoticonZip be = emoticonDao.queryForId(bean.getId());
            if (be != null&& be.getUserId().equals(ac.getLoginUserId())) {
                buyBtn.setEnabled(false);
                buyBtn.setText("已下载");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        refresh();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(bean.getName()!=null&&!"".equals(bean.getName())){
            getSupportActionBar().setTitle(bean.getName());
        } else {
            getSupportActionBar().setTitle("表情");
        }
    }

    @Override
    public void onRefresh() {
        RequestParams ap = getAjaxParams();
        ap.put("id", bean.getId());
        ac.finalHttp.post(URL.EMOTICON_DETAIL, ap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt(URL.STATUS);
                            switch (status) {
                                case 0:
                                    bean=new Gson().fromJson(
                                            response.getString(URL.RESPONSE),
                                            new TypeToken<EmoticonZip>() {
                                            }.getType());
                                    init(bean);
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

    public void init(EmoticonZip bean) {
        if (bean == null) {
            return;
        }
        child.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(bean.getName());
        ImageLoader.getInstance().displayImage(bean.getBannerUrl(), banner,
                ac.options_no_default, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        float scale = (float) getApplicationContext()
                                .getResources().getDisplayMetrics().widthPixels
                                / (float) loadedImage.getWidth();
                        int height = (int) (loadedImage.getHeight() * scale);
                        ((ImageView) view)
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        height));
                    }
                });

        name.setText(bean.getName());
        remark.setText(bean.getRemark());
        size.setText("大小：" + bean.getSize());
        switch (bean.getType()) {
            case 0:
                price.setText("免费");
                break;
            case 1:
                price.setText("会员专属");
                break;
            case 2:
                price.setText("¥" + bean.getPrice());
                break;
        }

        List<EmoticonImageBean> imgs = bean.getImglist();

        if (adapter != null) {
            adapter.setList(imgs);
            adapter.notifyDataSetChanged();
        } else {
            gridview.setListener(this);
            gridview.setAdapter(adapter = new EmoteLargeAdapter(this, imgs));
        }

    }

    @Override
    public void onEmoticonClick(EmoticonImageBean bean) {
        // TODO Auto-generated method stub

    }

    class EmoticonPreview {

        @Bind(R.id.gif)
        ImageView gif;
        @Bind(R.id.progress)
        View progress;

        View view;

        public EmoticonPreview() {
            ButterKnife.bind(this, view = View.inflate(EmoticonDetailActivity.this, R.layout.emoticon_preview, null));
        }
    }

    EmoticonImageBean loadedBean = null;
    PopupWindow pop;
    EmoticonPreview ep;

    @Override
    public void onEmoticonLongPress(EmoticonImageBean bean, int[] xy, int[] wh) {
        // TODO Auto-generated method stub
        scrollview.setEnableTouchScroll(false);
        setSwipeBackEnable(false);
        layout.setEnabled(false);
        if (ep == null) {
            ep = new EmoticonPreview();
        }
        if ((loadedBean == null || (bean != null && !loadedBean.equals(bean)))
                && bean != null) {
            if (loadedBean != null) {
                GifDrawable gd = (GifDrawable) ep.gif.getDrawable();
                if (gd != null)
                    gd.stop();
                ep.gif.setImageDrawable(null);
            }

            loadedBean = bean;

            ImageLoader.getInstance().loadImage(bean.getGifUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    ep.progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    ep.progress.setVisibility(View.GONE);
                    DrawableCache.getInstance().displayDrawable(ep.gif, ImageLoader.getInstance().getDiskCache().get(imageUri).getPath());
                }
            });

            if (pop == null) {
                pop = new PopupWindow(ep.view, ImageUtil.dip2px(this, 120),
                        ImageUtil.dip2px(this, 120));
                pop.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.emoticon_popup_bg));
            } else {
                pop.dismiss();
            }
            int x = (int) ((float) (pop.getWidth() - wh[0]) / 2);
            x = xy[0] - x;
            if (x < 0) {
                x = 0;
            } else if (x + pop.getWidth() > getResources().getDisplayMetrics().widthPixels) {
                x = getResources().getDisplayMetrics().widthPixels
                        - pop.getWidth();
            }

            int y = xy[1] - pop.getHeight()
                    - ImageUtil.dip2px(this, 20);

            if (y < 0) {
                y = xy[1] + wh[1]
                        + ImageUtil.dip2px(this, 20);
            }

            pop.showAtLocation(
                    scrollview,
                    Gravity.NO_GRAVITY,
                    x,
                    y);
        }
    }

    @Override
    public void onEmoticonLongPressCancle() {
        // TODO Auto-generated method stub
        scrollview.setEnableTouchScroll(true);
        setSwipeBackEnable(true);
        layout.setEnabled(true);
        if (loadedBean != null) {
            GifDrawable gd = (GifDrawable) ep.gif.getDrawable();
            if (gd != null)
                gd.stop();
            ep.gif.setImageDrawable(null);
        }
        loadedBean = null;
        if (pop != null) {
            pop.dismiss();
        }
    }
}

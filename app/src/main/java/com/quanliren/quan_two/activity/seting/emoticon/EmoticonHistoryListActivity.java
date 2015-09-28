package com.quanliren.quan_two.activity.seting.emoticon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.seting.EmoticonListActivity;
import com.quanliren.quan_two.adapter.EmoticonDownloadHistoryListAdapter;
import com.quanliren.quan_two.adapter.ShopAdapter.IBuyListener;
import com.quanliren.quan_two.bean.CacheBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip;
import com.quanliren.quan_two.custom.ScrollViewPager;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.service.DownLoadEmoticonService_;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.emoticonlist)
public class EmoticonHistoryListActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, IBuyListener {

    public static final String TAG = "EmoticonHistoryListActivity";
    public String CACHEKEY = TAG;

    @ViewById
    TextView empty;
    @ViewById
    SwipeRefreshLayout layout;
    @ViewById
    ListView listview;

    ScrollViewPager viewpager;

    EmoticonDownloadHistoryListAdapter adapter;

    @OrmLiteDao(helper = DBHelper.class, model = EmoticonZip.class)
    Dao<EmoticonZip, Integer> emoticonDao;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = getHelper().getUserInfo();
        CACHEKEY += user.getId();

    }

    @Receiver(actions = EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    public void receiver(Intent i) {
        String action = i.getAction();
        if (action.equals(EmoticonListActivity.EMOTICONDOWNLOAD_PROGRESS)) {
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
                    .getChildAt(position).findViewById(R.id.buy);
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
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSupportActionBar().setTitle("表情管理");
    }

    @UiThread(delay = 200)
    public void refresh(){
        layout.setRefreshing(true);
    }
    @AfterViews
    void initView() {
        layout.setOnRefreshListener(this);
        try {
            EmoticonActivityListBean list = null;
            CacheBean cb = cacheDao.queryForId(CACHEKEY);
            if (cb != null) {
                list = new Gson().fromJson(cb.getValue(),
                        new TypeToken<EmoticonActivityListBean>() {
                        }.getType());
            }
            initView(list);
        } catch (Exception e) {
            e.printStackTrace();
        }refresh();
    }

    @ItemClick
    void listview(int position) {
        EmoticonDetailActivity_.intent(this)
                .bean(((EmoticonZip) adapter.getItem(position))).start();
    }

    @Override
    public void onRefresh() {
        ac.finalHttp.post(URL.EMOCTION_MANAGE, getAjaxParams(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt(URL.STATUS);
                            switch (status) {
                                case 0:
                                    CacheBean cb = new CacheBean(CACHEKEY, response
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

    public void buyClick(final Button progress) {
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

        if (ez.isHave()) {
            try {
                emoticonDao.delete(ez);
                ez.setHave(false);
                Intent i = new Intent(
                        EmoticonListActivity.DELETE_EMOTICONDOWNLOAD);
                i.putExtra("id", ez.getId());
                sendBroadcast(i);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            boolean isExists = false;
            try {
                EmoticonZip ezb = emoticonDao.queryForId(ez.getId());
                if (ezb != null && ezb.getUserId().equals(ac.getLoginUserId())) {
                    isExists = true;
                }
            } catch (Exception e1) {
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
    }

    void doSuccess(Button mProgress) {
        EmoticonZip ez = (EmoticonZip) mProgress.getTag();
        ez.setHave(true);
        adapter.notifyDataSetChanged();
    }

    void initView(EmoticonActivityListBean bean) {
        if (bean == null) {
            empty.setVisibility(View.VISIBLE);
            return;
        }else{
            empty.setVisibility(View.GONE);
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
                adapter = new EmoticonDownloadHistoryListAdapter(this,
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

}

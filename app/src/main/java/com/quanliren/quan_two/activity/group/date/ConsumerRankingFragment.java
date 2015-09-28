package com.quanliren.quan_two.activity.group.date;

import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.AutoScrollImageSwitcher;
import com.quanliren.quan_two.fragment.base.BaseViewPagerListFragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by Shen on 2015/7/20.
 */
@EFragment
public class ConsumerRankingFragment extends BaseViewPagerListFragment<User> {

    @FragmentArg
    public ConsumerRankingType rankingType;

    public enum ConsumerRankingType {
        MONTH, ALL
    }

    @Override
    public BaseAdapter getAdapter() {
        return new ConsumerRankingAdapter(getActivity());
    }

    @Override
    public BaseApi getApi() {
        return new ConsumerRankingApi(getActivity(), rankingType);
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public int getConvertViewRes() {
        return R.layout.fragment_list;
    }

    @Override
    public void initParams() {
        super.initParams();
        api.initParam();
    }

    @Override
    public void setJsonData(JSONObject jo, boolean cache) {
        super.setJsonData(jo, cache);
        if (!handler.hasMessages(MSG_UPDATE_IMAGE)) {
            handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
        }
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public String getCacheKey() {
        switch (rankingType) {
            case MONTH:
                return super.getCacheKey() + "month";
            case ALL:
                return super.getCacheKey() + "all";
        }
        return super.getCacheKey();
    }

    public void changeImg() {
        if (listview.getChildCount() > 0) {
            if (listview.getChildAt(0) != null && listview.getChildAt(0) instanceof RelativeLayout) {
                //开始切换图片
                int rowRandom = (int) (Math.random() * listview.getChildCount());
                if (listview.getChildAt(rowRandom) instanceof RelativeLayout) {
                    GridView gridView = (GridView) listview.getChildAt(rowRandom).findViewById(R.id.gridview);
                    if (gridView.getChildCount() > 0) {
                        int colRandow = (int) (Math.random() * gridView.getChildCount());
                        if (gridView.getChildAt(colRandow) instanceof RelativeLayout) {
                            AutoScrollImageSwitcher switcher = (AutoScrollImageSwitcher) gridView.getChildAt(colRandow).findViewById(R.id.imgSwitcher);
                            switcher.showNextImg();
                        }
                    }
                }
            }
        }
    }

    /**
     * 请求更新显示的View。
     */
    public static final int MSG_UPDATE_IMAGE = 1;
    /**
     * 请求暂停轮播。
     */
    public static final int MSG_KEEP_SILENT = 2;
    /**
     * 请求恢复轮播。
     */
    public static final int MSG_BREAK_SILENT = 3;

    //轮播间隔时间
    public static long MSG_DELAY = 1500;

    private ImageHandler handler = new ImageHandler(new WeakReference<>(this));

    private static class ImageHandler extends Handler {

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<ConsumerRankingFragment> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<ConsumerRankingFragment> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ConsumerRankingFragment context = weakReference.get();
            if (context == null) {
                //Activity已经回收，无需再处理UI了
                return;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (context.handler.hasMessages(MSG_UPDATE_IMAGE)) {
                context.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    context.changeImg();
                    //准备下次播放
                    context.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    context.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                default:
                    break;
            }
        }
    }

}

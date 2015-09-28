package com.quanliren.quan_two.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.application.AppClass;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/7/22.
 */
public class AutoScrollImageSwitcher extends ImageSwitcher implements ViewSwitcher.ViewFactory {

    private ImageHandler handler = new ImageHandler(new WeakReference<>(this));
    private List<String> userlogo = new ArrayList<>();

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
    public static long MSG_DELAY = 3000;

    private int currentItem = 0;

    public AutoScrollImageSwitcher(Context context) {
        super(context);
    }

    public AutoScrollImageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        MSG_DELAY = (long) (Math.random() * 10000) + 3000;
        setFactory(this);
    }

    public List<String> getUserlogo() {
        return userlogo;
    }

    public void setUserlogo(List<String> userlogo) {
        this.userlogo = userlogo;
        if (!handler.hasMessages(MSG_UPDATE_IMAGE)) {
            setInAnimation(null);
            setOutAnimation(null);
            showNext(0);
            setInAnimation(getContext(), R.anim.slide_left_in);
            setOutAnimation(getContext(), R.anim.slide_left_out);
//            if (userlogo.size() > 10) {
//                handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//            }
        }
    }


    private static class ImageHandler extends Handler {

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<AutoScrollImageSwitcher> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<AutoScrollImageSwitcher> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AutoScrollImageSwitcher context = weakReference.get();
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
                    currentItem++;
                    if (currentItem >= context.userlogo.size() - 1) {
                        currentItem = 0;
                    }
                    context.showNext(currentItem);
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


    public void showNext(int currentItem) {
        ImageLoader.getInstance().displayImage(userlogo.get(currentItem), (ImageView) getNextView(), ((AppClass) getContext().getApplicationContext()).options_userlogo);
        super.showNext();
    }

    public void showNextImg() {
        if (userlogo.size() > 1) {
            currentItem++;
            if (currentItem >= userlogo.size()) {
                currentItem = 0;
            }
            showNext(currentItem);
        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(getContext());
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
        return i;
    }
}

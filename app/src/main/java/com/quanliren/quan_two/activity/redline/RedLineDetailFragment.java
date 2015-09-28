package com.quanliren.quan_two.activity.redline;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.MyScrollView;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.custom.ZanLinearLayout;
import com.quanliren.quan_two.fragment.base.BaseListFragment;
import com.quanliren.quan_two.util.AnimUtil;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shen on 2015/7/1.
 */
@OptionsMenu(R.menu.redline_detail)
@EFragment(R.layout.red_line_detail_fragment_list)
public class RedLineDetailFragment extends BaseListFragment<User> {

    @FragmentArg
    public RedLineBean bean;
    @ViewById
    LinearLayout bottom_tab;
    @ViewById
    MyScrollView scrollview;
    @ViewById
    View body;
    @ViewById
    View dog_heart;
    @ViewById
    ImageView red_line;
    @OptionsMenuItem
    public MenuItem share;
    @OptionsMenuItem
    public MenuItem favorite;
    @ViewById
    View tab_close;
    @ViewById
    View tab_chuandi;
    @ViewById
    View tab_qianshou;
    @ViewById
    View tab_success;

    private Head head;

    class Head {

        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nickLl;
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.dog_food)
        TextView dogFood;
        @Bind(R.id.zan)
        TextView zan;
        @Bind(R.id.zan_ll)
        ZanLinearLayout zanLl;
        @Bind(R.id.other_userlogo)
        UserLogo other_userlogo;
        @Bind(R.id.userlogo_ll)
        LinearLayout userlogo_ll;
        @Bind(R.id.heart_img)
        ImageView heart_img;

        public Head(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(RedLineBean bean) {
            ImageLoader.getInstance().displayImage(bean.user.getAvatar() + StaticFactory._320x320, userlogo, ac.options_userlogo);
            userlogo.setUser(bean.user);
            nickLl.setUser(bean.user);
            if (TextUtils.isEmpty(bean.message)) {
                message.setText(R.string.lazy);
            } else {
                message.setText(bean.message);
            }
            time.setText(Util.getTimeDateStr(bean.createtime));
            zanLl.setBean(bean);
            if (bean.zambiastate.equals("1")) {
                zanLl.setSelected(true);
            } else {
                zanLl.setSelected(false);
            }
            dogFood.setText(Util.getDogFood(bean.user.getCoin()));
            if (bean.pairingstate == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    userlogo_ll.setBackground(null);
                } else {
                    userlogo_ll.setBackgroundDrawable(null);
                }
                userlogo.setBorderColor(getResources().getColor(R.color.nav_press_txt));
                userlogo.setBorderWidth(ImageUtil.dip2px(getActivity(), 2));
                ImageLoader.getInstance().displayImage(bean.lastUser.getAvatar() + StaticFactory._160x160, other_userlogo, ac.options_userlogo);
                other_userlogo.setUser(bean.lastUser);
                LogUtil.d(userlogo_ll.getLeft() + "");
                ValueAnimator animator = ObjectAnimator.ofFloat(userlogo_ll, AnimUtil.TRANSLATIONX, 0, -ImageUtil.dip2px(getActivity(), 55));
                ValueAnimator animator1 = ObjectAnimator.ofFloat(other_userlogo, AnimUtil.TRANSLATIONX, 0, ImageUtil.dip2px(getActivity(), 50));
                ValueAnimator animator2 = ObjectAnimator.ofFloat(heart_img, AnimUtil.ALPHA, 0f, 1f);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(animator, animator1, animator2);
                set.setDuration(1000);
                set.start();

                ValueAnimator animator3 = new ObjectAnimator().ofFloat(heart_img, AnimUtil.SCALEX, 1f, 1.3f);
                animator3.setRepeatCount(Animation.INFINITE);
                animator3.setRepeatMode(Animation.REVERSE);
                ValueAnimator animator4 = new ObjectAnimator().ofFloat(heart_img, AnimUtil.SCALEY, 1f, 1.3f);
                animator4.setRepeatCount(Animation.INFINITE);
                animator4.setRepeatMode(Animation.REVERSE);
                animatorSet1 = new AnimatorSet();
                animatorSet1.playTogether(animator3, animator4);
                animatorSet1.setDuration(200);
                animatorSet1.start();
            }
        }

    }

    private SparseIntArray mChildrenHeights;
    private int mPrevFirstVisiblePosition;
    private int mPrevFirstVisibleChildHeight = -1;
    private int mPrevScrolledChildrenHeight;
    private int mScrollY;
    private boolean mFirstScroll;
    private boolean mDragging;
    private int mPrevScrollY;
    private Drawable mActionBarBackgroundDrawable;
    AtomicBoolean initDogHeart = new AtomicBoolean(false);
    AnimatorSet animatorSet, animatorSet1;
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constants.DESCRIPTOR);

    @Override
    public void init() {
        super.init();
        mChildrenHeights = new SparseIntArray();

        mActionBarBackgroundDrawable = getResources().getDrawable(
                R.drawable.actionbar_drawable);
        mActionBarBackgroundDrawable.setAlpha(0);

        getSupportActionBar().setBackgroundDrawable(
                mActionBarBackgroundDrawable);

        scrollview.setOnScrollChangedListener(mOnScrollChangedListener);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }

        listview.setAlpha(0f);

        // 配置需要分享的相关平台,并设置分享内容
        ((BaseActivity) getActivity()).configPlatforms(mController);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        share.setVisible(false);
        favorite.setVisible(false);
    }

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getSupportActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    private MyScrollView.OnScrollChangedListener mOnScrollChangedListener = new MyScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl,
                                    int oldt) {
            final int headerHeight = getSupportActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight)
                    / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };

    @Override
    public BaseAdapter getAdapter() {
        RedLineDetailAdapter adapter = new RedLineDetailAdapter(getActivity());
        adapter.setRedLineBean(bean);
        return adapter;
    }

    @Override
    public BaseApi getApi() {
        return new RedLineDetailApi(getActivity());
    }

    @Override
    public Class<?> getClazz() {
        return User.class;
    }

    @Override
    public void onSuccessCallBack(JSONObject jo) {
        if (jo.has(URL.RESPONSE)) {
            if (jo.optJSONObject(URL.RESPONSE).has(URL.RESULTMAP)) {
                RedLineBean bean = new Gson().fromJson(jo.optJSONObject(URL.RESPONSE).optString(URL.RESULTMAP), new TypeToken<RedLineBean>() {
                }.getType());
                this.bean = bean;
                head.bind(bean);

                getSupportActionBar().setTitle(getString(R.string.love_send) + "(" + bean.transcount + "人)");

                share.setVisible(true);
                favorite.setVisible(true);

                if (bean.collectionstate == 0) {
                    favorite.setIcon(R.drawable.ic_action_bar_favorite_normal);
                } else {
                    favorite.setIcon(R.drawable.ic_action_bar_favorite_pressed);
                }

                tab_close.setVisibility(View.GONE);
                tab_chuandi.setVisibility(View.GONE);
                tab_qianshou.setVisibility(View.GONE);
                tab_success.setVisibility(View.GONE);

                ObjectAnimator.ofFloat(listview, AnimUtil.ALPHA, 0f, 1f).setDuration(200).start();

                if (bean.pairingstate == 0) {
                    ObjectAnimator.ofFloat(bottom_tab, AnimUtil.ALPHA, 0f, 1f).setDuration(200).start();
                    if (bean.user.getId().equals(ac.getLoginUserId())) {
                        bottom_tab.setVisibility(View.VISIBLE);
                        tab_close.setVisibility(View.VISIBLE);
                    } else if (ac.getLoginUserId().equals(bean.lastUser.getId())) {
                        bottom_tab.setVisibility(View.VISIBLE);
                        tab_qianshou.setVisibility(View.VISIBLE);
                        tab_chuandi.setVisibility(View.VISIBLE);
                    }
                } else if (bean.pairingstate == 1) {
                    ObjectAnimator.ofFloat(bottom_tab, AnimUtil.ALPHA, 0f, 1f).setDuration(200).start();
                    bottom_tab.setVisibility(View.VISIBLE);
                    tab_success.setVisibility(View.VISIBLE);
                } else if (bean.pairingstate == -1) {
                    getSupportActionBar().setSubtitle("已关闭");
                }
                if (initDogHeart.compareAndSet(false, true)) {
                    dog_heart.setPivotX(dog_heart.getWidth() / 2);
                    dog_heart.setPivotY(dog_heart.getHeight() / 2);

                    ValueAnimator animatorScaleX = new ObjectAnimator().ofFloat(dog_heart, AnimUtil.SCALEX, 1f, 1.5f);
                    animatorScaleX.setRepeatCount(Animation.INFINITE);
                    animatorScaleX.setRepeatMode(Animation.REVERSE);
                    ValueAnimator animatorScaleY = new ObjectAnimator().ofFloat(dog_heart, AnimUtil.SCALEY, 1f, 1.5f);
                    animatorScaleY.setRepeatCount(Animation.INFINITE);
                    animatorScaleY.setRepeatMode(Animation.REVERSE);
                    animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animatorScaleX, animatorScaleY);
                    animatorSet.setDuration(200);
                    animatorSet.start();

                }
                showRedLine();
            }
        }
    }

    @Override
    public void setJsonData(JSONObject jo, boolean cache) {
        super.setJsonData(jo, cache);

        /**
         * 判断是否超时未传递
         *//*
        if (bean != null && bean.pairingstate == 0 && bean.user.getId().equals(ac.getLoginUserId())) {
            try {
                User lastUser = bean.lastUser;
                long lastCreateTime = Util.fmtDateTime.parse(lastUser.getCreate_time()).getTime() + 24l * 60l * 60l * 1000l;
                long nowTime = new Date().getTime();
                if (lastCreateTime < nowTime) {
                    User user = adapter.getItem(adapter.getCount() - 1);
                    user.setTimeOut(true);
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    AtomicBoolean isRedLineEnd = new AtomicBoolean(false);

    @UiThread(delay = 200l)
    public void showRedLine() {
        int height = 0;
        for (int i = 0; i < mChildrenHeights.size(); i++) {
            height += mChildrenHeights.get(i);
        }
        ValueAnimator animatorRedLine = new ObjectAnimator().ofInt(0, height);
        animatorRedLine.setDuration(500);
        animatorRedLine.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = red_line.getLayoutParams();
                layoutParams.height = Integer.valueOf(valueAnimator.getAnimatedValue().toString());
                red_line.setLayoutParams(layoutParams);
            }
        });
        animatorRedLine.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRedLineEnd.compareAndSet(true, false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRedLineEnd.compareAndSet(false, true);
            }
        });
        animatorRedLine.start();
    }

    @Override
    public void initListView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.red_line_detail_head, listview, false);
        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ImageUtil.dip2px(getActivity(), 290)));
        head = new Head(view);
        listview.addHeaderView(view);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                onScrollChanged();
            }
        });
    }

    int scrollH = 0;

    public void onScrollChanged(int mScrollY) {
        if (isRedLineEnd.get()) {
            ViewGroup.LayoutParams params = body.getLayoutParams();
            params.height = params.height + mScrollY - scrollH;
            body.setLayoutParams(params);

            ViewGroup.LayoutParams lineParams = red_line.getLayoutParams();
            lineParams.height = lineParams.height + mScrollY - scrollH;
            red_line.setLayoutParams(lineParams);
        }
        scrollview.scrollTo(0, mScrollY);
        scrollH = mScrollY;

    }

    public void onScrollChanged() {
        if (listview.getChildCount() > 0) {
            int firstVisiblePosition = listview.getFirstVisiblePosition();
            for (int i = listview.getFirstVisiblePosition(), j = 0; i <= listview.getLastVisiblePosition(); i++, j++) {
                if (mChildrenHeights.indexOfKey(i) < 0 || listview.getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
                    mChildrenHeights.put(i, listview.getChildAt(j).getHeight());
                }
            }

            View firstVisibleChild = listview.getChildAt(0);
            if (firstVisibleChild != null) {
                if (mPrevFirstVisiblePosition < firstVisiblePosition) {
                    // scroll down
                    int skippedChildrenHeight = 0;
                    if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
                        for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
                            if (0 < mChildrenHeights.indexOfKey(i)) {
                                skippedChildrenHeight += mChildrenHeights.get(i);
                            } else {
                                // Approximate each item's height to the first visible child.
                                // It may be incorrect, but without this, scrollY will be broken
                                // when scrolling from the bottom.
                                skippedChildrenHeight += firstVisibleChild.getHeight();
                            }
                        }
                    }
                    mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
                    mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                } else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
                    // scroll up
                    int skippedChildrenHeight = 0;
                    if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
                        for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
                            if (0 < mChildrenHeights.indexOfKey(i)) {
                                skippedChildrenHeight += mChildrenHeights.get(i);
                            } else {
                                // Approximate each item's height to the first visible child.
                                // It may be incorrect, but without this, scrollY will be broken
                                // when scrolling from the bottom.
                                skippedChildrenHeight += firstVisibleChild.getHeight();
                            }
                        }
                    }
                    mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
                    mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                } else if (firstVisiblePosition == 0) {
                    mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                }
                if (mPrevFirstVisibleChildHeight < 0) {
                    mPrevFirstVisibleChildHeight = 0;
                }
                mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop();
                mPrevFirstVisiblePosition = firstVisiblePosition;
                onScrollChanged(mScrollY);
                if (mFirstScroll) {
                    mFirstScroll = false;
                }

                mPrevScrollY = mScrollY;
            }
        }
    }

    @Override
    public void initParams() {
        super.initParams();
        api.initParam(bean.id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (animatorSet1 != null) {
            animatorSet1.cancel();
        }
    }

    @OptionsItem
    public void share() {
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
        mController.openShare(getActivity(), new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败";
                } else {
                    ac.finalHttp.post(URL.SHARE_RECORD, getAjaxParams(), new MyJsonHttpResponseHandler(getActivity()) {
                        @Override
                        public void onSuccessRetCode(JSONObject jo) throws Throwable {

                        }

                        @Override
                        public void onFailure() {
                        }
                    });
                }
                showCustomToast(showText);
            }
        });
    }

    @OptionsItem
    public void favorite() {
        if (bean != null) {
            RequestParams params = Util.getAjaxParams(getActivity());
            params.put("treadId", bean.id);
            String dialogMessage = "正在添加收藏";
            if (bean.collectionstate == 0) {
                params.put("type", 0);
            } else {
                dialogMessage = "正在删除收藏";
                params.put("type", 1);
            }

            ac.finalHttp.post(URL.RED_LINE_FAVORITE, params, new MyJsonHttpResponseHandler(getActivity(), dialogMessage) {
                @Override
                public void onSuccessRetCode(JSONObject jo) throws Throwable {
                    if (bean.collectionstate == 0) {
                        bean.collectionstate = 1;
                        favorite.setIcon(R.drawable.ic_action_bar_favorite_pressed);
                    } else {
                        bean.collectionstate = 0;
                        favorite.setIcon(R.drawable.ic_action_bar_favorite_normal);
                    }
                }
            });
        }
    }

    @Click
    public void tab_close() {
        if (bean != null) {
            if (bean.pairingstate == 0 && bean.user.getId().equals(ac.getLoginUserId())) {
                Util.showDialog(getActivity(), getString(R.string.red_line_close_message), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestParams params = Util.getAjaxParams(getActivity());
                        params.put("treadId", bean.id);
                        ac.finalHttp.post(URL.RED_LINE_CLOSE, params, new MyJsonHttpResponseHandler(getActivity(), getString(R.string.closeing_redline)) {
                            @Override
                            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                bean.pairingstate = -1;
                                bottom_tab.setVisibility(View.GONE);
                                getSupportActionBar().setSubtitle("已关闭");
                            }
                        });
                    }
                });
            }
        }
    }

    @Click
    public void tab_qianshou() {
        if (bean != null) {
            if (bean.pairingstate == 0 && bean.lastUser.getId().equals(ac.getLoginUserId())) {
                Util.showDialog(getActivity(), getString(R.string.red_line_qianshou_message), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestParams params = Util.getAjaxParams(getActivity());
                        params.put("treadId", bean.id);
                        params.put("transId", ac.getLoginUserId());
                        ac.finalHttp.post(URL.RED_LINE_COMPLETE, params, new MyJsonHttpResponseHandler(getActivity(), getString(R.string.loading)) {
                            @Override
                            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                bean.pairingstate = 1;
                                bottom_tab.setVisibility(View.VISIBLE);
                                tab_close.setVisibility(View.GONE);
                                tab_chuandi.setVisibility(View.GONE);
                                tab_qianshou.setVisibility(View.GONE);
                                tab_success.setVisibility(View.VISIBLE);
                                head.bind(bean);
                                Util.toast(getActivity(), getString(R.string.qianshou_success));
                            }
                        });
                    }
                });
            }
        }
    }

    @Click
    public void tab_chuandi() {
        if (bean != null) {
            if (bean.pairingstate == 0 && bean.lastUser.getId().equals(ac.getLoginUserId())) {
                RedLinePublishUserListActivity_.intent(this).bean(bean).start();
            }
        }
    }
}

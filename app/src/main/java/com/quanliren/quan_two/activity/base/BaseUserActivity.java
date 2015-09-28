package com.quanliren.quan_two.activity.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment;
import com.quanliren.quan_two.activity.user.PersonalDongTaiActivity_;
import com.quanliren.quan_two.activity.user.UserPicFragment_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.bean.MessageList;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.PullScrollView;
import com.quanliren.quan_two.custom.StateTextView;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity
public abstract class BaseUserActivity extends BaseActivity implements
        PullScrollView.OnTurnListener {

    @ViewById
    public View bg_click_ll;
    @ViewById
    public PullScrollView scroll_view;
    @ViewById
    public View scroll_ll;
    @ViewById
    public SwipeRefreshLayout swipe;
    @ViewById(R.id.personal_ll)
    public View personal_btn;
    @ViewById
    public TextView xing;
    @ViewById
    public UserNickNameRelativeLayout nick_ll;
    @ViewById
    public ImageView mHeadImg;
    @ViewById
    public TextView id_number;
    @ViewById
    public TextView dog_food;
    @ViewById
    public TextView personal;
    @ViewById
    public StateTextView date_state;
    @ViewById
    public TextView face;
    @ViewById
    public TextView money;
    @ViewById
    public StateTextView state;
    @ViewById
    public TextView love;
    @ViewById
    public TextView work;
    @ViewById
    public ImageView userlogo;
    @ViewById
    public TextView dycontent;
    @ViewById
    public TextView dytime;
    @ViewById
    public ImageView dyimg;
    @ViewById
    public View dongtai_no;
    @ViewById
    public View personal_ll;
    @ViewById
    public View pic_contents;

    public User user = null;

    private Drawable mActionBarBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    protected boolean translucentStatus() {
        return true;
    }

    @Override
    public boolean needAddParent() {
        return false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Click(R.id.personal_ll)
    public void personal_btn(View v) {
        if (user != null) {
            AM.getActivityManager().popActivity(PersonalDongTaiActivity_.class.getName());
            PersonalDongTaiActivity_.intent(this).otherid(user.getId()).listtype(QuanListFragment.LISTTYPE.OTHER)
                    .start();
        }

    }

    protected void initView() {
//        mActionBarBackgroundDrawable = getResources().getDrawable(
//                R.drawable.actionbar_drawable);
//        mActionBarBackgroundDrawable.setAlpha(0);
//
//        getSupportActionBar().setBackgroundDrawable(
//                mActionBarBackgroundDrawable);

        scroll_view.setOnScrollChangedListener(mOnScrollChangedListener);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
//        }

        scroll_view.setHeader(mHeadImg);
        scroll_view.setOnTurnListener(this);

    }

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
//            getSupportActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };
    private PullScrollView.OnScrollChangedListener mOnScrollChangedListener = new PullScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl,
                                    int oldt) {
//            final int headerHeight = mHeadImg.getHeight()
//                    - getSupportActionBar().getHeight();
//            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight)
//                    / headerHeight;
//            final int newAlpha = (int) (ratio * 255);
//            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };

    UserPicFragment_ fragment;

    public void initViewByUser() {

        if (user == null) {
            return;
        }
        try {

            scroll_ll.setVisibility(View.VISIBLE);

//            getSupportActionBar().setTitle("");
            if (Util.isStrNotNull(user.getAvatar())) {
                ImageLoader.getInstance().displayImage(
                        user.getAvatar() + StaticFactory._320x320, userlogo,
                        ac.options_userlogo);
            }

            if (Util.isStrNotNull(user.getBgimg())) {
                ImageLoader.getInstance().displayImage(
                        user.getBgimg() + StaticFactory._960x720, mHeadImg,
                        new DisplayImageOptions.Builder().cacheInMemory(true).showImageForEmptyUri(R.drawable.user_detail_top_bg)
                                .showImageOnLoading(R.drawable.user_detail_top_bg)
                                .showImageOnFail(R.drawable.user_detail_top_bg)
                                .cacheOnDisk(true).build());
            }

            ArrayList<ImageBean> imglist = user.getImglist();
            if (user.getImglist() == null) {
                imglist = new ArrayList<ImageBean>();
            }


            if (fragment == null) {
                fragment = new UserPicFragment_();
                MessageList list = new MessageList();
                list.arrayImgList = imglist;
                Bundle b = new Bundle();
                b.putSerializable("list", list);
                b.putString("userId", user.getId());
                fragment.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.pic_contents, fragment).commitAllowingStateLoss();
            } else {
                fragment.setList(imglist);
            }
            if (imglist.size() == 0 && !user.getId().equals(ac.getLoginUserId())) {
                pic_contents.setVisibility(View.GONE);
            } else {
                pic_contents.setVisibility(View.VISIBLE);
            }

            nick_ll.setDetail(user);

            if ("".equals(user.getSignature()) || user.getSignature() == null) {
                personal.setText("暂无个性签名");
            }
            if (Util.isStrNotNull(user.getSignature())) {
                personal.setText(user.getSignature());
            }
            if (Util.isStrNotNull(user.getJob())) {
                work.setText(user.getJob());
            }

            if (Util.isStrNotNull(user.getEmotion())) {
                love.setText(user.getEmotion());
            }

            if (Util.isStrNotNull(user.getIncome())) {
                money.setText(user.getIncome());
            }

            if (Util.isStrNotNull(user.getAppearance())) {
                face.setText(user.getAppearance());
            }

//            if (Util.isStrNotNull(user.getSignature())) {
//                signature.setText(user.getSignature());
//            } else {
//                signature.setText("无");
//            }

            StringBuilder sb = new StringBuilder();
            sb.append("ID：" + user.getUsernumber());
//            try {
//                sb.append(Util.getDistance(Double.valueOf(ac.cs.getLng()),
//                        Double.valueOf(ac.cs.getLat()), user.getLongitude(),
//                        user.getLatitude())
//                        + "km | ");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            sb.append(Util.getTimeDateStr(user.getActivetime()));

            id_number.setText(sb.toString());

            dog_food.setText("狗粮：" + user.getCoin() + "");

            date_state.setState(user.getUserstate());

            if (Util.isStrNotNull(user.getConstell())) {
                xing.setText(user.getConstell());
            }

            if (!Util.isStrNotNull(user.getDyimgurl())
                    && !Util.isStrNotNull(user.getDycontent())) {
                dongtai_no.setVisibility(View.VISIBLE);
                personal_ll.setVisibility(View.GONE);
            } else {
                dongtai_no.setVisibility(View.GONE);
                personal_ll.setVisibility(View.VISIBLE);
                if (Util.isStrNotNull(user.getDyimgurl())) {
                    dyimg.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(
                            user.getDyimgurl() + StaticFactory._320x320, dyimg);
                } else {
                    dyimg.setVisibility(View.GONE);
                }
                dycontent.setText(user.getDycontent());
                dytime.setText(Util.getTimeDateStr(user.getDytime()));
            }
            if (!Util.isStrNotNull(user.getDyid())) {
                dongtai_no.setVisibility(View.VISIBLE);
                personal_ll.setVisibility(View.GONE);
            } else {
                dongtai_no.setVisibility(View.GONE);
                personal_ll.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void progress(int progress) {
        ViewCompat.setAlpha(bg_click_ll, (float) (100 - progress) / 100f);
        getSupportActionBar().hide();
    }

    @Override
    public void onTurn() {
        bg_click_ll.animate().alpha(1).setDuration(200);
        getSupportActionBar().show();
    }
}
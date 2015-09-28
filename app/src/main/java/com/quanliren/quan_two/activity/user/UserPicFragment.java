package com.quanliren.quan_two.activity.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.group.PhotoAlbumMainActivity_;
import com.quanliren.quan_two.activity.image.ImageBeanList;
import com.quanliren.quan_two.activity.image.ImageBrowserActivity_;
import com.quanliren.quan_two.adapter.UserInfoPicAdapter;
import com.quanliren.quan_two.adapter.UserInfoPicAdapter.OnImageClickListener;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.bean.MessageList;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.custom.CustomPicWall;
import com.quanliren.quan_two.custom.NoScrollGridView;
import com.quanliren.quan_two.custom.RoundProgressBar;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.user_pic_fragment)
public class UserPicFragment extends MenuFragmentBase implements
        OnImageClickListener {

    @ViewById
    ViewPager viewpager;
    @ViewById
    View album_selecter;
    @ViewById
    ImageView page_select0;
    @ViewById
    ImageView page_select1;

    ArrayList<ImageBean> listSource = new ArrayList<ImageBean>();
    ArrayList<ImageBean> copyList = new ArrayList<ImageBean>();

    PicPageAdapter adapter;

    User user;
    String userId;

    public void setList(ArrayList<ImageBean> list) {
        this.listSource = list;

        this.copyList = (ArrayList<ImageBean>) list.clone();
        if (user != null && user.getId().equals(userId)) {
            if ((user.getIsvip() == 0 && list.size() < 8)
                    || (user.getIsvip() != 0 && list.size() < 16))
                this.copyList.add(new ImageBean(true));
        }

        if (copyList != null && copyList.size() > 0) {
            if (copyList.size() > 4) {
                viewpager.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT, getResources()
                        .getDisplayMetrics().widthPixels / 2 + ImageUtil.dip2px(getActivity(), 4)));
            } else {
                viewpager.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT, getResources()
                        .getDisplayMetrics().widthPixels / 4 + ImageUtil.dip2px(getActivity(), 4)));
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                viewpager.setAdapter(adapter = new PicPageAdapter());
            }
            if (adapter.getCount() > 1) {
                album_selecter.setVisibility(View.VISIBLE);
            } else {
                album_selecter.setVisibility(View.GONE);
            }

            viewpager.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    switch (arg0) {
                        case 0:
                            page_select0
                                    .setImageResource(R.drawable.ic_album_selected);
                            page_select1
                                    .setImageResource(R.drawable.ic_album_normal);
                            break;
                        case 1:
                            page_select1
                                    .setImageResource(R.drawable.ic_album_selected);
                            page_select0
                                    .setImageResource(R.drawable.ic_album_normal);
                            break;
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });

			/*
             * if (b.compareAndSet(false, true)) { if (adapter.getCount() > 0) {
			 * viewpager.setCurrentItem(adapter.getCount() - 1); }
			 * 
			 * new Handler().postDelayed(new Runnable() {
			 * 
			 * @Override public void run() { viewpager.setCurrentItem(0); } },
			 * 1500); }
			 */
        }
    }

    int lineNum = 8;
    List<GridView> views = new ArrayList<GridView>();
    List<List<ImageBean>> imgLists = new ArrayList<List<ImageBean>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            MessageList bean = (MessageList) getArguments().get("list");
            listSource = bean.arrayImgList;
            userId = getArguments().getString("userId");
        }

        super.onCreate(savedInstanceState);

        user = getHelper().getUserInfo();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @AfterViews
    void initView() {
        setList(listSource);
    }

    public NoScrollGridView createGridView() {
        NoScrollGridView gridview = new NoScrollGridView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        gridview.setGravity(Gravity.CENTER);
        gridview.setNumColumns(4);
        gridview.setPadding(ImageUtil.dip2px(getActivity(), 4),
                ImageUtil.dip2px(getActivity(), 4),
                ImageUtil.dip2px(getActivity(), 4),
                ImageUtil.dip2px(getActivity(), 4));
        gridview.setVerticalSpacing(ImageUtil.dip2px(getActivity(), 4));
        gridview.setHorizontalSpacing(ImageUtil.dip2px(getActivity(), 4));
        gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridview.setLayoutParams(lp);
        return gridview;
    }

    public CustomPicWall createLinearLayout() {
        CustomPicWall layout = new CustomPicWall(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(lp);
        return layout;
    }

    class PicPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            int lines = (int) (copyList.size() / lineNum);
            if (copyList.size() % lineNum > 0) {
                lines++;
            }
            return lines;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // CustomPicWall gridview=createLinearLayout();
            GridView gridview = createGridView();
            List<ImageBean> imgs = new ArrayList<ImageBean>();
            for (int j = position * lineNum; j < copyList.size(); j++) {
                imgs.add(copyList.get(j));
                if (imgs.size() == lineNum) {
                    break;
                }
            }
            UserInfoPicAdapter adapter = new UserInfoPicAdapter(getActivity(),
                    imgs, UserPicFragment.this);
            gridview.setAdapter(adapter);
            // gridview.setImgList(imgs);
            container.addView(gridview);
            return gridview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void addImg() {
        uploadImg();
    }

    @Override
    public void imgClick(ImageBean id) {
        int position = -1;
        for (ImageBean ib : listSource) {
            if (ib.imgid == id.imgid && !ib.imgpath.startsWith(Util.FILE)) {
                position = listSource.indexOf(ib);
            }
        }
        if (position > -1)
            ImageBrowserActivity_.intent(this).mPosition(position).ibl(new ImageBeanList(listSource)).start();
    }

    @Override
    public void imgLongClick(final ImageBean ibs) {
        if (user != null && user.getId().equals(userId)) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setItems(new String[]{"设为头像", "设为背景", "删除"},
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    final int which) {
                                    RequestParams rp = getAjaxParams();
                                    rp.put("imgid", ibs.imgid);
                                    rp.put("actiontype", which);
                                    ac.finalHttp.post(URL.DELETE_USERLOGO, rp,
                                            new JsonHttpResponseHandler() {
                                                @Override
                                                public void onStart() {
                                                    switch (which) {
                                                        case 0:
                                                        case 1:
                                                            customShowDialog("正在设置");
                                                            break;
                                                        case 2:
                                                            customShowDialog("正在删除");
                                                            break;
                                                    }

                                                }

                                                @Override
                                                public void onFailure() {
                                                    customDismissDialog();
                                                    showIntentErrorToast();
                                                }

                                                @Override
                                                public void onSuccess(
                                                        JSONObject response) {
                                                    try {
                                                        int status = response
                                                                .getInt(URL.STATUS);
                                                        switch (status) {
                                                            case 0:
                                                                switch (which) {
                                                                    case 0:
                                                                        User user1 = getHelper().getUserInfo();
                                                                        user1.setAvatar(ibs.imgpath);
                                                                        UserTable ut = new UserTable(
                                                                                user1);
                                                                        getHelper()
                                                                                .getUserTableDao().update(ut);
                                                                        Intent i = new Intent(UserInfoActivity.USERINFO_UPDATE_UI);
                                                                        if (getActivity() != null)
                                                                            getActivity().sendBroadcast(i);
                                                                        break;
                                                                    case 1:
                                                                        User user2 = getHelper().getUserInfo();
                                                                        user2.setBgimg(ibs.imgpath);
                                                                        UserTable ut1 = new UserTable(
                                                                                user2);
                                                                        getHelper()
                                                                                .getUserTableDao().update(ut1);
                                                                        Intent i1 = new Intent(UserInfoActivity.USERINFO_UPDATE_UI);
                                                                        if (getActivity() != null)
                                                                            getActivity().sendBroadcast(i1);
                                                                        break;
                                                                    case 2:
                                                                        int index = 0;
                                                                        for (ImageBean ib : listSource) {
                                                                            if (ib.imgid == ibs.imgid) {
                                                                                index = listSource
                                                                                        .indexOf(ib);
                                                                                break;
                                                                            }
                                                                        }
                                                                        View pagerView = viewpager
                                                                                .getChildAt(index > 7 ? 1
                                                                                        : 0);
                                                                        if (pagerView instanceof NoScrollGridView) {
                                                                            final ImageView rp = (ImageView) ((NoScrollGridView) pagerView)
                                                                                    .getChildAt(
                                                                                            index % 7)
                                                                                    .findViewById(
                                                                                            R.id.img);
                                                                            ValueAnimator va = ObjectAnimator
                                                                                    .ofFloat(
                                                                                            rp,
                                                                                            "alpha",
                                                                                            1f,
                                                                                            0f);
                                                                            va.setDuration(200);
                                                                            final int j = index;
                                                                            va.addListener(new AnimatorListenerAdapter() {
                                                                                public void onAnimationEnd(
                                                                                        Animator animation) {
                                                                                    rp.clearAnimation();
                                                                                    listSource
                                                                                            .remove(j);
                                                                                    user.setImglist(listSource);
                                                                                    UserTable ut = new UserTable(
                                                                                            user);
                                                                                    getHelper()
                                                                                            .getUserTableDao()
                                                                                            .deleteById(
                                                                                                    ut.getId());
                                                                                    getHelper()
                                                                                            .getUserTableDao()
                                                                                            .create(ut);
                                                                                    setList(listSource);
                                                                                }

                                                                                ;
                                                                            });
                                                                            va.start();
                                                                        }
                                                                        break;
                                                                }
                                                                break;
                                                            default:
                                                                showFailInfo(response);
                                                                break;
                                                        }

                                                    } catch (Exception e) {
                                                    } finally {
                                                        customDismissDialog();
                                                    }
                                                }
                                            });
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    private String picPath;
    public static final int Album = 6, Camera = 1;

    public void uploadImg() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("添加图片")
                .setItems(new String[]{"相册", "拍照"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 1:
                                        if (Util.existSDcard()) {
                                            Intent intent = new Intent(); // 调用照相机
                                            String messagepath = StaticFactory.APKCardPath;
                                            File fa = new File(messagepath);
                                            if (!fa.exists()) {
                                                fa.mkdirs();
                                            }
                                            picPath = messagepath
                                                    + new Date().getTime();// 图片路径
                                            intent.putExtra(
                                                    MediaStore.EXTRA_OUTPUT,
                                                    Uri.fromFile(new File(picPath)));
                                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, Camera);
                                        } else {
                                            showCustomToast("亲，请检查是否安装存储卡!");
                                        }
                                        break;
                                    case 0:
                                        if (Util.existSDcard()) {
                                            String messagepath = StaticFactory.APKCardPath;
                                            File fa = new File(messagepath);
                                            if (!fa.exists()) {
                                                fa.mkdirs();
                                            }
                                            ArrayList<String> tempList = new ArrayList<String>();
                                            for (int i = 0; i < listSource.size(); i++) {
                                                tempList.add("");
                                            }
                                            PhotoAlbumMainActivity_
                                                    .intent(UserPicFragment.this)
                                                    .maxnum(user.getIsvip() == 0 ? 8
                                                            : 16).paths(tempList)
                                                    .startForResult(Album);
                                        } else {
                                            showCustomToast("亲，请检查是否安装存储卡!");
                                        }
                                        break;
                                }
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @OnActivityResult(Camera)
    void onCameraResult(int resultCode, Intent data) {
        if (picPath != null) {
            File fi = new File(picPath);
            if (fi != null && fi.exists()) {
                ImageUtil.downsize(picPath, picPath, getActivity());
                List<String> strs = new ArrayList<String>();
                strs.add(picPath);
                uploadImgByList(strs, 0);
            }
            fi = null;
        }
    }

    @OnActivityResult(Album)
    void onAlbumResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        final ArrayList<String> list = data.getStringArrayListExtra("images");
        customShowDialog("正在处理");
        replaceList(list);
    }

    List<RequestHandle> rhs = new ArrayList<RequestHandle>();

    @Background
    public void replaceList(ArrayList<String> list) {
        List<String> liststr = new ArrayList<String>();
        for (String string : list) {
            if (!string.equals("")) {
                ImageUtil.downsize(string, string = StaticFactory.APKCardPath
                        + string.hashCode(), getActivity());
                liststr.add(string);
            }
        }
        uploadImgByList(liststr, 0);
        initSource();
    }

    @UiThread
    public void uploadImgByList(final List<String> list, final int position) {
        if (position == list.size()) {
            return;
        }
        RequestParams rp = getAjaxParams();
        try {
            rp.put("file", new File(list.get(position)));

            this.listSource.add(new ImageBean(Util.FILE + list.get(position)));
            initSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ac.finalHttp.post(URL.UPLOAD_ALBUM_IMG, rp,
                new JsonHttpResponseHandler() {
                    RoundProgressBar rp = null;

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            ImageBean ibs = listSource.get(listSource.size() - 1);
                            ibs.imgid = response.getJSONObject(URL.RESPONSE)
                                    .getInt("imgid");
                            ibs.imgpath = response.getJSONObject(URL.RESPONSE)
                                    .getString("imgurl");
                            user.setImglist(listSource);

                            UserTable ut = new UserTable(user);
                            getHelper().getUserTableDao().deleteById(ut.getId());
                            getHelper().getUserTableDao().create(ut);

                            if (listSource.size() == 9) {
                                viewpager.setCurrentItem(1);
                            }
                        } catch (Exception e) {
                        } finally {
                            if (rp != null)
                                rp.setVisibility(View.GONE);
                        }
                        if (position == list.size() - 1) {
                            setList(listSource);
                        } else {
                            uploadImgByList(list, position + 1);
                        }
                    }

                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        int progress = (int) (((float) bytesWritten / (float) totalSize) * 100);
                        if (progress > 0 && progress < 100) {
                            if (rp == null) {
                                try {
                                    int viewposition = UserPicFragment.this.listSource
                                            .size() - 1;
                                    View pagerView = viewpager
                                            .getChildAt(viewposition > 7 ? 1
                                                    : 0);
                                    if (pagerView instanceof NoScrollGridView) {
                                        Object obj = ((NoScrollGridView) pagerView)
                                                .getChildAt(viewposition);
                                        if (obj != null) {
                                            rp = (RoundProgressBar) ((View) obj)
                                                    .findViewById(R.id.loadProgressBar);
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                rp.setVisibility(View.VISIBLE);
                                rp.setProgress((int) (((float) bytesWritten / (float) totalSize) * 100));
                            }
                        }
                    }

                    @Override
                    public void onFailure() {
                    }
                });
    }

    @UiThread
    void initSource() {
        customDismissDialog();
        setList(listSource);
    }
}

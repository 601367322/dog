package com.quanliren.quan_two.fragment.custom;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

@EFragment(R.layout.add_pic_fragment)
public class AddPicFragment extends MenuFragmentBase {

    public ArrayList<String> imagePath = new ArrayList<String>();
    int imageWidth = 0;
    @ViewById
    LinearLayout pic_ll;
    TextView textView;
    int lineNum = 4;
    int maxNum = 6;
    public String cameraPath;
    ArrayList<String> ibs = new ArrayList<String>();
    ArrayList<String> sdibs = new ArrayList<String>();

    public ArrayList<String> getIbs() {
        return ibs;
    }

    public ArrayList<String> getSdibs() {
        return sdibs;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public static final String DEFAULT = "default";
    public static final int Album = 2, Camera = 1,RADIO = 3;;

    @AfterViews
    void initView() {
        imageWidth = (int) ((float) (getResources().getDisplayMetrics().widthPixels - ImageUtil
                .dip2px(getActivity(), 24 + (lineNum - 1) * lineNum)) / lineNum);
        initSource(imagePath);
    }

    public void init() {
        LinearLayout ll = createLayout();
        pic_ll.addView(ll);
        ll.addView(createImageView(0));
        ll.addView(textView = createTextView());
    }


    public void initSource(ArrayList<String> list) {
        imagePath = (ArrayList<String>) list.clone();
        pic_ll.removeAllViews();
        if (imagePath.size() < maxNum) {
            imagePath.add("-1");
        }
        int lines = (int) (imagePath.size() / lineNum);
        if (imagePath.size() % lineNum > 0) {
            lines++;
        }
        for (int i = 0; i < lines; i++) {
            pic_ll.addView(createLayout());
        }
        for (int i = 0; i < imagePath.size(); i++) {
            int num = (int) (i / lineNum);
            LinearLayout ll = (LinearLayout) pic_ll.getChildAt(num);
            ImageView iv;
            ll.addView(iv = createImageView(i));
            if (imagePath.get(i).equals("-1")) {
            } else {
                iv.setTag(imagePath.get(i));
                if (imagePath.get(i).startsWith("http://")) {
                    ImageLoader.getInstance()
                            .displayImage(imagePath.get(i) + StaticFactory._320x320, iv);
                } else {
                    ImageLoader.getInstance().displayImage(
                            Util.FILE + imagePath.get(i), iv);
                }
            }
            if (imagePath.size() == 1) {
                ll.addView(textView = createTextView());
            }
        }
    }

    public ImageView createImageView(int i) {
        LayoutParams lp = new LayoutParams(imageWidth, imageWidth);
        int margin = ImageUtil.dip2px(getActivity(), 2);
        lp.setMargins(margin, margin, margin, margin);
        ImageView image = new ImageView(getActivity());
        image.setLayoutParams(lp);
        image.setAdjustViewBounds(true);
        image.setScaleType(ScaleType.CENTER_CROP);
        image.setImageResource(R.drawable.publish_add_pic_icon_big);
        image.setTag(DEFAULT);
        image.setOnClickListener(imgClick);
        image.setId(i);
        return image;
    }

    OnClickListener imgClick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            mListener.onArticleSelected(arg0);
        }
    };

    public TextView createTextView() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.leftMargin = ImageUtil.dip2px(getActivity(), 10);
        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(lp);
        tv.setText("添加图片");
        tv.setTextSize(14);
        tv.setTextColor(getResources().getColor(R.color.manage_member_text));
        return tv;
    }

    public LinearLayout createLayout() {
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(lp);
        return ll;
    }

    public interface OnArticleSelectedListener {
        public void onArticleSelected(View articleUri);
    }

    OnArticleSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public void removeByPath(String url) {
        if (ibs.indexOf(url) > -1) sdibs.remove(ibs.indexOf(url));
        ibs.remove(url);

        File file = new File(url);
        if (file.exists()) {
            file.delete();
        }
        initSource(ibs);
    }

    public void addPath(String url) {
        ibs.add(url);
        sdibs.add(url);
        initSource(ibs);
    }

    public int getCount() {
        return ibs.size();
    }

    public String getItem(int i) {
        return ibs.get(i);
    }

    public void removeByView(View view) {
        String url = view.getTag().toString();
        removeByPath(url);
    }

    public void replaceList(ArrayList<String> list) {
        for (String string : list) {
            if (!sdibs.contains(string)) {
                ImageUtil.downsize(string,
                        string = StaticFactory.APKCardPath
                                + string.hashCode(),
                        getActivity());
                ibs.add(string);
            }
        }
        for (String string2 : sdibs) {
            if (!list.contains(string2)) {
                File file = new File(StaticFactory.APKCardPath + string2.hashCode());
                if (file.exists()) {
                    file.delete();
                }
                ibs.remove(StaticFactory.APKCardPath + string2.hashCode());
            }
        }
        sdibs = list;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Album:
                if (data == null) {
                    return;
                }
                final ArrayList<String> list = data
                        .getStringArrayListExtra("images");
                customShowDialog("正在处理");
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        replaceList(list);
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                initSource(ibs);
                                customDismissDialog();
                            }
                        });
                    }
                }).start();
                break;
            case Camera:
                if (cameraPath != null) {
                    File fi = new File(cameraPath);
                    if (fi != null && fi.exists()) {
                        ImageUtil.downsize(cameraPath, cameraPath, getActivity());
                        addPath(cameraPath);
                        ;
                    }
                    fi = null;
                }
                break;
            default:
                break;
        }
    }
}

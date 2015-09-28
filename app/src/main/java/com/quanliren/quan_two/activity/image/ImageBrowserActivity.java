package com.quanliren.quan_two.activity.image;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.adapter.ImageBrowserAdapter;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.custom.PhotoTextView;
import com.quanliren.quan_two.custom.ScrollViewPager;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

@WindowFeature({Window.FEATURE_NO_TITLE})
@Fullscreen
@EActivity(R.layout.product_activity_imagebrowser)
public class ImageBrowserActivity extends BaseActivity implements
        OnPageChangeListener {

    @ViewById(R.id.imagebrowser_svp_pager)
    public ScrollViewPager mSvpPager;
    @ViewById(R.id.imagebrowser_ptv_page)
    public PhotoTextView mPtvPage;
    private ImageBrowserAdapter mAdapter;
    @Extra
    public int mPosition;
    private int mTotal;
    public List<ImageBean> mProfile;
    @Extra
    public ImageBeanList ibl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
    }

    @Override
    public boolean needAddParent() {
        return false;
    }

    public void init() {
        mProfile = ibl.mProfile;

        mSvpPager.setOnPageChangeListener(this);

        mTotal = mProfile.size();
        if (mPosition > mTotal) {
            mPosition = mTotal - 1;
        }
        if (mTotal > 0) {
            mPosition += 1000 * mTotal;
            mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
            mAdapter = new ImageBrowserAdapter(mProfile, this);
            mSvpPager.setAdapter(mAdapter);
            mSvpPager.setCurrentItem(mPosition, false);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        mPosition = arg0;
        mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
    }

    @Click(R.id.save)
    public void save_img(View v) {
        try {
            File fromFile = ImageLoader.getInstance().getDiskCache()
                    .get(mProfile.get(mPosition % mTotal).imgpath);
            String path = mProfile.get(mPosition % mTotal).imgpath;
            LogUtil.d("----------", path);
            int position = path.lastIndexOf("/");
            File toFile = new File(StaticFactory.SDCardPath
                    + "/DCIM/Camera/");
            if (!toFile.exists()) {
                toFile.mkdirs();
            }
            if (!path.endsWith(".jpg")) {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                byte[] buffer = new byte[100];
                int temp = 0;
                try {
                    fis = new FileInputStream(path);
                    toFile = new File(toFile, path.substring(position + 1, path.length()) + ".jpg");
                    fos = new FileOutputStream(toFile);
                    while (true) {
                        temp = fis.read(buffer, 0, buffer.length);
                        if (temp == -1) {
                            break;
                        }
                        fos.write(buffer, 0, temp);
                    }
                    ContentValues localContentValues = new ContentValues();
                    localContentValues.put("_data", toFile.toString());
                    localContentValues.put("description", "save image ---");
                    localContentValues.put("mime_type", "image/jpeg");
                    ContentResolver localContentResolver = getContentResolver();
                    Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    localContentResolver.insert(localUri, localContentValues);
                } catch (Exception e) {
                    showCustomToast("保存失败");
                }

            } else {
                toFile = new File(toFile, path.substring(position + 1, path.length()));
            }

            if (toFile.exists()) {
                Toast.makeText(ImageBrowserActivity.this,
                        "图片已经保存到" + toFile.getParent() + "文件夹下",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Util.doCopyFile(fromFile, toFile);
            Toast.makeText(ImageBrowserActivity.this,
                    "图片已经保存到" + toFile.getParent() + "文件夹下",
                    Toast.LENGTH_SHORT).show();

            ContentValues localContentValues = new ContentValues();
            localContentValues.put("_data", toFile.toString());
            localContentValues.put("description", "save image ---");
            localContentValues.put("mime_type", "image/jpeg");
            ContentResolver localContentResolver = getContentResolver();
            Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            localContentResolver.insert(localUri, localContentValues);
        } catch (Exception e) {
            Toast.makeText(ImageBrowserActivity.this, "已保存",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}

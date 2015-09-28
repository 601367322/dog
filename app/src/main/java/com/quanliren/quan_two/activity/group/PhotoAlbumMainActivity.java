package com.quanliren.quan_two.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.PhotoAibum;
import com.quanliren.quan_two.bean.PhotoItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

import java.util.ArrayList;

@EActivity
@OptionsMenu(R.menu.filter_people_menu)
public class PhotoAlbumMainActivity extends BaseActivity {

    @Extra
    int maxnum = 0;
    @Extra
    ArrayList<String> paths = new ArrayList<String>();

    @OptionsMenuItem
    MenuItem ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_album_main);
        getSupportActionBar().setTitle("相册");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new PhotoAlbumActivity_()).commitAllowingStateLoss();
    }


    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        ok.setTitle("确定(" + paths.size() + "/" + maxnum + ")");
        return super.onCreateOptionsMenu(menu);
    }

    ;

    PhotoActivity pa;

    public void replaceFragment(PhotoAibum i) {

        for (PhotoItem item : i.getBitList()) {
            if (paths.contains(item.getPath())) {
                item.setSelect(true);
            }
        }

        Bundle b = new Bundle();
        b.putSerializable("aibum", i);
        pa = new PhotoActivity_();
        pa.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, pa).addToBackStack(null).commitAllowingStateLoss();
    }

    public void finishActivity() {
        if (pa != null && pa.isVisible()) {
            onBackPressed();
        } else {
            scrollToFinishActivity();
        }
    }

    @OptionsItem(R.id.ok)
    public void rightClick() {
        Intent i = new Intent();
        i.putStringArrayListExtra("images", paths);
        setResult(1, i);
        finish();
    }

    public void changeNum() {
        ok.setTitle("确定(" + paths.size() + "/" + maxnum + ")");
    }

    public void addPath(String path) {
        paths.add(path);
        changeNum();
    }

    public void removePath(String path) {
        paths.remove(path);
        changeNum();
    }
}

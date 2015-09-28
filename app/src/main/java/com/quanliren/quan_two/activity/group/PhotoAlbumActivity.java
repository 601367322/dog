package com.quanliren.quan_two.activity.group;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.PhotoAibumAdapter;
import com.quanliren.quan_two.bean.PhotoAibum;
import com.quanliren.quan_two.bean.PhotoItem;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ***************************************
 * 类描述： 相册管理类 类名称：PhotoAlbumActivity
 *
 * @version: 1.0
 * @author: why
 * @time: 2013-10-18 下午2:10:46
 * ***************************************
 */
@EFragment(R.layout.activity_photoalbum)
public class PhotoAlbumActivity extends MenuFragmentBase {
    @ViewById(R.id.album_gridview)
    ListView aibumGV;
    private List<PhotoAibum> aibumList;

    // 设置获取图片的字段信�?
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME, // 显示的名�?
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.LONGITUDE, // 经度
            MediaStore.Images.Media._ID, // id
            MediaStore.Images.Media.BUCKET_ID, // dir id 目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void initVIew() {
        aibumList = getPhotoAlbum();
        aibumGV.setAdapter(new PhotoAibumAdapter(aibumList, getActivity()));
        aibumGV.setOnItemClickListener(aibumClickListener);
        getSupportActionBar().setTitle("相册");
    }

    /**
     * 相册点击事件
     */
    OnItemClickListener aibumClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ((PhotoAlbumMainActivity) getActivity()).replaceFragment(aibumList
                    .get(position));
            // Intent intent = new Intent(getActivity(),
            // PhotoActivity.class);
            // intent.putExtra("aibum", aibumList.get(position));
            // startActivityForResult(intent, 1);
        }
    };

    /**
     * 方法描述：按相册获取图片信息
     *
     * @author: why
     * @time: 2013-10-18 下午1:35:24
     */
    private List<PhotoAibum> getPhotoAlbum() {
        List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();
        Cursor cursor = MediaStore.Images.Media.query(getActivity()
                        .getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        LinkedHashMap<String, PhotoAibum> countMap = new LinkedHashMap<String, PhotoAibum>();
        PhotoAibum pa = null;
        while (cursor.moveToNext()) {
            String path = cursor.getString(1);
            String id = cursor.getString(3);
            String dir_id = cursor.getString(4);
            String dir = cursor.getString(5);
            if (!countMap.containsKey(dir_id)) {
                pa = new PhotoAibum();
                pa.setName(dir);
                pa.setBitmap(Integer.parseInt(id));
                pa.setCount("1");
                pa.setPath(path);
                pa.getBitList().add(new PhotoItem(Integer.valueOf(id), path));
                countMap.put(dir_id, pa);
            } else {
                pa = countMap.get(dir_id);
                pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
                pa.getBitList().add(new PhotoItem(Integer.valueOf(id), path));
            }
        }
        cursor.close();
        Iterable<String> it = countMap.keySet();
        for (String key : it) {
            aibumList.add(countMap.get(key));
        }
        return aibumList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case 1:
                        getActivity().setResult(1, data);
                        getActivity().finish();
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

package com.quanliren.quan_two.fragment.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.LoginActivity_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.CacheBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

@EFragment
public class MenuFragmentBase extends Fragment {

    @App
    public AppClass ac;

    @OrmLiteDao(helper = DBHelper.class, model = CacheBean.class)
    public Dao<CacheBean, String> cacheDao;
    @ViewById(R.id.left_city_icon)
    public ImageView left_city_icon;
    @ViewById(R.id.left)
    public LinearLayout left;
    @ViewById(R.id.left_icon)
    public ImageView left_icon;
    @ViewById(R.id.left_title)
    public TextView left_title;
    @ViewById(R.id.center)
    public TextView center;
    @ViewById(R.id.actionbar)
    public View actionbar;

    @ViewById(R.id.right)
    public LinearLayout right;
    @ViewById(R.id.right_icon)
    public ImageView right_icon;
    @ViewById(R.id.right_title)
    public TextView right_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
    }

    public void setTitleTxt(String title) {
        if (this.center != null) {
            this.center.setText(title);
        }

    }

    public void setRightTitleTxt(int title) {
        if (this.right_title != null)
            this.right_title.setText(title);
    }

    public AtomicBoolean init = new AtomicBoolean(false);

    public void closeInput() {
        if (getActivity().getCurrentFocus() != null) {
            ((InputMethodManager) getActivity().getSystemService(
                    getActivity().INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            ((InputMethodManager) getActivity().getSystemService(
                    getActivity().INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), 0);
        }
    }

    public RequestParams getAjaxParams() {
        return Util.getAjaxParams(getActivity());
    }

    public RequestParams getAjaxParams(String str, String strs) {
        RequestParams ap = getAjaxParams();
        ap.put(str, strs);
        return ap;
    }

    public void showCustomToast(String str) {
        if (getActivity() != null) {
            Util.toast(getActivity(), str);
        }
    }

    public void startLogin() {
        LoginActivity_.intent(this).start();
        if (getActivity() != null)
            getActivity().finish();
    }

    public void showFailInfo(JSONObject jo) {
        try {
            int status = jo.getInt(URL.STATUS);
            switch (status) {
                case 2:
                    showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                            URL.INFO));
                    loginOut();
                    break;
                case 1:
                    showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(
                            URL.INFO));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loginOut() {
        try {
            DBHelper.clearTable(getActivity(), LoginUser.class);
            AM.getActivityManager().popAllActivity();
            LoginActivity_.intent(getActivity()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressDialog progressDialog;

    public void customDismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void customShowDialog(String str) {
        if (getActivity() == null) {
            return;
        }
        progressDialog = Util.progress(getActivity(), str);
    }

    String[] str = new String[]{"", "正在获取数据", "正在登录", "正在提交", "请稍等"};

    public void customShowDialog(int i) {
        if (getActivity() == null) {
            return;
        }
        progressDialog = Util.progress(getActivity(), str[i]);
    }

    public void showIntentErrorToast() {
        showCustomToast("网络连接失败");
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public DBHelper getHelper() {
        DBHelper helper = OpenHelperManager.getHelper(getActivity(),
                DBHelper.class);
        return helper;
    }

    public ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void setTransStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getActivity() != null) {
            ViewGroup.LayoutParams layoutParams = actionbar.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + Util.getStatusBarHeight(getActivity());
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//                actionbar.setBackgroundResource(R.color.ripple_material_dark);
//            }
        }
    }
}

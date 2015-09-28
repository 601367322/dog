package com.quanliren.quan_two.activity.group.date;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Shen on 2015/7/3.
 */
@OptionsMenu(R.menu.date_publish_menu)
@EActivity(R.layout.activity_date_filter)
public class DateFilterActivity extends BaseActivity {

    @OrmLiteDao(helper = DBHelper.class)
    RuntimeExceptionDao<FilterBean, Integer> filterDao;

    @ViewById
    LinearLayout sexAll;
    @ViewById
    LinearLayout boyToGirl;
    @ViewById
    LinearLayout girlToBoy;
    @ViewById
    LinearLayout boyToBoy;
    @ViewById
    LinearLayout girlToGirl;
    @ViewById
    LinearLayout xfAll;
    @ViewById
    LinearLayout xfAa;
    @ViewById
    LinearLayout xfFree;
    @ViewById
    LinearLayout timeAll;
    @ViewById
    LinearLayout timeToday;
    @ViewById
    LinearLayout timeTomorrow;
    @ViewById
    LinearLayout timeLater;

    public String date_usex, date_sex, date_whopay, date_dtime;

    AtomicBoolean init = new AtomicBoolean(false);

    @Override
    public void init() {
        super.init();

        List<FilterBean> usexBeans = filterDao.queryForEq(FilterBean.KEY, DateListApi.DATE_USEX);
        List<FilterBean> sexBeans = filterDao.queryForEq(FilterBean.KEY, DateListApi.DATE_SEX);
        List<FilterBean> whoBeans = filterDao.queryForEq(FilterBean.KEY, DateListApi.DATE_WHOPAY);
        List<FilterBean> timeBeans = filterDao.queryForEq(FilterBean.KEY, DateListApi.DATE_DTIME);

        FilterBean usexBean = usexBeans != null && usexBeans.size() > 0 ? usexBeans.get(0) : null;
        FilterBean sexBean = sexBeans != null && sexBeans.size() > 0 ? sexBeans.get(0) : null;
        FilterBean whoBean = whoBeans != null && whoBeans.size() > 0 ? whoBeans.get(0) : null;
        FilterBean timeBean = timeBeans != null && timeBeans.size() > 0 ? timeBeans.get(0) : null;

        String usexValue = usexBean == null ? null : usexBean.getValue();
        String sexValue = sexBean == null ? null : sexBean.getValue();
        String whoValue = whoBean == null ? null : whoBean.getValue();
        String timeValue = timeBean == null ? null : timeBean.getValue();

        if (usexValue == null && sexValue == null) {
            sexAll.performClick();
        } else if (usexValue != null && sexValue != null) {
            if (usexValue.equals("1") && sexValue.equals("0")) {
                boyToGirl.performClick();
            }
            if (usexValue.equals("0") && sexValue.equals("1")) {
                girlToBoy.performClick();
            }
            if (usexValue.equals("1") && sexValue.equals("1")) {
                boyToBoy.performClick();
            }
            if (usexValue.equals("0") && sexValue.equals("0")) {
                girlToGirl.performClick();
            }
        }

        if (whoValue == null) {
            xfAll.performClick();
        } else if (whoValue != null) {
            if (whoValue.equals("0")) {
                xfAa.performClick();
            }
            if (whoValue.equals("1")) {
                xfFree.performClick();
            }
        }

        if (timeValue == null) {
            timeAll.performClick();
        } else if (timeValue != null) {
            if (timeValue.equals("1")) {
                timeToday.performClick();
            }
            if (timeValue.equals("2")) {
                timeTomorrow.performClick();
            }
            if (timeValue.equals("3")) {
                timeLater.performClick();
            }
        }

        init.compareAndSet(false, true);
    }

    @Click
    void sexAll(View view) {
        showIcon(view);

        date_usex = "-1";
        date_sex = "-1";
    }

    @Click
    void boyToGirl(View view) {
        showIcon(view);

        date_usex = "1";
        date_sex = "0";
    }

    @Click
    void girlToBoy(View view) {
        showIcon(view);

        date_usex = "0";
        date_sex = "1";

    }

    @Click
    void boyToBoy(View view) {
        showIcon(view);

        date_usex = "1";
        date_sex = "1";

    }

    @Click
    void girlToGirl(View view) {
        showIcon(view);

        date_usex = "0";
        date_sex = "0";

    }

    @Click
    void xfAll(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_whopay = "-1";
    }

    @Click
    void xfAa(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_whopay = "0";
    }

    @Click
    void xfFree(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_whopay = "1";
    }


    @Click
    void timeAll(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_dtime = "-1";
    }

    @Click
    void timeToday(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_dtime = "1";
    }

    @Click
    void timeTomorrow(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_dtime = "2";
    }

    @Click
    void timeLater(View view) {
        if (!checkVip()) {
            return;
        }
        showIcon(view);

        date_dtime = "3";
    }


    @OptionsItem
    public void ok() {
        try {
            saveData(date_usex, DateListApi.DATE_USEX);
            saveData(date_sex, DateListApi.DATE_SEX);
            saveData(date_whopay, DateListApi.DATE_WHOPAY);
            saveData(date_dtime, DateListApi.DATE_DTIME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setResult(RESULT_OK);
        scrollToFinishActivity();
    }

    public void saveData(String tempKey, String sqlKey) {
        try {
            if (tempKey != null) {
                if (tempKey.equals("-1")) {
                    filterDao.delete(filterDao.deleteBuilder().where().eq(FilterBean.KEY, sqlKey).prepareDelete());
                } else {
                    filterDao.delete(filterDao.deleteBuilder().where().eq(FilterBean.KEY, sqlKey).prepareDelete());

                    FilterBean filterBean1 = new FilterBean(sqlKey, tempKey);
                    filterDao.create(filterBean1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkVip() {
        if(init.get()) {
            User user = getHelper().getUserInfo();
            if (user.getIsvip() == 0) {
                Util.goVip(this);
                return false;
            }
        }
        return true;
    }

    public void showIcon(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View icon = ((ViewGroup) parent.getChildAt(i)).getChildAt(1);
            icon.setVisibility(View.GONE);
        }
        View icon = ((ViewGroup) view).getChildAt(1);
        icon.setVisibility(View.VISIBLE);
    }
}

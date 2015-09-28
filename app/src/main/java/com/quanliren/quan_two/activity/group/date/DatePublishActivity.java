package com.quanliren.quan_two.activity.group.date;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.a.mirko.android.datetimepicker.date.DatePickerDialog;
import com.a.mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.a.mirko.android.datetimepicker.time.RadialPickerLayout;
import com.a.mirko.android.datetimepicker.time.TimePickerDialog;
import com.a.mirko.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.location.GDLocation;
import com.quanliren.quan_two.activity.location.ILocationImpl;
import com.quanliren.quan_two.activity.public_comments.ChoseLocationActivity_;
import com.quanliren.quan_two.activity.public_comments.RestaurantListActivity_;
import com.quanliren.quan_two.bean.Area;
import com.quanliren.quan_two.bean.BusinessBean;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@EActivity(R.layout.date_publish_last)
public class DatePublishActivity extends BaseActivity implements ILocationImpl {
    public static final int PLACE_SELECT = 33;
    public static final String TAG = "DatePublishActivity";
    @ViewById
    TextView title_btn, money_btn, time_btn, place_eat, want_btn, place_change;
    @ViewById
    View money_ll, want_ll;
    @ViewById
    ImageView want_line;
    @ViewById
    EditText remark_et;
    @ViewById
    Button publish_date;
    @ViewById
    RadioButton girl_boy, boy_girl, everyone;
    @ViewById
    RadioButton me_give_money, you_me;
    @ViewById
    DiscreteSeekBar coin_bar;

    @ViewById
    RadioGroup sex_btn;
    @ViewById
    RadioGroup xiaofei_btn;
    String business_id = "";
    String longitude;
    String latitude;
    String busurl;
    String cityid;

    @Override
    protected void onStart() {
        super.onStart();
//        getSupportActionBar().setTitle("偷偷约");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String[] title = {"约人吃饭", "约看电影", "临时情侣"};
    String[] want = {"安慰老妈", "阻击相亲", "充场面", "试着交往"};
    String[] money = {"5", "10", "20", "30", "40", "50", "60", "70",
            "80", "90", "100"};

    GDLocation location;
    int sex = 0;
    RequestParams rp;
    boolean isOk = true;

    //    @OptionsItem
    @Click(R.id.publish_date)
    void ok() {
        if (!isOk) {
            return;
        }
//        isOk=false;
        int dtype = Arrays.asList(title)
                .indexOf(title_btn.getText().toString());
        if (dtype < 0) {
            showCustomToast("请选择题目");
            return;
        } else {
            if ("约人吃饭".equals(title_btn.getText().toString())) {
                dtype = 1;
            } else if ("约看电影".equals(title_btn.getText().toString())) {
                dtype = 2;
            } else if ("临时情侣".equals(title_btn.getText().toString())) {
                dtype = 5;
            }
        }
        int aim = -1;
        if (dtype == 5) {
            aim = Arrays.asList(want).indexOf(want_btn.getText().toString());
            if (aim == -1) {
                showCustomToast("请选择目的");
                return;
            }
        }
        String time = time_btn.getText().toString();
        if (time.equals("")) {
            showCustomToast("请选择时间");
            return;
        }

        String place = "";
        if (dtype == 1 || dtype == 2) {
            place = place_eat.getText().toString().trim();
        } else {
            place = place_eat.getText().toString().trim();
            business_id = "";
        }

        if (place.equals("")) {
            showCustomToast("请选择地点");
            return;
        }
        int sex = 0;
        int xf = 0;
        int ctype = 2;
        int coin = coin_bar.getProgress();
        int sexButtonId = sex_btn.getCheckedRadioButtonId();
        switch (sexButtonId) {
            case R.id.boy_girl:
                sex = 0;
                break;
            case R.id.girl_boy:
                sex = 1;
                break;
            case R.id.everyone:
                sex = 2;
                break;
        }
        int xfButtonId = xiaofei_btn.getCheckedRadioButtonId();
        switch (xfButtonId) {
            case R.id.me_give_money:
                xf = 1;
                break;
            case R.id.you_me:
                xf = 0;
                break;
        }

        String remark = remark_et.getText().toString();
        isOk = false;
        rp = getAjaxParams();
        rp.put("dtype", dtype);
        if (aim > -1)
            rp.put("aim", aim);
        rp.put("dtime", time);
        rp.put("address", place);
        rp.put("peoplenum", 1);
        rp.put("objsex", sex);
        rp.put("whopay", xf);
        rp.put("ctype", ctype);
        rp.put("coin", coin);
        rp.put("busid", "");
        rp.put("busid", business_id);
        rp.put("remark", remark);
        if (dtype == 1 || dtype == 2) {
            rp.put("longitude", longitude);
            rp.put("latitude", latitude);
            rp.put("busurl", busurl);
            rp.put("cityid", cityid);
            LogUtil.d("===========cityid", cityid);
        } else {
            rp.put("longitude", ac.cs.getLng());
            rp.put("latitude", ac.cs.getLat());
            rp.put("cityid", ac.cs.getLocationID());
        }
        LogUtil.d("==========busurl", rp.toString());

        customShowDialog("正在定位");
        location.startLocation();
    }

    public int checkRadio() {
        int xf = Arrays.asList(money).indexOf(money_btn.getText().toString());
        if (xf > -1) {
            return Integer.valueOf(money[xf]);
        }
        return -1;
    }

    @AfterViews
    void initView() {
        cityid = ac.cs.getLocationID();
        setTitleTxt("偷偷约");
        setLeftIcon(R.drawable.actionbar_homeasup_indicator);
        location = new GDLocation(this, this, false);
    }

    @Click
    void left() {
        finish();
    }

    @Click
    void place_eat() {
        if ("约人吃饭".equals(title_btn.getText().toString())) {
            RestaurantListActivity_.intent(this).extra("category", "美食").startForResult(PLACE_SELECT);
        } else if ("约看电影".equals(title_btn.getText().toString())) {
            RestaurantListActivity_.intent(this).extra("category", "电影").startForResult(PLACE_SELECT);
        } else if ("临时情侣".equals(title_btn.getText().toString())) {
            ChoseLocationActivity_.intent(this).startForResult(11);
        }

    }

    @OnActivityResult(11)
    void onLocationResult(int result, Intent data) {
        if (result == 11) {
            Area a = (Area) data.getSerializableExtra("area");
            place_eat.setText(a.name.replace("市", ""));
        }
    }

    @OnActivityResult(PLACE_SELECT)
    void onFilterResult(int result, Intent data) {
        if (result == 33) {
            BusinessBean bb = (BusinessBean) data.getSerializableExtra("bean");
            place_eat.setText(bb.getName());
            business_id = bb.getBusiness_id() + "";
            latitude = bb.getLatitude() + "";
            longitude = bb.getLongitude() + "";
            busurl = bb.getBusiness_url();
            cityid = bb.getCityId();
        }
    }

    @Click({R.id.title_btn, R.id.want_btn, R.id.sex_btn, R.id.want_btn,
            R.id.money_btn, R.id.time_btn})
    void Click(final View v) {
        switch (v.getId()) {
            case R.id.time_btn:
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                DatePickerDialog datePickerDialog = DatePickerDialog
                        .newInstance(dateListener, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), datePickerDialog.getMaxYear());
                datePickerDialog.show(getSupportFragmentManager(), "");
                break;
            default:
                String[] str = null;
                switch (v.getId()) {
                    case R.id.title_btn:
                        str = title;
                        break;
                    case R.id.want_btn:
                        str = want;
                        break;
                    case R.id.money_btn:
                        str = money;
                        break;
                }
                final String[] cstr = str;
                AlertDialog dialog = new AlertDialog.Builder(this).setItems(str,
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((TextView) v).setText(cstr[which]);

                                if (v.getId() == R.id.title_btn) {
                                    if ("约人吃饭".equals(cstr[which]) || "约看电影".equals(cstr[which]) || "临时情侣".equals(cstr[which])) {
//                                        place_et.setVisibility(View.GONE);
                                        if ("临时情侣".equals(cstr[which])) {
                                            place_change.setText("城市");
                                            want_ll.setVisibility(View.VISIBLE);
                                            want_line.setVisibility(View.VISIBLE);
                                        } else if ("约人吃饭".equals(cstr[which])) {
                                            place_change.setText("餐厅");
                                            want_line.setVisibility(View.GONE);
                                            want_ll.setVisibility(View.GONE);
                                        } else if ("约看电影".equals(cstr[which])) {
                                            place_change.setText("影院");
                                            want_line.setVisibility(View.GONE);
                                            want_ll.setVisibility(View.GONE);
                                        }
                                        if (!place_eat.getText().toString().equals(cstr[which])) {
                                            place_eat.setText("");
                                        }
                                        place_eat.setVisibility(View.VISIBLE);
                                        closeInput();
                                    } else {
                                        want_ll.setVisibility(View.GONE);
//                                        place_et.setVisibility(View.VISIBLE);
                                        place_eat.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
        }

    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private static String pad2(int c) {
        if (c == 12)
            return String.valueOf(c);
        if (c == 00)
            return String.valueOf(c + 12);
        if (c > 12)
            return String.valueOf(c - 12);
        else
            return String.valueOf(c);
    }

    StringBuilder timeSb = new StringBuilder();

    OnTimeSetListener timeListener1 = new OnTimeSetListener() {

        @Override
        public void onTimeSet(RadialPickerLayout view,
                              int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            if (!time_btn.getText().equals("")) {
                try {
                    Date date = Util.fmtDate.parse(time_btn.getText().toString());
                    Calendar cdate = Calendar.getInstance();
                    cdate.setTime(date);
                    dateAndTime.set(Calendar.YEAR, cdate.get(Calendar.YEAR));
                    dateAndTime.set(Calendar.MONTH, cdate.get(Calendar.MONTH));
                    dateAndTime.set(Calendar.DAY_OF_MONTH, cdate.get(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (calendar.getTime().after(dateAndTime.getTime())) {
                showCustomToast("约会时间不能小于当前时间！");
                return;
            }
            timeSb.append(" ").append(pad(hourOfDay)).append(":")
                    .append(pad(minute));
            time_btn.setText(timeSb.toString());
        }
    };

    OnDateSetListener dateListener = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePickerDialog dialog, int year,
                              int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (calendar.getTime().after(dateAndTime.getTime())) {
                showCustomToast("约会时间不能小于当前时间！");
                return;
            }
            if (calendar.getTime().before(dateAndTime.getTime())) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 15);
                if (calendar.getTime().before(dateAndTime.getTime())) {
                    showCustomToast("只能发布15天内约会!");
                    return;
                }

            }
            timeSb = new StringBuilder();
            timeSb.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
            TimePickerDialog timePickerDialog24h = TimePickerDialog
                    .newInstance(timeListener1, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar
                            .get(Calendar.MINUTE), true);
            timePickerDialog24h.show(getSupportFragmentManager(), "");
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isOk = true;
    }

    @Override
    public void onLocationSuccess() {
        customDismissDialog();
        ac.finalHttp.post(URL.PUB_DATA, rp, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                customShowDialog("正在上传");
            }

            @Override
            public void onFailure() {
                customDismissDialog();
                showIntentErrorToast();
            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int status = response.getInt(URL.STATUS);
                    switch (status) {
                        case 0:
                            showCustomToast("发布成功");
                            setResult(RESULT_OK);
                            finish();
                            isOk = false;
                            break;
                        case 1:
                            String info = response.getJSONObject(URL.RESPONSE).getString(URL.INFO);
                            if (info.indexOf("会员") > -1) {
//                                new android.support.v7.app.AlertDialog.Builder(DatePublishActivity.this)
//                                        .setMessage(info)
//                                        .setTitle("提示")
//                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface arg0, int arg1) {
//                                            }
//                                        })
//                                        .setPositiveButton("成为会员",
//                                                new DialogInterface.OnClickListener() {
//
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog,
//                                                                        int which) {
//                                                        ShopVip_.intent(DatePublishActivity.this).start();
//                                                    }
//                                                }).create().show();
                                Util.goVip(DatePublishActivity.this, info);
                            } else {
                                showFailInfo(response);
                            }
                            isOk = true;
                            break;
                        default:
                            isOk = true;
                            showFailInfo(response);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    customDismissDialog();
                }
            }
        });
    }

    @Override
    public void onLocationFail() {
        showCustomToast("定位失败");
        isOk = true;
    }

}

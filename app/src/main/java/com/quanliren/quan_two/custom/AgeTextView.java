package com.quanliren.quan_two.custom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.a.mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.util.Util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AgeTextView extends TextView {

    Context context;

    public AgeTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AgeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        setOnClickListener(ageClick);
    }

    OnClickListener ageClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
//			Calendar cal=Calendar.getInstance();
//			new DatePickerDialog(context, d, (cal.get(Calendar.YEAR)),
//					cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (!getText().toString().equals("")) {
                try {
                    Date date = Util.fmtDate.parse(getText().toString());
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Date date = Util.fmtDate.parse("1990-12-08");
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
//			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//				DatePickerDialog dialog_date = new DatePickerDialog(
//						context, d, calendar.get(Calendar.YEAR),
//						calendar.get(Calendar.MONTH),
//						calendar.get(Calendar.DAY_OF_MONTH));
//				dialog_date.show();
//			} else {
            com.a.mirko.android.datetimepicker.date.DatePickerDialog datePickerDialog = com.a.mirko.android.datetimepicker.date.DatePickerDialog
                    .newInstance(dateListener, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setYearRange(Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR) - 100,
                    Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR));
            datePickerDialog.show(((BaseActivity) context).getSupportFragmentManager(), "");
        }
//		}
    };

    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            Calendar cal = Calendar.getInstance();
            if (cal.getTime().before(dateAndTime.getTime())) {
                dateAndTime.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                dateAndTime.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                dateAndTime.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
            }

            updateLabel();
        }
    };

    OnDateSetListener dateListener = new OnDateSetListener() {

        @Override
        public void onDateSet(
                com.a.mirko.android.datetimepicker.date.DatePickerDialog dialog,
                int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            d.onDateSet(null, year, monthOfYear, dayOfMonth);
        }
    };

    private void updateLabel() {
        try {
            String age = Util.fmtDate.format(dateAndTime.getTime());
            setText(age);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

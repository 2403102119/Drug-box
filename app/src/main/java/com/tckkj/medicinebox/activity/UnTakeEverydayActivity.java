package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.util.StringUtil;

/*
* 非日服
* */
public class UnTakeEverydayActivity extends BaseActivity {
    private LinearLayout ll_monday, ll_tuesday, ll_wednesday, ll_thursday, ll_friday, ll_saturday, ll_sunday;
    private CheckBox cb_monday, cb_tuesday, cb_wednesday, cb_thursday, cb_friday, cb_saturday, cb_sunday;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_un_take_everyday);

        title.setText(getString(R.string.not_everyday_take));
        rightTxt.setText(getString(R.string.save));
        rightTxt.setVisibility(View.VISIBLE);

        ll_monday = findViewById(R.id.ll_monday);
        ll_tuesday = findViewById(R.id.ll_tuesday);
        ll_wednesday = findViewById(R.id.ll_wednesday);
        ll_thursday = findViewById(R.id.ll_thursday);
        ll_friday = findViewById(R.id.ll_friday);
        ll_saturday = findViewById(R.id.ll_saturday);
        ll_sunday = findViewById(R.id.ll_sunday);
        cb_monday = findViewById(R.id.cb_monday);
        cb_tuesday = findViewById(R.id.cb_tuesday);
        cb_wednesday = findViewById(R.id.cb_wednesday);
        cb_thursday = findViewById(R.id.cb_thursday);
        cb_friday = findViewById(R.id.cb_friday);
        cb_saturday = findViewById(R.id.cb_saturday);
        cb_sunday = findViewById(R.id.cb_sunday);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);

        ll_monday.setOnClickListener(this);
        ll_tuesday.setOnClickListener(this);
        ll_wednesday.setOnClickListener(this);
        ll_thursday.setOnClickListener(this);
        ll_friday.setOnClickListener(this);
        ll_saturday.setOnClickListener(this);
        ll_sunday.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            String weekdays = getIntent().getStringExtra("medicineTime");
            if (getString(R.string.everyday).equals(weekdays)){
                cb_sunday.setChecked(true);
                cb_monday.setChecked(true);
                cb_tuesday.setChecked(true);
                cb_wednesday.setChecked(true);
                cb_thursday.setChecked(true);
                cb_friday.setChecked(true);
                cb_saturday.setChecked(true);
            }else if (getString(R.string.is_not_set).equals(weekdays)){
                cb_sunday.setChecked(false);
                cb_monday.setChecked(false);
                cb_tuesday.setChecked(false);
                cb_wednesday.setChecked(false);
                cb_thursday.setChecked(false);
                cb_friday.setChecked(false);
                cb_saturday.setChecked(false);
            }else {
                String weekday[] = weekdays.split(getString(R.string.dot));
                for (int i = 0; i < weekday.length; i++) {
                    setCheckWeekdays(weekday[i]);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rightTxt:
                String weekdays = getCheckWeekdays();
                Intent intent = new Intent();
                intent.putExtra("weekdays", weekdays);
                setResult(222, intent);
                finish();
                break;
            case R.id.ll_monday:
                if (cb_monday.isChecked()) {
                    cb_monday.setChecked(false);
                }else {
                    cb_monday.setChecked(true);
                }
                break;
            case R.id.ll_tuesday:
                if (cb_tuesday.isChecked()) {
                    cb_tuesday.setChecked(false);
                }else {
                    cb_tuesday.setChecked(true);
                }
                break;
            case R.id.ll_wednesday:
                if (cb_wednesday.isChecked()) {
                    cb_wednesday.setChecked(false);
                }else {
                    cb_wednesday.setChecked(true);
                }
                break;
            case R.id.ll_thursday:
                if (cb_thursday.isChecked()) {
                    cb_thursday.setChecked(false);
                }else {
                    cb_thursday.setChecked(true);
                }
                break;
            case R.id.ll_friday:
                if (cb_friday.isChecked()) {
                    cb_friday.setChecked(false);
                }else {
                    cb_friday.setChecked(true);
                }
                break;
            case R.id.ll_saturday:
                if (cb_saturday.isChecked()) {
                    cb_saturday.setChecked(false);
                }else {
                    cb_saturday.setChecked(true);
                }
                break;
            case R.id.ll_sunday:
                if (cb_sunday.isChecked()) {
                    cb_sunday.setChecked(false);
                }else {
                    cb_sunday.setChecked(true);
                }
                break;
        }
    }

    /*
    * 根据需要设置选中的周天
    * */
    private void setCheckWeekdays(String weekday){
        if (getString(R.string.Monday).equals(weekday)){
            cb_monday.setChecked(true);
        }else if (getString(R.string.Tuesday).equals(weekday)){
            cb_tuesday.setChecked(true);
        }else if (getString(R.string.Wednesday).equals(weekday)){
            cb_wednesday.setChecked(true);
        }else if (getString(R.string.Thursday).equals(weekday)){
            cb_thursday.setChecked(true);
        }else if (getString(R.string.Friday).equals(weekday)){
            cb_friday.setChecked(true);
        }else if (getString(R.string.Saturday).equals(weekday)){
            cb_saturday.setChecked(true);
        }else if (getString(R.string.Sunday).equals(weekday)){
            cb_sunday.setChecked(true);
        }
    }

    /*
    * 获取周天选择状态
    * */
    private String getCheckWeekdays() {
        if (cb_monday.isChecked() && cb_saturday.isChecked() && cb_wednesday.isChecked() &&
                cb_thursday.isChecked() && cb_tuesday.isChecked() && cb_friday.isChecked() &&
                cb_sunday.isChecked()){
            return getString(R.string.everyday);
        }

        if (!cb_monday.isChecked() && !cb_saturday.isChecked() && !cb_wednesday.isChecked() &&
                !cb_thursday.isChecked() && !cb_tuesday.isChecked() && !cb_friday.isChecked() &&
                !cb_sunday.isChecked()){
            return getString(R.string.is_not_set);
        }

        StringBuffer sbStr = new StringBuffer();

        if (cb_monday.isChecked()){
            sbStr.append(getString(R.string.monday_dot));
        }
        if (cb_tuesday.isChecked()){
            sbStr.append(getString(R.string.tuesday_dot));
        }
        if (cb_wednesday.isChecked()){
            sbStr.append(getString(R.string.wednesday_dot));
        }
        if (cb_thursday.isChecked()){
            sbStr.append(getString(R.string.thursday_dot));
        }
        if (cb_friday.isChecked()){
            sbStr.append(getString(R.string.friday_dot));
        }
        if (cb_saturday.isChecked()){
            sbStr.append(getString(R.string.saturday_dot));
        }
        if (cb_sunday.isChecked()){
            sbStr.append(getString(R.string.sunday_dot));
        }
        String weekdayStr = "";

        if (!StringUtil.isSpace(sbStr.toString())) {
            weekdayStr = sbStr.toString().substring(0, sbStr.toString().length() - 1);
        }

        return weekdayStr;
    }
}

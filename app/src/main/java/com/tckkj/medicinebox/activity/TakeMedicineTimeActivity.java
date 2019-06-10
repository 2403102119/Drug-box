package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.view.wheelview.WheelView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
* 服药时间
* */
public class TakeMedicineTimeActivity extends BaseActivity {
    private WheelView wheel_minute, wheel_second;
    private List<String> minuteList = new ArrayList<>(), secondList = new ArrayList<>();
    private String hour = "00", minute = "00";
    private int hourPosition = 0, minutePosition = 0;
    private boolean isAdd = true;      //是否是添加时间   false：即为修改

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_take_medicine_time);

        title.setText(R.string.taking_medicine_time);

        wheel_minute = findViewById(R.id.wheel_minute);
        wheel_second = findViewById(R.id.wheel_second);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            isAdd = getIntent().getBooleanExtra("iaAdd", true);
            if (!isAdd){
                hour = getIntent().getStringExtra("hour");
                minute = getIntent().getStringExtra("minute");
            }else {
                String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                String[] timeHM = time.split(":");
                hour = timeHM[0];
                minute = timeHM[1];
            }
        }else {
            String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
            String[] timeHM = time.split(":");
            hour = timeHM[0];
            minute = timeHM[1];
        }

        if (isAdd){
            rightTxt.setText(R.string.add);
        }else {
            rightTxt.setText(R.string.confirm);
        }
        rightTxt.setVisibility(View.VISIBLE);

        for (int i = 0; i < 24; i++) {
            minuteList.add(new DecimalFormat("00").format(i));
            if (hour.equals(new DecimalFormat("00").format(i))){
                hourPosition = i;
            }
        }

        for (int i = 0; i < 60; i++) {
            secondList.add(new DecimalFormat("00").format(i));
            if (minute.equals(new DecimalFormat("00").format(i))){
                minutePosition = i;
            }
        }

        wheel_minute.setItems(minuteList, hourPosition);
        wheel_second.setItems(secondList, minutePosition);
        wheel_minute.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                hour = item;
            }
        });
        wheel_second.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                minute = item;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rightTxt:
                Intent intent = new Intent();
                intent.putExtra("time", hour+ minute);
                setResult(222, intent);
                finish();
                break;
        }
    }
}

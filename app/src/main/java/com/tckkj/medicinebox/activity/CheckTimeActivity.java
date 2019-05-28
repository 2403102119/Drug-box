package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.adapter.CalendarAdapter;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.util.CalendarUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.view.NiceRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 查看时段
 * */
public class CheckTimeActivity extends BaseActivity {
    private TextView tv_calendar_title;
    private NiceRecyclerView nrv_calendar;
    private CalendarAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private int checkPosition;          //当前选中位置，默认为本日，点击改变
    public final static int SELECT_RESULT = 32;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_check_time);

        title.setText(getString(R.string.check_the_time));
        rightTxt.setText(getString(R.string.confirm));
        rightTxt.setVisibility(View.VISIBLE);

        tv_calendar_title = findViewById(R.id.tv_calendar_title);
        nrv_calendar = findViewById(R.id.nrv_calendar);

        tv_calendar_title.setText(CalendarUtil.getYear() + getString(R.string.year) + CalendarUtil.getMonth() + getString(R.string.month));
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //获取本月1号是周几
        int week = CalendarUtil.getWeekNoFormat(CalendarUtil.getFirstDayOfMonth());
        //本月1号之前添加空数据
        for (int i = 1; i < week; i++) {
            Map map = new HashMap();
            map.put("isCheck", false);
            map.put("dayNum", "");
            list.add(map);
        }

        //获取本月天数
        String monthDayStr = CalendarUtil.getTwoDay(CalendarUtil.getFinalDayOnMonth(),CalendarUtil.getFirstDayOfMonth());
        int monthDays = Integer.parseInt(monthDayStr) + 1;
        //获取当天是本月多少号
        String today = CalendarUtil.getNowTime("dd");
        final int todayNum = Integer.parseInt(today);

        for (int i = 1; i < monthDays + 1; i++) {
            Map map = new HashMap();
            if (todayNum == i){
                map.put("isCheck", true);
                checkPosition = list.size();
            }else {
                map.put("isCheck", false);
            }
            map.put("dayNum", i + "");
            list.add(map);
        }

        adapter = new CalendarAdapter(this, list);
        nrv_calendar.setAdapter(adapter);
        adapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                if (!StringUtil.isSpace((String) list.get(position).get("dayNum"))){
                    if (Integer.parseInt((String) list.get(position).get("dayNum")) <= todayNum){

                        Map map1 = list.get(checkPosition);
                        map1.put("isCheck", false);
                        list.set(checkPosition, map1);

                        Map map2 = list.get(position);
                        map2.put("isCheck", true);
                        list.set(position, map2);

                        checkPosition = position;
                        adapter.notifyDataSetChanged();
                    }else {
                        toast(getString(R.string.check_remind));
                    }
                }

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
                String checkDay = (String) list.get(checkPosition).get("dayNum");
//                if (StringUtil.isSpace(checkDay)){
//                    toast("请选择要查看的时段");
//                    break;
//                }
                Intent intent = new Intent();
                intent.putExtra("checkDay", CalendarUtil.getYear() + "-" + CalendarUtil.getMonth() + "-" + checkDay);
                setResult(SELECT_RESULT, intent);
                finish();
                break;
        }
    }
}

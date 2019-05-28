package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.adapter.RemindTimeAdapter;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.view.ClearEditText;
import com.tckkj.medicinebox.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
 * 提醒设置
 * */
public class RemindSettingActivity extends BaseActivity {
    private CheckBox cb_voice_remind, cb_screen_remind, cb_other_remind;
    private LinearLayout ll_voice_remind, ll_screen_remind, ll_volume_setting, ll_other_remind;
    private TextView tv_add_remind_time;
    private ClearEditText cet_input_remind_content;

    private NiceRecyclerView nrv_remind_time;
    private NestedScrollView nsv_remind_setting;

    private List<String> list = new ArrayList<>();
    private RemindTimeAdapter adapter;

    private Handler handler = new Handler();

    private int curPosition = 0;                //点击编辑的位置

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_remind_setting);

        title.setText(R.string.remind_set);
        rightTxt.setText(R.string.save);
        rightTxt.setVisibility(View.VISIBLE);

        tv_add_remind_time = findViewById(R.id.tv_add_remind_time);
        cb_voice_remind = findViewById(R.id.cb_voice_remind);
        cb_screen_remind = findViewById(R.id.cb_screen_remind);
        cb_other_remind = findViewById(R.id.cb_other_remind);

        ll_voice_remind = findViewById(R.id.ll_voice_remind);
        ll_screen_remind = findViewById(R.id.ll_screen_remind);
        ll_volume_setting = findViewById(R.id.ll_volume_setting);
        ll_other_remind = findViewById(R.id.ll_other_remind);
        cet_input_remind_content = findViewById(R.id.cet_input_remind_content);
        nrv_remind_time = findViewById(R.id.nrv_remind_time);
        nsv_remind_setting = findViewById(R.id.nsv_remind_setting);

        if (cb_other_remind.isChecked()){
            cet_input_remind_content.setEnabled(true);
        }else {
            cet_input_remind_content.setEnabled(false);
        }
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
        tv_add_remind_time.setOnClickListener(this);
        cb_voice_remind.setOnClickListener(this);
        cb_screen_remind.setOnClickListener(this);
        cb_other_remind.setOnClickListener(this);
        ll_voice_remind.setOnClickListener(this);
        ll_screen_remind.setOnClickListener(this);
        ll_volume_setting.setOnClickListener(this);
        ll_other_remind.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new RemindTimeAdapter(this, list);
        nrv_remind_time.setAdapter(adapter);
        adapter.setOnItemClickListener(new RemindTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                curPosition = position;
                String timeStr = list.get(position);
                String time[] = timeStr.split(":");

                Intent remindIntent = new Intent(RemindSettingActivity.this, TakeMedicineTimeActivity.class);
                remindIntent.putExtra("iaAdd", false);
                remindIntent.putExtra("hour", time[0]);
                remindIntent.putExtra("minute", time[1]);
                startActivityForResult(remindIntent, 121);
            }

            @Override
            public void onDelete(int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                if (list.size() == 0){
                    tv_add_remind_time.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < list.size(); i++) {
                    String time[] = list.get(i).split(":");
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
                break;
            case R.id.tv_add_remind_time:       //添加时间提醒
                Intent remindIntent = new Intent(RemindSettingActivity.this, TakeMedicineTimeActivity.class);
                remindIntent.putExtra("iaAdd", true);
                startActivityForResult(remindIntent, 111);
                break;
            case R.id.ll_volume_setting:            //音量设置界面
                Intent volumeIntent = new Intent(RemindSettingActivity.this, VolumeSettingActivity.class);
                volumeIntent.putExtra("isCheck",cb_voice_remind.isChecked());
                startActivity(volumeIntent);
                break;
            case R.id.cb_voice_remind:              //语音提醒
            case R.id.ll_voice_remind:
                if (cb_voice_remind.isChecked()){
                    cb_voice_remind.setChecked(false);
                }else {
                    cb_voice_remind.setChecked(true);
                }
                break;
            case R.id.cb_screen_remind:             //屏幕提醒
            case R.id.ll_screen_remind:
                if (cb_screen_remind.isChecked()){
                    cb_screen_remind.setChecked(false);
                }else {
                    cb_screen_remind.setChecked(true);
                }
                break;
            case R.id.cb_other_remind:              //其他提醒
            case R.id.ll_other_remind:
                if (cb_other_remind.isChecked()){
                    cb_other_remind.setChecked(false);
                    cet_input_remind_content.setEnabled(false);
                }else {
                    cb_other_remind.setChecked(true);
                    cet_input_remind_content.setEnabled(true);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 222){
            if (null != data){
                String time = data.getStringExtra("time");
                list.add(time);
                if (list.size() > 0){
                    tv_add_remind_time.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        nsv_remind_setting.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }

        if (requestCode == 121 && resultCode == 222){
            if (null != data){
                String time = data.getStringExtra("time");
                list.set(curPosition, time);
                adapter.notifyDataSetChanged();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        nsv_remind_setting.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
    }
}

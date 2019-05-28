package com.tckkj.medicinebox.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
/*
 * 音量设置
 * */
public class VolumeSettingActivity extends BaseActivity {
    private int volumeLevel;
    private int max;

    private TextView tv_volume_percent;
    private ImageView img_volume_add, img_volume_minus;
    private Button btn_volume_five, btn_volume_four, btn_volume_three, btn_volume_two, btn_volume_one;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_volume_setting);

        title.setText(R.string.volume_setting);

        tv_volume_percent = findViewById(R.id.tv_volume_percent);
        img_volume_add = findViewById(R.id.img_volume_add);
        img_volume_minus = findViewById(R.id.img_volume_minus);
        btn_volume_one = findViewById(R.id.btn_volume_one);
        btn_volume_two = findViewById(R.id.btn_volume_two);
        btn_volume_three = findViewById(R.id.btn_volume_three);
        btn_volume_four = findViewById(R.id.btn_volume_four);
        btn_volume_five = findViewById(R.id.btn_volume_five);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        img_volume_add.setOnClickListener(this);
        img_volume_minus.setOnClickListener(this);
        btn_volume_one.setOnClickListener(this);
        btn_volume_two.setOnClickListener(this);
        btn_volume_three.setOnClickListener(this);
        btn_volume_four.setOnClickListener(this);
        btn_volume_five.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            if (getIntent().getBooleanExtra("isCheck", false)){
                volumeLevel = 1;
            }else {
                volumeLevel = 0;
            }
            setVolumeLevel(volumeLevel);
        }
//        getVolumeLevel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.img_volume_add:
                if (volumeLevel < 5){
                    volumeLevel ++;
                    setVolumeLevel(volumeLevel);
                }else {
                    toast(getString(R.string.max_volume_warning));
                }
                break;
            case R.id.img_volume_minus:
                if (volumeLevel > 0){
                    volumeLevel --;
                    setVolumeLevel(volumeLevel);
                }else {
                    toast(getString(R.string.min_volume_warning));
                }
                break;
            case R.id.btn_volume_one:
                setVolumeLevel(1);
                volumeLevel = 1;
                break;
            case R.id.btn_volume_two:
                setVolumeLevel(2);
                volumeLevel = 2;
                break;
            case R.id.btn_volume_three:
                setVolumeLevel(3);
                volumeLevel = 3;
                break;
            case R.id.btn_volume_four:
                setVolumeLevel(4);
                volumeLevel = 4;
                break;
            case R.id.btn_volume_five:
                setVolumeLevel(5);
                volumeLevel = 5;
                break;
        }
    }

    /*
     * 获取音量等级
     * */
    private void getVolumeLevel() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 获取音乐最大音量
        max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = am.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前音乐音量
        int volumePercent = (int)((double)current/ max * 100 + 0.5);//计算当前音量百分比

        Log.i("11111111", "getVolumeLevel: " + max + "----" + current);

        if (volumePercent > 75){
            if (100 - volumePercent < 13){
                volumeLevel = 4;
            }else {
                volumeLevel = 3;
            }
        } else if (volumePercent > 50) {
            if (75 - volumePercent < 13){
                volumeLevel = 3;
            }else {
                volumeLevel = 2;
            }
        }else if (volumePercent > 25){
            if (50 - volumePercent < 13){
                volumeLevel = 2;
            }else {
                volumeLevel = 1;
            }
        }else {
            if (25 - volumePercent < 13){
                volumeLevel = 1;
            }else {
                volumeLevel = 0;
            }
        }

        Log.i("11111111", "getVolumeLevel: " + volumePercent + "----" + volumeLevel);

        setVolumeLevel(volumeLevel);
    }

    /*
     * 设置音量等级
     * */
    private void setVolumeLevel(int volumeLevel) {
        switch (volumeLevel){
            case 0:
                tv_volume_percent.setText("0%");
                btn_volume_one.setBackgroundResource(R.drawable.shape_low_gray_bottom_5);
                btn_volume_two.setBackgroundResource(R.color.editBg);
                btn_volume_three.setBackgroundResource(R.color.editBg);
                btn_volume_four.setBackgroundResource(R.color.editBg);
                btn_volume_five.setBackgroundResource(R.drawable.shape_low_gray_top_5);
//                setVolume(0);
                break;
            case 1:
                tv_volume_percent.setText("20%");
                btn_volume_one.setBackgroundResource(R.drawable.shape_low_green_bottom_5);
                btn_volume_two.setBackgroundResource(R.color.editBg);
                btn_volume_three.setBackgroundResource(R.color.editBg);
                btn_volume_four.setBackgroundResource(R.color.editBg);
                btn_volume_five.setBackgroundResource(R.drawable.shape_low_gray_top_5);
//                setVolume((int)(max * 0.25 + 0.5));
                break;
            case 2:
                tv_volume_percent.setText("40%");
                btn_volume_one.setBackgroundResource(R.drawable.shape_low_green_bottom_5);
                btn_volume_two.setBackgroundResource(R.color.lowGreen);
                btn_volume_three.setBackgroundResource(R.color.editBg);
                btn_volume_four.setBackgroundResource(R.color.editBg);
                btn_volume_five.setBackgroundResource(R.drawable.shape_low_gray_top_5);
//                setVolume((int)(max * 0.5 + 0.5));
                break;
            case 3:
                tv_volume_percent.setText("60%");
                btn_volume_one.setBackgroundResource(R.drawable.shape_low_green_bottom_5);
                btn_volume_two.setBackgroundResource(R.color.lowGreen);
                btn_volume_three.setBackgroundResource(R.color.lowGreen);
                btn_volume_four.setBackgroundResource(R.color.editBg);
                btn_volume_five.setBackgroundResource(R.drawable.shape_low_gray_top_5);
//                setVolume((int)(max * 0.75 + 0.5));
                break;
            case 4:
                tv_volume_percent.setText("80%");
                btn_volume_one.setBackgroundResource(R.drawable.shape_low_green_bottom_5);
                btn_volume_two.setBackgroundResource(R.color.lowGreen);
                btn_volume_three.setBackgroundResource(R.color.lowGreen);
                btn_volume_four.setBackgroundResource(R.color.lowGreen);
                btn_volume_five.setBackgroundResource(R.drawable.shape_low_gray_top_5);
//                setVolume(max);
                break;
            case 5:
                tv_volume_percent.setText("100%");
                btn_volume_one.setBackgroundResource(R.drawable.shape_low_green_bottom_5);
                btn_volume_two.setBackgroundResource(R.color.lowGreen);
                btn_volume_three.setBackgroundResource(R.color.lowGreen);
                btn_volume_four.setBackgroundResource(R.color.lowGreen);
                btn_volume_five.setBackgroundResource(R.drawable.shape_low_green_top_5);
                break;
        }
    }

    /*
     * 设置音乐音量
     * */
    public void setVolume (int index) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,
    /*修改第二个参数为一个固定的值，就是设置成功。
    下面的方法：获取音乐类型的音频流的最大值*/
                index, AudioManager.FLAG_PLAY_SOUND);
    }
}
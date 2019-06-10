package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.LocalManageUtil;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.SPUtil;

import okhttp3.Call;

/*
 * 语言选择
 * */
public class LanguageSelectActivity extends BaseActivity {
    private LinearLayout ll_language_chinese, ll_language_english;
    private CheckBox cb_language_chinese, cb_language_english;
    //是否从主页跳转过来
    private boolean isFromMain = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_language_select);

        title.setText(R.string.language_select);
        back.setVisibility(View.GONE);
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText(getResources().getString(R.string.save));

        ll_language_chinese = findViewById(R.id.ll_language_chinese);
        ll_language_english = findViewById(R.id.ll_language_english);
        cb_language_chinese = findViewById(R.id.cb_language_chinese);
        cb_language_english = findViewById(R.id.cb_language_english);

    }

    @Override
    protected void initListener() {
        rightTxt.setOnClickListener(this);
        ll_language_chinese.setOnClickListener(this);
        ll_language_english.setOnClickListener(this);
        cb_language_chinese.setOnClickListener(this);
        cb_language_english.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            isFromMain = getIntent().getBooleanExtra("isMain", false);
        }

        if (isFromMain){
            //如果是主页跳转过来
            if ("en".equals(LocalManageUtil.getSetLanguageLocale(this).getLanguage())) {
                cb_language_chinese.setChecked(false);
                cb_language_english.setChecked(true);
            } else {
                cb_language_chinese.setChecked(true);
                cb_language_english.setChecked(false);
            }
        }else {
            //如果是第一次打开应用，不获取语言选择状态，默认系统设置
            if (App.isFirstIn) {
                if ("en".equals(LocalManageUtil.getSetLanguageLocale(this).getLanguage())) {
                    cb_language_chinese.setChecked(false);
                    cb_language_english.setChecked(true);
                } else {
                    cb_language_chinese.setChecked(true);
                    cb_language_english.setChecked(false);
                }
            } else {
                //如果不是第一次打开应用,判断是否登录，如果登录跳转主机选择界面，否则跳转登录界面、
                if (App.islogin) {
                    if (App.serialNumber.equals("1")||App.serialNumber.equals("2")){
                        startActivity(new Intent(LanguageSelectActivity.this, MedicineBoxSettingActivity.class));
                        finish();
                    }else if (App.serialNumber.equals("0")){
                        startActivity(new Intent(LanguageSelectActivity.this, MainEngineConnectActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(LanguageSelectActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTxt:
                if (isFromMain){
                    if (cb_language_english.isChecked()) {
                        SPUtil.saveData(this, "languageType", "en");
                        App.languageType = "en";
                        selectLanguage(1, true);
//                        selectLanguage("2");
                    } else {
                        SPUtil.saveData(this, "languageType", "zh");
                        App.languageType = "zh";
                        selectLanguage(0, true);
//                        selectLanguage("1");
                    }
                }else {
                    if (cb_language_english.isChecked()) {
                        SPUtil.saveData(this, "languageType", "en");
                        App.languageType = "en";
                        selectLanguage(1, false);
                    } else {
                        SPUtil.saveData(this, "languageType", "zh");
                        App.languageType = "zh";
                        selectLanguage(0, false);
                    }
                    App.isFirstIn = false;
                    SPUtil.saveData(this, "isFirstIn", false);
                }
                break;
            case R.id.ll_language_chinese:
            case R.id.cb_language_chinese:
                cb_language_chinese.setChecked(true);
                cb_language_english.setChecked(false);
                break;
            case R.id.ll_language_english:
            case R.id.cb_language_english:
                cb_language_chinese.setChecked(false);
                cb_language_english.setChecked(true);
                break;
        }
    }

    private void selectLanguage(int select, boolean isMain) {
        LocalManageUtil.saveSelectLanguage(this, select);
        if (isMain) {
            MedicineBoxSettingActivity.reStart(this);
        }else {
            LoginActivity.reStart(this);
        }
    }
}

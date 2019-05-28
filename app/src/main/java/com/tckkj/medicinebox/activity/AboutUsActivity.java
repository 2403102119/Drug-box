package com.tckkj.medicinebox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
/*
* 关于我们
* */
public class AboutUsActivity extends BaseActivity {
    private TextView tv_current_version, tv_development_date;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_about_us);

        title.setText(R.string.version);

        tv_current_version = findViewById(R.id.tv_current_version);
        tv_development_date = findViewById(R.id.tv_development_date);

        tv_current_version.setText(getString(R.string.version) + "V1.1.0");
        tv_development_date.setText(getString(R.string.date_of_development) + "2018/08/08");
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}

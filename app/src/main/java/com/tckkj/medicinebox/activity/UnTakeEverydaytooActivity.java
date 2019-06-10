package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
/*
*  选择服药时间界面
*  Author:李迪迦
* date：2019.6.10
* */
public class UnTakeEverydaytooActivity extends BaseActivity {
    private PopupWindow add_item;
    private LinearLayout linearLayout,add_item_lin;
    private Button ll_all_day,ll_not_day;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_un_take_everydaytoo);
        title.setText(getString(R.string.not_everyday_take));
        ll_all_day = findViewById(R.id.ll_all_day);
        ll_not_day = findViewById(R.id.ll_not_day);
    }

    @Override
    protected void initListener() {
         ll_all_day.setOnClickListener(this);
         ll_not_day.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_all_day:
                Intent intent = new Intent();
                intent.putExtra("weekdays", "每天");
                setResult(222, intent);
                finish();
                break;
            case R.id.ll_not_day:
                grouping();
                linearLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_translate_in));
                add_item.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.back:
                finish();
                break;

        }
    }
    public void grouping() {
        add_item = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.activity_add_item, null);
        linearLayout = view.findViewById(R.id.add_popuo);
        add_item.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        add_item.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        add_item.setBackgroundDrawable(new BitmapDrawable());
        add_item.setFocusable(true);
        add_item.setOutsideTouchable(true);
        add_item.setContentView(view);
        add_item_lin = view.findViewById(R.id.add_item_lin);
        final EditText mingzi = view.findViewById(R.id.mingzi);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        add_item_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_item.dismiss();
                linearLayout.clearAnimation();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_item.dismiss();
                linearLayout.clearAnimation();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mingzi.getText();
                Log.i("00000","0000"+mingzi.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("weekdays", mingzi.getText().toString());
                setResult(222, intent);

                add_item.dismiss();
                linearLayout.clearAnimation();
                finish();
            }
        });
    }
}

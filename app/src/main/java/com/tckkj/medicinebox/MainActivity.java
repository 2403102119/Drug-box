package com.tckkj.medicinebox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.fragment.FourFragment;
import com.tckkj.medicinebox.fragment.OneFragment;
import com.tckkj.medicinebox.fragment.ThreeFragment;
import com.tckkj.medicinebox.fragment.TwoFragment;
import com.tckkj.medicinebox.util.MUIToast;
import com.tckkj.medicinebox.view.CommonProgressDialog;

public class MainActivity extends BaseActivity {
    private Fragment oneFragment, twoFragment, threeFragment, fourFragment;
    RadioGroup rg_main;
    RadioButton rb_one,rb_two,rb_three,rb_four;

    private CommonProgressDialog pBar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        rg_main = findView(R.id.rg_main);

        top.setVisibility(View.GONE);

        rb_one = findView(R.id.rb_one);
        rb_two = findView(R.id.rb_two);
        rb_three = findView(R.id.rb_three);
        rb_four = findView(R.id.rb_four);
    }

    @Override
    protected void initListener() {

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_one:
                        setSelect(0);
                        break;
                    case R.id.rb_two:
                        setSelect(1);
                        break;
                    case R.id.rb_three:
                        setSelect(2);
                        break;
                    case R.id.rb_four:
                        setSelect(3);
                        break;
                }
            }
        });

    }


    private int exitFlag;

    @Override
    public void onBackPressed() {
        if (exitFlag==1){
            super.onBackPressed();
            App.finishRealAllActivity();
        }else {
            exitFlag=1;
            handler.sendEmptyMessageDelayed(0x10,2000);
            MUIToast.show(MainActivity.this,"再次按返回键退出");
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            exitFlag=0;
        }
    };


    @Override
    protected void initData() {

        rb_one.setChecked(true);
        setSelect(0);
    }

    public void setSelect(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);//先隐藏所有界面
        switch (i) {
            case 0:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    transaction.add(R.id.fl_main, oneFragment);
                } else {
                    transaction.show(oneFragment);
                    oneFragment.onResume();
                }
                break;
            case 1:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    transaction.add(R.id.fl_main, twoFragment);
                } else {
                    transaction.show(twoFragment);
                }
                break;
            case 2:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    transaction.add(R.id.fl_main, threeFragment);
                } else {
                    transaction.show(threeFragment);
                }
                break;
            case 3:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    transaction.add(R.id.fl_main, fourFragment);
                } else {
                    transaction.show(fourFragment);
                }
                break;
        }
        transaction.commit();
    }

    //用于隐藏界面
    private void hideFragment(FragmentTransaction transaction) {
        if (oneFragment != null) {
            transaction.hide(oneFragment);
        }
        if (twoFragment != null) {
            transaction.hide(twoFragment);
        }
        if (threeFragment != null) {
            transaction.hide(threeFragment);
        }
        if (fourFragment != null) {
            transaction.hide(fourFragment);
        }
    }


}

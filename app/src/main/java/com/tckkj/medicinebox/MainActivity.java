package com.tckkj.medicinebox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.fragment.FourFragment;
import com.tckkj.medicinebox.fragment.OneFragment;
import com.tckkj.medicinebox.fragment.ThreeFragment;
import com.tckkj.medicinebox.fragment.TwoFragment;
import com.tckkj.medicinebox.util.MUIToast;
import com.tckkj.medicinebox.util.SPUtil;
import com.tckkj.medicinebox.view.CommonProgressDialog;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.TagAliasCallback;
/*
* Author:李迪迦
* Date：2019.6.4
* */
public class MainActivity extends BaseActivity {
    private Fragment oneFragment, twoFragment, threeFragment, fourFragment;
    RadioGroup rg_main;
    RadioButton rb_one,rb_two,rb_three,rb_four;

    private CommonProgressDialog pBar;
    private static final String TAG = "获取.成功";

    /*
   给用户设置别名
    */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    Log.i("123456789","11111"+msg.obj);
                    Log.i("11111111111", "gotResult: " + new JPushMessage().toString());
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    SPUtil.saveData(MainActivity.this,"isBindAlias",true);

                    App.isBindAlias = true;
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            toast(logs);
        }
    };

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

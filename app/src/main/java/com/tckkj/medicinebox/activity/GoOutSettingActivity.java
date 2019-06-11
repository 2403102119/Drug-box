package com.tckkj.medicinebox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.InputDialogUtil;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.TimePicker;
import okhttp3.Call;

/*
* 外出设置
* */
public class GoOutSettingActivity extends BaseActivity {
    private LinearLayout ll_back_date, ll_back_time, ll_dispense_medicine_number;
    private TextView tv_current_date, tv_back_date, tv_back_time, tv_dispense_medicine_number,
    tv_host_number, tv_host_name;
    private Button btn_dispense_medicine_immediately, btn_dispense_medicine_save;
    private Dialog dialog;
    private String a,b;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_go_out_setting);

        title.setText(R.string.set_out);
        rightTxt.setText(R.string.cancel);

        ll_back_date = findViewById(R.id.ll_back_date);
        ll_back_time = findViewById(R.id.ll_back_time);
        ll_dispense_medicine_number = findViewById(R.id.ll_dispense_medicine_number);
        tv_current_date = findViewById(R.id.tv_current_date);
        tv_back_date = findViewById(R.id.tv_back_date);
        tv_back_time = findViewById(R.id.tv_back_time);
        tv_dispense_medicine_number = findViewById(R.id.tv_dispense_medicine_number);
        tv_host_number = findViewById(R.id.tv_host_number);
        tv_host_name = findViewById(R.id.tv_host_name);
        btn_dispense_medicine_immediately = findViewById(R.id.btn_dispense_medicine_immediately);
//        btn_dispense_medicine_save = findViewById(R.id.btn_dispense_medicine_save);

        tv_current_date.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        tv_back_date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        tv_back_time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
        ll_back_date.setOnClickListener(this);
        ll_back_time.setOnClickListener(this);
        ll_dispense_medicine_number.setOnClickListener(this);
        btn_dispense_medicine_immediately.setOnClickListener(this);
//        btn_dispense_medicine_save.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        a= getIntent().getStringExtra("number");
        tv_host_number.setText(a);
        b = getIntent().getStringExtra("name");
        tv_host_name.setText(b);
        //判断登录、连接主机、获取配药状态
        /*if (App.islogin){
            if (StringUtil.isSpace(App.hostOid)){
                InputDialogUtil.showHostConnectTipDialog(this);
            }else {
                getDispensingInformation(App.hostOid);
            }
        }else {
            InputDialogUtil.showLoginTipDialog(this);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rightTxt:
                cancelDispensing("");
                break;
            case R.id.ll_back_date:
                dateSetting();
                break;
            case R.id.ll_back_time:
                timeSetting();
                break;
            case R.id.ll_dispense_medicine_number:
                /*final InputDialogUtil nameDialogUtil = new InputDialogUtil(this, true, getString(R.string.dispensing_copies), true, InputType.TYPE_CLASS_NUMBER);
                nameDialogUtil.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_dispense_medicine_number.setText(inputContent);
                        nameDialogUtil.cancel();
                    }
                });
                nameDialogUtil.show();*/
                break;
            case R.id.btn_dispense_medicine_immediately:
                //点击立即配药重新拉取当前时间，并判断当前时间和返回时间的先后
                tv_current_date.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

                String currentTime = tv_current_date.getText().toString();
                String returnDate = tv_back_date.getText().toString() + " " + tv_back_time.getText().toString();
                try {
                    long curTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(currentTime).getTime();
                    long returnTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(returnDate).getTime();
                    if (curTime < returnTime){
                        if (App.islogin){
                            if (StringUtil.isSpace(App.hostOid)){
                                InputDialogUtil.showHostConnectTipDialog(this);
                            }else {
                                outgoingDispensing(currentTime, returnDate, App.hostOid);
                            }
                        }else {
                            InputDialogUtil.showLoginTipDialog(this);
                        }
                    }else {
                        toast("返回时间应晚于当前时间");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            /*case R.id.btn_dispense_medicine_save:
                rightTxt.setVisibility(View.GONE);
                break;*/
        }
    }

    /*
    * 展示提示弹窗
    * */
    private void showTipDialog() {
        dialog = new Dialog(this, R.style.Dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_go_out_tips, null);
        view.findViewById(R.id.tv_tips_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                changePattern("");
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*
    * 设置返回时间
    * */
    private void timeSetting() {
        Calendar calendar = Calendar.getInstance();
        TimePicker timePicker = new TimePicker(this);
        timePicker.setCanLoop(true);
        timePicker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        timePicker.setLabel(getString(R.string.hour), getString(R.string.minute));
        timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String s, String s1) {
                tv_back_time.setText(s + ":" + s1);
            }
        });
        timePicker.show();
    }

    /*
     * 设置返回日期
     * */
    private void dateSetting() {
        Calendar cal= Calendar.getInstance();
        DatePicker datePicker = new DatePicker(this);
        datePicker.setCanLoop(false);
        datePicker.setRangeStart(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));    //设置起始选择时间
        datePicker.setRangeEnd(2100, 12, 31);    //设置选择的结束时间
        datePicker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));             //设置默认显示时间
        datePicker.setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day));
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String s, String s1, String s2) {
                tv_back_date.setText(s + "-" + s1 + "-" + s2);
            }
        });
        datePicker.show();
    }

    /*
     * App15/外出设置配药 >
     * */
    private void outgoingDispensing(final String currentTime, final String returnTime, final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.outgoingDispensing(currentTime, returnTime, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if (1 == data.status){
                                toast(data.message);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

    /*
     * App22/获取配药信息 >
     * */
    private void getDispensingInformation(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getDispensingInformation(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if (1 == data.status){
                                //如果是正在配药状态则显示取消按钮
                                rightTxt.setVisibility(View.VISIBLE);
                                //如果是完成状态则显示弹窗
                                showTipDialog();

                                toast(data.message);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

    /*
     * App23/取消配药 >
     * */
    private void cancelDispensing(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.cancelDispensing(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if (1 == data.status){
                                GoOutSettingActivity.this.finish();
                                toast(data.message);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

    /*
     * App24/配药成功保存（改变模式） >
     * */
    private void changePattern(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.changePattern(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if (1 == data.status){
                                GoOutSettingActivity.this.finish();
                                toast(data.message);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

}

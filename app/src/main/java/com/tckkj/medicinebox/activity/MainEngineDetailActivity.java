package com.tckkj.medicinebox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.InputDialogUtil;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;

import okhttp3.Call;

/*
* 主机详情
* */
public class MainEngineDetailActivity extends BaseActivity {
    private TextView tv_host_number1, tv_host_name1, tv_warehouse_temperature1,
            tv_warehouse_temperature_state1, tv_warehouse_humidity1, tv_warehouse_humidity_state1,
            tv_current_pattern1, tv_log_message1, tv_host_number2, tv_host_name2,
            tv_warehouse_temperature2, tv_warehouse_temperature_state2, tv_warehouse_humidity2,
            tv_warehouse_humidity_state2, tv_current_pattern2, tv_log_message2;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_main_engine_detail);

        title.setText(R.string.host_details);

        tv_host_number1 = findViewById(R.id.tv_host_number1);
        tv_host_name1 = findViewById(R.id.tv_host_name1);
        tv_warehouse_temperature1 = findViewById(R.id.tv_warehouse_temperature1);
        tv_warehouse_temperature_state1 = findViewById(R.id.tv_warehouse_temperature_state1);
        tv_warehouse_humidity1 = findViewById(R.id.tv_warehouse_humidity1);
        tv_warehouse_humidity_state1 = findViewById(R.id.tv_warehouse_humidity_state1);
        tv_current_pattern1 = findViewById(R.id.tv_current_pattern1);
        tv_log_message1 = findViewById(R.id.tv_log_message1);
        tv_host_number2 = findViewById(R.id.tv_host_number2);
        tv_host_name2 = findViewById(R.id.tv_host_name2);
        tv_warehouse_temperature2 = findViewById(R.id.tv_warehouse_temperature2);
        tv_warehouse_temperature_state2 = findViewById(R.id.tv_warehouse_temperature_state2);
        tv_warehouse_humidity2 = findViewById(R.id.tv_warehouse_humidity2);
        tv_warehouse_humidity_state2 = findViewById(R.id.tv_warehouse_humidity_state2);
        tv_current_pattern2 = findViewById(R.id.tv_current_pattern2);
        tv_log_message2 = findViewById(R.id.tv_log_message2);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(false);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {

            }
        });
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            String oid = getIntent().getStringExtra("oid");
            if (!StringUtil.isSpace(oid)){
                /*if (App.islogin){
                    getHostMessage(oid);
                }else {
                    InputDialogUtil.showLoginTipDialog(this);
                }*/
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /*
     * App10/查看主机详情 >
     * */
    private void getHostMessage(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getHostMessage(oid, new MApiResultCallback() {
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
}
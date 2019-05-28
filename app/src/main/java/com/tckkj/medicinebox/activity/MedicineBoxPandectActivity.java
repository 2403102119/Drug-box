package com.tckkj.medicinebox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.adapter.MedicineBoxPandectAdapter;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.InputDialogUtil;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/*
* 药盒总览
* */
public class MedicineBoxPandectActivity extends BaseActivity {
    private TextView tv_host_number, tv_host_name;
    private NiceRecyclerView nrv_medicine_box_pandect;
    private MedicineBoxPandectAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_medicine_box_pandect);

        title.setText(getString(R.string.kit_overview));

        tv_host_number = findViewById(R.id.tv_host_number);
        tv_host_name = findViewById(R.id.tv_host_name);
        nrv_medicine_box_pandect = findViewById(R.id.nrv_medicine_box_pandect);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 6; i++) {
            list.add(new HashMap<String, Object>());
        }

        /*if (App.islogin){
            if (StringUtil.isSpace(App.hostOid)){
                InputDialogUtil.showLoginTipDialog(this);
            }else {
                getStorehouseListByHostNumber(App.hostOid);
            }
        }else {
            InputDialogUtil.showHostConnectTipDialog(this);
        }*/

        adapter = new MedicineBoxPandectAdapter(this, list);
        nrv_medicine_box_pandect.setAdapter(adapter);
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
     * App19/根据主机号获取药仓信息列表 >
     * */
    private void getStorehouseListByHostNumber(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getStorehouseListByHostNumber(oid, new MApiResultCallback() {
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

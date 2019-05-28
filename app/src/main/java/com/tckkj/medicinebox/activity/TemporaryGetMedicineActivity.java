package com.tckkj.medicinebox.activity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.adapter.TemporaryGetMidicineAdapter;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/*
* 临时取药
* */
public class TemporaryGetMedicineActivity extends BaseActivity {
    private NiceRecyclerView nrv_tem_get_medicine;
    private TemporaryGetMidicineAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private boolean checkWarehouse[] = {false, false, false, false, false, false};
    private int medicineNumber[] = {0, 0, 0, 0, 0, 0};

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_temporary_get_medicine);

        title.setText(R.string.temporary_get_medicine);
        rightTxt.setText(R.string.get_medicine);
        rightTxt.setVisibility(View.VISIBLE);

        nrv_tem_get_medicine = findViewById(R.id.nrv_tem_get_medicine);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 6; i++) {
            list.add(new HashMap<String, Object>());
        }

        adapter = new TemporaryGetMidicineAdapter(this, list);
        nrv_tem_get_medicine.setAdapter(adapter);
        adapter.setOnItemClickLister(new TemporaryGetMidicineAdapter.OnItemClickLister() {
            @Override
            public void checkListener(int position, boolean isCheck) {
                checkWarehouse[position] = isCheck;
            }

            @Override
            public void editNumber(int position, int number) {
                medicineNumber[position] = number;
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
                String toast = "";
                for (int i = 0; i < checkWarehouse.length; i++) {
                    toast =toast + checkWarehouse[i] + "--" + medicineNumber[i] +"\n";
                }
                toast(toast);
                temporaryDrugWithdrawal("", "");
                break;
        }
    }

    /*
     * App14/临时取药 >
     * */
    private void temporaryDrugWithdrawal(final String medicineAmount, final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.temporaryDrugWithdrawal(medicineAmount, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);
                            toast(data.message);
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

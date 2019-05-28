package com.tckkj.medicinebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.adapter.AllMedicineAdapter;
import com.tckkj.medicinebox.adapter.TakeMedicineTimeAdapter;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.ConvertUtils;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.view.CustomBarChart;
import com.tckkj.medicinebox.view.NiceRecyclerView;
import com.tckkj.medicinebox.view.PieChartView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/*
* 报表信息
* */
public class StatementMessageActivity extends BaseActivity {
    private LinearLayout ll_bar_diagram;        //条形统计图
    private PieChartView pc_all_medicine;       //扇形统计图
    private NiceRecyclerView nrv_all_medicine;      //药量总览
    private NiceRecyclerView nrv_take_medicine_time;      //服药时段
    private TextView tv_check_time;             //查看时段
    private TextView tv_host_number, tv_host_name;

    private AllMedicineAdapter allMedicineAdapter;
    private TakeMedicineTimeAdapter takeMedicineTimeAdapter;
    private List<Map<String, Object>> allMedicineList = new ArrayList<>();
    private List<Map<String, Object>> takeMedicineTimeList = new ArrayList<>();
    private final static int SELECT_DATE_REQUEST_CODE = 21;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_statement_message);

        title.setText(getString(R.string.report_information));

        ll_bar_diagram = findViewById(R.id.ll_bar_diagram);
        pc_all_medicine = findViewById(R.id.pc_all_medicine);
        nrv_all_medicine = findViewById(R.id.nrv_all_medicine);
        nrv_take_medicine_time = findViewById(R.id.nrv_take_medicine_time);
        tv_check_time = findViewById(R.id.tv_check_time);
        tv_host_number = findViewById(R.id.tv_host_number);
        tv_host_name = findViewById(R.id.tv_host_name);

        tv_host_number.setText(App.hostOid != null ? App.hostOid : null);

        initBarChart1();

        initPieChart();
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_check_time.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 6; i++) {
            allMedicineList.add(new HashMap<String, Object>());
        }

        allMedicineAdapter = new AllMedicineAdapter(this, allMedicineList);
        nrv_all_medicine.setAdapter(allMedicineAdapter);

        for (int i = 0; i < 4; i++) {
            takeMedicineTimeList.add(new HashMap<String, Object>());
        }

        takeMedicineTimeAdapter = new TakeMedicineTimeAdapter(this, takeMedicineTimeList);
        nrv_take_medicine_time.setAdapter(takeMedicineTimeAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_check_time:
                Intent intent = new Intent(StatementMessageActivity.this, CheckTimeActivity.class);
                startActivityForResult(intent, SELECT_DATE_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SELECT_DATE_REQUEST_CODE == requestCode && CheckTimeActivity.SELECT_RESULT == resultCode && null != data){
            String checkDay = data.getStringExtra("checkDay");
            if (!StringUtil.isSpace(checkDay))
            tv_check_time.setText(checkDay);

            /*if (App.islogin){
                if (StringUtil.isSpace(App.hostOid)){
                    InputDialogUtil.showHostConnectTipDialog(this);
                }else {
                    getDrugRecordingList(App.hostOid, checkDay);
                }
            }else {
                InputDialogUtil.showHostConnectTipDialog(this);
            }*/

        }
    }

    /**
     * 初始化柱状图1数据
     */
    private void initBarChart1() {
        String[] xLabel = {"", getString(R.string.warehouse_one), getString(R.string.warehouse_two),
                getString(R.string.warehouse_three), getString(R.string.warehouse_four),
                getString(R.string.warehouse_five), getString(R.string.warehouse_six)};   //X坐标轴刻度
        String[] yLabel = {"0", "40", "80", "120"};            //Y坐标轴刻度
        int[] data1 = {54, 76, 61, 98, 66, 85};                 //条形柱所显示的值

        List<int[]> data = new ArrayList<>();
        data.add(data1);
        List<Integer> color = new ArrayList<>();
        color.add(R.color.barDiagramLine);
        color.add(R.color.greenText);
        color.add(R.color.normalText);
        CustomBarChart customBarChart = new CustomBarChart(this, xLabel, yLabel, data, color);
        customBarChart.setScaleTextSize(ConvertUtils.dp2px(this, 13));  //设置刻度字体大小
        customBarChart.setScale(40.0f);     //设置刻度值
        ll_bar_diagram.addView(customBarChart);
    }

    private void initPieChart() {
        List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();
        pieceDataHolders.add(new PieChartView.PieceDataHolder(100, 0xFFE1645D, ""));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(25, 0xFF70ADC7, ""));

        pc_all_medicine.setData(pieceDataHolders);
    }

    /*
     * App13/根据服药时段获取服药率数据 >
     * */
    private void getDrugRecordingList(final String oid, final String takingDate){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getDrugRecordingList(oid, takingDate, new MApiResultCallback() {
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

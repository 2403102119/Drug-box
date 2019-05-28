package com.tckkj.medicinebox.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseFragment;
import com.tckkj.medicinebox.view.CustomBarChart;

import java.util.ArrayList;
import java.util.List;

public class ThreeFragment extends BaseFragment {
    private LinearLayout customBarChart1;

    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_three);

        customBarChart1 = findView(R.id.cbc_three);
        initBarChart1();
    }


    /**
     * 初始化柱状图1数据
     */
    private void initBarChart1() {
//        String[] xLabel = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
//                "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
//                "28", "29", "30", "31"};
//        String[] yLabel = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900"};
//        int[] data1 = {300, 500, 550, 500, 300, 700, 800, 750, 550, 600, 400, 300, 400, 600, 500,
//                700, 300, 500, 550, 500, 300, 700, 800, 750, 550, 600, 400, 300, 400, 600, 500};

        String[] xLabel = {"", "周一", "周二", "周三", "周四", "周五"};
        String[] yLabel = {"", "1", "2", "3", "4"};
        int[] data1 = {3, 6, 8, 3, 5};

        List<int[]> data = new ArrayList<>();
        data.add(data1);
        List<Integer> color = new ArrayList<>();
        color.add(R.color.red);
        color.add(R.color.gray);
        color.add(R.color.black);
        CustomBarChart customBarChart = new CustomBarChart(getActivity(), xLabel, yLabel, data, color);
        customBarChart.setScaleTextSize(40);
        customBarChart1.addView(customBarChart);
    }
}

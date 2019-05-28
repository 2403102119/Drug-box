package com.tckkj.medicinebox.fragment;

import android.graphics.Color;
import android.view.View;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseFragment;
import com.tckkj.medicinebox.view.PieChartView;

import java.util.ArrayList;
import java.util.List;

public class OneFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_one);

        PieChartView pieChartView = (PieChartView) view.findViewById(R.id.pie_chart);

        List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();

        pieceDataHolders.add(new PieChartView.PieceDataHolder(100,0xFF77CCAA, "今天，１"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(1000, 0xFF11AA33, "明天，２"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(1200, Color.GRAY, "就是风，３"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(5000, Color.YELLOW, "呵呵，４"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(10000, Color.RED, "小京，５"));
        pieceDataHolders.add(new PieChartView.PieceDataHolder(13000, Color.BLUE, "花花，６"));

        pieChartView.setData(pieceDataHolders);
    }
}

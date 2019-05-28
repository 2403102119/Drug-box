package com.tckkj.medicinebox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tckkj.medicinebox.R;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class MedicineWarehouseBannerHolder implements MZViewHolder<Integer> {
    private ImageView img_medicine_warehouse_code;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_warehouse_selector, null);
        img_medicine_warehouse_code = view.findViewById(R.id.img_medicine_warehouse_code);
        return view;
    }

    @Override
    public void onBind(Context context, int i, Integer o) {
        img_medicine_warehouse_code.setImageResource(o);
    }
}

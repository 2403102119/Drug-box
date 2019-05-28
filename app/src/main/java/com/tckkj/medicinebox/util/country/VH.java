package com.tckkj.medicinebox.util.country;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tckkj.medicinebox.R;

public class VH extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView tvCode;

    public VH(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCode = (TextView) itemView.findViewById(R.id.tv_code);
    }
}

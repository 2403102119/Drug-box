package com.tckkj.medicinebox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tckkj.medicinebox.R;

import java.util.List;
import java.util.Map;
/*
* 药盒总览
* */
public class MedicineBoxPandectAdapter extends RecyclerView.Adapter<MedicineBoxPandectAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;

    public MedicineBoxPandectAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_box_pandect, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        switch (position){
            case 0:
                holder.img_medicine_warehouse_icon.setImageResource(R.mipmap.box_check_yes_one);
                holder.view_left_edge.setBackgroundResource(R.drawable.shape_half_green_bg_10);
                break;
            case 1:
                holder.img_medicine_warehouse_icon.setImageResource(R.mipmap.box_check_yes_two);
                holder.view_left_edge.setBackgroundResource(R.drawable.shape_half_red_bg_10);
                break;
            case 2:
                holder.view_left_edge.setBackgroundResource(R.drawable.shape_half_orange_bg_10);
                holder.img_medicine_warehouse_icon.setImageResource(R.mipmap.box_check_yes_three);
                break;
            case 3:
                holder.view_left_edge.setBackgroundResource(R.drawable.shape_half_green_bg_10);
                holder.img_medicine_warehouse_icon.setImageResource(R.mipmap.box_check_yes_four);
                break;
            case 4:
                holder.view_left_edge.setBackgroundResource(R.drawable.shape_half_red_bg_10);
                holder.img_medicine_warehouse_icon.setImageResource(R.mipmap.box_check_yes_five);
                break;
            case 5:
                holder.view_left_edge.setBackgroundResource(R.drawable.shape_half_orange_bg_10);
                holder.img_medicine_warehouse_icon.setImageResource(R.mipmap.box_check_yes_six);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView tv_medicine_name, tv_remaining_quantity, tv_useful_life, tv_dosage;
        private ImageView img_medicine_warehouse_icon, img_medicine_warehouse;
        private View view_left_edge;

        public MyHolder(View itemView) {
            super(itemView);

            tv_medicine_name = itemView.findViewById(R.id.tv_medicine_name);
            tv_remaining_quantity = itemView.findViewById(R.id.tv_remaining_quantity);
            tv_useful_life = itemView.findViewById(R.id.tv_useful_life);
            tv_dosage = itemView.findViewById(R.id.tv_dosage);
            img_medicine_warehouse_icon = itemView.findViewById(R.id.img_medicine_warehouse_icon);
            img_medicine_warehouse = itemView.findViewById(R.id.img_medicine_warehouse);
            view_left_edge = itemView.findViewById(R.id.view_left_edge);
        }
    }
}

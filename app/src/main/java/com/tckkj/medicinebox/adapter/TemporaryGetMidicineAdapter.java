package com.tckkj.medicinebox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.view.ClearEditText;

import java.util.List;
import java.util.Map;

public class TemporaryGetMidicineAdapter extends RecyclerView.Adapter<TemporaryGetMidicineAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;

    public TemporaryGetMidicineAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_temporary_get_medicine, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
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
        holder.cb_get_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickLister.checkListener(position, holder.cb_get_medicine.isChecked());
            }
        });
        holder.cet_medicine_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int number = 0;
                if (!StringUtil.isSpace(s.toString())){
                    number = Integer.parseInt(s.toString());
                }
                onItemClickLister.editNumber(position, number);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == list)
            return 0;
        else
            return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView tv_medicine_name, tv_remaining_quantity, tv_useful_life;
        private ClearEditText cet_medicine_number;
        private CheckBox cb_get_medicine;
        private ImageView img_medicine_warehouse_icon;
        private View view_left_edge;

        public MyHolder(View itemView) {
            super(itemView);

            tv_medicine_name = itemView.findViewById(R.id.tv_medicine_name);
            tv_remaining_quantity = itemView.findViewById(R.id.tv_remaining_quantity);
            tv_useful_life = itemView.findViewById(R.id.tv_useful_life);
            cet_medicine_number = itemView.findViewById(R.id.cet_medicine_number);
            cb_get_medicine = itemView.findViewById(R.id.cb_get_medicine);
            img_medicine_warehouse_icon = itemView.findViewById(R.id.img_medicine_warehouse_icon);
            view_left_edge = itemView.findViewById(R.id.view_left_edge);
        }
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    private OnItemClickLister onItemClickLister;

    public interface OnItemClickLister{
        void checkListener(int position, boolean isCheck);
        void editNumber(int position, int number);
    }
}

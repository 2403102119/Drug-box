package com.tckkj.medicinebox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tckkj.medicinebox.R;

import java.util.List;
import java.util.Map;

public class RemindTimeAdapter extends RecyclerView.Adapter<RemindTimeAdapter.MyHolder> {
    private Context context;
    private List<String> list;

    public RemindTimeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_remind_time, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tv_remind_number.setText(position + 1 + "");
        String a=list.get(position);
        String c=  a.substring(0,2)+":"+a.substring(2);
        holder.tv_remind_time.setText(c);
        holder.ll_remind_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
        holder.btn_remind_time_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onDelete(position);
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
        private TextView tv_remind_number, tv_remind_time;
        private Button btn_remind_time_delete;
        private LinearLayout ll_remind_item;

        public MyHolder(View itemView) {
            super(itemView);

            tv_remind_number = itemView.findViewById(R.id.tv_remind_number);
            tv_remind_time = itemView.findViewById(R.id.tv_remind_time);
            btn_remind_time_delete = itemView.findViewById(R.id.btn_remind_time_delete);
            ll_remind_item = itemView.findViewById(R.id.ll_remind_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDelete(int position);
    }
}

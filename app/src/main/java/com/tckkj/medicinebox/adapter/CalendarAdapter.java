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

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public CalendarAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tv_calendar.setText(list.get(position).get("dayNum") + "");
        if ((boolean)list.get(position).get("isCheck")){        //如果被选中，文字变色，背景显示
            holder.img_calendar_bg.setVisibility(View.VISIBLE);
            holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.img_calendar_bg.setVisibility(View.GONE);    //未被选中，背景隐藏
            if (0 == position%7 || 6 == position%7){                //如果是周六或者周末，文字为灰色
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.hintColor));
            }else {
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.normalText));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.itemClick(position);
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
        private ImageView img_calendar_bg;
        private TextView tv_calendar;

        public MyHolder(View itemView) {
            super(itemView);

            img_calendar_bg = itemView.findViewById(R.id.img_calendar_bg);
            tv_calendar = itemView.findViewById(R.id.tv_calendar);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void itemClick(int position);
    }
}

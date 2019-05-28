package com.tckkj.medicinebox.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tckkj.medicinebox.R;

import java.util.List;


public class SpinerPopWindow<T> extends PopupWindow {
	private LayoutInflater inflater;
	private ListView mListView;
	private List<T> list;
	private MyAdapter  mAdapter;
	private Context context;
	
	public SpinerPopWindow(Context context, List<T> list, OnItemClickListener clickListener) {
		super(context);
		this.context = context;
		inflater= LayoutInflater.from(context);
		this.list=list;
		init(clickListener);
	}
	
	private void init(OnItemClickListener clickListener){
		View view = inflater.inflate(R.layout.spiner_window_layout, null);
		setContentView(view);		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
    		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setAdapter(mAdapter=new MyAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mAdapter.setmCurrentItem(position);
				mAdapter.setClick(true);
			}
		});
		mListView.setOnItemClickListener(clickListener);
	}
	
	private class MyAdapter extends BaseAdapter {
		private int mCurrentItem=0;
		private boolean isClick=false;

		public void setmCurrentItem(int mCurrentItem) {
			this.mCurrentItem = mCurrentItem;
		}

		public void setClick(boolean click) {
			isClick = click;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if(convertView==null){
				holder=new ViewHolder();
				convertView=inflater.inflate(R.layout.spiner_item_layout, null);
				holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			if (position == mCurrentItem && isClick){
				holder.tvName.setTextColor(context.getResources().getColor(R.color.login_guide_login_btn_bg));
			}else {
				holder.tvName.setTextColor(context.getResources().getColor(R.color.black));
			}

			holder.tvName.setText(getItem(position).toString());
			return convertView;
		}
	}
	
	private class ViewHolder{
		private TextView tvName;
	}
}

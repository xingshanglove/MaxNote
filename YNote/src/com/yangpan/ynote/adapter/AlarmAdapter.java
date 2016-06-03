package com.yangpan.ynote.adapter;

import java.util.List;

import com.example.test.R;
import com.yangpan.ynote.bean.Alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter{

	List<Alarm> alarmList;
	Context context;
	onStateChangeListener onchangeListener;
	public interface onStateChangeListener{
		public void onChange(Alarm alarm,int status);
	}
	public void setOnStateChangeListener(onStateChangeListener onchangeListener){
		this.onchangeListener=onchangeListener;
	}
	public AlarmAdapter(Context  context, List<Alarm> alarmList) {
		this.alarmList=alarmList;
		this.context=context;
	}
	@Override
	public int getCount() {
		return alarmList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.alalm_list_item, null);
			holder=new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv_type.setText(alarmList.get(position).getType());
		holder.tv_time.setText(alarmList.get(position).getTime());
		
		if(alarmList.get(position).getStatus()==0){
			holder.s_toggler.setChecked(false);
		}else{
//			holder.s_toggler.setChecked(true);
		}
		holder.s_toggler.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					onchangeListener.onChange(alarmList.get(position), 1);
				}else{
					onchangeListener.onChange(alarmList.get(position), 0);
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		TextView tv_type;
		TextView tv_time;
		Switch s_toggler;
		public ViewHolder(View v) {
			tv_type=(TextView) v.findViewById(R.id.tv_type);
			tv_time=(TextView) v.findViewById(R.id.tv_time);
			s_toggler=(Switch) v.findViewById(R.id.s_toggler);
		}
	}
}

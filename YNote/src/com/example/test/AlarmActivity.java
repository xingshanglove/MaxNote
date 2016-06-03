package com.example.test;

import java.util.Calendar;
import java.util.List;

import com.example.test.R;
import com.yangpan.ynote.adapter.AlarmAdapter;
import com.yangpan.ynote.adapter.AlarmAdapter.onStateChangeListener;
import com.yangpan.ynote.bean.Alarm;
import com.yangpan.ynote.bean.Contacts;
import com.yangpan.ynote.broadcast.AlarmReceiver;
import com.yangpan.ynote.db.AlarmDao;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ListView;

public class AlarmActivity extends Activity {
	private ListView lv_alarm;
	private ImageView iv_addalarm;
	private AlarmAdapter adapter;
	private List<Alarm> alarmList;
	private AlarmDao dao;
	private AlarmManager alarmManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		initView();
		initData();
	}

	private void initData() {
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		dao = new AlarmDao(this);
		alarmList = dao.findAll();
		adapter = new AlarmAdapter(AlarmActivity.this, alarmList);
		lv_alarm.setAdapter(adapter);
		adapter.setOnStateChangeListener(new onStateChangeListener() {
			@Override
			public void onChange(Alarm alarm, int status) {
				changeStatus(alarm, status);
			}
		});

		lv_alarm.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				Log.v("-------->", "long");
				AlertDialog.Builder builder = new Builder(AlarmActivity.this);
				builder.setMessage("ȷ��ɾ����?");
				builder.setPositiveButton("ɾ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteAlarm(alarmList.get(position));
							}
						});
				builder.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.show();
				return false;
			}
		});
	}

	/**
	 * ��������ɾ������
	 * 
	 * @param alarm
	 */
	private void deleteAlarm(Alarm alarm) {
		dao.deleteAlarm(alarm);
		alarmList = dao.findAll();
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		lv_alarm = (ListView) this.findViewById(R.id.lv_alarm);
		iv_addalarm = (ImageView) this.findViewById(R.id.iv_addalarm);
		iv_addalarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GotoCreateAlarm();
			}
		});
	}

	/**
	 * ���ش򿪺͹ر� �����޸�
	 * 
	 * @param alarm
	 * @param status
	 */
	public void changeStatus(Alarm alarm, int status) {
		alarm.setStatus(status);
		dao.updateAlarm(alarm);
		if (status == 0)
			closeAlarm(alarm);
		else
			openAlarm(alarm);
	}

	/**
	 * �ر�����
	 * 
	 * @param alarm
	 */
	private void closeAlarm(Alarm alarm) {
		String[] time = alarm.getTime().split(":");
		int hour = Integer.parseInt(time[0]);
		int min = Integer.parseInt(time[1]);
		Log.v("-------->", hour + "/" + min);
		Calendar c = Calendar.getInstance();// ��ȡ���ڶ���
		c.setTimeInMillis(System.currentTimeMillis()); // ����Calendar����
		c.set(Calendar.HOUR, hour); // ��������Сʱ��
		c.set(Calendar.MINUTE, min); // �������ӵķ�����
		c.set(Calendar.SECOND, 0); // �������ӵ�����
		c.set(Calendar.MILLISECOND, 0); // �������ӵĺ�����
		Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class); // ����Intent����
		PendingIntent pi = PendingIntent.getBroadcast(AlarmActivity.this, 0,
				intent, 0); // ����PendingIntent
		alarmManager.cancel(pi);
		Toast.makeText(AlarmActivity.this, "�����ѹر�", 1).show();
	}
	/**
	 * ���ڴ���һ���µ�����
	 */
	private void GotoCreateAlarm() {
		View diaView = View.inflate(this, R.layout.dialog_add_alarm, null);
		final TextView tv_time = (TextView) diaView.findViewById(R.id.tv_time);
		final RadioButton rb_meeting = (RadioButton) diaView
				.findViewById(R.id.rb_meeting);
		Calendar calendar = Calendar.getInstance();
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int min=calendar.get(Calendar.MINUTE);
		tv_time.setText(hour+":"+min);
		TimePicker timePicker = (TimePicker) diaView
				.findViewById(R.id.timepicker);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				tv_time.setText(hourOfDay + ":" + minute);
			}
		});

		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(diaView);
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String time = tv_time.getText().toString();
				String type;
				if (rb_meeting.isChecked()) {
					type = Contacts.TYPE_MEET;
				} else {
					type = Contacts.TYPE_FLIGHT;
				}
				int status = 1;

				// �������
				Alarm alarm = new Alarm(time, type, status);
				if (!isNotExit(alarm)) {
					dao.addAlarm(alarm);
					// �����ӷ���
					openAlarm(alarm);
					// ˢ���б�
					alarmList.add(alarm);
					adapter = new AlarmAdapter(AlarmActivity.this, alarmList);
					adapter.setOnStateChangeListener(new onStateChangeListener() {
						@Override
						public void onChange(Alarm alarm, int status) {
							changeStatus(alarm, status);
						}
					});
					lv_alarm.setAdapter(adapter);
				} else {
					Toast.makeText(AlarmActivity.this, "�����Ѵ���", 1).show();
				}
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();

	}
	/**
	 * �鿴�����Ƿ��Ѿ�����
	 * @param alarm
	 * @return
	 */
	protected boolean isNotExit(Alarm alarm) {
		for (Alarm alarm2 : alarmList) {
			if (alarm2.getTime().equals(alarm.getTime())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ϊ�´��������Ӵ򿪷���
	 * 
	 * @param alarm
	 */
	protected void openAlarm(Alarm alarm) {
		String[] time = alarm.getTime().split(":");
		int hour = Integer.parseInt(time[0]);
		int min = Integer.parseInt(time[1]);
		Log.v("-------->", hour + "/" + min);
		Calendar c = Calendar.getInstance();// ��ȡ���ڶ���
		c.setTimeInMillis(System.currentTimeMillis()); // ����Calendar����
		c.set(Calendar.HOUR, hour); // ��������Сʱ��
		c.set(Calendar.MINUTE, min); // �������ӵķ�����
		c.set(Calendar.SECOND, 0); // �������ӵ�����
		c.set(Calendar.MILLISECOND, 0); // �������ӵĺ�����
		Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class); // ����Intent����
		PendingIntent pi = PendingIntent.getBroadcast(AlarmActivity.this, 0,
				intent, 0); // ����PendingIntent
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi); // ��������
		Toast.makeText(AlarmActivity.this, "�������óɹ�", Toast.LENGTH_LONG).show();// ��ʾ�û�
	}
}

package com.example.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.yangpan.ynote.broadcast.CallAlarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class SetTimeActy extends Activity implements OnClickListener {
	/**
	 * 设置闹钟
	 */
	private Button set_alarm;
	/**
	 * 取消闹钟
	 */
	private Button cancle_alarm;

	/**
	 * 设置日期
	 */
	private Button date;
	/**
	 * 设置时间
	 */
	private Button time;

	/**
	 * 先设置闹钟的提醒
	 */
	private TextView hint_text;

	private SimpleDateFormat dateFormat;
	private Calendar calendar;
	private String notes;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// 传过来的笔记
		try {
			notes = getIntent().getStringExtra("notes");
		} catch (Exception e) {

		}

		// 初始化布局
		initView();

	}

	private void initView() {

		calendar = Calendar.getInstance();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		hint_text = (TextView) findViewById(R.id.hint_text);

		set_alarm = (Button) findViewById(R.id.set_alarm);
		cancle_alarm = (Button) findViewById(R.id.cancle_alarm);

		date = (Button) findViewById(R.id.date);
		time = (Button) findViewById(R.id.time);
		set_alarm.setOnClickListener(this);
		cancle_alarm.setOnClickListener(this);

		date.setOnClickListener(this);
		time.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.date:
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			new DatePickerDialog(SetTimeActy.this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							calendar.set(Calendar.YEAR, year);
							calendar.set(Calendar.MONTH, monthOfYear);
							calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						}
					}, year, month, day).show();
			break;
		case R.id.time:
			int mHour = calendar.get(Calendar.HOUR_OF_DAY);
			int mMinute = calendar.get(Calendar.MINUTE);
			new TimePickerDialog(SetTimeActy.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							// TODO Auto-generated method stub
							calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
							calendar.set(Calendar.MINUTE, minute);
							calendar.set(Calendar.SECOND, 0);
							calendar.set(Calendar.MILLISECOND, 0);
						}
					}, mHour, mMinute,
		 			
					DateFormat.is24HourFormat(SetTimeActy.this)).show();
			break;
		case R.id.set_alarm:
			SharedPreferences preferences = getSharedPreferences("TEST",
					MODE_PRIVATE);
			Editor edit = preferences.edit();
			edit.putLong("time", calendar.getTimeInMillis());
			edit.commit();
			/* 建立Intent和PendingIntent，来调用目标组件 */
			Intent intent = new Intent(SetTimeActy.this, CallAlarm.class);
			intent.putExtra("_id", 0);
			
			
			intent.putExtra("notes", notes);
            
			
			
			
			
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					SetTimeActy.this, 0, intent, 0);
			AlarmManager am;
			/* 获取闹钟管理的实例 */
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
			/* 设置闹钟 */
			am.cancel(pendingIntent);
			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					pendingIntent);

			Date dd = new Date(calendar.getTimeInMillis());
			hint_text.setText(dateFormat.format(dd));
			break;
		case R.id.cancel_alarm:
			Intent intent1 = new Intent(SetTimeActy.this, CallAlarm.class);
			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
					SetTimeActy.this, 0, intent1, 0);
			AlarmManager am1;
			/* 获取闹钟管理的实例 */
			am1 = (AlarmManager) getSystemService(ALARM_SERVICE);
			/* 取消 */
			am1.cancel(pendingIntent1);
			hint_text.setText("闹钟已取消！");
			break;

		default:
			break;
		}
	}
}

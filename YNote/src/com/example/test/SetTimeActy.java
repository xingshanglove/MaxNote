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
	 * ��������
	 */
	private Button set_alarm;
	/**
	 * ȡ������
	 */
	private Button cancle_alarm;

	/**
	 * ��������
	 */
	private Button date;
	/**
	 * ����ʱ��
	 */
	private Button time;

	/**
	 * ���������ӵ�����
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

		// �������ıʼ�
		try {
			notes = getIntent().getStringExtra("notes");
		} catch (Exception e) {

		}

		// ��ʼ������
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
			/* ����Intent��PendingIntent��������Ŀ����� */
			Intent intent = new Intent(SetTimeActy.this, CallAlarm.class);
			intent.putExtra("_id", 0);
			
			
			intent.putExtra("notes", notes);
            
			
			
			
			
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					SetTimeActy.this, 0, intent, 0);
			AlarmManager am;
			/* ��ȡ���ӹ����ʵ�� */
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
			/* �������� */
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
			/* ��ȡ���ӹ����ʵ�� */
			am1 = (AlarmManager) getSystemService(ALARM_SERVICE);
			/* ȡ�� */
			am1.cancel(pendingIntent1);
			hint_text.setText("������ȡ����");
			break;

		default:
			break;
		}
	}
}

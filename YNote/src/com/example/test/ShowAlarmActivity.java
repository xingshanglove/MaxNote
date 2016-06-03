package com.example.test;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.yangpan.ynote.bean.Alarm;
import com.yangpan.ynote.bean.Contacts;
import com.yangpan.ynote.db.AlarmDao;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class ShowAlarmActivity extends Activity {
	TextView tv_type;
	TextView btn_cancle;

	AlarmDao dao;

	MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_showalarm);
		Log.v("--->cur", System.currentTimeMillis() + "");
		tv_type = (TextView) this.findViewById(R.id.tv_type);
		btn_cancle = (TextView) this.findViewById(R.id.btn_cancle);

		long cur = System.currentTimeMillis();

		dao = new AlarmDao(this);
		List<Alarm> all = dao.findAll();
		for (int i = 0; i < all.size(); i++) {
			Alarm alarm = all.get(i);
			Log.v("---KKK", alarm.getTime());
			String[] time = alarm.getTime().split(":");
			Log.v("---KKK", time[0]);
			int hour = Integer.parseInt(time[0]);
			int min = Integer.parseInt(time[1]);
			Log.v("---KKK", hour + "/" + min);
			Calendar c = Calendar.getInstance();// ��ȡ���ڶ���
			c.setTimeInMillis(System.currentTimeMillis()); // ����Calendar����
			c.set(Calendar.HOUR, hour); // ��������Сʱ��
			c.set(Calendar.MINUTE, min); // �������ӵķ�����
			c.set(Calendar.SECOND, 0); // �������ӵ�����
			c.set(Calendar.MILLISECOND, 0); // �������ӵĺ�����
			Log.v("--->alarm", c.getTimeInMillis() + "");
			if (cur - c.getTimeInMillis() < 1000) {
				String type = alarm.getType();
				tv_type.setText(type + "ʱ�䵽�ˣ�����");
				if (type.equals(Contacts.TYPE_MEET)) {
					// ���Ż������
					player = MediaPlayer.create(this, R.raw.daleng);
					player.start();
				} else {
					// ���ź������
					player = MediaPlayer.create(this, R.raw.xinzhisuozai);
					player.start();
				}
			}
		}

		btn_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowAlarmActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (player != null)
			player.release();
	}
}

package com.yangpan.ynote.service;

import java.io.IOException;

import com.yangpan.ynote.db.DBOpenHelper;
import com.yangpan.ynote.utils.Utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

/**
 * 该服务只用来处理多媒体铃声播放
 * 
 * @author way
 * 
 */
public class AlarmService extends Service {

	private static String TAG = "AlarmService";
	private MediaPlayer mp;
	private String action;
	private String ringsname;
	private Vibrator v;
	private String rings;
	private boolean isVibrate;
	private String defaultUri = "content://settings/system/ringtone";
	String external = "content://media/external/";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate");

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.way.note.STOP_ALARM");
		// 防止在灭屏时不能接受停止闹钟的广播，所以用静态注册。
		this.registerReceiver(this.receiver, filter);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		initDatas(intent);
		setVibrator();
		play();
	}

	private void initDatas(Intent intent) {
		if (intent != null) {
			rings = intent.getStringExtra(DBOpenHelper.RINGTONE_URI);
			ringsname = intent.getStringExtra(DBOpenHelper.RINGTONE_NAME);
			isVibrate = intent.getBooleanExtra(DBOpenHelper.ISVIBRATE, false);
			Log.v(TAG, "rings-->" + rings + " ringName-->" + ringsname
					+ " isVbritate-->" + isVibrate);
		}
	}

	private void setVibrator() {
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if (isVibrate) {
			v.vibrate(new long[] { 1000, 2000 }, 0);
		} else {
			closeVirbrator();
		}
	}

	private void play() {
		Log.v(TAG, "setPlayer");
		if (rings == null) {
			return;
		}
		mp = new MediaPlayer();
		AudioManager adm = (AudioManager) getSystemService(AUDIO_SERVICE);
		try {
			if (rings == null) {
			} else if (rings.equals("defalut")) {
				mp.setDataSource(AlarmService.this, Uri.parse(defaultUri));
			} else if (rings.length() > 25
					&& rings.substring(0, 25).equals(external)
					&& !(Utils.isSongExist(this, ringsname))) {
				mp.setDataSource(AlarmService.this, Uri.parse(defaultUri));
			} else {
				mp.setDataSource(AlarmService.this, Uri.parse(rings));
			}

			if (adm.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				// 设置声音播放通道
				mp.setAudioStreamType(AudioManager.STREAM_ALARM);
				mp.setLooping(true);
				mp.prepare();
				mp.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();

			if (action != null && action.equals("com.way.note.STOP_ALARM")) {
				Log.v(TAG, "receiver   time=" + System.currentTimeMillis());
				closePlay();
				closeVirbrator();
			}
		}
	};

	public void onDestroy() {
		Log.v(TAG, "OnDestory");
		super.onDestroy();
		closeVirbrator();
		closePlay();
		unregisterReceiver(receiver);
	}

	private void closePlay() {
		try {
			if (mp != null && mp.isPlaying()) {
				Log.v(TAG, "onDestroy closeMediaPlayer");
				mp.stop();
				mp.release();
				mp = null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private void closeVirbrator() {
		if (v != null) {
			v.cancel();
			v = null;
		}
	}

}
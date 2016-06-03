package com.yangpan.ynote.broadcast;

import java.util.Calendar;

import com.example.test.AlarmAlertActy;
import com.example.test.R;
import com.yangpan.ynote.db.DBOpenHelper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * 
 * 
 */
public class CallAlarm extends BroadcastReceiver {
	private static final String TAG = "CallAlarm";

	private Context mContext;
	private static final String DEFAULT_SNOOZE = "10";
	int mNoteID;
	int tag;

	private String notes;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;

		// 传过来的笔记
		try {
			notes = intent.getStringExtra("notes");
		} catch (Exception e) {

		}

		// 接受其他闹钟事件，电话事件，短信事件等，进行交互处理
		String action = intent.getAction();

		if (action != null
				&& action.equals("android.intent.action.PHONE_STATE")) {
			Log.v(TAG, "onReceive:action.PHONE_STATE");
			snooze();
		} else if (action != null
				&& action.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Log.v(TAG, "onReceive:Telephony.SMS_RECEIVED");
			snooze();
		} else {
			tag = intent.getIntExtra("_id", -1);
			System.out.println(tag + "");
			if (tag == -1) {
				return;
			}

			Intent i = new Intent(context, AlarmAlertActy.class);

			i.putExtra("notes", notes);
			
			
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

	// Attempt to snooze this alert.
	private void snooze() {

		final String snooze = DEFAULT_SNOOZE;
		int snoozeMinutes = Integer.parseInt(snooze);
		final long snoozeTime = System.currentTimeMillis()
				+ (1000 * 60 * snoozeMinutes);

		// Get the display time for the snooze and update the notification.
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(snoozeTime);
		// Append (snoozed) to the label.
		String label = "闹铃";
		label = mContext.getResources().getString(R.string.song_pause, label);

		// Notify the user that the alarm has been snoozed.
		Intent cancelSnooze = new Intent();
		cancelSnooze.setAction("com.way.note.STOP_ALARM");
		cancelSnooze.putExtra(DBOpenHelper.ID, mNoteID);
		mContext.sendBroadcast(cancelSnooze);

		PendingIntent broadcast = PendingIntent.getBroadcast(mContext, mNoteID,
				cancelSnooze, 0);
		Notification n = new Notification(R.drawable.stat_notify_alarm, label,
				0);
		n.setLatestEventInfo(
				mContext,
				label,
				mContext.getResources().getString(R.string.click_cancel,
						(String) DateFormat.format("kk:mm", c)), broadcast);
		n.flags |= Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_ONGOING_EVENT;
	}
}
package com.yangpan.ynote.db;

import java.util.ArrayList;
import java.util.List;

import com.yangpan.ynote.bean.Alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmDao implements IAlarmImpl {
	private static final String TABLE_NAME="alarm";
	String [] cloums={"time","type","status"};
	Context context;
	AlarmOpenHelper openHelper;
	SQLiteDatabase db;
	public AlarmDao(Context context) {
		openHelper=new AlarmOpenHelper(context);
	}
	/**
	 * ÄÖÖÓ¸öÊý
	 */
	@Override
	public int findCount() {
		int count=0;
		db=openHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME, cloums, null, null, null, null, null);
		while(cursor.moveToNext()){
			count++;
		}
		db.close();
		return count;
	}
	/**
	 * Ìí¼ÓÄÖÖÓ
	 */
	@Override
	public boolean addAlarm(Alarm alarm) {
		db=openHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("time", alarm.getTime());
		cv.put("type", alarm.getType());
		cv.put("status", alarm.getStatus());
		db.insert(TABLE_NAME, null, cv);
		db.close();
		return true;
	}
	/**
	 * ÐÞ¸ÄÄÖÖÓ×´Ì¬
	 */
	@Override
	public boolean updateAlarm(Alarm alarm) {
		db=openHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("status", alarm.getStatus());
		db.update(TABLE_NAME, values, "time=?", new String[]{alarm.getTime()});
		db.close();
		return false;
	}
	/**
	 * É¾³ýÄÖÖÓ
	 */
	@Override
	public boolean deleteAlarm(Alarm alarm) {
		db=openHelper.getWritableDatabase();
		db.delete(TABLE_NAME, "time=?", new String[]{alarm.getTime()});
		db.close();
		return false;
	}
	/**
	 * »ñÈ¡È«²¿ÄÖÖÓ
	 */
	@Override
	public List<Alarm> findAll() {
		db=openHelper.getWritableDatabase();
		List<Alarm> alarms=new ArrayList<Alarm>();
		Cursor cursor = db.query(TABLE_NAME, cloums, null, null, null, null, null);
		while(cursor.moveToNext()){
			Alarm alarm=new Alarm(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
			alarms.add(alarm);
		}
		db.close();
		return alarms;
	}
	
	
}

package com.yangpan.ynote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库帮助类
 * 
 * @author way
 * 
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 11;// 数据库版本号
	private static final String DB_NAME = "Notes1.db";// 数据库名称
	public static final String TABLE_NAME = "items"; // 数据库中的表的名称
	public static final String ID = "_id";// 主键
	public static final String NOTE_CONTENT = "content";// 便签内容
	public static final String UPDATE_DATE = "cdate";// 最后更新的日期(date类型)
	public static final String UPDATE_TIME = "ctime";// 最后更新的时间(time类型)
	public static final String NOTE_ALARM_ENABLE = "alarm_enable";// 闹钟是否开启
	public static final String NOTE_BG_COLOR = "bgcolor";// 便签的背景颜色(varchar类型)
	public static final String NOTE_IS_FOLDER = "isfilefolder";// 标识是否为文件夹(varchar类型,yes和no来区分)
	public static final String NOTE_PARENT_FOLDER = "parentfile";// 如果不是文件夹,本字段存储其所属文件夹(varchar类型(存储被标记为文件夹的记录的_id字段的值))
	public static final String NOTE_UPDATE_DATE = "cdata_long";
	public static final String NOTE_TITLE = "title";

	public static final String[] NOTE_ALL_COLUMS = new String[] {
			DBOpenHelper.ID, DBOpenHelper.NOTE_CONTENT,
			DBOpenHelper.NOTE_ALARM_ENABLE, DBOpenHelper.NOTE_BG_COLOR,
			DBOpenHelper.NOTE_IS_FOLDER, DBOpenHelper.NOTE_PARENT_FOLDER,
			DBOpenHelper.NOTE_UPDATE_DATE, DBOpenHelper.RINGTONE_URI,
			DBOpenHelper.ISVIBRATE, DBOpenHelper.RINGTONE_DATE,
			DBOpenHelper.RINGTONE_TIME, DBOpenHelper.RINGTONE_NAME, NOTE_TITLE };

	public final static String RINGTONE_DATE = "date";
	public final static String RINGTONE_TIME = "time";
	public final static String ISVIBRATE = "isvibrate";
	public final static String RINGTONE_NAME = "rings";
	public final static String RINGTONE_URI = "uri";

	private static DBOpenHelper helper = null;

	public static DBOpenHelper getInstance(Context context) {
		if (helper == null) {
			helper = new DBOpenHelper(context);
		}
		return helper;

	}

	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID
				+ " integer primary key autoincrement , " + NOTE_CONTENT
				+ " text , " + RINGTONE_URI + " text ," + RINGTONE_NAME
				+ " text ," + ISVIBRATE + " int ," + RINGTONE_DATE + " text ,"
				+ RINGTONE_TIME + " text ,"

				+ NOTE_ALARM_ENABLE + " integer , " + NOTE_BG_COLOR
				+ " integer , " + NOTE_IS_FOLDER + " int , "
				+ NOTE_PARENT_FOLDER + " varchar, " + NOTE_TITLE + " text, "
				+ NOTE_UPDATE_DATE + " long);");
		Log.v("way", "Create Table: " + TABLE_NAME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 数据库升级调用此函数,需要重写，不然编译不过。
		db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
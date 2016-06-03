package com.yangpan.ynote.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmOpenHelper extends SQLiteOpenHelper{
	
	private static final String TABLE_NAME="alarm";
	private static int VERSION=1;
	private String sql="create table alarm (time varchar(255) primary key,type varchar(16),status integer );";
	public AlarmOpenHelper(Context context) {
		this(context,TABLE_NAME,null,VERSION);
	}
	public AlarmOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}

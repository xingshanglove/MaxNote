package com.yangpan.ynote.db;



import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DatabaseOperation {
	private SQLiteDatabase db;
	private Context context;
	public DatabaseOperation(Context context,SQLiteDatabase db) {
		this.db = db;
		this.context = context;
	}
	 //数据库的打开或创建
    public void create_db(){
    	//创建或打开数据库
    	db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().toString()+"/mynotes.db3", null);
    	db.execSQL("DROP TABLE IF EXISTS studentScore");  

    	if(db == null){
    		Toast.makeText(context,"数据库创建不成功",Toast.LENGTH_LONG).show();
    	}
    	//Toast.makeText(context,"数据库创建成功",Toast.LENGTH_LONG).show();
    	
    	//创建表
    	db.execSQL("create table if not exists notes(_id integer primary key autoincrement," +
    			"title text," +
    			"context text," +
    			"time varchar(20))");
    	
    }
    public void insert_db(String title,String text,String time){
    	
    	
    	if(text.isEmpty()){
    		Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
    	}
    	else{
    		db.execSQL("insert into notes(title,context,time) values('"+ title+"','"+ text+ "','"+time+"');");
        	//Toast.makeText(context, "插入成功", Toast.LENGTH_LONG).show();
    	}
    	
    }
    public void update_db(String title,String text,String time,int item_ID){
    	if( text.isEmpty()){
    		Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
    	}
    	else{
    		//String sql = "update main set class1='" + class1 + "',class2='" + class2 + "',class3='" + class4 + "',class4='" + class4 + "'where days='" + days + "';";
    		db.execSQL("update notes set context='"+text+ "',title='"+title+"',time='"+time+"'where _id='" + item_ID+"'");
        	//Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
        	}
    }
    
    public Cursor query_db(){
    	Cursor cursor = db.rawQuery("select * from notes",null);
    	return cursor;
    }
    public Cursor query_db(int item_ID){
    	Cursor cursor = db.rawQuery("select * from notes where _id='"+item_ID+"';",null);
    	return cursor;
    	
    }
    public void delete_db(int item_ID){
    	db.execSQL("delete from notes where _id='" + item_ID+"'");
    	//Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
    }
    
     	
    
    
    
    //关闭数据库
    public void close_db(){
    	db.close();
    }
}

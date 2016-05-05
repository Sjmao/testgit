package com.example.webview.dbutil;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WebDao {
	private DBHelper mDbHelper;
	public WebDao(Context context,int version){
		mDbHelper=new DBHelper(context,version);
	}
	
	public void insert(String str1,String str2){
		 SQLiteDatabase db = mDbHelper.getWritableDatabase();
//	        db.execSQL("insert into myweb (name,url) values (?,?)",new String[]{str1,str2});
//	        db.close();
		ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
		//cv.put(str1,str2); //添加密码
		cv.put("name", str1);
		cv.put("url", str2);
		db.insert("myweb",null,cv);//执行插入操作
		db.close();
	}
	public Map<String,String> check(){
		String str1,str2;
		SQLiteDatabase db=mDbHelper.getReadableDatabase();
//		Cursor cursor = db.rawQuery("select * from myweb" , null);
	    Map<String, String> map=new HashMap<String, String>();
//		while(cursor.moveToNext()){
//		str1=cursor.getString(1);
//		str2=cursor.getString(2);
//		map.put(str1,str2);
//		}
		Cursor c = db.query("myweb",null,null,null,null,null,null);//查询并获得游标
		if(c.moveToFirst()){//判断游标是否为空
		    for(int i=0;i<c.getCount();i++){
		        c.move(i);//移动到指定记录
		        String name = c.getString(c.getColumnIndex("name"));
		        String url = c.getString(c.getColumnIndex("url"));
		        map.put(name, url);
		    }
		}
		c.close();
		db.close();
		return map;
	}
	public void delete(String[] str1){
		SQLiteDatabase db=mDbHelper.getReadableDatabase();
		db.delete("myweb", "name=?", str1);
		db.close();
	}

}

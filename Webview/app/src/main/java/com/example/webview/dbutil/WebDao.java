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
		ContentValues cv = new ContentValues();//ʵ����һ��ContentValues����װ�ش����������cv.put("username","Jack Johnson");//����û���
		//cv.put(str1,str2); //�������
		cv.put("name", str1);
		cv.put("url", str2);
		db.insert("myweb",null,cv);//ִ�в������
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
		Cursor c = db.query("myweb",null,null,null,null,null,null);//��ѯ������α�
		if(c.moveToFirst()){//�ж��α��Ƿ�Ϊ��
		    for(int i=0;i<c.getCount();i++){
		        c.move(i);//�ƶ���ָ����¼
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

package com.example.meeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqlHelper {
	private static final String TAG = "SqlHelper";
	public static String User_Table = "User";
	public static String Group_TABLE = "StuGroup";
	public  static final String DB_NAME = "data/data/com.example.stugra/databases/stugra.db";
	public Long Insert(Context context, String table, ContentValues values){
		Long id = null;
		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null); 
		try{
			id = db.insert(table, null, values);
			Log.i(TAG, "Insert");
			}
			catch(Exception e){
				e.getStackTrace();
			}
		    db.close();
			return id;
		}
	
	public void CreateTable(Context context, String table){
		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null);
		String sql="CREATE TABLE " + table + " ( ID text not null, SPELLING text not null , MEANNING text not null, PHONETIC_ALPHABET text, LIST text not null" + ");";
		try{
			db.execSQL(sql);
			Log.i(TAG, sql);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public void Update(Context context, String table, ContentValues values, String whereClause, String[] whereArgs){
		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null); 
		try{
			db.update(table, values, whereClause, whereArgs); 
			Log.i(TAG, "Update");
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public Cursor Query(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null); 
		Cursor cursor = null ;
		try{
			cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			Log.i(TAG, "query");
			Log.i("countofcursor=",""+cursor.getCount());
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
		return cursor;
		
	}
	
	public void Delete(Context context, String table, String whereClause, String[] whereArgs){
		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null); 
		try{
			db.delete(table, whereClause, whereArgs);
			Log.i(TAG, "delete");
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public void DeleteTable(Context context, String table){

		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null); 
		String sql="drop table " + table;
		try{
		db.execSQL(sql);
		Log.i(TAG, sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
		db.close();
	}


	public void DeleteTableData(Context context, String table){

		SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(
				DB_NAME, null);
		String sql="delete from " + table;
		try{
			db.execSQL(sql);
			Log.i(TAG, sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
		db.close();
	}


}

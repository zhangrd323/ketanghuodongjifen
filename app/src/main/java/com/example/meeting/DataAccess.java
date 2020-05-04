package com.example.meeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.example.meeting.bean.Group;
import com.example.meeting.bean.User;

import java.util.ArrayList;


public class DataAccess {

    public Context context;
    private SqlHelper helper;

    public DataAccess(Context context) {
        helper = new SqlHelper();
        this.context = context;

    }


    /**
     * 插入
     *
     * @param data
     */
    public Long insertUser(User data) {
        ContentValues cv = new ContentValues();
        cv.put("Name", data.getName());
        cv.put("Num", data.getNum());
        cv.put("Score", data.getScore());
        cv.put("fScore", data.getfScore());
        cv.put("GroupId",data.getGroupId());
        return  helper.Insert(context, SqlHelper.User_Table, cv);
    }

    /**
     * 修改数据
     *
     * @param data
     */
    public void updateUser(User data) {
        ContentValues cv = new ContentValues();
        cv.put("Name", data.getName());
        cv.put("Num", data.getNum());
        cv.put("Score", data.getScore());
        cv.put("fScore", data.getfScore());
        cv.put("GroupId",data.getGroupId());
        helper.Update(context, SqlHelper.User_Table, cv, "ID ='" + data.getId() + "'", null);
    }

    /**
     * 查询数据
     *
     * @param selection
     * @param selectionArgs
     * @return
     */
    public ArrayList<User> queryUser(String selection, String[] selectionArgs) {
        Cursor cursor = helper.Query(context, SqlHelper.User_Table, null, selection, selectionArgs, null, null, "ID desc");
        ArrayList<User> list = new ArrayList<User>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    User data = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getInt(3),cursor.getString(4),cursor.getInt(5));
                    list.add(data);
                }
                while (cursor.moveToNext());
            }
        }else {
            return list;
        }
        cursor.close();
        return list;
    }


    /**
     * 删除
     *
     * @param
     */
    public void deleteUser(User data) {
        helper.Delete(context, SqlHelper.User_Table, "ID ='" + data.getId() + "'", null);
    }

    /**
     * 插入
     *
     * @param data
     */
    public void insertGroup(Group data) {
        ContentValues cv = new ContentValues();
        cv.put("Name", data.getName());
        cv.put("Score", data.getScore());
        cv.put("Star", data.getStar());
        helper.Insert(context, SqlHelper.Group_TABLE, cv);
    }

    /**
     * 修改数据
     *
     * @param data
     */
    public void updateGroup(Group data) {
        ContentValues cv = new ContentValues();
        cv.put("Name", data.getName());
        cv.put("Score", data.getScore());
        cv.put("Star", data.getStar());
        helper.Update(context, SqlHelper.Group_TABLE, cv, "ID ='" + data.getId() + "'", null);
    }

    /**
     * 查询数据
     *
     * @param selection
     * @param selectionArgs
     * @return
     */
    public ArrayList<Group> queryGroup(String selection, String[] selectionArgs) {
        Cursor cursor = helper.Query(context, SqlHelper.Group_TABLE, null, selection, selectionArgs, null, null, "ID desc");
        ArrayList<Group> list = new ArrayList<Group>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Group data = new Group(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
                    list.add(data);
                }
                while (cursor.moveToNext());
            }
        }else {
            return list;
        }
        cursor.close();
        return list;
    }


    /**
     * 删除
     *
     * @param
     */
    public void deleteGroup(Group data) {
        helper.Delete(context, SqlHelper.Group_TABLE, "ID ='" + data.getId() + "'", null);
    }

}

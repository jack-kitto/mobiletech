package com.example.sampleassignment1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.RadioGroup;

import com.example.sampleassignment1.R;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "LocationTracker";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "colour";
    public static final String COLUMN_ISSELECTED = "isSelected";


    public MyDbHelper(Context context, String name,
                      SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_NAME + " text, " +
                        COLUMN_ISSELECTED + " text" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public long insertUser(String user, String isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, user);
        contentValues.put(COLUMN_ISSELECTED, isSelected);

        long id = db.insert(TABLE_NAME, null, contentValues);

        return id;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> allUsers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            @SuppressLint("Range") String user = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String isSelected = cursor.getString(cursor.getColumnIndex(COLUMN_ISSELECTED));
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            allUsers.add(user + "," + isSelected + "," + id);
            cursor.moveToNext();
        }
        return allUsers;
    }

    public int deleteUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "id = ? ", new String[]{Long.toString(id)});

        return result;
    }

    public int updateUser(String id, String newName, String newSelection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, newName);
        contentValues.put(COLUMN_ISSELECTED, newSelection);
        int result = db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return result;
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }
    public String getNameFromUser(String user){
        return user.split(",")[0];
    }
    public String getIdFromUser(String user){
        return user.split(",")[2];
    }

    public boolean getIsSelectedFromUser(String user){
        String isSelected = user.split(",")[1];
        if(isSelected.matches("1")) return true;
        else return false;
    }
    public String getSelectedFromUser(String user){
        return user.split(",")[1];
    }
}

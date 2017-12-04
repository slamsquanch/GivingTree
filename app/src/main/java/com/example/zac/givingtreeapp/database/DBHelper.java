package com.example.zac.givingtreeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zac.givingtreeapp.classes.Day;

import java.util.ArrayList;

/**
 * Created by Zac on 2017-11-28.
 */

public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GivingTree.db";
    public static final String DATABASE_ID = "id";
    public static final String DAY_TABLE_NAME = "DayTable";
    public static final String MSG_TABLE_NAME = "MessageTable";
    public static final String COUNTER_TABLE_NAME = "CountTable";
    public static final String[] DAY_COLUMN_STRING = {"id", "message1", "message2", "message3", "send_status", "open_status", ""};
    private static DBHelper database = null;




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE;
        CREATE_TABLE = "CREATE TABLE " + DAY_TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "message1 TEXT," +
                "message2 TEXT," +
                "message3 TEXT," +
                "send_status TEXT," +
                "open_status DOUBLE," +
                "longitude DOUBLE" +
                ")";
        db.execSQL(CREATE_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS ImageTable");
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public static DBHelper getInstance(Context context) {
        if (database == null) {
            database = new DBHelper(context.getApplicationContext());
        }
        return database;
    }


    // insert pic
    public void addDay(Day day, String sendStat, String openStat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("id", img.getID());
        values.put("message1", day.getMessage1());
        values.put("message2", day.getMessage2());
        values.put("message3", day.getMessage3());
        values.put("send_status", sendStat);
        values.put("open_status", openStat);
        db.insert(DAY_TABLE_NAME, null, values);
        db.close();
    }


    // delete pic
    public void deletePhoto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DAY_TABLE_NAME, DATABASE_ID + " = ?",
                new String[] {id});
        db.close();
    }


    // retrieve pic
    public Day getDay(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DAY_TABLE_NAME,
                DAY_COLUMN_STRING,
                DATABASE_ID + "=?",
                new String[] {id},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();
        Day day = new Day();
        populate(day, cursor);

        db.close();
        return day;
    }


    // get all the photos
    public ArrayList<Day> getCalendar() {
        ArrayList<Day> days = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DAY_TABLE_NAME + " ORDER BY ID ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Day temp = new Day();
                populate(temp, cursor);

                days.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return days;
    }



    private void populate(Day day, Cursor cursor) {
        day.setMessage1(cursor.getString(0));
        day.setMessage2(cursor.getString(1));
        day.setMessage3(cursor.getString(2));
        day.setSendStatus(cursor.getString(3));
        day.setOpenStatus(cursor.getString(4));
    }

}

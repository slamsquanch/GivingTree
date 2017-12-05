package com.example.zac.givingtreeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.zac.givingtreeapp.classes.Day;

import java.util.ArrayList;

/**
 * Created by Zac on 2017-11-28.
 */

public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "GivingTree.db";
    public static final String DATABASE_ID = "id";
    public static final String MESSAGE_ID = "msgID";
    public static final String MESSAGE_DAY = "day";
    public static final String DAY_TABLE_NAME = "DayTable";
    public static final String MSG_TABLE_NAME = "MessageTable";
    public static final String COUNTER_TABLE_NAME = "CountTable";
    public static final String[] DAY_COLUMN_STRING = {"id", "msgID", "message", "day"};
    private static DBHelper database = null;




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE;
        CREATE_TABLE = "CREATE TABLE " + DAY_TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "msgID INTEGER," +
                "message TEXT," +
                "day INTEGER" +
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


    // Add message to the database
    public void addMessage(Day day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("DBhelper", "" + day.getID());     // msgID WORKS!... here AND works when I getCalendar in main
        values.put("msgID", day.getID());
        Log.e("DBhelper", "" +day.getMessage());  // msg WORKS!...  here but is null when I getCalendar in main
        values.put("message", day.getMessage());
        Log.e("DBhelper", "" + day.getDay());    // day WORKS!...here but is 0 when I getCalendar in main
        values.put("day", day.getDay());
        db.insert(DAY_TABLE_NAME, null, values);
        db.close();
    }


    // delete message of day
    /*public void deleteMessage(int msgID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DAY_TABLE_NAME, MESSAGE_ID + " = ",
                new String[] {("" + msgID)});
        db.close();
    }*/

    // delete message of day
    public void deleteMessage(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DAY_TABLE_NAME, MESSAGE_DAY + " =? ",
                new String[] {("" + day)});
        db.close();
    }


    // get message of day
    public Day getMessage(int day) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DAY_TABLE_NAME,
                DAY_COLUMN_STRING,
                "day" + "= ?",
                new String[] {("" + day)},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();
        Day d = new Day();
        populate(d, cursor);

        db.close();
        return d;
    }


    // get all the days
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


    public int getLastDay(){
        int lastDay = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + DAY_TABLE_NAME + " ORDER BY cast(day as REAL) DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(3) > lastDay)
                    lastDay = cursor.getInt(3);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lastDay;
    }


    public String getDay(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + DAY_TABLE_NAME + " ORDER BY cast(day as REAL) ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String message;
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                if (cursor.getInt(3) == day) {
                    message = cursor.getString(2);
                    Log.e("DBhelperGetMessage", message);
                    return message;
                }
            }
        }
        Log.e("DBhelperGetMessage", "poo");

        return "poo";
    }

    private void populate(Day day, Cursor cursor) {
        day.setID(cursor.getInt(1));
        day.setMessage(cursor.getString(2));
        day.setDay(cursor.getInt(3));
    }

}

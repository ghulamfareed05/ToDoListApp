package com.example.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {
    public static final String ID="Id";
    public static final String NOTES="Notes";
    public static final String DUE_DATE="Due_Date";
    public static final String DUE_TIME="Due_Time";
    public static final String SNOOZE_TEXT="Snooze_Text";
    public static final String REMINDER_TABLE="Reminder_Table";
    public static final String DB_NAME="Reminder_DB";

    public static final String LIST_NAME = "List_Name";
    public static final String REMINDER_LIST_TABLE = "REMINDER_LIST_TABLE";



    public SqlHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String second_query = "Create Table If Not Exists "+ REMINDER_LIST_TABLE+"(" +

                LIST_NAME+" varchar(25) primary key)";
        db.execSQL(second_query);
        String query = "Create Table If Not Exists "+ REMINDER_TABLE+"(" +
                 ID+" integer primary key autoincrement," +
                 LIST_NAME+" varchar(35) NOT NULL,"+
                 NOTES+" text Not null," +
                 DUE_DATE+" varchar(30)," +
                 DUE_TIME+" varchar(15)," +
                 SNOOZE_TEXT+" varchar(15)," +

                "FOREIGN KEY ("+LIST_NAME+") REFERENCES "+REMINDER_LIST_TABLE+"("+LIST_NAME+"))";

        db.execSQL(query);
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlHelper.LIST_NAME,"Default");
        db.insert(REMINDER_LIST_TABLE,null,contentValues);
        contentValues = new ContentValues();
        contentValues.put(SqlHelper.LIST_NAME,"Personal");
        db.insert(REMINDER_LIST_TABLE,null,contentValues);
        contentValues = new ContentValues();
        contentValues.put(SqlHelper.LIST_NAME,"Shopping");
        db.insert(REMINDER_LIST_TABLE,null,contentValues);
        contentValues = new ContentValues();
        contentValues.put(SqlHelper.LIST_NAME,"Wishlist");
        db.insert(REMINDER_LIST_TABLE,null,contentValues);
        contentValues = new ContentValues();
        contentValues.put(SqlHelper.LIST_NAME,"Work");
        db.insert(REMINDER_LIST_TABLE,null,contentValues);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

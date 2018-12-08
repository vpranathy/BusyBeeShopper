package com.example.mobileapp.busybeeshopper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SampleDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="SampleData.db";
    public static final String TABLE_NAME="UserGroup";
    public static final String COL1= "ID";
    public static final String COL2="username";
    public static final String COL3="item";
    public static final String COL4="itemDesc";
    public SampleDatabase(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable= "CREATE TABLE "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL2+ " TEXT,"+ COL3+ " TEXT,"+ COL4 +" TEXT)";
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean add(String userName, String item,String itemDesc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, userName);
        contentValues.put(COL3,item);
        contentValues.put(COL4,itemDesc);
        Log.i("DataBase", "adding data to table " + TABLE_NAME);
        long res = db.insert(TABLE_NAME, null, contentValues);
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db= this.getReadableDatabase();
        String query="SELECT * FROM "+ TABLE_NAME;
        Cursor data=db.rawQuery(query,null);
        return data;

    }
}

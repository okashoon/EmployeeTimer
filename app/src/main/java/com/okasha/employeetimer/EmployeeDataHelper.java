package com.okasha.employeetimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ahmed on 20-May-16.
 */
public class EmployeeDataHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="EmployeeTimer";
    private static final int DB_VERSION =1;

    EmployeeDataHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("aaa","oncreate called");



       /** db.execSQL("PRAGMA writable_schema = 1;" +
        " delete from sqlite_master where type = 'table';" +
        " PRAGMA writable_schema = 0;");


        db.execSQL("CREATE TABLE AHMED(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "col_FROM TIME," +
                "col_TO TIME," +
                "col_TOTAL TIME);");


        insertDay(db, "AHMED", 75720000, 75750000);



        db.execSQL("CREATE TABLE AMR (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "col_FROM TIME," +
                "col_TO TIME," +
                "col_TOTAL TIME);");
        */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.d("aaa","onUpgrade called");
        onCreate(db);

    }

    public void insertDay(SQLiteDatabase db,String table, int from, int to){
        ContentValues values = new ContentValues();
        values.put("col_FROM", from);
        values.put("col_TO", to);
        //values.put("col_TOTAL", total);
        db.insert(table,null,values);
    }
}

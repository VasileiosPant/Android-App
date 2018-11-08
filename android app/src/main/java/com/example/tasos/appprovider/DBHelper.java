package com.example.tasos.appprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tasos on 14-Feb-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String KEY_ID = "_ID";
    public static final String KEY_USERID = "_USERID";
    public static final String KEY_LAT = "_LATITUDE";
    public static final String KEY_LON = "_LONGITUDE";
    public static final String KEY_DT = "_DT";

    private static final String DATABASE_NAME = "users_manager";
    public static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" ("+ KEY_ID +" INTEGER , "
            + KEY_USERID +" TEXT, "+ KEY_LAT +" FLOAT," + KEY_LON +" FLOAT,"+ KEY_DT + " TEXT" + ")";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bookDB) {
        bookDB.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}

package com.example.tasos.appprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.security.Provider;

/**
 * Created by tasos on 14-Feb-17.
 */

public class MyProvider extends ContentProvider {

    private static final String AUTHORITY="com.example.tasos.appprovider";
    private static final String PATH="users";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(AUTHORITY, PATH, 1);
        sUriMatcher.addURI(AUTHORITY, PATH+"/#", 2);
    }

    DBHelper mDBHelper;

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = -1;
        SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case 1:
                id = mDB.insert(DBHelper.DATABASE_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException ("Content URI pattern not recognizable: "+uri);
        }
        return Uri.parse(uri.toString()+"/"+id);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        Cursor mCursor;
        switch(sUriMatcher.match(uri)){
            case 1:
                mCursor = mDB.query(DBHelper.DATABASE_TABLE, null, null, null, null, null, null);
                break;
            case 2:
                selection = "_ID=?";
                selectionArgs[0] = uri.getLastPathSegment();
                mCursor = mDB.query(DBHelper.DATABASE_TABLE, null, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException ("Content URI pattern not recognizable: "+uri);
        }
        return mCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }
}

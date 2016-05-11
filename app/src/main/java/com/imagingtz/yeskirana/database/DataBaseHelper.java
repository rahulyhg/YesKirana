package com.imagingtz.yeskirana.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mishra on 1/16/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static com.imagingtz.yeskirana.database.DataBaseHelper dataBaseHelper;
    public static final String DATABASE_NAME = "yesKirana.db";
    public static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static com.imagingtz.yeskirana.database.DataBaseHelper getInstance(Context context) {
        if (dataBaseHelper == null) {
            dataBaseHelper = new com.imagingtz.yeskirana.database.DataBaseHelper(context.getApplicationContext());
        }
        return dataBaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table.Cart.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

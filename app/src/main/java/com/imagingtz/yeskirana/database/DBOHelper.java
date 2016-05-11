package com.imagingtz.yeskirana.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/*
 * Created by Mishra on 1/14/2016.
 */
public class DBOHelper {

    public long insert(Context context, String tableName, ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase;

        long row_id = -1;
        try {
            sqLiteDatabase = DataBaseHelper.getInstance(context).getWritableDatabase();
            if (sqLiteDatabase != null) {
                row_id = sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return row_id;
    }

    public long update(Context context, String tablename, ContentValues contentValues,
                       String whereClause) {
        SQLiteDatabase sqLiteDatabase;
        long row_id = 0;

        try {
            sqLiteDatabase = DataBaseHelper.getInstance(context).getWritableDatabase();
            row_id = sqLiteDatabase.update(tablename, contentValues, whereClause, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (row_id > 0) {
            Log.d("No of updated row is", "1");
        }
        return row_id;
    }

    public int delete(Context context, String tableName, String whereClause) {

        SQLiteDatabase sqLiteDatabase;
        int row = 0;

        try {
            sqLiteDatabase = DataBaseHelper.getInstance(context).getWritableDatabase();

            row = sqLiteDatabase.delete(tableName, whereClause, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (row > 0) {
            Log.d("No of deleted row is", row + "");
        }
        return row;
    }

}

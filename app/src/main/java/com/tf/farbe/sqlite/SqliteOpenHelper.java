package com.tf.farbe.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TF on 14/11/2017.
 */

public class SqliteOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "farbe_db.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ PaletteContract.PaletteEntry.TABLE_NAME + " ("+
                    PaletteContract.PaletteEntry._ID + " INTEGER PRIMARY KEY,"+
                    PaletteContract.PaletteEntry.COLUMN_NAME +    " TEXT,"+
                    PaletteContract.PaletteEntry.COLUMN_COLOR_1 + " TEXT,"+
                    PaletteContract.PaletteEntry.COLUMN_COLOR_2 + " TEXT,"+
                    PaletteContract.PaletteEntry.COLUMN_COLOR_3 + " TEXT,"+
                    PaletteContract.PaletteEntry.COLUMN_COLOR_4 + " TEXT,"+
                    PaletteContract.PaletteEntry.COLUMN_COLOR_5 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PaletteContract.PaletteEntry.TABLE_NAME;

    public SqliteOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Empty.
    }
}
